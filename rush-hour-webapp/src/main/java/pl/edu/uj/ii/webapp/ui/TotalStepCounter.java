package pl.edu.uj.ii.webapp.ui;

import java.util.List;

import static pl.edu.uj.ii.verify.MovesChecker.ESCAPE_UNREACHABLE;

/**
 * Created by gauee on 5/14/16.
 */
public class TotalStepCounter {

    public int countSteps(List<Integer>... collectionOfSteps) {
        int totalSteps = 0;
        for (List<Integer> steps : collectionOfSteps) {
            for (Integer step : steps) {
                totalSteps += step != ESCAPE_UNREACHABLE ? step : 0;

            }
        }
        return totalSteps;
    }
}
