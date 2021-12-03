package nl.codeengineer.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day3 {

    public static void main(String[] args) throws IOException {
        System.out.println(part1());
        System.out.println(part2());
    }

    private static int part1() throws IOException {
        List<String> lines = getInput();

        int len = lines.get(0).length();
        StringBuilder gamma = new StringBuilder();
        StringBuilder epsilon = new StringBuilder();

        for (int i = 0; i < len; i++) {
            if (getOneToZeroDiff(i, lines) > 0) {
                gamma.append("1");
                epsilon.append("0");
            } else {
                gamma.append("0");
                epsilon.append("1");
            }
        }

        return Integer.parseInt(gamma.toString(), 2) * Integer.parseInt(epsilon.toString(), 2);
    }

    /**
     * Return the difference of zero's and one. Positive value == more ones, negative value == more zeroes
     * @param position String index to check
     * @param lines Lines of binary strings to check
     * @return Difference between the number of ones and zeroes at the given index
     */
    private static int getOneToZeroDiff(int position, List<String> lines ) {
        int one = 0;
        int zero = 0;
        for (String line: lines) {
            if (line.charAt(position) == '0') {
                zero++;
            } else {
                one++;
            }
        }

        return one - zero;
    }

    private static int part2() throws IOException {
        List<String> oxygenLines = getInput();
        List<String> scrubberLines = new ArrayList<>(oxygenLines);

        int len = oxygenLines.get(0).length();

        for (int i = 0; i < len; i++) {
            final int charIndex = i;

            if (oxygenLines.size() > 1) {
                final char oxygenFilterChar = getOneToZeroDiff(i, oxygenLines) >= 0 ? '1' : '0';
                oxygenLines = oxygenLines.stream().filter(l -> l.charAt(charIndex) == oxygenFilterChar).collect(Collectors.toList());
            }
            if (scrubberLines.size() > 1) {
                final char scrubberFilterChar = getOneToZeroDiff(i, scrubberLines) >= 0 ? '0' : '1';
                scrubberLines = scrubberLines.stream().filter(l -> l.charAt(charIndex) == scrubberFilterChar).collect(Collectors.toList());
            }
        }

       return Integer.parseInt(oxygenLines.get(0), 2) * Integer.parseInt(scrubberLines.get(0), 2);

    }

    private static List<String> getInput() throws IOException {
        return Files.readAllLines(Path.of("inputs/day3-1.txt"));

    }


}
