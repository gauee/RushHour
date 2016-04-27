package pl.edu.uj.ii.webapp;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import pl.edu.uj.ii.webapp.execute.*;
import pl.edu.uj.ii.webapp.execute.test.TestResult;
import spark.ModelAndView;
import spark.Request;
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
import static spark.Spark.post;

/**
 * Created by gauee on 4/7/16.
 */
public class StartApp {
    public static final String PARAM_SUPPORTED_LANG = "supportedLang";
    public static final String PARAM_FILE_CONTENT = "fileContent";
    private static final Logger LOGGER = Logger.getLogger(StartApp.class);
    private RushHourExecutor rushHourExecutor;

    public static void main(String[] args) throws IOException {
        StartApp startApp = new StartApp();
        startApp.init();
    }

    private void init() throws IOException {
        initRoutes();
        rushHourExecutor = new RushHourExecutor(new TaskFactory(initLanguages()));
    }

    private void initRoutes() {
        VelocityTemplateEngine templateEngine = new VelocityTemplateEngine();
        get("/", (req, res) -> uploadPageView(), templateEngine);
        post("/submit", "multipart/form-data", (req, res) -> processNewSolution(req), templateEngine);
    }

    private Map<SupportedLang, Task> initLanguages() {
        Map<SupportedLang, Task> languageCompilers = Maps.newHashMap();
        languageCompilers.put(SupportedLang.JAVA_7, new JavaTask(CONFIG.getCompiledFileDirForJava7(), CONFIG.getJava7Home()));
        languageCompilers.put(SupportedLang.JAVA_8, new JavaTask(CONFIG.getCompiledFileDirForJava8(), CONFIG.getJava8Home()));
        languageCompilers.put(SupportedLang.PYTHON_2, new PythonTask());
        languageCompilers.put(SupportedLang.PYTHON_3, new PythonTask());
        return languageCompilers;
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
            return setMessage(modelAndView, "Cannot execute all testCases:\n" + e.getMessage());
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
