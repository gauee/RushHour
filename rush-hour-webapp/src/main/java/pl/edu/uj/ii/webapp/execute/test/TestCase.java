package pl.edu.uj.ii.webapp.execute.test;

import pl.edu.uj.ii.model.Board;

import java.io.File;
import java.util.List;

/**
 * Created by gauee on 4/7/16.
 */
public class TestCase {
    private final String id;
    private final File file;
    private final List<Board> boards;

    public TestCase(String id, File file, List<Board> boards) {
        this.id = id;
        this.file = file;
        this.boards = boards;
    }

    public String getId() {
        return id;
    }

    public File getFile() {
        return file;
    }

    public List<Board> getBoards() {
        return boards;
    }
}
