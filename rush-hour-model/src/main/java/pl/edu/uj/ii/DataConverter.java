package pl.edu.uj.ii;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import pl.edu.uj.ii.model.CarId;
import pl.edu.uj.ii.model.CarMove;
import pl.edu.uj.ii.model.Direction;

import java.util.List;

/**
 * Created by gauee on 4/8/16.
 */
public final class DataConverter {
    private DataConverter() {
    }

    public static List<List<CarMove>> parseOutputLines(List<String> lines) {
        List<List<CarMove>> carMovesTestResults = Lists.newLinkedList();
        List<CarMove> carMoves = Lists.newLinkedList();

        // ignore the amount of moves in the first line as we do not need this
        lines.remove(0);

        for (String line : lines) {
            if (StringUtils.isBlank(line)) {
                carMovesTestResults.add(carMoves);
                carMoves = Lists.newLinkedList();
            } else {
                carMoves.add(parseOutputLine(line));
            }
        }

        carMovesTestResults.add(carMoves);

        return carMovesTestResults;
    }

    private static CarMove parseOutputLine(String output) {
        String[] args = output.split(" ");

        if (args.length != 3) {
            throw new IllegalArgumentException("Cannot parse line: " + output);
        }

        return new CarMove(
                CarId.valueOf(args[0]),
                Direction.convert(args[1]),
                Byte.valueOf(args[2])
        );
    }
}
