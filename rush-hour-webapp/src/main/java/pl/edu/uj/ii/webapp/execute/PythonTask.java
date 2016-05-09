package pl.edu.uj.ii.webapp.execute;

import java.io.File;

import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 4/7/16.
 */
public class PythonTask extends Task {
    private final String interpreter;
    private final String solutionTargetDir;

    public PythonTask(String interpreter, String solutionTargetDir) {
        this.interpreter = interpreter;
        this.solutionTargetDir = solutionTargetDir;
    }

    @Override
    protected String getTempFileName() {
        return String.format("%s.py", baseFileName);
    }

    @Override
    ProcessBuilder createExecutionProcess() {
        ProcessBuilder processBuilder = createProcessBuilder("../" + interpreter + "python", solutionTargetDir + getTempFileName());
        processBuilder.directory(new File(CONFIG.getUploadedFileDir()));
        return processBuilder;
    }

    @Override
    String getSolutionTypeDir() {
        return solutionTargetDir;
    }
}
