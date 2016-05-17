package pl.edu.uj.ii.webapp.solution;

import com.google.common.base.MoreObjects;
import pl.edu.uj.ii.webapp.execute.SupportedLang;
import pl.edu.uj.ii.webapp.execute.UploadFile;

import java.util.UUID;

/**
 * Created by gauee on 4/7/16.
 */
public class Task {
    private final String solutionId;
    private final SupportedLang supportedLang;
    private final UploadFile uploadFile;

    public Task(SupportedLang supportedLang, UploadFile uploadFile) {
        this.supportedLang = supportedLang;
        this.uploadFile = uploadFile;
        this.solutionId = UUID.randomUUID().toString();
    }

    public SupportedLang getSupportedLang() {
        return supportedLang;
    }

    public UploadFile getUploadFile() {
        return uploadFile;
    }

    public String getSolutionId() {
        return solutionId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("solutionId", solutionId)
                .add("supportedLang", supportedLang)
                .add("uploadFile", uploadFile)
                .toString();
    }
}
