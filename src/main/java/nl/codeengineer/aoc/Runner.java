package nl.codeengineer.aoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;

public class Runner {
    private static String PACKAGE = "nl.codeengineer.aoc";

    public static void main(String[] args) throws IOException {
        Runner runner = new Runner();
        runner.runYear("2021");
    }

    public void runYear(String year) throws IOException {
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(PACKAGE.replaceAll("[.]", "/") + "/aoc" + year);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        List<Object> daysToRun = reader.lines()
                .filter(line -> line.startsWith("Day") && line.endsWith(".class") && !line.contains("$"))
                .sorted(Comparator.naturalOrder())
                .map(line -> getClass(line, PACKAGE + ".aoc" + year))
                .map(this::getInstance)
                .toList();


        System.out.println(String.format("| %4s | %20s | %20s | %13s | %13s |", "Day", "Part 1", "Part 2", "Time p1", "Time p2"));
        System.out.println("--------------------------------------------------------------------------------------");
        for (Object day: daysToRun) {
            AocSolver daySolver = (AocSolver) day;

            long start = System.currentTimeMillis();
            long res1 = daySolver.part1();
            long time1 = System.currentTimeMillis() - start;

            start = System.currentTimeMillis();
            long res2 = daySolver.part2();
            long time2 = System.currentTimeMillis() - start;


            System.out.println(String.format("|%5s | %20d | %20d | %10d ms | %10d ms |", daySolver.getClass().getSimpleName().replaceAll("Day", ""), res1, res2, time1, time2));
        }
        System.out.println("--------------------------------------------------------------------------------------");
    }

    private Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // Should not happen, yeah, I know it will...
        }
        return null;
    }

    private Object getInstance(Class clasz) {
        try {
            return clasz.getConstructors()[0].newInstance();
        } catch (Exception e) {
            // Should not happen, yeah, I know it will...
        }
        return null;
    }
}
