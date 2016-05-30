package pl.edu.uj.ii.webapp.solution;

import com.google.common.collect.ImmutableMap;
import org.apache.log4j.Logger;
import pl.edu.uj.ii.webapp.db.Result;
import pl.edu.uj.ii.webapp.db.ResultDao;
import pl.edu.uj.ii.webapp.db.ResultDetail;
import pl.edu.uj.ii.webapp.execute.RushHourExecutor;
import pl.edu.uj.ii.webapp.execute.SupportedLang;
import pl.edu.uj.ii.webapp.execute.tasks.ExecutionTask;
import pl.edu.uj.ii.webapp.execute.tasks.TaskFactory;
import pl.edu.uj.ii.webapp.execute.test.TestResult;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 5/16/16.
 */
public class Scheduler extends Thread {
    private static final Logger LOGGER = Logger.getLogger(Scheduler.class);
    private final ResultDao resultDao;
    private BlockingQueue<Task> tasks = new LinkedBlockingDeque<>(32);
    private final RushHourExecutor rushHourExecutor;

    public Scheduler(ResultDao resultDao) {
        this.resultDao = resultDao;
        rushHourExecutor = new RushHourExecutor(new TaskFactory(initLanguages()));
    }

    @Override
    public void run() {
        while (true) {
            try {
                processTask(tasks.poll(10, TimeUnit.MINUTES));
            } catch (InterruptedException e) {
                LOGGER.error("Caught interrupted exception inside SolutionScheduler.");
            } catch (Exception e) {
                LOGGER.error("Caught unexpected exception", e);
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
        Result newResult = new Result()
                .withId(task.getSolutionId())
                .withLang(task.getSupportedLang().toString())
                .withCreationDate(new Date())
                .withAuthor(task.getAuthor());
        resultDao.save(newResult);
        String solutionId = task.getSolutionId();
        List<Future<TestResult>> futures = rushHourExecutor.runAllTestCases(task);
        if (futures.isEmpty()) {
            return;
        }
        for (Future<TestResult> future : futures) {
            TestResult testResult = new TestResult(EMPTY, 0, emptyList());
            try {
                testResult = future.get(CONFIG.getExecutionTimeoutInSec(), TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error("Exception occurred during execution solution " + solutionId, e);
            } catch (TimeoutException e) {
                LOGGER.warn("Executing process for solution " + solutionId + " timed out");
                future.cancel(true);
            }
            resultDao.save(new ResultDetail()
                    .withResultId(newResult.getId())
                    .withTestCaseId(testResult.getTestCaseId())
                    .withDuration(testResult.getDuration())
                    .withMoves(testResult.getStepsOfAllTestCases()));
        }
    }


    private Map<SupportedLang, ExecutionTask> initLanguages() {
        ImmutableMap.Builder<SupportedLang, ExecutionTask> mapBuilder = ImmutableMap.<SupportedLang, ExecutionTask>builder();
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
