package nl.codeengineer.aoc.aoc2021;

import nl.codeengineer.aoc.AocSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Day06 implements AocSolver {

   public long part1() throws IOException {
        long[] fishCounts = getInput();

        for (int i = 0; i < 80; i++) {
            handleStep(fishCounts);
        }

        return Arrays.stream(fishCounts).reduce(Long::sum).getAsLong();
    }

    public long part2() throws IOException {
        long[] fishCounts = getInput();

        for (int i = 0; i < 256; i++) {
            handleStep(fishCounts);
        }

        return Arrays.stream(fishCounts).reduce(Long::sum).getAsLong();
    }

    private static void handleStep(long[] counts) {
        long newBorn = counts[0];
        System.arraycopy(counts, 1, counts, 0, 8);

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
