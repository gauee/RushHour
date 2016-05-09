package pl.edu.uj.ii.webapp.execute;

import org.apache.commons.lang.StringUtils;

import java.io.File;

import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 4/7/16.
 */
public enum SupportedLang implements Versionable {

    JAVA_7("Java 7 ") {
        @Override
        public String getVersion() {
            return CONFIG.getJava7Home();
        }
    },
    JAVA_8("Java 8") {
        @Override
        public String getVersion() {
            return CONFIG.getJava8Home();
        }
    },
    PYTHON_2("Python 2") {
        @Override
        public String getVersion() {
            return CONFIG.getPython2Interpreter();
        }
    },
    PYTHON_3("Python 3") {
        @Override
        public String getVersion() {
            return CONFIG.getPython3Interpreter();
        }
    };

    private final String description;

    SupportedLang(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description + " (ver. " + StringUtils.substringAfterLast(StringUtils.substringBeforeLast(getVersion(), File.separator), File.separator) + " )";
    }


}
