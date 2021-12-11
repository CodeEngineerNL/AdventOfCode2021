package nl.codeengineer.aoc.aoc2021;

import nl.codeengineer.aoc.AocSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day02 implements AocSolver {

    private List<Command> commands;

    public long part1() throws IOException {
        commands = getInput();
        long x = 0;
        int depth = 0;

        for (Command command: commands) {
            switch (command.direction) {
                case "forward" -> x = x + command.distance;
                case "down" -> depth = depth + command.distance;
                case "up" -> depth = depth - command.distance;
            }
        }

        return x * depth;
    }

    public long part2() throws IOException {
        int x = 0;
        int depth = 0;
        int aim = 0;

        for (Command command: commands) {
            switch (command.direction) {
                case "forward" -> {
                    x = x + command.distance;
                    depth = depth + (command.distance * aim);
                }
                case "down" -> aim = aim + command.distance;
                case "up" -> aim = aim - command.distance;
            }
        }

        return x * depth;
    }

    private List<Command> getInput() throws IOException {
        return Files.readAllLines(Path.of("inputs/day2-1.txt"))
                .stream().map(line -> {
                    String[] parts = line.split(" ");
                    return new Command(parts[0], Integer.parseInt(parts[1]));
                }).collect(Collectors.toList());
    }

    public record Command(String direction, int distance) {}
}
