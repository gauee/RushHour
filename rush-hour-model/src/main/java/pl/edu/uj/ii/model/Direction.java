package pl.edu.uj.ii.model;

import static pl.edu.uj.ii.model.Position.H;
import static pl.edu.uj.ii.model.Position.V;

/**
 * Created by gauee on 4/7/16.
 */
public enum Direction {
    Up("U", 1, V),
    Down("D", -1, V),
    Left("L", -1, H),
    Right("R", 1, H);

    private final String action;
    private final int course;
    private final Position way;

    Direction(String action, int course, Position way) {
        this.action = action;
        this.course = course;
        this.way = way;
    }

    public static Direction convert(String shotcut) {
        for (Direction direction : values()) {
            if (direction.getAction().equals(shotcut)) {
                return direction;
            }
        }
        return Direction.valueOf(shotcut);
    }

    public String getAction() {
        return action;
    }

    public int getCourse() {
        return course;
    }

    public Position getWay() {
        return way;
    }
}
