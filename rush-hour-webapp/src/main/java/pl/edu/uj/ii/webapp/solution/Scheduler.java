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
import pl.edu.uj.ii.webapp.execute.test.TestCaseDetails;
import pl.edu.uj.ii.webapp.execute.test.TestResult;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.Collections.nCopies;
import static java.util.concurrent.TimeUnit.SECONDS;
import static pl.edu.uj.ii.webapp.AppConfig.CONFIG;

/**
 * Created by gauee on 5/16/16.
 */
public class Scheduler extends Thread {
    private static final Logger LOGGER = Logger.getLogger(Scheduler.class);
    public static final String EXECUTED = "executed";
    public static final String PENDING = "pending";
    public static final String TIMED_OUT = "timed out";
    public static final String EXECUTION_ERROR = "execution error";
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
        Result newResult = new Result()
                .withMsg("Your solution is waiting in queue to execute.")
                .withId(task.getSolutionId())
                .withLang(task.getSupportedLang().toString())
                .withCreationDate(new Date())
                .withAuthor(task.getAuthor());
        resultDao.save(newResult);
    }

    private void processTask(Task task) {
        if (task == null) {
            LOGGER.info("No new task in queue.");
            return;
        }
        Result result = new Result()
                .withId(task.getSolutionId())
                .withMsg("");
        resultDao.update(result);
        String solutionId = task.getSolutionId();
        List<TestCaseDetails> testCaseDetailses = rushHourExecutor.runAllTestCases(task);
        if (testCaseDetailses.isEmpty()) {
            return;
        }
        for (TestCaseDetails testCaseDetails : testCaseDetailses) {
            ResultDetail resultDetail = new ResultDetail()
                    .withResultId(result.getId())
                    .withTestCaseId(testCaseDetails.getId())
                    .withDuration(0)
                    .withMsg(PENDING)
                    .withMoves(nCopies(testCaseDetails.getCasesAmount(), -1));
            resultDao.save(resultDetail);
            testCaseDetails.setResultDetail(resultDetail);

        }
        for (TestCaseDetails testCaseDetails : testCaseDetailses) {
            try {
                TestResult testResult = testCaseDetails.getResultFuture().get(CONFIG.getExecutionTimeoutInSec(), SECONDS);
                testCaseDetails.getResultDetail()
                        .withDuration(testResult.getDuration())
                        .withMoves(testResult.getStepsOfAllTestCases())
                        .withMsg(testResult.getExecutionMessage().isEmpty() ? EXECUTED : testResult.getExecutionMessage().substring(0, 128));
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error("Exception occurred during execution solution " + solutionId, e);
                testCaseDetails.getResultDetail()
                        .withMsg(EXECUTION_ERROR);
            } catch (TimeoutException e) {
                LOGGER.warn("Executing process for solution " + solutionId + " timed out");
                testCaseDetails.getResultFuture().cancel(true);
                testCaseDetails.getResultDetail()
                        .withMsg(TIMED_OUT)
                        .withDuration(SECONDS.toMillis(CONFIG.getExecutionTimeoutInSec()));
            }
            resultDao.update(testCaseDetails.getResultDetail());
        }
        LOGGER.info("All test cases executed for " + task.getSolutionId());
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
