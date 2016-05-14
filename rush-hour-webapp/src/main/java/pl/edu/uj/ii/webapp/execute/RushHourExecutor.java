package pl.edu.uj.ii.webapp.execute;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.uj.ii.DataConverter;
import pl.edu.uj.ii.model.Board;
import pl.edu.uj.ii.model.CarMove;
import pl.edu.uj.ii.verify.MovesChecker;
import pl.edu.uj.ii.webapp.execute.tasks.Task;
import pl.edu.uj.ii.webapp.execute.tasks.TaskFactory;
import pl.edu.uj.ii.webapp.execute.test.TestCase;
import pl.edu.uj.ii.webapp.execute.test.TestResult;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.io.FilenameUtils.removeExtension;
import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 4/7/16.
 */
public class RushHourExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RushHourExecutor.class);
    private final TaskFactory taskFactory;
    private ExecutorService taskExecutor = Executors.newFixedThreadPool(2);
    private static final long RUN_TIMEOUT = TimeUnit.SECONDS.toMillis(CONFIG.getExecutionTimeoutInSec());
    private final MovesChecker movesChecker = new MovesChecker();

    public RushHourExecutor(TaskFactory taskFactory) {
        this.taskFactory = taskFactory;
    }

    public List<TestResult> runAllTestCases(final Param param) {
        try {
            List<TestCase> testCases = loadTestCases();
            Task task = this.taskFactory.createTask(param);
            LOGGER.info(String.format("Running %d tests", testCases.size()));
            List<TestResult> results = testCases.stream()
                    .map(testCase -> retrieveTestCaseOutputs(task, testCase))
                    .collect(Collectors.toList());
            LOGGER.info(String.format("RESULTS: %s", results));
            return results;
        } catch (ClassNotFoundException | IOException e) {
            LOGGER.warn("Cannot execute code " + param, e);
        }
        return emptyList();
    }

    private TestResult retrieveTestCaseOutputs(Task task, TestCase testCase) {
        long duration = System.currentTimeMillis();
        List<String> outputLines = getOutput(task, testCase);
        duration = System.currentTimeMillis() - duration;
        List<List<CarMove>> carMovesForAllBoards = DataConverter.parseOutputLines(outputLines);
        List<Integer> stepsForAllBoards = Lists.newLinkedList();
        for (Board board : testCase.getBoards()) {
            List<CarMove> carMoves = carMovesForAllBoards.isEmpty() ? emptyList() : carMovesForAllBoards.remove(0);
            int steps = movesChecker.canSpecialCarEscapeBoard(board, carMoves);
            stepsForAllBoards.add(steps);
        }
        return new TestResult(
                testCase.getId(),
                duration,
                stepsForAllBoards
        );
    }

    public List<String> getOutput(Task task, TestCase testCase) {
        final Future<List<String>> future = taskExecutor.submit(() -> task.runWithInput(testCase.getFile()));
        try {
            return future.get(RUN_TIMEOUT, MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            LOGGER.error("Running program has timed out");
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return emptyList();
    }

    private List<TestCase> loadTestCases() {
        try {
            return Files.list(Paths.get(getClass().getClassLoader().getResource(CONFIG.getTestCasesDir()).toURI()))
                    .map(this::convertToTestCase)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException | URISyntaxException e) {
            LOGGER.warn("Cannot load test cases from " + CONFIG.getTestCasesDir());
        }
        return emptyList();
    }

    private TestCase convertToTestCase(Path path) {
        try (InputStream is = new FileInputStream(path.toFile())) {
            return new TestCase(
                    removeExtension(path.toFile().getName()),
                    path.toFile(),
                    DataConverter.parseInput(IOUtils.readLines(is))
            );
        } catch (Exception e) {
            LOGGER.warn("Cannot load input for " + path.toString());
            return null;
        }
    }
}
