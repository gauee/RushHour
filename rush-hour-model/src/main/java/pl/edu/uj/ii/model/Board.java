package pl.edu.uj.ii.model;

import com.google.common.collect.Maps;

import java.awt.Point;
import java.util.Arrays;
import java.util.Map;

import static pl.edu.uj.ii.model.Position.H;
import static pl.edu.uj.ii.model.Position.V;

/**
 * Created by gauee on 3/31/16.
 */
public class Board {
    public static final char EMPTY_POSITION = ' ';
    private final int width;
    private final int height;
    private final Map<CarId, Car> cars = Maps.newHashMap();
    private final char[][] carsOnBoard;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.carsOnBoard = new char[width][height];
        for (char[] rows : carsOnBoard) {
            Arrays.fill(rows, EMPTY_POSITION);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean add(Car car) {
        if (!canCarBeAdd(car)) {
            return false;
        }
        CarId carId = car.getId();
        putCarOnPosition(car, carId.getId());
        cars.put(carId, car);
        return true;
    }

    private boolean canCarBeAdd(Car car) {
        Point startPoint = car.getStartPoint();
        int xDirection = getDirection(H, car.getPosition());
        int yDirection = getDirection(V, car.getPosition());
        for (int i = 0; i < car.getLength(); i++) {
            int xPosition = startPoint.x + i * xDirection;
            int yPosition = startPoint.y + i * yDirection;
            if (isPositionNotReachable(xPosition, yPosition) || carsOnBoard[xPosition][yPosition] != EMPTY_POSITION) {
                return false;
            }
        }
        return true;
    }

    private boolean isPositionNotReachable(int xPosition, int yPosition) {
        return xPosition < 0
                || yPosition < 0
                || xPosition >= width
                || yPosition >= height;
    }

    private void putCarOnPosition(Car car, char boardSign) {
        Point startPoint = car.getStartPoint();
        int xDirection = getDirection(H, car.getPosition());
        int yDirection = getDirection(V, car.getPosition());
        for (int i = 0; i < car.getLength(); i++) {
            carsOnBoard[startPoint.x + i * xDirection][startPoint.y + i * yDirection] = boardSign;
        }
    }

    public boolean move(CarMove carMove) {
        Car car = cars.get(carMove.getCarId());
        if (car == null) {
            return false;
        }
        if (car.getPosition() != carMove.getDirection().getWay()) {
            return false;
        }
        int xDirection = getDirection(H, car.getPosition()) * carMove.getDirection().getCourse();
        int yDirection = getDirection(V, car.getPosition()) * carMove.getDirection().getCourse();
        Point startPoint = car.getStartPoint();
        int xPosition = startPoint.x + xDirection * carMove.getSteps();
        int yPosition = startPoint.y + yDirection * carMove.getSteps();
        if (isPositionNotReachable(xPosition, yPosition)) {
            return false;
        }
        int offset = 0;
        for (int i = 0; i < carMove.getSteps(); i++) {
            char currentSign = carsOnBoard[startPoint.x + (offset + i) * xDirection][startPoint.y + (offset + i) * yDirection];
            if (currentSign != EMPTY_POSITION) {
                if (currentSign == car.getId().getId()) {
                    i--;
                    offset++;
                } else {
                    return false;
                }
            }
        }
        putCarOnPosition(car, EMPTY_POSITION);
        startPoint.move(xPosition, yPosition);
        putCarOnPosition(car, car.getId().getId());
        return true;
    }

    private int getDirection(Position position, Position carPosition) {
        return position == carPosition ? 1 : 0;
    }

    public char[][] getCarsOnBoard() {
        return carsOnBoard;
    }

    public boolean isCarAtPosition(CarId id, Point point) {
        return cars.get(id).getStartPoint().equals(point);
    }
}
