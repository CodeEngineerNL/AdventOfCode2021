package nl.codeengineer.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Day6 {

    public static void main(String[] args) throws IOException {
        System.out.println(part1());
        System.out.println(part2());
    }

    private static long part1() throws IOException {
        long[] fishCounts = getInput();

        for (int i = 0; i < 80; i++) {
            handeStap(fishCounts);
        }

        return Arrays.stream(fishCounts).reduce(Long::sum).getAsLong();    }

    private static long part2() throws IOException {
        long[] fishCounts = getInput();

        for (int i = 0; i < 256; i++) {
            handeStap(fishCounts);
        }

        return Arrays.stream(fishCounts).reduce(Long::sum).getAsLong();
    }

    private static void handeStap(long[] counts) {
        long newBorn = counts[0];
        for (int i = 1; i < 9; i++) {
            counts[i - 1] = counts[i];
        }

        counts[6] += newBorn;
        counts[8] = newBorn;
    }

    private static long[] getInput() throws IOException {
        long[] result = new long[9];

        Arrays.stream(Files.readAllLines(Path.of("inputs/day6-1.txt")).get(0).split(","))
                .mapToInt(Integer::parseInt).forEach(val -> result[val]++);

        return result;
    }

}
