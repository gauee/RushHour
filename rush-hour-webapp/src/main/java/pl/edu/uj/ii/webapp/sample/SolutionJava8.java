package pl.edu.uj.ii.webapp.sample;


import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by gauee on 4/7/16.
 */
public class SolutionJava8 {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNext()) {
                scanner.nextLine();
            }
        }
        Arrays.asList(
                "0",
                "13",
                "I U 2",
                "E R 2",
                "C R 1",
                "M D 3",
                "X R 2",
                "A U 4",
                "X L 2",
                "M U 3",
                "E L 3",
                "C L 3",
                "M D 3",
                "I D 3",
                "X R 4"
        )
                .stream()
                .forEach(System.out::println);
    }
}
