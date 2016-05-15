package pl.edu.uj.ii.model;

import com.google.common.base.MoreObjects;

import java.awt.Point;

/**
 * Created by gauee on 3/31/16.
 */
public class Car {
    private final CarId id;
    private final Point point;
    private final Position position;
    private final int length;

    public Car(CarId id, Point startPoint, Position position, int length) {
        this.id = id;
        this.point = startPoint;
        this.position = position;
        this.length = length;
    }

    public CarId getId() {
        return id;
    }

    public Point getPoint() {
        return point;
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
                .add("point", point)
                .add("position", position)
                .add("length", length)
                .toString();
    }
}
