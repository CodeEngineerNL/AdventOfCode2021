package nl.codeengineer.aoc.aoc2021;

import nl.codeengineer.aoc.AocSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day17  implements AocSolver {

    private int hits = 0;

    @Override
    public long part1() throws IOException {
        final Area area = getInput();

        int steps = 0;

        int startVx = 1;
        int startVy = area.ey;
        int endVx = area.ex;
        int endY = -area.ey;

        int maxY = 0;

        Probe probe = new Probe(0, 0);
        for (int xv = startVx; xv <= endVx; xv++) {
            for (int yv = startVy; yv <= endY; yv++) {
                int maxYThisRound = 0;
                probe.setX(0);
                probe.setY(0);
                probe.setYv(yv);
                probe.setXv(xv);

                while (!area.isHit(probe.getX(), probe.getY()) && probe.getX() <= area.ex && probe.getY() >= area.ey) {
                    probe.doStep();
                    steps++;
                    if (maxYThisRound < probe.getY()) {
                        maxYThisRound = probe.getY();
                    }
                }

                if (area.isHit(probe.getX(), probe.getY())) {
                    hits++;
                    if (maxYThisRound > maxY) {
                        maxY = maxYThisRound;
                    }
                }
            }
        }

        System.out.println(steps);
        return maxY;
    }

    @Override
    public long part2() throws IOException {
        return hits;
    }

    public Area getInput() throws IOException {
        String line = Files.readAllLines(Path.of("inputs/day17-1.txt")).get(0).replace("target area: ", "");
        String[] xyParts = line.split(", ");

        String[] xParts = xyParts[0].substring(2).split("\\.\\.");
        String[] yParts = xyParts[1].substring(2).split("\\.\\.");

        int x1 = Integer.parseInt(xParts[0]);
        int x2 = Integer.parseInt(xParts[1]);
        int y1 = Integer.parseInt(yParts[0]);
        int y2 = Integer.parseInt(yParts[1]);

        // y = always negative
        return new Area(Math.min(x1, x2), Math.max(x1, x2),
                Math.max(y1, y2), Math.min(y1, y2));
    }

    public static class Probe {
        private int x;
        private int y;

        private int xv;
        private int yv;

        public Probe(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void doStep() {
            x = x + xv;
            y = y + yv;

            if (xv < 0) {
                xv++;
            } else if (xv > 0) {
                xv--;
            }

            yv--;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getXv() {
            return xv;
        }

        public void setXv(int xv) {
            this.xv = xv;
        }

        public int getYv() {
            return yv;
        }

        public void setYv(int yv) {
            this.yv = yv;
        }
    }

    public record Area(int sx, int ex, int sy, int ey) {
        boolean isHit(int x, int y) {
            // y = always negative!
            return x >= sx && x <= ex && y <= sy && y >= ey;
        }
    }
}
