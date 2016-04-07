package pl.edu.uj.ii.webapp.execute;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import pl.edu.uj.ii.webapp.execute.test.TestCase;
import pl.edu.uj.ii.webapp.execute.test.TestResult;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gauee on 4/7/16.
 */
public class RushHourExecutor {
    private static final Logger LOGGER = Logger.getLogger(RushHourExecutor.class);
    private final TaskFactory taskFactory;

    public RushHourExecutor(TaskFactory taskFactory) {
        this.taskFactory = taskFactory;
    }

    public List<TestResult> resolveAllTestCases(final Param param) {
        List<TestResult> result = Lists.newLinkedList();
        try {
            Task task = taskFactory.resolveTask(param);
            Files.list(Paths.get(getClass().getClassLoader().getResource("testCases").toURI())).filter(f -> f.endsWith(".in"))
                    .map(path -> {
                        File file = path.toFile();
                        return new TestCase(file.getName(), file);
                    })
                    .map(testCase -> new TestResult(testCase.getId(), task.resolveTestCases(testCase)))
                    .collect(Collectors.toCollection(() -> result));
        } catch (URISyntaxException | IOException e) {
            LOGGER.warn("Cannot execute code source " + param);
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Cannot execute code " + param, e);
        }
        return result;
    }
}
