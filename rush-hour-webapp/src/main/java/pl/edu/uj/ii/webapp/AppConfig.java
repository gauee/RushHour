package pl.edu.uj.ii.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by gauee on 4/7/16.
 */
public class AppConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);
    public final static AppConfig CONFIG = new AppConfig();
    private final Properties appProperties = new Properties();

    private AppConfig() {
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("app.properties")) {
            if (resourceAsStream != null) {
                appProperties.load(resourceAsStream);
            } else {
                LOGGER.info("Cannot find app.properties file.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot load app.properties", e);
        }
    }

    public String getJava8Home() {
        return appProperties.getProperty("java.8.home");
    }

    public String getJava7Home() {
        return appProperties.getProperty("java.7.home");
    }

    public String getPython3Interpreter() {
        return appProperties.getProperty("python.3.interpreter");
    }

    public String getPython2Interpreter() {
        return appProperties.getProperty("python.2.interpreter");
    }

    public String getUploadedFileDir() {
        return appProperties.getProperty("uploaded.file.dir");
    }

    public String getCompiledFileDirForJava7() {
        return appProperties.getProperty("compiled.file.dir.java.7");
    }

    public String getCompiledFileDirForJava8() {
        return appProperties.getProperty("compiled.file.dir.java.8");
    }

    public String getCompiledFileDirForPython2() {
        return appProperties.getProperty("compiled.file.dir.python.2");
    }

    public String getCompiledFileDirForPython3() {
        return appProperties.getProperty("compiled.file.dir.python.3");
    }

    public String getTestCasesDir() {
        return appProperties.getProperty("test.cases.dir");
    }

}
