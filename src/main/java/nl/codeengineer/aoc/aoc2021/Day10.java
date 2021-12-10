package nl.codeengineer.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day10 {

    public static void main(String[] args) throws IOException {
        System.out.println(part1());
        System.out.println(part2());
    }

    private static long part1() throws IOException {
        List<String> input = getInput();

        long score = 0;

        for (String line : input) {
            char result = getFirstIllegalChar(line);
            switch (result) {
                case ')' -> score += 3;
                case ']' -> score += 57;
                case '}' -> score += 1197;
                case '>' -> score += 25137;
            }
        }

        return score;
    }

    private static long part2() throws IOException {
        List<String> input = getInput();
        List<String> incomplete = input.stream().filter(s -> getFirstIllegalChar(s) == 0).toList();

        List<Long> scores = new ArrayList<>();
        for (String line: incomplete) {
            String remainder = getRemainder(line);

            long score = 0;
            for (int i = 0; i < remainder.length(); i++) {
                score *= 5;

                switch (remainder.charAt(i)) {
                    case ')' -> score += 1;
                    case ']' -> score += 2;
                    case '}' -> score += 3;
                    case '>' -> score += 4;
                }
            }

            scores.add(score);
        }

        scores.sort(Comparator.naturalOrder());
        return scores.get(scores.size() / 2);
    }


    private static char getFirstIllegalChar(String line) {
        Deque<Character> stack = new ArrayDeque<>();

        char wrong = 0;

        for (int i = 0; i < line.length(); i++) {
            switch (line.charAt(i)) {
                case '[', '(', '{', '<' -> stack.push(line.charAt(i));
                case ']' -> {
                    if (stack.pop() != '[') {
                        return line.charAt(i);
                    }
                }
                case ')' -> {
                    if (stack.pop() != '(') {
                        return line.charAt(i);
                    }
                }
                case '}' -> {
                    if (stack.pop() != '{') {
                        return line.charAt(i);
                    }
                }
                case '>' -> {
                    if (stack.pop() != '<') {
                        return line.charAt(i);
                    }
                }
            }
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
            switch (stack.pop()) {
                case '[' -> remainder.append(']');
                case '(' -> remainder.append(")");
                case '{' -> remainder.append("}");
                case '<' -> remainder.append(">");
            }
        }

        return remainder.toString();
    }

    private static List<String> getInput() throws IOException {
        return Files.readAllLines(Path.of("inputs/day10-1.txt"));
    }

}
