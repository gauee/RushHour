package pl.edu.uj.ii.webapp.execute.tasks;

import pl.edu.uj.ii.webapp.execute.SupportedLang;
import pl.edu.uj.ii.webapp.solution.Task;

import java.io.IOException;
import java.util.Map;

/**
 * Created by gauee on 4/7/16.
 */
public class TaskFactory {
    private final Map<SupportedLang, pl.edu.uj.ii.webapp.execute.tasks.Task> supportedTask;

    public TaskFactory(Map<SupportedLang, pl.edu.uj.ii.webapp.execute.tasks.Task> supportedTask) {
        this.supportedTask = supportedTask;
    }

    public pl.edu.uj.ii.webapp.execute.tasks.Task createTask(Task solutionTask) throws IOException, ClassNotFoundException {
        pl.edu.uj.ii.webapp.execute.tasks.Task task = supportedTask.get(solutionTask.getSupportedLang());
        task.processUpload(solutionTask.getUploadFile());
        task.preExecution();
        return task;
    }
}
