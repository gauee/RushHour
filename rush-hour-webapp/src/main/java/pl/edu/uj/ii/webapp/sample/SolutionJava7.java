package pl.edu.uj.ii.webapp.sample;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by gauee on 4/7/16.
 */
public class SolutionJava7 {
    public static final List<String> FIRST_TEST_CASE = Arrays.asList(
            "14",
            "I U 2",
            "E U 2",
            "M R 4",
            "A U 1",
            "C L 3",
            "E D 3",
            "X R 2",
            "A U 3",
            "X L 1",
            "E U 2",
            "M L 3",
            "E D 3",
            "I D 3",
            "X R 3"
    );

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNext()) {
                scanner.nextLine();
            }
        }
        for (String step : FIRST_TEST_CASE) {
            System.out.println(step);
        }
    }
}
