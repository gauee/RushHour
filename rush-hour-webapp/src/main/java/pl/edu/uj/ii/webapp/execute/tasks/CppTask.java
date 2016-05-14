package pl.edu.uj.ii.webapp.execute.tasks;

import java.io.File;
import java.io.IOException;

import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 5/14/16.
 */
public class CppTask extends CompilableTask {
    private final String cppHome;

    public CppTask(String cppHome, String solutionDir) {
        super(solutionDir);
        this.cppHome = cppHome;
    }

    @Override
    void preCompile() throws IOException, ClassNotFoundException {
    }

    @Override
    String createCompileCommand() {
        return "../" + this.cppHome + "g++ -o " + this.sourceFile.getParent().toFile().getAbsolutePath() + baseFileName;
    }

    @Override
    ProcessBuilder createExecutionProcess() {
        ProcessBuilder processBuilder = createProcessBuilder("./", getUniqSolutionDir() + baseFileName);
        processBuilder.directory(new File(CONFIG.getUploadedFileDir()));
        return processBuilder;
    }

    @Override
    String getTempFileName() {
        return String.format("%s.cpp", baseFileName);
    }
}
