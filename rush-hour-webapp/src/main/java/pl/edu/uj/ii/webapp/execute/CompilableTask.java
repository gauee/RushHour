package pl.edu.uj.ii.webapp.execute;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by gauee on 4/7/16.
 */
public abstract class CompilableTask extends Task {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompilableTask.class);
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

        return Lists.newArrayList();
    }
}
