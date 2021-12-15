package nl.codeengineer.aoc.aoc2021;

import nl.codeengineer.aoc.AocSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day15  implements AocSolver {

    int[][] map;

    @Override
    public long part1() throws IOException {
        map = getInput();

        return calcPath(0, 0,  map, new boolean[map.length][map[0].length], 0, Integer.MAX_VALUE) - map[0][0];
    }


    public long calcPath(int x, int y, int[][] map, boolean[][] visited, long currentScore, long lowestScore) {
        if (!isValidPosition(x, y, map) || visited[y][x]) {
            return Integer.MAX_VALUE;
        }

        currentScore += map[y][x];

        if (lowestScore < currentScore) {
            return currentScore;
        }

        if (y == map.length - 1 && x == map[0].length - 1) {
            return currentScore;
        }

        visited[y][x] = true;

        List<Point> neighbours = getNeighbours(x, y);
        neighbours = neighbours.stream().filter(p -> isValidPosition(p.x, p.y, map)).collect(Collectors.toList());
        neighbours.sort(Comparator.comparingInt(p -> map[p.y][p.x]));


        for(Point p: neighbours) {
            long newScore = calcPath(p.x, p.y, map, visited, currentScore, lowestScore);
            if (newScore < lowestScore) {
                lowestScore = newScore;
            }
        }

        visited[y][x] = false;

        return lowestScore;
    }

    private List<Point> getNeighbours(int x, int y) {
        List<Point> result = new ArrayList<>();
        result.add(new Point(x + 1, y));
        result.add(new Point(x , y + 1));
        result.add(new Point(x - 1, y));
        result.add(new Point(x, y - 1));
        return result;
    }

    private static boolean isValidPosition(int x, int y, int[][] map) {
        return x >= 0 && y >=0 && x < map[0].length && y < map.length;
    }



    @Override
    public long part2() throws IOException {
        return 0;
    }

    private static int[][] getInput() throws IOException {
        List<String> input = Files.readAllLines(Path.of("inputs/day15-test.txt"));

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
