package pl.edu.uj.ii.webapp.execute.tasks;

import java.io.File;

import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 4/7/16.
 */
public class PythonTask extends ExecutionTask {
    private final String interpreter;

    public PythonTask(String interpreter, String solutionDir) {
        super(solutionDir);
        this.interpreter = interpreter;
    }

    @Override
    protected boolean preExecution() {
        this.updateSourceCode(sourceCode);
        return true;
    }

    @Override
    protected String getTempFileName() {
        return String.format("%s.py", baseFileName);
    }

    @Override
    ProcessBuilder createExecutionProcess() {
        ProcessBuilder processBuilder = createProcessBuilder("../" + interpreter + "python", getUniqueSolutionDir() + getTempFileName());
        processBuilder.directory(new File(CONFIG.getUploadedFileDir()));
        return processBuilder;
    }

}
