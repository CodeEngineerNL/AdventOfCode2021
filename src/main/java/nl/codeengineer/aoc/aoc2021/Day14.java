package nl.codeengineer.aoc.aoc2021;

import nl.codeengineer.aoc.AocSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Day14 implements AocSolver {

    String polymer;
    final Map<String, String> insertionRules = new HashMap<>();

    @Override
    public long part1() throws IOException {
        getInput();
        return doSteps(10);
    }

    @Override
    public long part2() throws IOException {
        return doSteps(40);
    }

    private Long doSteps(int numSteps) {
        Map<String, Long> pairCounts = getPairCounts();

        for (int i = 0; i < numSteps; i++) {
            pairCounts = handleStep(pairCounts);
        }

        Map<Character, Long> counts = getCharHistogram(pairCounts);
        List<Long> sorted = counts.values().stream().sorted(Long::compareTo).toList();
        return sorted.get(sorted.size() - 1) - sorted.get(0);
    }

    private Map<Character, Long> getCharHistogram(Map<String, Long> pairCounts) {
        Map<Character, Long> counts = getCounts(pairCounts);
        Character last = polymer.charAt(polymer.length() - 1);
        Long lastCount = counts.get(last);
        counts.put(last, lastCount == null ? 1 : lastCount + 1);
        return counts;
    }

    private Map<String, Long> getPairCounts() {
        Map<String, Long> pairCounts = new HashMap<>();
        for (int i = 0; i < polymer.length() - 1; i++) {
            String pair = polymer.substring(i, i + 2);
            Long value = pairCounts.get(pair);
            pairCounts.put(pair, value == null ? 1 : value + 1);
        }
        return pairCounts;
    }

    public Map<Character, Long> getCounts(Map<String, Long> pairs) {
        Map<Character, Long> result = new HashMap<>();

        for (Map.Entry<String, Long> e: pairs.entrySet()) {
            Character c = e.getKey().charAt(0);
            Long value = e.getValue();
            Long count = result.get(c);
            result.put(c, count == null ? value : count + value);
        }

        return  result;
    }

    public Map<String, Long> handleStep(Map<String, Long> pairs) {
        Map<String, Long> result = new HashMap<>(pairs);

        for (Map.Entry<String, Long> pairValue: pairs.entrySet()) {
            Long value = pairValue.getValue();
            if (value != 0) {
                String pair = pairValue.getKey();

                String newChar = insertionRules.get(pair);

                if (newChar != null) {
                    String newPair1 = pair.charAt(0) + newChar;
                    String newPair2 = newChar + pair.charAt(1);

                    result.put(pair, result.get(pair) - value);

                    addPair(newPair1, result, value);
                    addPair(newPair2, result, value);

                }
            }
        }

        return result;
    }

    public void addPair(String pair, Map<String, Long> result, Long count) {
        Long value = result.get(pair);
        result.put(pair, value == null ? count : value + count);
    }

    private void getInput() throws IOException {
        List<String> inputLines = Files.readAllLines(Path.of("inputs/day14-1.txt"));
        Iterator<String> iter = inputLines.iterator();

        this.polymer = iter.next();
        iter.next();

        while (iter.hasNext()) {
            String line = iter.next();
            String[] parts = line.split(" -> ");
            this.insertionRules.put(parts[0], parts[1]);
        }
    }
}