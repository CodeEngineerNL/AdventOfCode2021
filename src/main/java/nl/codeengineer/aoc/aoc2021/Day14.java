package nl.codeengineer.aoc.aoc2021;

import nl.codeengineer.aoc.AocSolver;

import java.io.IOException;
import java.math.BigInteger;
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
        return doSteps(10).longValue();
    }

    @Override
    public long part2() throws IOException {
        BigInteger value = doSteps(40);
        return value.longValue();
    }

    private BigInteger doSteps(int numSteps) {
        Map<String, BigInteger> pairCounts = getPairCounts();

        for (int i = 0; i < numSteps; i++) {
            pairCounts = handleStep(pairCounts);
        }

        Map<Character, BigInteger> counts = getCharHistogram(pairCounts);
        List<BigInteger> sorted = counts.values().stream().sorted(BigInteger::compareTo).toList();
        return sorted.get(sorted.size() - 1).subtract(sorted.get(0));
    }

    private Map<Character, BigInteger> getCharHistogram(Map<String, BigInteger> pairCounts) {
        Map<Character, BigInteger> counts = getCounts(pairCounts);
        Character last = polymer.charAt(polymer.length() - 1);
        BigInteger lastCount = counts.get(last);
        counts.put(last, lastCount == null ? BigInteger.ONE : lastCount.add(BigInteger.ONE));
        return counts;
    }

    private Map<String, BigInteger> getPairCounts() {
        Map<String, BigInteger> pairCounts = new HashMap<>();
        for (int i = 0; i < polymer.length() - 1; i++) {
            String pair = polymer.substring(i, i + 2);
            BigInteger value = pairCounts.get(pair);
            pairCounts.put(pair, value == null ? BigInteger.ONE : value.add(BigInteger.ONE));
        }
        return pairCounts;
    }

    public Map<Character, BigInteger> getCounts(Map<String, BigInteger> pairs) {
        Map<Character, BigInteger> result = new HashMap<>();

        for (Map.Entry<String, BigInteger> e: pairs.entrySet()) {
            Character c = e.getKey().charAt(0);
            BigInteger value = e.getValue();
            BigInteger count = result.get(c);
            result.put(c, count == null ? value : count.add(value));
        }

        return  result;
    }

    public Map<String, BigInteger> handleStep(Map<String, BigInteger> pairs) {
        Map<String, BigInteger> result = new HashMap<>(pairs);

        for (Map.Entry<String, BigInteger> pairValue: pairs.entrySet()) {
            BigInteger value = pairValue.getValue();
            if (!value.equals(BigInteger.ZERO)) {
                String pair = pairValue.getKey();

                String newChar = insertionRules.get(pair);

                if (newChar != null) {
                    String newPair1 = pair.charAt(0) + newChar;
                    String newPair2 = newChar + pair.charAt(1);

                    result.put(pair, result.get(pair).subtract(value));

                    addPair(newPair1, result, value);
                    addPair(newPair2, result, value);

                }
            }
        }

        return result;
    }

    public void addPair(String pair, Map<String, BigInteger> result, BigInteger count) {
        BigInteger value = result.get(pair);
        result.put(pair, value == null ? count : value.add(count));
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