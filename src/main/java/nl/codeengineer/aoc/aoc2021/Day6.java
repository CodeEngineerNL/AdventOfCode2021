package nl.codeengineer.aoc.aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Day6 {

    private static List<Fish> fishes;

    public static void main(String[] args) throws IOException {
        fishes = new ArrayList<>(getInput());

        System.out.println(part1());
        System.out.println(part2());
    }

    private static long part1() {
        for (int i = 0; i < 80; i++) {
            handleStep();
        }

        return fishes.size();
    }

    private static long part2() throws IOException {
        List<Fish> part2fishes = getInput();

        HashMap<Integer, Long> fishCounts = new HashMap<>();
        for (int i = -1; i < 9; i++) {
            fishCounts.put(i, 0l);
        }

        part2fishes.forEach(fish -> {
            long count = fishCounts.get(fish.c);
            fishCounts.put(fish.c, count + 1);
        });

        for (int i = 0; i < 256; i++) {
            handeStap2(fishCounts);
        }

        long total = 0;

        for (int i = 0; i < 9; i++) {
             total = total + fishCounts.get(i);
        }

        return total;
    }

    private static void handeStap2(HashMap<Integer, Long> counts) {
        for (int i = 0; i < 9; i++) {
            long count = counts.get(i);
            counts.put(i -1 , count);
        }

        long newBorn = counts.get(-1);
        counts.put(-1, 0l);
        counts.put(6, counts.get(6) + newBorn);
        counts.put(8, newBorn);
    }


    private static void handleStep() {
        List<Fish> newFishes = new ArrayList<>();
        for (Fish fish: fishes) {
            if (fish.decC() == -1) {
                newFishes.add(new Fish(8));
                fish.setC(6);
            }
        }

        fishes.addAll(newFishes);
    }

    private static List<Fish> getInput() throws IOException {
       return Arrays.asList(
                Files.readAllLines(Path.of("inputs/day6-1.txt")).get(0).split(",")
       ).stream().map(num -> new Fish(Integer.parseInt(num))).toList();
    }


    public static class Fish {
        private int c;

        public Fish(int c) {
            this.c = c;
        }

        public int getC() {
            return c;
        }

        public void setC(int c) {
            this.c = c;
        }

        public int decC() {
            return --c;
        }

    }


}
