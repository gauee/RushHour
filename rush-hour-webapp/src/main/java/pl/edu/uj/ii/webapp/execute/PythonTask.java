package pl.edu.uj.ii.webapp.execute;

import pl.edu.uj.ii.model.CarMove;
import pl.edu.uj.ii.webapp.execute.test.TestCase;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static java.io.File.separator;
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
        return String.format(solutionTargetDir + separator + "%s.py", this.baseFileName);
    }

    @Override
    public List<List<CarMove>> getOutputFor(TestCase testCase) {
        return Collections.emptyList();
    }

    @Override
    ProcessBuilder createExecutionProcess() {
        ProcessBuilder processBuilder = createProcessBuilder("../" + interpreter, solutionTargetDir + separator + baseFileName);
        processBuilder.directory(new File(CONFIG.getUploadedFileDir()));
        return processBuilder;
    }
}
