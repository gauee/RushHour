package pl.edu.uj.ii.webapp.execute;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.uj.ii.DataConverter;
import pl.edu.uj.ii.model.Board;
import pl.edu.uj.ii.model.CarMove;
import pl.edu.uj.ii.verify.MovesChecker;
import pl.edu.uj.ii.webapp.execute.tasks.ExecutionTask;
import pl.edu.uj.ii.webapp.execute.tasks.TaskFactory;
import pl.edu.uj.ii.webapp.execute.test.TestCase;
import pl.edu.uj.ii.webapp.execute.test.TestCaseDetails;
import pl.edu.uj.ii.webapp.execute.test.TestResult;
import pl.edu.uj.ii.webapp.solution.Task;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.apache.commons.io.FilenameUtils.removeExtension;
import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 4/7/16.
 */
public class RushHourExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RushHourExecutor.class);
    private final TaskFactory taskFactory;
    private ExecutorService taskExecutor = Executors.newFixedThreadPool(1);
    private final MovesChecker movesChecker = new MovesChecker();

    public RushHourExecutor(TaskFactory taskFactory) {
        this.taskFactory = taskFactory;
    }

    public List<TestCaseDetails> runAllTestCases(final Task solutionTask) {
        List<TestCaseDetails> testCaseDetailses = Lists.newLinkedList();
        try {
            List<TestCase> testCases = loadTestCases();
            ExecutionTask executionTask = this.taskFactory.createTask(solutionTask);
            if (executionTask == null) {
                LOGGER.warn("Cannot compile uploaded file.");
                return testCaseDetailses;
            }
            LOGGER.info(String.format("Running %d tests", testCases.size()));
            for (TestCase testCase : testCases) {
                Future<TestResult> testResultFuture = retrieveTestCaseOutputs(executionTask, testCase);
                testCaseDetailses.add(new TestCaseDetails(testCase.getId(), testCases.size(), testResultFuture));
            }
        } catch (ClassNotFoundException | IOException e) {
            LOGGER.warn("Cannot execute code " + solutionTask, e);
        }
        return testCaseDetailses;
    }

    private Future<TestResult> retrieveTestCaseOutputs(ExecutionTask executionTask, TestCase testCase) {
        return taskExecutor.submit(() -> {
            List<List<CarMove>> carMovesForAllBoards = emptyList();
            long duration = System.currentTimeMillis();
            try {
                List<String> outputLines = executionTask.runWithInput(testCase.getFile());
                carMovesForAllBoards = DataConverter.parseOutputLines(outputLines);
            } catch (Exception e) {
                LOGGER.error("Cannot retrieve result", e);
            }
            duration = System.currentTimeMillis() - duration;
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
        });
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
