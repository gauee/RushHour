package pl.edu.uj.ii.webapp.execute.tasks;

import pl.edu.uj.ii.webapp.execute.SupportedLang;
import pl.edu.uj.ii.webapp.solution.Task;

import java.io.IOException;
import java.util.Map;

/**
 * Created by gauee on 4/7/16.
 */
public class TaskFactory {
    private final Map<SupportedLang, ExecutionTask> supportedTask;

    public TaskFactory(Map<SupportedLang, ExecutionTask> supportedTask) {
        this.supportedTask = supportedTask;
    }

    public ExecutionTask createTask(Task solutionTask) throws IOException, ClassNotFoundException {
        ExecutionTask executionTask = supportedTask.get(solutionTask.getSupportedLang());
        executionTask.processUpload(solutionTask.getUploadFile());
        return executionTask.preExecution() ? executionTask : null;
    }
}
