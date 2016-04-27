package pl.edu.uj.ii.webapp.execute;

import pl.edu.uj.ii.model.CarMove;
import pl.edu.uj.ii.webapp.execute.test.TestCase;

import java.util.Collections;
import java.util.List;

import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 4/7/16.
 */
public class PythonTask extends Task {
    @Override
    protected String getExecuteCommand() {
        return String.format("%s %s", CONFIG.getPython3Interpreter(), this.getSourceFilePath());
    }

    @Override
    protected String getTempFileName() {
        return String.format("%s.py", this.baseFileName);
    }

    @Override
    public List<List<CarMove>> getOutputFor(TestCase testCase) {
        return Collections.emptyList();
    }
}
