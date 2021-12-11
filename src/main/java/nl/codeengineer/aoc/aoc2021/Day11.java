package nl.codeengineer.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day11 {

    public static void main(String[] args) throws IOException {
        System.out.println(part1());
        System.out.println(part2());
    }

    private static long part1() throws IOException {
        int[][] input = getInput();

        long flashed = 0;

        for (int i = 0; i < 100; i++) {
            flashed += handleStep(input);
        }

        return flashed;
    }

    private static long part2() throws IOException {
        int[][] input = getInput();
        int size = input.length * input[0].length;

        int i = 0;
        long flashed = 0;
        while (flashed != size) {
            flashed = handleStep(input);
            i++;
        }

        return i;
    }

    private static long handleStep(int[][] input) {
        int flashed = 0;

        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[0].length; x++) {
                input[y][x]++;
            }
        }

        long flashedThisStep = -1;
        while (flashedThisStep != 0) {
            flashedThisStep = 0;
            for (int y = 0; y < input.length; y++) {
                for (int x = 0; x < input[0].length; x++) {
                    if (input[y][x] >= 10) {
                        flashedThisStep++;
                        input[y][x] = 0;

                        increaseNeighbors(x, y, input);
                    }
                }
            }
            flashed += flashedThisStep;
        }
        return flashed;
    }

    private static void increaseNeighbors(final int x, final int y, int[][] map) {
        for (int y1 = y - 1; y1 <= y+1; y1++) {
            for (int x1 = x - 1; x1 <= x + 1; x1++) {
                if (isValidPosition(x1, y1, map) && map[y1][x1] != 0 ) {
                    map[y1][x1]++;
                }
            }
        }
    }

    private static boolean isValidPosition(int x, int y, int[][] map) {
        return x >= 0 && y >=0 && x < map[0].length && y < map.length;
    }


    private static int[][] getInput() throws IOException {
        List<String> input = Files.readAllLines(Path.of("inputs/day11-1.txt"));

        int[][] result = new int[input.size()][input.get(0).length()];

        int y = 0;
        for(String line: input) {
            for (int x = 0; x < line.length(); x++) {
                result[y][x] = line.charAt(x) - '0';
            }
            y++;
        }

        return result;
    }
}
