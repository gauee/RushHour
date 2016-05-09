package pl.edu.uj.ii.webapp;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.uj.ii.webapp.execute.JavaTask;
import pl.edu.uj.ii.webapp.execute.Param;
import pl.edu.uj.ii.webapp.execute.PythonTask;
import pl.edu.uj.ii.webapp.execute.RushHourExecutor;
import pl.edu.uj.ii.webapp.execute.SupportedLang;
import pl.edu.uj.ii.webapp.execute.Task;
import pl.edu.uj.ii.webapp.execute.TaskFactory;
import pl.edu.uj.ii.webapp.execute.UploadFile;
import pl.edu.uj.ii.webapp.execute.test.TestResult;
import spark.ModelAndView;
import spark.Request;
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
    private RushHourExecutor rushHourExecutor;

    public static void main(String[] args) {
        try {
            StartApp startApp = new StartApp();
            startApp.init();
        } catch (Exception e) {
            LOGGER.error("Main thread throw exception.", e);
        }
    }

    @Override
    public void init() {
        LOGGER.info("Starting application RushHour");
        port(CONFIG.getSrvPort());
        ipAddress(CONFIG.getIpAddress());
        initRoutes();
        rushHourExecutor = new RushHourExecutor(new TaskFactory(initLanguages()));
    }

    private void initRoutes() {
        VelocityTemplateEngine templateEngine = new VelocityTemplateEngine();
        get("/", (req, res) -> uploadPageView(), templateEngine);
        post("/submit", "multipart/form-data", (req, res) -> processNewSolution(req), templateEngine);
    }

    private Map<SupportedLang, Task> initLanguages() {
        return ImmutableMap.<SupportedLang, Task>builder()
                .put(SupportedLang.JAVA_7, new JavaTask(CONFIG.getJava7Home(), CONFIG.getCompiledFileDirForJava7()))
                .put(SupportedLang.JAVA_8, new JavaTask(CONFIG.getJava8Home(), CONFIG.getCompiledFileDirForJava8()))
                .put(SupportedLang.PYTHON_2, new PythonTask(CONFIG.getPython2Interpreter(), CONFIG.getCompiledFileDirForPython2()))
                .put(SupportedLang.PYTHON_3, new PythonTask(CONFIG.getPython3Interpreter(), CONFIG.getCompiledFileDirForPython3()))
                .build();
    }

    private ModelAndView processNewSolution(Request req) {
        Param param = createParam(req);
        ModelAndView modelAndView = uploadPageView();
        LOGGER.debug("Request param: " + param);
        try {
            List<TestResult> testResults = rushHourExecutor.runAllTestCases(param);
            appendToModel(modelAndView, "testResults", testResults);
        } catch (Exception e) {
            LOGGER.error("Cannot retrieve output", e);
            return setMessage(modelAndView, "Cannot execute all testCases.");
        }
        return setMessage(modelAndView, "File uploaded.");
    }

    private Param createParam(Request req) {
        req.raw().setAttribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("./target"));
        return new Param(retrieveSupportedLang(req), retrieveSourceCode(req));
    }

    private UploadFile retrieveSourceCode(Request req) {
        StringBuilder sourceCodeBuilder = new StringBuilder();
        String fileName = StringUtils.EMPTY;
        try {
            Part part = req.raw().getPart(PARAM_FILE_CONTENT);
            fileName = part.getSubmittedFileName();
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
        return new ModelAndView(model, "templates/index.vm");
    }

    private ModelAndView setMessage(ModelAndView modelAndView, String message) {
        appendToModel(modelAndView, "message", message);
        return modelAndView;
    }

    private void appendToModel(ModelAndView modelAndView, String modelKey, Object modelValue) {
        ((Map<String, Object>) modelAndView.getModel()).put(modelKey, modelValue);
    }


}
