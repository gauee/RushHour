package pl.edu.uj.ii.webapp.solution;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import pl.edu.uj.ii.webapp.execute.RushHourExecutor;
import pl.edu.uj.ii.webapp.execute.SupportedLang;
import pl.edu.uj.ii.webapp.execute.tasks.TaskFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by gauee on 5/16/16.
 */
public class Scheduler extends Thread {
    private static final Logger LOGGER = Logger.getLogger(Scheduler.class);
    public static final int TEST_CASES_AMOUNT = 4;
    private final Source source;
    private BlockingQueue<Task> tasks = new LinkedBlockingDeque<>(32);
    private final RushHourExecutor rushHourExecutor;

    public Scheduler(Source source) {
        this.source = source;
        rushHourExecutor = new RushHourExecutor(new TaskFactory(initLanguages()));
    }

    @Override
    public void run() {
        while (true) {
            try {
                processTask(tasks.poll(1, TimeUnit.MINUTES));
            } catch (InterruptedException e) {
                LOGGER.error("Caught interrupted exception inside SolutionScheduler.");
            }
        }
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    private void processTask(Task task) {
        if (task == null) {
            LOGGER.info("No new task in queue.");
            return;
        }

        String solutionId = task.getSolutionId();
        source.startProcessing(solutionId, TEST_CASES_AMOUNT);
        for (int i = 0; i < TEST_CASES_AMOUNT; i++) {
            List<String> steps = Lists.newArrayList();
            for (int j = 0; j < (int) (Math.random() * 100); j++) {
                steps.add(UUID.randomUUID().toString());
            }
            source.save(new Solution(
                    solutionId,
                    "testCase" + i,
                    steps
            ));
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                LOGGER.error("Temporary solution");
            }
        }


    }

//    public List<TestResult> runAllTestCases(final SolutionTask solutionTask) {
//        try {
//            List<TestCase> testCases = loadTestCases();
//            Task task = this.taskFactory.createTask(solutionTask);
//            LOGGER.info(String.format("Running %d tests", testCases.size()));
//            List<TestResult> results = testCases.stream()
//                    .map(testCase -> retrieveTestCaseOutputs(task, testCase))
//                    .collect(Collectors.toList());
//            LOGGER.info(String.format("RESULTS: %s", results));
//            return results;
//        } catch (ClassNotFoundException | IOException e) {
//            LOGGER.warn("Cannot execute code " + solutionTask, e);
//        }
//        return emptyList();
//    }

    private Map<SupportedLang, pl.edu.uj.ii.webapp.execute.tasks.Task> initLanguages() {
        ImmutableMap.Builder<SupportedLang, pl.edu.uj.ii.webapp.execute.tasks.Task> mapBuilder = ImmutableMap.<SupportedLang, pl.edu.uj.ii.webapp.execute.tasks.Task>builder();
        for (SupportedLang supportedLang : SupportedLang.values()) {
            mapBuilder.put(
                    supportedLang,
                    supportedLang.createTask()
            );
        }
        return mapBuilder
                .build();
    }
}
