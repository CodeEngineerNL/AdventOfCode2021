package nl.codeengineer.aoc.aoc2021;

import nl.codeengineer.aoc.AocSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day05 implements AocSolver {

    private List<Line> lines;

    public long part1() throws IOException {
        this.lines = getInput();
        Point max = getMaxCoord(lines);
        int[][]map = new int[max.x+1][max.y+1];

        lines.forEach(line -> drawLine(map, line, false));
        return Arrays.stream(map).flatMapToInt(Arrays::stream).filter(num -> num > 1).count();
    }

    public long part2() {
        Point max = getMaxCoord(lines);
        int[][]map = new int[max.x+1][max.y+1];

        lines.forEach(line -> drawLine(map, line, true));
        return Arrays.stream(map).flatMapToInt(Arrays::stream).filter(num -> num > 1).count();
    }

    private static Point getMaxCoord(List<Line> lines) {
        int maxX = 0;
        int maxY = 0;

        for (Line line: lines) {
            if (line.start.x > maxX) {
                maxX = line.start.x;
            }
            if (line.end.x > maxX) {
                maxX = line.end.x;
            }
            if (line.start.y > maxY) {
                maxY = line.start.y;
            }
            if (line.end.y > maxY) {
                maxY = line.end.y;
            }
        }

        return new Point(maxX, maxY);
    }

    private static void drawLine(int[][] map, Line line, boolean handleDiag) {
        if (line.start.x == line.end.x) {
            int x = line.start.x;
            int startY = Math.min(line.start.y, line.end.y);
            int endY = Math.max(line.start.y, line.end.y);

            while (startY <= endY) {
                map[x][startY]++;
                startY++;
            }
        } else if (line.start.y == line.end.y) {
            int y = line.start.y;
            int startX = Math.min(line.start.x, line.end.x);
            int endX = Math.max(line.start.x, line.end.x);

            while (startX <= endX) {
                map[startX][y]++;
                startX++;
            }
        } else if (handleDiag) {
            int x = line.start.x;
            int y = line.start.y;
            final int stepX = line.start.x < line.end.x ? 1 : -1;
            final int stepY = line.start.y < line.end.y ? 1 : -1;

            while (x != line.end.x) {
                map[x][y]++;
                x += stepX;
                y += stepY;
            }
            map[x][y]++;
        }
    }

    private List<Line> getInput() throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day5-1.txt"));

        return lines.stream().map(line -> {
            String[] parts = line.split(" -> ");

            String[] p1parts = parts[0].split(",");
            String[] p2parts = parts[1].split(",");
            return new Line(
                    new Point(Integer.parseInt(p1parts[0]), Integer.parseInt(p1parts[1])),
                    new Point(Integer.parseInt(p2parts[0]), Integer.parseInt(p2parts[1])));
        }).toList();
    }

    public record Point(int x, int y) {}
    public record Line(Point start, Point end) {}
}
