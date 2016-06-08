package pl.edu.uj.ii.webapp.execute.tasks;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.uj.ii.webapp.execute.UploadFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

public abstract class ExecutionTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionTask.class);
    protected String baseFileName;
    protected Path sourceFile;
    protected String sourceCode;
    private final String solutionDir;
    private String uniqueSolutionDir;

    public ExecutionTask(String solutionDir) {
        this.solutionDir = solutionDir;
    }

    abstract ProcessBuilder createExecutionProcess();

    abstract String getTempFileName();

    abstract protected boolean preExecution();

    private void initSolutionDir() {
        this.uniqueSolutionDir = createSolutionDir(solutionDir);
    }

    public List<String> runWithInput(File inputFile) {
        ProcessBuilder processBuilder = createExecutionProcess();
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectInput(inputFile);
        List<String> lines = Lists.newLinkedList();
        try {
            LOGGER.info("Started process with input " + inputFile.getName());
            Process process = processBuilder.start();
            new ProcessAwaitThread(process).start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while (process.isAlive() && (line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            LOGGER.info("Read lines: " + lines);
        } catch (IOException e) {
            LOGGER.error("Cannot execute process.", e);
        }
        return lines;
    }

    protected void updateSourceCode(String newCode) {
        try {
            Files.write(this.sourceFile, newCode.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error("Can not update source code", e);
        }
    }

    public void processUpload(UploadFile uploadFile) throws IOException {
        initSolutionDir();
        this.baseFileName = uploadFile.getName().split("\\.")[0];
        Path root = Paths.get(CONFIG.getUploadedFileDir(), getUniqueSolutionDir());
        this.sourceFile = Paths.get(root.toString(), getTempFileName());
        Files.createDirectories(sourceFile.getParent());
        this.sourceCode = uploadFile.getData();
    }


    protected ProcessBuilder createProcessBuilder(String command, String args) {
        LOGGER.info("Create command: " + command + ", args: " + args);
        ProcessBuilder processBuilder = new ProcessBuilder(command, args);
        processBuilder.redirectErrorStream(true);
        return processBuilder;
    }

    private static String createSolutionDir(String dirType) {
        return String.format("runtime/%s_%d/", dirType, System.currentTimeMillis());
    }

    public String getUniqueSolutionDir() {
        return uniqueSolutionDir;
    }

    class ProcessAwaitThread extends Thread {
        private final Process process;

        public ProcessAwaitThread(Process process) {
            this.process = process;
        }

        @Override
        public void run() {
            try {
                process.waitFor(CONFIG.getExecutionTimeoutInSec(), TimeUnit.SECONDS);
                process.destroy();
            } catch (InterruptedException e) {
                LOGGER.info("Caught InterruptedException inside Executor");
            }
        }
    }
}
