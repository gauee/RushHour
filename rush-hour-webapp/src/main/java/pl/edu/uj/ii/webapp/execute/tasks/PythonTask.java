package pl.edu.uj.ii.webapp.execute.tasks;

import java.io.File;

import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 4/7/16.
 */
public class PythonTask extends Task {
    private final String interpreter;

    public PythonTask(String interpreter, String solutionDir) {
        super(solutionDir);
        this.interpreter = interpreter;
    }

    @Override
    protected void preExecution() {
        this.updateSourceCode(sourceCode);
    }

    @Override
    protected String getTempFileName() {
        return String.format("%s.py", baseFileName);
    }

    @Override
    ProcessBuilder createExecutionProcess() {
        ProcessBuilder processBuilder = createProcessBuilder("../" + interpreter + "python", getUniqSolutionDir() + getTempFileName());
        processBuilder.directory(new File(CONFIG.getUploadedFileDir()));
        return processBuilder;
    }

}
