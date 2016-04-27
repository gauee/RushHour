package pl.edu.uj.ii.webapp.execute;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import pl.edu.uj.ii.model.CarMove;
import pl.edu.uj.ii.webapp.execute.test.TestCase;
import pl.edu.uj.ii.webapp.execute.test.TestResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.io.FilenameUtils.removeExtension;
import static org.apache.commons.lang3.tuple.Pair.of;
import static pl.edu.uj.ii.DataConverter.parseOutputLines;
import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 4/7/16.
 */
public class RushHourExecutor {
    private static final Logger LOGGER = Logger.getLogger(RushHourExecutor.class);
    private final TaskFactory taskFactory;
    private final Map<TestCase, List<List<CarMove>>> testCases;

    public RushHourExecutor(TaskFactory taskFactory) {
        this.taskFactory = taskFactory;
        this.testCases = this.loadTestCases();
    }

    public List<TestResult> runAllTestCases(final Param param) {
        try {
            Task task = this.taskFactory.createTask(param);

            LOGGER.debug(String.format("Running %d tests", this.testCases.size()));

            List<TestResult> results = this.testCases.entrySet()
                    .stream()
                    .map(entry -> of(entry, task.getOutputFor(entry.getKey())))
                    .map(pair -> new TestResult(pair.getLeft().getKey().getId(), pair.getRight(), pair.getLeft().getValue()))
                    .collect(Collectors.toList());

            LOGGER.debug(String.format("RESULTS: %s", results));

            return results;
        } catch (ClassNotFoundException | IOException e) {
            LOGGER.warn("Cannot execute code " + param, e);
        }

        return emptyList();
    }

    private Map<TestCase, List<List<CarMove>>> loadTestCases() {
        try {
            return Files.list(Paths.get(getClass().getClassLoader().getResource(CONFIG.getTestCasesDir()).toURI()))
                    .collect(Collectors.toMap(
                            p -> removeExtension(p.toFile().getName()),
                            p -> of(p.toFile(), p.toFile()),
                            (p1, p2) -> "in".equals(getExtension(p1.getLeft().getName())) ? of(p1.getLeft(), p2.getRight()) : of(p2.getLeft(), p1.getRight())
                    ))
                    .entrySet().stream()
                    .map(testCasePair -> of(
                            new TestCase(testCasePair.getKey(), testCasePair.getValue().getLeft()),
                            loadExpectedCarMoves(testCasePair.getValue().getRight())
                    ))
                    .collect(Collectors.toMap(pair -> pair.getLeft(), pair -> pair.getRight()));
        } catch (IOException | URISyntaxException e) {
            LOGGER.warn(String.format("Cannot load test cases from %s", CONFIG.getTestCasesDir()), e);
        }

        return emptyMap();
    }

    private List<List<CarMove>> loadExpectedCarMoves(File expectedResult) {
        List<String> lines = Lists.newLinkedList();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(expectedResult))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IllegalArgumentException | IOException e) {
            LOGGER.error("Cannot load file " + expectedResult.getAbsolutePath(), e);
        }

        return parseOutputLines(lines);
    }
}
