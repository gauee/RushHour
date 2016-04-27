package pl.edu.uj.ii.webapp.execute;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gauee on 4/7/16.
 */
public abstract class CompilableTask extends Task {
    protected boolean isCompiled = false;

    protected abstract Task compile() throws IOException, ClassNotFoundException;

    @Override
    protected List<String> runWithInput(File inputFile) {
        try {
            if (!this.isCompiled) {
                this.compile();
                this.isCompiled = true;
            }

            return super.runWithInput(inputFile);
        } catch (IOException e) {
            LOGGER.error(String.format("IO Error: %s", e.getMessage()));
        } catch (ClassNotFoundException e) {
            LOGGER.error(String.format("Class Not Found Error: %s", e.getMessage()));
        }

        return new ArrayList<String>();
    }
}
