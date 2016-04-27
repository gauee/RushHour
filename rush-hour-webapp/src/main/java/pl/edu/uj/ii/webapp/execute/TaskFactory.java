package pl.edu.uj.ii.webapp.execute;

import java.io.IOException;
import java.util.Map;

/**
 * Created by gauee on 4/7/16.
 */
public class TaskFactory {
    private final Map<SupportedLang, Task> supportedTask;

    public TaskFactory(Map<SupportedLang, Task> supportedTask) {
        this.supportedTask = supportedTask;
    }

    public Task createTask(Param param) throws IOException, ClassNotFoundException {
        Task task = supportedTask.get(param.getSupportedLang());

        return task.processUpload(param.getUploadFile());
    }
}
