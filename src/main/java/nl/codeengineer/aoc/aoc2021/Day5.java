package nl.codeengineer.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day5 {

    private static List<Line> lines;

    public static void main(String[] args) throws IOException {
        lines = getInput();

        System.out.println(part1());
        System.out.println(part2());
    }

    private static long part1() {
        Point max = getMaxCoord(lines);
        int[][]map = new int[max.x+1][max.y+1];

        lines.forEach(line -> drawLine(map, line, false));
        return Arrays.stream(map).flatMapToInt(Arrays::stream).filter(num -> num > 1).count();
    }

    private static long part2() {
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

    private static List<Line> getInput() throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day5-1.txt"));

        return lines.stream().map(line -> {
            Scanner scanner = new Scanner(line).useDelimiter("[, ]");
            int x1 = scanner.nextInt();
            int y1 = scanner.nextInt();
            scanner.next();
            int x2 = scanner.nextInt();
            int y2 = scanner.nextInt();
            return new Line(new Point(x1, y1), new Point(x2, y2));
        }).toList();
    }

    public record Point(int x, int y) {}
    public record Line(Point start, Point end) {}
}
