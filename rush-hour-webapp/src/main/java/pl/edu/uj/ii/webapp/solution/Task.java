package pl.edu.uj.ii.webapp.solution;

import com.google.common.base.MoreObjects;
import pl.edu.uj.ii.webapp.execute.SupportedLang;
import pl.edu.uj.ii.webapp.execute.UploadFile;

import java.util.UUID;

/**
 * Created by gauee on 4/7/16.
 */
public class Task {
    private final String author;
    private final String solutionId;
    private final SupportedLang supportedLang;
    private final UploadFile uploadFile;

    public Task(String author, SupportedLang supportedLang, UploadFile uploadFile) {
        this.author = author;
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

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("author", author)
                .add("solutionId", solutionId)
                .add("supportedLang", supportedLang)
                .toString();
    }

}
