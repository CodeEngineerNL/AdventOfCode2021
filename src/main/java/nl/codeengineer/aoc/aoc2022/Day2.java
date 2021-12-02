package nl.codeengineer.aoc.aoc2022;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day2 {

    public static void main(String[] args) throws IOException {
        System.out.println(part1());
        System.out.println(part2());
    }

    private static int part1() throws IOException {
        int x = 0;
        int depth = 0;

        List<Command> commands = getInput();

        for (Command command: commands) {
            switch (command.direction) {
                case "forward" -> x = x + command.distance;
                case "down" -> depth = depth + command.distance;
                case "up" -> depth = depth - command.distance;
            }
        }

        return x * depth;
    }

    private static int part2() throws IOException {
        int x = 0;
        int depth = 0;
        int aim = 0;

        List<Command> commands = getInput();

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

    private static List<Command> getInput() throws IOException {
        return Files.readAllLines(Path.of("inputs/day2-1.txt"))
                .stream().map(line -> {
                    Scanner scanner = new Scanner(line);
                    return new Command(scanner.next(), scanner.nextInt());
                }).collect(Collectors.toList());
    }

    public record Command(String direction, int distance) {}
}
