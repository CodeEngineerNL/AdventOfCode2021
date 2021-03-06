package nl.codeengineer.aoc.aoc2021;

import nl.codeengineer.aoc.AocSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day09 implements AocSolver {

  public long part1() throws IOException {
        int[][] map = getInput();

        List<Integer> lowest = new ArrayList<>();

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (isLowest(x,y, map)) {
                    lowest.add(map[y][x]);
                }
            }
        }

        return lowest.stream().mapToInt(i -> i + 1).sum();
    }

    public long part2() throws IOException {
        int[][] map = getInput();

        List<Point> lowest = new ArrayList<>();

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (isLowest(x,y, map)) {
                    lowest.add(new Point(x,y));
                }
            }
        }

        List<Long> sizes = new ArrayList<>();

        boolean[][] visited = new boolean[map.length][map[0].length];
        for (Point p: lowest) {
            sizes.add(calcBasin(p.x, p.y, map, visited));
        }

        return sizes.stream().sorted(Comparator.reverseOrder()).limit(3).reduce(1L, (a, b) -> (a *b));
    }

    private static long calcBasin(final int x, final int y, final int[][] map, final boolean[][]visited) {
        if (x < 0 || y < 0 || y >= map.length || x >= map[0].length || visited[y][x] || map[y][x] == 9) {
            return 0;
        }

        visited[y][x] = true;
        int size = 1;

        size += calcBasin(x + 1, y, map, visited);
        size += calcBasin(x - 1, y, map, visited);
        size += calcBasin(x, y + 1, map, visited);
        size += calcBasin(x, y - 1, map, visited);

        return size;
    }

    private static boolean isLowest(final int x, final int y, final int[][] map) {
        for (int y1 = y - 1; y1 <= y + 1; y1++) {
            if (y1 != y && isValidPosition(x,y1, map) && map[y1][x] <= map[y][x]) {
                return false;
            }
        }

        for (int x1 = x - 1; x1 <= x + 1; x1++) {
            if (x1 != x && isValidPosition(x1,y, map) && map[y][x1] <= map[y][x]) {
                return false;
            }
        }

        return true;
    }

    private static boolean isValidPosition(int x, int y, int[][] map) {
        return x >= 0 && y >=0 && x < map[0].length && y < map.length;
    }

    private static int[][] getInput() throws IOException {
        List<String> input = Files.readAllLines(Path.of("inputs/day9-1.txt"));

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

    public record Point(int x, int y){}
}
