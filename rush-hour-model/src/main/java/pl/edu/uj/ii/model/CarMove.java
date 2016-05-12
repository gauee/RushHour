package pl.edu.uj.ii.model;

/**
 * Created by gauee on 4/7/16.
 */
public class CarMove {
    private final CarId carId;
    private final Direction direction;
    private final int steps;

    public CarMove(CarId carId, Direction direction, int steps) {
        this.carId = carId;
        this.direction = direction;
        this.steps = steps;
    }

    public CarId getCarId() {
        return carId;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getSteps() {
        return steps;
    }

    @Override
    public String toString() {
        return carId + " " + direction + " " + steps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CarMove carMove = (CarMove) o;

        if (steps != carMove.steps) return false;
        if (carId != carMove.carId) return false;
        return direction == carMove.direction;

    }

    @Override
    public int hashCode() {
        int result = carId != null ? carId.hashCode() : 0;
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        result = 31 * result + steps;
        return result;
    }
}
