package nl.codeengineer.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day9 {

    public static void main(String[] args) throws IOException {
        System.out.println(part1());
        System.out.println(part2());
    }

    private static long part1() throws IOException {
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

    private static long part2() throws IOException {
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

        for (Point p: lowest) {
            boolean[][] visited = new boolean[map.length][map[0].length];
            sizes.add(calcBasin(p.x, p.y, map, visited));
        }

        return sizes.stream().sorted(Comparator.reverseOrder()).limit(3).reduce(1L, (a, b) -> (a *b));
    }

    private static long calcBasin(final int x, final int y, final int[][] map, final boolean[][]visited) {
        if (visited[y][x]) {
            return 0;
        }

        visited[y][x] = true;
        int size = 1;

        int x1 = x + 1;
        while (x1 < map[0].length && map[y][x1] != 9 && map[y][x1] > map[y][x] ) {
            size += calcBasin(x1, y, map, visited);
            x1++;
        }

        x1 = x - 1;
        while (x1 >=0 && map[y][x1] != 9 && map[y][x1] > map[y][x] ) {
            size += calcBasin(x1, y, map, visited);
            x1--;
        }

        int y1 = y + 1;
        while (y1 < map.length && map[y1][x] != 9 && map[y1][x] > map[y][x] ) {
            size += calcBasin(x, y1, map, visited);
            y1++;
        }

        y1 = y - 1;
        while (y1 >=0 && map[y1][x] != 9 && map[y1][x] > map[y][x] ) {
            size += calcBasin(x, y1, map, visited);
            y1--;
        }

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
