package pl.edu.uj.ii.webapp.execute.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by gauee on 4/7/16.
 */
public abstract class CompilableTask extends Task {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompilableTask.class);

    public CompilableTask(String solutionDir) {
        super(solutionDir);
    }

    abstract void preCompile() throws IOException, ClassNotFoundException;

    abstract String createCompileCommand();

    @Override
    protected void preExecution() {
        try {
            preCompile();
            compile();
        } catch (IOException e) {
            LOGGER.error(String.format("IO Error: %s", e.getMessage()));
        } catch (ClassNotFoundException e) {
            LOGGER.error(String.format("Class Not Found Error: %s", e.getMessage()));
        }
    }

    public void compile() throws IOException, ClassNotFoundException {
        String sourceFile = this.sourceFile.toFile().getAbsolutePath();
        ProcessBuilder processBuilder = createProcessBuilder(createCompileCommand(), sourceFile);
        StringBuilder compilerOut = new StringBuilder();
        try {
            Process start = processBuilder.start();
            BufferedReader outputReader = new BufferedReader(new InputStreamReader(start.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(start.getErrorStream()));
            String line;
            compilerOut.append("Errors:");
            while ((line = errorReader.readLine()) != null) {
                compilerOut.append(line);
            }
            compilerOut.append(" Compiler output:");
            while ((line = outputReader.readLine()) != null) {
                compilerOut.append(line);
            }
        } catch (IOException e) {
            LOGGER.error("Cannot execute process.", e);
        }
        LOGGER.info("Compilation finished. " + compilerOut.toString());
    }

}