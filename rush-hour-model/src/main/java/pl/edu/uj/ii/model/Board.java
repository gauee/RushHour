package pl.edu.uj.ii.model;

import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import pl.edu.uj.ii.ConsoleDrawer;

import java.awt.Point;
import java.util.Arrays;
import java.util.Map;

import static pl.edu.uj.ii.model.Position.H;
import static pl.edu.uj.ii.model.Position.V;

/**
 * Created by gauee on 3/31/16.
 */
public class Board {
    private static final Logger LOGGER = Logger.getLogger(Board.class);
    public static final char EMPTY_POSITION = ' ';
    public static final int DEFAULT_SIZE = 6;
    private final int width;
    private final int height;
    private final Map<CarId, Car> cars = Maps.newHashMap();
    private final char[][] carsOnBoard;
    private ConsoleDrawer consoleDrawer = new ConsoleDrawer();

    public Board() {
        this(DEFAULT_SIZE, DEFAULT_SIZE);
    }

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
        Point startPoint = car.getPoint();
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
        Point startPoint = car.getPoint();
        int xDirection = getDirection(H, car.getPosition());
        int yDirection = getDirection(V, car.getPosition());
        for (int i = 0; i < car.getLength(); i++) {
            carsOnBoard[startPoint.x + i * xDirection][startPoint.y + i * yDirection] = boardSign;
        }
    }

    public boolean move(CarMove carMove) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Trying to move " + carMove + " on board " + consoleDrawer.printWithLog(this));
        }
        Car car = cars.get(carMove.getCarId());
        if (car == null) {
            LOGGER.debug("Car does not exist on board. " + carMove);
            return false;
        }
        if (car.getPosition() != carMove.getDirection().getWay()) {
            LOGGER.debug("Car has position " + car.getPosition() + " direction of move is " + carMove.getDirection() + ".");
            return false;
        }
        if (carMove.getSteps() < 0) {
            LOGGER.debug("Cannot move car by negative steps. " + carMove);
            return false;
        }
        int xDirection = getDirection(H, car.getPosition()) * carMove.getDirection().getCourse();
        int yDirection = getDirection(V, car.getPosition()) * carMove.getDirection().getCourse();
        Point startPoint = car.getPoint();
        int xPosition = startPoint.x + xDirection * carMove.getSteps();
        int yPosition = startPoint.y + yDirection * carMove.getSteps();
        if (isPositionNotReachable(xPosition, yPosition)) {
            LOGGER.debug("Car will be move out of the board (" + xPosition + "," + yPosition + ") for " + car + " and " + carMove);
            return false;
        }
        int offset = 0;
        for (int i = 0; i < carMove.getSteps(); i++) {
            int xCandidate = startPoint.x + (offset + i) * xDirection;
            int yCandidate = startPoint.y + (offset + i) * yDirection;
            if (isPositionNotReachable(xCandidate, yCandidate)) {
                LOGGER.debug("Invalid " + carMove + " for board " + consoleDrawer.printWithLog(this));
                return false;
            }
            char currentSign = carsOnBoard[xCandidate][yCandidate];
            if (currentSign != EMPTY_POSITION) {
                if (currentSign == car.getId().getId()) {
                    i--;
                    offset++;
                } else {
                    LOGGER.debug("Other car is on this position " + currentSign + " " + carMove + " on board" + consoleDrawer.printWithLog(this));
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
        return cars.get(id).getPoint().equals(point);
    }
}
