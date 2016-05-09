package pl.edu.uj.ii.webapp.execute;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.uj.ii.DataConverter;
import pl.edu.uj.ii.model.CarMove;
import pl.edu.uj.ii.webapp.execute.test.TestCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by shybovycha on 22/04/16.
 */
public abstract class Task {
    private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);
    private static final Duration RUN_TIMEOUT = Duration.ofSeconds(30);
    protected String baseFileName;
    protected Path sourceFile;
    protected String sourceCode;
    private final String solutionDir;

    public Task(String solutionDir) {
        this.solutionDir = createSolutionDir(solutionDir);
    }

    abstract ProcessBuilder createExecutionProcess();

    abstract String getTempFileName();

    protected List<String> runWithInput(File inputFile) {
        ProcessBuilder processBuilder = createExecutionProcess();
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectInput(inputFile);
        List<String> lines = Lists.newLinkedList();
        try {
            Process start = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(start.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
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

    public Task processUpload(UploadFile uploadFile) throws IOException {
        this.baseFileName = uploadFile.getName().split("\\.")[0];
        Path root = Paths.get(CONFIG.getUploadedFileDir(), solutionDir);
        this.sourceFile = Paths.get(root.toString(), getTempFileName());
        Files.createDirectories(sourceFile.getParent());
        this.sourceCode = uploadFile.getData();
        LOGGER.info("Source code to compile:\n" + StringUtils.replaceChars(sourceCode, '\n', ' '));
        this.updateSourceCode(sourceCode);
        return this;
    }

    public List<List<CarMove>> getOutputFor(TestCase testCase) {
        List<String> output = Lists.newArrayList();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future<List<String>> future = executor.submit(() -> runWithInput(testCase.getFile()));
        try {
            output = future.get(RUN_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            LOGGER.error("Running program has timed out");
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            executor.shutdownNow();
        }

        try {
            return DataConverter.parseOutputLines(output);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot parse output " + output, e);
        }
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

    public String getSolutionDir() {
        return solutionDir;
    }
}
