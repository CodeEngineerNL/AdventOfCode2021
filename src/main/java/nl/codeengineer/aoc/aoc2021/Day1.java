package nl.codeengineer.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 {

    public static void main(String[] args) throws IOException {
        System.out.println(part1());
        System.out.println(part2());
    }

    private static int part1() throws IOException {
        List<Integer> lines = getInput();

        int count = 0;
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i) > lines.get(i - 1)) {
                count++;
            }
        }

        return count;
    }

    private static List<Integer> getInput() throws IOException {
        return Files.readAllLines(Path.of("inputs/day1-1.txt"))
                .stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    private static int part2() throws IOException {
        List<Integer> lines = getInput();

        int count = 0;
        int prev = 0;
        for (int i = 0; i < lines.size() - 2; i++) {
            int sum = lines.get(i) + lines.get(i + 1) + lines.get(i + 2);

            if (prev != 0 && (sum > prev)) {
                   count++;
            }

            prev = sum;
        }

        return count;
    }
}
