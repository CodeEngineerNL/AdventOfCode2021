package nl.codeengineer.aoc.aoc2021;

import nl.codeengineer.aoc.AocSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Day08 implements AocSolver {

    private List<String> lines;

    public long part1() throws IOException {
        lines = getInput();
        long easyCount = 0;
        for (String line: lines) {
            String[] outputs = line.split("\\|")[1].trim().split(" ");
            HashSet<Integer> lenghts = new HashSet<>(Arrays.asList(2,4,3, 7));

            for (String outputValue: outputs) {
                if (lenghts.contains(outputValue.length())) {
                    easyCount++;
                }
            }
        }

        return easyCount;
    }

    public long part2() throws IOException {
        long total = 0;
        for (String line: lines) {
            String[] parts = line.split("\\|");

            List<String> uniques = Arrays.stream(parts[0].trim().split(" "))
                    .map(Day08::sortString).collect(Collectors.toList());

            List<String> output = Arrays.stream(parts[1].trim().split(" ")).map(Day08::sortString).toList();

            List<String> numSegments = Arrays.asList(calcNumSegments(uniques));

            int num = 0;
            for (String value: output) {
                num = num * 10;
                num = num + numSegments.indexOf(value);
            }

            total = total + num;

        }

        return total;
   }

    private static String[] calcNumSegments(List<String> uniques) {
        String[] numPatterns = new String[10];

        for (String pattern: uniques) {
            switch (pattern.length()) {
                case 2 -> numPatterns[1] = pattern;
                case 3 -> numPatterns[7] = pattern;
                case 4 -> numPatterns[4] = pattern;
                case 7 -> numPatterns[8] = pattern;
                default -> { /* do nothing */}
            }
        }

        uniques.remove(numPatterns[1]);
        uniques.remove(numPatterns[7]);
        uniques.remove(numPatterns[4]);
        uniques.remove(numPatterns[8]);

        // Of all numbers with 6 segments (0,6,9), only 9 contains all segments of 4
        numPatterns[9] = uniques.stream().filter(e -> e.length() == 6 && containsAll(e, numPatterns[4])).findFirst().get();
        uniques.remove(numPatterns[9]);

        // only 0 and 6 have 6 segments. 0 contains all segments of 1, 6 does not
        numPatterns[0] = uniques.stream().filter(e -> e.length() == 6 && containsAll(e, numPatterns[1])).findFirst().get();
        uniques.remove(numPatterns[0]);

        // One 6 has a length of 6 segments now.
        numPatterns[6] = uniques.stream().filter(e -> e.length() == 6).findFirst().get();
        uniques.remove(numPatterns[6]);

        // Only 2, 3 and 5 left. 3 contains all segments of 1
        numPatterns[3] = uniques.stream().filter(e -> containsAll(e, numPatterns[1])).findFirst().get();
        uniques.remove(numPatterns[3]);

        // Only 2 and 5 left. 9 contains all segments of 5.
        numPatterns[5] = uniques.stream().filter(e -> containsAll(numPatterns[9], e)).findFirst().get();
        uniques.remove(numPatterns[5]);

        // Only 2 is left now
        numPatterns[2] = uniques.get(0);

        return numPatterns;
    }

    private static boolean containsAll(String val1, String val2) {
        for (int i = 0; i < val2.length(); i++) {
            if (!val1.contains("" + val2.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static String sortString(String s) {
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        return String.copyValueOf(chars);
    }

    private static List<String> getInput() throws IOException {
        return Files.readAllLines(Path.of("inputs/day8-1.txt"));
    }
}
