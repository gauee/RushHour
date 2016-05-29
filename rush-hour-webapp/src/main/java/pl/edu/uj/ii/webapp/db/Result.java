package pl.edu.uj.ii.webapp.db;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Date;
import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * Created by gauee on 5/29/16.
 */
public class Result {
    private String id;
    private String author;
    private String lang;
    private Date creationDate;
    private Set<ResultDetail> details = emptySet();

    public String getId() {
        return id;
    }

    public Result withId(String id) {
        this.id = id;
        return this;
    }

    public Result withAuthor(String author) {
        this.author = author;
        return this;
    }

    public Result withLang(String lang) {
        this.lang = lang;
        return this;
    }

    public Result withCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public String getLang() {
        return lang;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Result withDetails(Set<ResultDetail> details) {
        this.details = details;
        return this;
    }

    public Set<ResultDetail> getDetails() {
        return details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return Objects.equal(id, result.id) &&
                Objects.equal(author, result.author) &&
                Objects.equal(lang, result.lang) &&
                Objects.equal(creationDate, result.creationDate) &&
                Objects.equal(details, result.details);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, author, lang, creationDate, details);
    }

    public Object[] toObjects() {
        return new Object[]{
                id,
                author,
                lang,
                creationDate
        };
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("author", author)
                .add("lang", lang)
                .add("creationDate", creationDate)
                .add("details", details)
                .toString();
    }
}
