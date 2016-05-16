package pl.edu.uj.ii.webapp.execute;

import org.apache.commons.lang.StringUtils;
import pl.edu.uj.ii.webapp.execute.tasks.CppTask;
import pl.edu.uj.ii.webapp.execute.tasks.ExecutionTask;
import pl.edu.uj.ii.webapp.execute.tasks.JavaTask;
import pl.edu.uj.ii.webapp.execute.tasks.PythonTask;

import java.io.File;

import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 4/7/16.
 */
public enum SupportedLang {

    JAVA_8("Java 8") {
        @Override
        public String getVersion() {
            return CONFIG.getJava8Home();
        }

        @Override
        public ExecutionTask createTask() {
            return new JavaTask(
                    CONFIG.getJava8Home(),
                    CONFIG.getCompiledFileDirForJava8()
            );
        }
    },
    GPP("C++") {
        @Override
        public String getVersion() {
            return CONFIG.getGppHome();
        }

        @Override
        public ExecutionTask createTask() {
            return new CppTask(
                    CONFIG.getGppHome(),
                    CONFIG.getCompiledFileDirForCpp()
            );
        }
    },
    PYTHON_3("Python 3") {
        @Override
        public String getVersion() {
            return CONFIG.getPython3Interpreter();
        }

        @Override
        public ExecutionTask createTask() {
            return new PythonTask(
                    CONFIG.getPython3Interpreter(),
                    CONFIG.getCompiledFileDirForPython3()
            );
        }
    };

    private final String description;

    SupportedLang(String description) {
        this.description = description;
    }

    public abstract String getVersion();

    public abstract ExecutionTask createTask();

    public String getDescription() {
        return description + " (ver. " + StringUtils.substringAfterLast(StringUtils.substringBeforeLast(getVersion(), File.separator), File.separator) + " )";
    }


}
