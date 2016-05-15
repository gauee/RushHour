package pl.edu.uj.ii.webapp.execute.tasks;

import java.io.File;
import java.io.IOException;

import static java.io.File.separator;
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
        this.updateSourceCode(sourceCode);
    }

    @Override
    String createCompileCommand() {
        return this.cppHome + "g++ -o " + this.sourceFile.getParent().toFile().getAbsolutePath() + separator + baseFileName;
    }

    protected ProcessBuilder createProcessBuilder(String command, String args) {
        String sourceFile = this.sourceFile.toFile().toString();
        ProcessBuilder processBuilder = new ProcessBuilder(
                "g++",
                sourceFile
        );
        processBuilder.redirectErrorStream(true);
        processBuilder.directory(this.sourceFile.getParent().toFile());
        return processBuilder;
    }

    @Override
    ProcessBuilder createExecutionProcess() {
        ProcessBuilder processBuilder = new ProcessBuilder("./" + getUniqSolutionDir() + baseFileName);
        processBuilder.directory(new File(CONFIG.getUploadedFileDir()));
        return processBuilder;
    }

    @Override
    String getTempFileName() {
        return String.format("%s.cpp", baseFileName);
    }
}
