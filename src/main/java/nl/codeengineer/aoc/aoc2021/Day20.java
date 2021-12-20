package nl.codeengineer.aoc.aoc2021;

import nl.codeengineer.aoc.AocSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Day20 implements AocSolver {

    boolean[] enhancementAlgo;
    boolean[][] image;

    @Override
    public long part1() throws IOException {
        getInput();

        boolean infinitePixel = false;
        for (int i = 0; i < 2; i++) {
            image = enhance(image, enhancementAlgo, infinitePixel);
            infinitePixel = infinitePixel ? enhancementAlgo[511] : enhancementAlgo[0];
        }

        return countPixels(image);
    }

    @Override
    public long part2() throws IOException {
        getInput();

        boolean infinitePixel = false;
        for (int i = 0; i < 50; i++) {
            image = enhance(image, enhancementAlgo, infinitePixel);
            infinitePixel = infinitePixel ? enhancementAlgo[511] : enhancementAlgo[0];
        }

        return countPixels(image);
    }

    public long countPixels(boolean[][] image) {
        int count = 0;

        for (boolean[] line: image) {
            for (boolean b: line) {
                if (b) {
                    count++;
                }
            }
        }

        return count;
    }

    public boolean[][] enhance(boolean[][] image, boolean[] enhancementAlgo, boolean infinitePixel) {
        boolean[][] enhanced = new boolean[image.length + 2][image[0].length + 2];

        for (int y = -1; y < image.length + 1; y++) {
            for (int x = -1; x < image[0].length + 1; x++) {
                boolean[] binaryBools = new boolean[9];
                binaryBools[0] = getPixel(image, x - 1, y - 1, infinitePixel);
                binaryBools[1] = getPixel(image, x , y - 1, infinitePixel);
                binaryBools[2] = getPixel(image, x + 1, y - 1, infinitePixel);
                binaryBools[3] = getPixel(image, x - 1, y , infinitePixel);
                binaryBools[4] = getPixel(image, x, y , infinitePixel);
                binaryBools[5] = getPixel(image, x + 1, y , infinitePixel);
                binaryBools[6] = getPixel(image, x - 1, y + 1, infinitePixel);
                binaryBools[7] = getPixel(image, x , y + 1, infinitePixel);
                binaryBools[8] = getPixel(image, x + 1, y + 1, infinitePixel);

                int num = 0;
                for(boolean b: binaryBools) {
                    num = num << 1;
                    if (b) {
                        num = num | 1;
                    }
                }

                enhanced[y +1][x + 1] = enhancementAlgo[num];
            }
        }

        return enhanced;
    }

    public boolean getPixel(boolean[][] image, int x, int y, boolean infinitePixel) {
        if (isValidPixel(x, y, image)) {
            return image[y][x];
        } else {
            return infinitePixel;
        }
    }

    public boolean isValidPixel(int x, int y, boolean[][] image) {
        return x>=0 && y >= 0 && x < image[0].length && y < image.length;
    }

    public void printImage(boolean[][] image) {
        for (boolean[]line: image) {
            StringBuilder builder = new StringBuilder();
            for (boolean b: line) {
                builder.append(b ? '#' : '.');
            }
            System.out.println(builder);
        }
    }

    public void getInput() throws IOException {
        Iterator<String> iter = Files.readAllLines(Path.of("inputs/day20-1.txt")).iterator();

        String algoString = iter.next();
        this.enhancementAlgo = new boolean[algoString.length()];
        for (int i = 0; i < algoString.length(); i++) {
            this.enhancementAlgo[i] = algoString.charAt(i) == '#';
        }

        iter.next();

        List<String> inputImage = new ArrayList<>();
        while (iter.hasNext()) {
            inputImage.add(iter.next());
        }

        int width = inputImage.get(0).length();
        int height = inputImage.size();

        this.image = new boolean[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x <width; x++) {
                this.image[y][x] = inputImage.get(y).charAt(x) == '#';
            }
        }
    }
}
