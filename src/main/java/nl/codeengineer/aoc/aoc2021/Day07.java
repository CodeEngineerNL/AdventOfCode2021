package nl.codeengineer.aoc.aoc2021;

import nl.codeengineer.aoc.AocSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day07 implements AocSolver {

   public long part1() throws IOException {
        List<Integer> input = getInput();
        input.sort(Integer::compareTo);

        int pos = input.get(input.size() / 2);

        int total = 0;
        for (Integer num: input) {
            total += Math.abs(num - pos);
        }
        return total;
    }

    public long part2() throws IOException {
        List<Integer> input = getInput();
        double avg = input.stream().mapToInt(Integer::intValue).average().getAsDouble();

        long f1 = calcFuel(input, (int) Math.floor(avg));
        long f2 = calcFuel(input, (int) Math.ceil(avg));

        return Long.min(f1, f2);
   }

    private static long calcFuel(List<Integer> input, int pos) {
        long total = 0;
        for (Integer num: input) {
            long dist = Math.abs(num - pos);
            total += (dist * (dist + 1)) / 2;
        }
        return total;
    }

    private static List<Integer> getInput() throws IOException {
        return new ArrayList<>(Arrays.stream(Files.readAllLines(Path.of("inputs/day7-1.txt")).get(0).split(","))
                .map(Integer::parseInt).toList());

    }

}
