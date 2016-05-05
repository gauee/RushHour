package pl.edu.uj.ii.model;

import com.google.common.base.MoreObjects;

import java.awt.Point;

/**
 * Created by gauee on 3/31/16.
 */
public class Car {
    private final CarId id;
    private final Point startPoint;
    private final Position position;
    private final int length;

    public Car(CarId id, Point startPoint, Position position, int length) {
        this.id = id;
        this.startPoint = startPoint;
        this.position = position;
        this.length = length;
    }

    public CarId getId() {
        return id;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Position getPosition() {
        return position;
    }

    public int getLength() {
        return length;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("startPoint", startPoint)
                .add("position", position)
                .add("length", length)
                .toString();
    }
}
