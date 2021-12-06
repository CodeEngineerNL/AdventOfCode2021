package nl.codeengineer.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;

public class Day6 {

    public static void main(String[] args) throws IOException {
        System.out.println(part1());
        System.out.println(part2());
    }

    private static long part1() throws IOException {
        HashMap<Integer, Long> fishCounts = getInput();

        for (int i = 0; i < 80; i++) {
            handeStap(fishCounts);
        }

        return fishCounts.values().stream().reduce(Long::sum).get();
    }

    private static long part2() throws IOException {
       HashMap<Integer, Long> fishCounts = getInput();

        for (int i = 0; i < 256; i++) {
            handeStap(fishCounts);
        }

        return fishCounts.values().stream().reduce(Long::sum).get();
    }

    private static void handeStap(HashMap<Integer, Long> counts) {
        for (int i = 0; i < 9; i++) {
            counts.put(i -1 , counts.get(i));
        }

        counts.put(6, counts.get(6) + counts.get(-1));
        counts.put(8, counts.get(-1));
        counts.put(-1, 0L);
    }

    private static HashMap<Integer, Long> getInput() throws IOException {
        HashMap<Integer, Long> result = new HashMap<>();
        for (int i = -1; i < 9; i++) {
            result.put(i, 0L);
        }

        Arrays.stream(Files.readAllLines(Path.of("inputs/day6-1.txt")).get(0).split(","))
                .map(Integer::parseInt).forEach(i -> result.put(i, result.get(i) + 1));
        return result;
    }

}
