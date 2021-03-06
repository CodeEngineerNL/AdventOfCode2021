package nl.codeengineer.aoc.aoc2021;

import nl.codeengineer.aoc.AocSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Day13 implements AocSolver {

    private Set<Point> points;
    private List<Point> folds;

    @Override
    public long part1() throws IOException {
        getInput();

        Set<Point> result = doFold(points, folds.get(0));
        return result.size();
    }

    @Override
    public long part2() throws IOException {
        getInput();

        for (Point fold: folds) {
            points = doFold(points, fold);
        }

        printMap(points);

        // Print the output and ready yourself :P
        return 0;
    }

    private void printMap(Set<Point> points) {
        int width = 0;
        int height = 0;

        for (Point p: points) {
            width = Integer.max(width,(int) p.x);
            height = Integer.max(height, (int) p.y);
        }

        boolean[][] map = new boolean[height+1][width+1];

        points.forEach(p -> map[(int) p.y][(int) p.x] = true);

        for (int y = 0; y <= height; y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x <= width; x++) {
                line.append(map[y][x] ? '#' : ' ');
            }
            System.out.println(line);
        }
    }

    public Set<Point> doFold(Set<Point> points, Point foldPoint) {
        if (foldPoint.x == 0) {
            return points.stream().map(p -> new Point(p.x, foldPoint(p.y, foldPoint.y))).collect(Collectors.toSet());
        } else {
            return points.stream().map(p -> new Point(foldPoint(p.x, foldPoint.x), p.y)).collect(Collectors.toSet());
        }
    }

    private long foldPoint(long i, long fold) {
        if (i > fold) {
            return fold - ( i - fold);
        } else {
            return i;
        }
    }

    public void getInput() throws IOException {
        List<String> inputLines = Files.readAllLines(Path.of("inputs/day13-1.txt"));

        Map<Boolean, List<String>> splitted = inputLines.stream().filter(l -> !l.isEmpty()).collect(Collectors.partitioningBy(l -> l.startsWith("fold along")));

        points = splitted.get(false).stream().map(l -> {
            String[] parts = l.split(",");
            return new Point(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
        }).collect(Collectors.toSet());

        folds = splitted.get(true).stream().map(l -> {
            String[] parts = l.replace("fold along ", "").split("=");
            if (parts[0].equals("y")) {
                return new Point(0, Long.parseLong(parts[1]));
            } else {
                return new Point(Long.parseLong(parts[1]), 0);
            }
        }).toList();
    }

    public record Point(long x, long y){}
}
