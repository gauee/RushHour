package pl.edu.uj.ii.webapp.execute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by gauee on 4/7/16.
 */
public abstract class CompilableTask extends Task {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompilableTask.class);

    public CompilableTask(String solutionDir) {
        super(solutionDir);
    }

    abstract void compile() throws IOException, ClassNotFoundException;

    @Override
    protected void preExecution() {
        try {
            compile();
        } catch (IOException e) {
            LOGGER.error(String.format("IO Error: %s", e.getMessage()));
        } catch (ClassNotFoundException e) {
            LOGGER.error(String.format("Class Not Found Error: %s", e.getMessage()));
        }
    }
}
