package nl.codeengineer.aoc.aoc2021;

import nl.codeengineer.aoc.AocSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day10 implements AocSolver {

    public long part1() throws IOException {
        List<String> input = getInput();

        long score = 0;

        for (String line : input) {
            char result = getFirstIllegalChar(line);
            score = score + switch (result) {
                case ')' -> 3;
                case ']' -> 57;
                case '}' -> 1197;
                case '>' -> 25137;
                default -> 0;
            };
        }

        return score;
    }

    public long part2() throws IOException {
        List<String> input = getInput();
        List<String> incomplete = input.stream().filter(s -> getFirstIllegalChar(s) == 0).toList();

        List<Long> scores = new ArrayList<>();
        for (String line: incomplete) {
            String remainder = getRemainder(line);

            long score = 0;
            for (int i = 0; i < remainder.length(); i++) {
                score *= 5;

                score = score + switch (remainder.charAt(i)) {
                    case ')' -> 1;
                    case ']' -> 2;
                    case '}' -> 3;
                    case '>' -> 4;
                    default -> 0;
                };
            }

            scores.add(score);
        }

        scores.sort(Comparator.naturalOrder());
        return scores.get(scores.size() / 2);
    }


    private static char getFirstIllegalChar(String line) {
        Deque<Character> stack = new ArrayDeque<>();

        char wrong = 0;

        for (int i = 0; i < line.length() && wrong == 0; i++) {
            wrong = switch (line.charAt(i)) {
                case '[', '(', '{', '<' -> { stack.push(line.charAt(i)); yield 0; }
                case ']' -> stack.pop() != '[' ? line.charAt(i) : 0;
                case ')' -> stack.pop() != '(' ? line.charAt(i) : 0;
                case '}' -> stack.pop() != '{' ? line.charAt(i) : 0;
                case '>' -> stack.pop() != '<' ? line.charAt(i) : 0;
                default -> 0;
            };
        }

        return wrong;
    }

    private static String getRemainder(String line) {
        Deque<Character> stack = new ArrayDeque<>();

        for (int i = 0; i < line.length(); i++) {
            switch (line.charAt(i)) {
                case '[', '(', '{', '<' -> stack.push(line.charAt(i));
                case ']', ')', '}', '>' -> stack.pop();
            }
        }

        StringBuilder remainder = new StringBuilder();

        while (!stack.isEmpty()) {
             remainder.append(switch (stack.pop()) {
                case '[' -> "]";
                case '(' -> ")";
                case '{' -> "}";
                case '<' -> ">";
                default -> "";
            });
        }

        return remainder.toString();
    }

    private static List<String> getInput() throws IOException {
        return Files.readAllLines(Path.of("inputs/day10-1.txt"));
    }

}
