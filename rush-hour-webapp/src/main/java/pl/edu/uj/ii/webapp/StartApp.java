package pl.edu.uj.ii.webapp;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.uj.ii.webapp.db.Result;
import pl.edu.uj.ii.webapp.db.ResultDao;
import pl.edu.uj.ii.webapp.execute.SupportedLang;
import pl.edu.uj.ii.webapp.execute.UploadFile;
import pl.edu.uj.ii.webapp.solution.Scheduler;
import pl.edu.uj.ii.webapp.solution.Source;
import pl.edu.uj.ii.webapp.solution.Task;
import pl.edu.uj.ii.webapp.ui.TopResults;
import pl.edu.uj.ii.webapp.ui.TopResultsSource;
import pl.edu.uj.ii.webapp.ui.TotalResult;
import pl.edu.uj.ii.webapp.ui.TotalStepCounter;
import pl.edu.uj.ii.webapp.ui.UserResults;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.servlet.SparkApplication;
import spark.template.velocity.VelocityTemplateEngine;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;
import static pl.edu.uj.ii.webapp.execute.SupportedLang.JAVA_8;
import static spark.Spark.get;
import static spark.Spark.ipAddress;
import static spark.Spark.port;
import static spark.Spark.post;

/**
 * Created by gauee on 4/7/16.
 */
public class StartApp implements SparkApplication {
    public static final String PARAM_SUPPORTED_LANG = "supportedLang";
    public static final String PARAM_FILE_CONTENT = "fileContent";
    private static final Logger LOGGER = LoggerFactory.getLogger(StartApp.class);
    public static final String PARAM_SOLUTION_ID = "solutionId";
    private DurationFormatUtils timeDuration = new DurationFormatUtils();
    private final TotalStepCounter stepCounter = new TotalStepCounter();
    private final ResultDao resultDao = new ResultDao();
    private final TopResultsSource topResultsSource = new TopResultsSource(resultDao);
    private final Scheduler scheduler;
    private final Source solutionSource;

    public static void main(String[] args) {
        try {
            new StartApp().init();
        } catch (Exception e) {
            LOGGER.error("Main thread throw exception.", e);
        }
    }

    public StartApp() {
        this.solutionSource = new Source();
        this.scheduler = new Scheduler(solutionSource);
    }

    @Override
    public void init() {
        LOGGER.info("Starting application RushHour");
        port(CONFIG.getSrvPort());
        ipAddress(CONFIG.getIpAddress());
        initRoutes();
        scheduler.start();
    }

    private void initRoutes() {
        VelocityTemplateEngine templateEngine = new VelocityTemplateEngine();
        get("/", (req, res) -> uploadPageView(), templateEngine);
        get("/solution/:solutionId", (req, res) -> handleSubmission(req), templateEngine);
        get("/author/:authorId", (req, res) -> retrieveAuthorHistory(req), templateEngine);
        post("/submit", "multipart/form-data", (req, res) -> processNewSolution(req, res));
    }

    private ModelAndView retrieveAuthorHistory(Request req) {
        String authorId = req.params("authorId");
        List<Result> authorResults = resultDao.getAuthorResults(authorId);
        return authorView(new UserResults(authorId, authorResults));
    }

    private ModelAndView handleSubmission(Request req) {
        String solutionId = req.params(PARAM_SOLUTION_ID);
        return solutionView(new TotalResult(resultDao.get(solutionId)));
    }

    private ModelAndView processNewSolution(Request req, Response res) {
        Task task = createParam(req);
        ModelAndView modelAndView = uploadPageView();
        LOGGER.debug("Request param: " + task);
        try {
            scheduler.addTask(task);
            appendToModel(modelAndView, "solutionId", task.getSolutionId());
        } catch (Exception e) {
            LOGGER.error("Cannot retrieve output", e);
            return setMessage(modelAndView, "Cannot execute all testCases.");
        }
        res.redirect("/" + task.getSolutionId());
        return modelAndView;
    }

    private Task createParam(Request req) {
        req.raw().setAttribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("./target"));
        return new Task(retrieveSupportedLang(req), retrieveSourceCode(req));
    }

    private UploadFile retrieveSourceCode(Request req) {
        StringBuilder sourceCodeBuilder = new StringBuilder();
        String fileName = StringUtils.EMPTY;
        try {
            Part part = req.raw().getPart(PARAM_FILE_CONTENT);
            fileName = part.getSubmittedFileName().trim().replaceAll("\\s+", "_");
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(part.getInputStream()))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sourceCodeBuilder.append(line).append('\n');
                }
            }
        } catch (ServletException | IOException e) {
            LOGGER.info("Cannot load file.", e);
        }
        return new UploadFile(fileName, sourceCodeBuilder.toString());
    }

    private SupportedLang retrieveSupportedLang(Request req) {
        String langIdx = req.queryParams(PARAM_SUPPORTED_LANG);
        if (isEmpty(langIdx)) {
            return JAVA_8;
        }
        int idx = Integer.valueOf(langIdx);
        SupportedLang[] values = SupportedLang.values();
        return idx >= 0 && idx < values.length ? values[idx] : JAVA_8;
    }

    private ModelAndView uploadPageView() {
        Map<String, Object> model = Maps.newHashMap();
        model.put("supportedLang", SupportedLang.values());
        model.put("timeDuration", timeDuration);
        model.put("stepsCounter", stepCounter);
        model.put("solutionSource", solutionSource);
        model.put("topResults", new TopResults(topResultsSource.getTopResults()));
        model.put("dateFormatter", FastDateFormat.class);
        return new ModelAndView(model, "templates/view_index.vm");
    }

    private ModelAndView authorView(UserResults userResults) {
        Map<String, Object> model = Maps.newHashMap();
        model.put("timeDuration", timeDuration);
        model.put("userResults", userResults);
        model.put("dateFormatter", FastDateFormat.class);
        return new ModelAndView(model, "templates/view_author.vm");
    }

    private ModelAndView solutionView(TotalResult totalResult) {
        Map<String, Object> model = Maps.newHashMap();
        model.put("timeDuration", timeDuration);
        model.put("stepsCounter", stepCounter);
        model.put("totalResult", totalResult);
        model.put("dateFormatter", FastDateFormat.class);
        return new ModelAndView(model, "templates/view_solution.vm");
    }

    private ModelAndView setMessage(ModelAndView modelAndView, String message) {
        appendToModel(modelAndView, "message", message);
        return modelAndView;
    }

    private void appendToModel(ModelAndView modelAndView, String modelKey, Object modelValue) {
        ((Map<String, Object>) modelAndView.getModel()).put(modelKey, modelValue);
    }


}
