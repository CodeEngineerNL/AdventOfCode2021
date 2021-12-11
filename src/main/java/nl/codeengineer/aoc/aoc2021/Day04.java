package nl.codeengineer.aoc.aoc2021;

import nl.codeengineer.aoc.AocSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day04 implements AocSolver {

    private static final int CARDSIZE = 5;

    private List<Integer> drawOrder;
    private List<Card> cards;

    public Day04() throws IOException {
        parseInput();
    }

    public long part1() {
        for (int num: drawOrder) {
            for (Card card: cards) {
                card.markAsDrawn(num);
                if (card.hasBingo()) {
                    return num * sumAllUnmarked(card);
                }
            }
        }

        return -1;
    }

    public long part2() {
        Set<Card> allCards = new HashSet<>(cards);

        for (int num: drawOrder) {
            for (Card card: cards) {
                card.markAsDrawn(num);
                if (card.hasBingo()) {
                    allCards.remove(card);
                    // No cards left? Current card is de last one!
                    if (allCards.isEmpty()) {
                        return num * sumAllUnmarked(card);
                    }
                }
            }
        }

        return -1;
    }

    private void parseInput() throws IOException {
        List<String> lines = Files.readAllLines(Path.of("inputs/day4-1.txt"));

        Iterator<String> lineIter = lines.iterator();
        drawOrder = parseDrawOrder(lineIter.next());
        // Skip emtpy line
        lineIter.next();

        cards = new ArrayList<>();
        while (lineIter.hasNext()) {
            cards.add(parseCard(lineIter));
        }
    }

    private static int sumAllUnmarked(Card card) {
        int result = 0;
        for (int y = 0; y < CARDSIZE; y++) {
            for (int x = 0; x < CARDSIZE; x++) {
                if (!card.getIsDrawn()[x][y]) {
                    result += card.getNums()[x][y];
                }
            }
        }

        return result;
    }

    private static List<Integer> parseDrawOrder(String line) {
        String[] numStrings = line.split(",");
        return Arrays.stream(numStrings).map(Integer::parseInt).toList();
    }

    private static Card parseCard(Iterator<String> lineIter) {
        int[][] result = new int[CARDSIZE][CARDSIZE];

        for (int y = 0; y < CARDSIZE; y++) {
            String line = lineIter.next();
            String[] numStrings = line.trim().split("\\s+");
            for (int x = 0; x < CARDSIZE; x++) {
                result[x][y] = Integer.parseInt(numStrings[x]);
            }
        }

        // Skip empty line
        if (lineIter.hasNext()) {
            lineIter.next();
        }

        return new Card(result);
    }

    public static class Card {
        private final int[][] nums;
        private boolean[][] isDrawn = new boolean[CARDSIZE][CARDSIZE];

        public Card(int[][] nums) {
            this.nums = nums;
        }

        public void markAsDrawn(int num) {
            for (int y = 0; y < CARDSIZE; y++) {
                for (int x = 0; x < CARDSIZE; x++) {
                    if (nums[x][y] == num) {
                        isDrawn[x][y] = true;
                        return;
                    }
                }
            }
        }

        public boolean hasBingo() {
            // Check rows
            for (int y = 0; y < CARDSIZE; y++) {
                int x = 0;
                while (x < CARDSIZE && isDrawn[x][y]) {
                    x++;
                }
                if (x == CARDSIZE) {
                    return true;
                }
            }
            // Check columns

            for (int x = 0; x < CARDSIZE; x++) {
                int y = 0;
                while (y < CARDSIZE && isDrawn[x][y]) {
                    y++;
                }
                if (y == CARDSIZE) {
                    return true;
                }
            }

            return false;
        }

        public int[][] getNums() {
            return nums;
        }

        public boolean[][] getIsDrawn() {
            return isDrawn;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Card card = (Card) o;
            return Arrays.equals(nums, card.nums);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(nums);
        }
    }

}
