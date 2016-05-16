package pl.edu.uj.ii.webapp.solution;

import java.util.List;

/**
 * Created by gauee on 5/16/16.
 */
public class Solution {
    private final String id;
    private final String testCaseId;
    private final List<String> moves;

    public Solution(String id, String testCaseId, List<String> moves) {
        this.id = id;
        this.testCaseId = testCaseId;
        this.moves = moves;
    }

    public String getId() {
        return id;
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public List<String> getMoves() {
        return moves;
    }
}
