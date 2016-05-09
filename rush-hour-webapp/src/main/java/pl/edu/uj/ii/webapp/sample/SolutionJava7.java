package pl.edu.uj.ii.webapp.sample;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by gauee on 4/7/16.
 */
public class SolutionJava7 {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNext()) {
                scanner.nextLine();
            }
        }
        for (String step : Arrays.asList("C D 1", "N L 2", "M D 2", "X R 4", "C D 1", "N L 2", "M D 2", "X R 2")) {
            System.out.println(step);
        }
    }
}
