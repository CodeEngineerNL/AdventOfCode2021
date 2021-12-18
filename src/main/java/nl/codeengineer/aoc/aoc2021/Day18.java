package nl.codeengineer.aoc.aoc2021;

import nl.codeengineer.aoc.AocSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Day18 implements AocSolver {

    private List<String> lines;

    @Override
    public long part1() throws IOException {
        lines = getInput();
        Pair p = addAllLines(lines);
        return calcMagnitude(p);
    }

    @Override
    public long part2() throws IOException {
        lines = getInput();
        long max = calcMaxMag(lines);
        Collections.reverse(lines);
        return Long.max(max, calcMaxMag(lines));
    }

    private long calcMaxMag(List<String> lines) {
        long maxMag = 0;
        for (int i = 0; i < lines.size() - 1; i++) {
            for (int k = i + 1; k < lines.size(); k++) {
                Pair p1 = parse(lines.get(i), 0);
                Pair p2 = parse(lines.get(k), 0);

                long magnitude = reduceAndGetMag(p1.add(p2));

                if (maxMag < magnitude) {
                    maxMag = magnitude;
                }
            }
        }


        return maxMag;
    }

    private long reduceAndGetMag(Pair p) {
        boolean reduce = true;
        while (reduce) {
            reduce = reducePairs(p);
        }

        return calcMagnitude(p);
    }

    private Pair addAllLines(List<String> lines) {
        Iterator<String> lineIter = lines.iterator();

        Pair p = parse(lineIter.next(), 0);

        while (lineIter.hasNext()) {
            Pair nextPair = parse(lineIter.next(), 0);
            p = p.add(nextPair);

            boolean reduce = true;
            while (reduce) {
                reduce = reducePairs(p);
            }
        }
        return p;
    }

    public void print(Pair p) {
        StringBuilder b = new StringBuilder();
        print(p, b);
        System.out.println(b);
    }

    public void flatten(Pair p, List<Pair> flatList) {
        boolean added = false;
        if (p.leftPair != null) {
            flatten(p.leftPair, flatList);
        } else {
            flatList.add(p);
            added = true;
            p.index = flatList.size() - 1;
        }

        if (p.rightPair != null) {
            flatten(p.rightPair, flatList);
        } else {
            if (!added) {
                flatList.add(p);
                p.index = flatList.size() - 1;
            }
        }
    }

    public void print(Pair p, StringBuilder builder) {
        builder.append("[");
        if (p.leftPair == null) {
            builder.append(p.left);
        } else {
            print(p.leftPair, builder);
        }

        builder.append(",");

        if (p.rightPair == null) {
            builder.append(p.right);
        } else {
            print(p.rightPair, builder);
        }

        builder.append("]");
    }

    public boolean reducePairs(Pair p) {
        List<Pair> pairsInOrder = new ArrayList<>();
        flatten(p, pairsInOrder);

        boolean exploded = explode(p, 0, pairsInOrder);
        if (exploded) {
            return true;
        }

        return split(p);
    }

    public boolean explode(Pair p,int depth, List<Pair> pairsInOrder) {
        boolean hasExploded = false;
        if (depth == 4) {
            // search to left;
            for (int i = p.index - 1; i >=0; i--) {
                Pair currentPair = pairsInOrder.get(i);
                if (currentPair.rightPair == null) {
                    currentPair.right += p.left;
                    break;
                } else if (currentPair.leftPair == null) {
                    currentPair.left += p.left;
                    break;
                }
            }
            // search to right
            for (int i = p.index + 1; i < pairsInOrder.size(); i++) {
                Pair currentPair = pairsInOrder.get(i);
                if (currentPair.leftPair == null) {
                    currentPair.left += p.right;
                    break;
                } else if (currentPair.rightPair == null) {
                    currentPair.right += p.right;
                    break;
                }
            }

            return true;
        }

        if (p.leftPair != null) {
            hasExploded = explode(p.leftPair,  depth + 1, pairsInOrder);
            if (hasExploded && depth == 3) {
                p.leftPair = null;
                p.left = 0;

            }
        }
        if (!hasExploded && p.rightPair != null) {
            hasExploded = explode(p.rightPair, depth + 1, pairsInOrder);

            if (hasExploded && depth == 3) {
                p.rightPair = null;
                p.right = 0;
            }
        }

        return hasExploded;

    }

    public boolean split(Pair p) {
        boolean hasSplit = false;

        if (p.leftPair == null && p.left > 9) {
            Pair newPair = new Pair();
            newPair.left = p.left / 2;
            newPair.right = (long) Math.ceil(p.left / 2.0);
            p.leftPair = newPair;
            newPair.parent = p;
            return true;
        } else if (p.leftPair != null) {
            hasSplit = split(p.leftPair);
        }

        if (!hasSplit) {
            if (p.rightPair == null && p.right > 9) {
                Pair newPair = new Pair();
                newPair.left = p.right / 2;
                newPair.right = (long) Math.ceil(p.right / 2.0);
                p.rightPair = newPair;
                newPair.parent = p;
                return true;
            } else if (p.rightPair != null) {
                hasSplit = split(p.rightPair);
            }
        }

        return hasSplit;
    }

    public long calcMagnitude(Pair p) {
        if (p.leftPair != null) {
            p.left = calcMagnitude(p.leftPair);
        }
        if (p.rightPair != null) {
            p.right = calcMagnitude(p.rightPair);
        }

        return 3 * p.left + 2 * p.right;
    }

    public Pair parse(String line, int pos) {
        Pair pair = new Pair();


        if (line.charAt(pos + 1) == '[') {
            pair.leftPair = parse(line, pos+1);
            pair.leftPair.parent = pair;
            pos = pair.leftPair.end + 1;
        } else {
            pair.left = Integer.parseInt(line, pos+1, pos+2, 10);
            pos = pos + 3;
        }

        if (line.charAt(pos) == '[') {
            pair.rightPair = parse(line, pos);
            pair.rightPair.parent = pair;
            pair.end = pair.rightPair.end + 1;
        } else {
            pair.right = Integer.parseInt(line, pos, pos+1, 10);
            pair.end = pos + 2;
        }

        return pair;
    }

    private List<String> getInput() throws IOException {
        return Files.readAllLines(Path.of("inputs/day18-1.txt"));
    }

    public static class Pair {
        public long left;
        public long right;

        public int end = 0;

        public Pair leftPair;
        public Pair rightPair;
        public Pair parent = null;

        public int index = -1;

        public Pair add(Pair p) {
            Pair newPair = new Pair();
            newPair.leftPair = this;
            newPair.rightPair = p;
            return newPair;
        }
    }

}
