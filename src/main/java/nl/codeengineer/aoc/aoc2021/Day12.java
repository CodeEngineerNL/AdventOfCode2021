package nl.codeengineer.aoc.aoc2021;

import nl.codeengineer.aoc.AocSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day12 implements AocSolver {

    private Map<String, Node> input;

    @Override
    public long part1() throws IOException {
        input = getInput();
        return calcPaths(input, false);
    }

    @Override
    public long part2() throws IOException {
       return calcPaths(input, true);
    }


    public int calcPaths(Map<String, Node> nodes, boolean visitOneSmallCaveTwice) {
        Node startNode = nodes.get("start");

        return visitNodes(startNode, new ArrayDeque<>(), visitOneSmallCaveTwice, false);
    }

    public int visitNodes(Node currentNode, Deque<Node> currentPath, boolean canVisitOneSmallTwice, boolean visitedSmallTwice) {
        if (currentNode.name.equals("end")) {
            return 1;
        }

        if (currentNode.name.toLowerCase().equals(currentNode.name) && currentPath.contains(currentNode)) {
            if (!canVisitOneSmallTwice || "start".equals(currentNode.name) || visitedSmallTwice) {
                return 0;
            }
            visitedSmallTwice = true;
        }

        currentPath.push(currentNode);

        int result = 0;

        for(Node childNode: currentNode.connected) {
            result += visitNodes(childNode, currentPath, canVisitOneSmallTwice, visitedSmallTwice);
        }

        currentPath.pop();

        return result;
    }

    public Map<String, Node> getInput() throws IOException {
        HashMap<String, Node> result = new HashMap<>();

        List<String> inputLines = Files.readAllLines(Path.of("inputs/day12-1.txt"));

        for(String line: inputLines) {
            String[] nodes = line.split("-");

            Node nodeA = result.get(nodes[0]);
            Node nodeB = result.get(nodes[1]);

            if (nodeA == null) {
                nodeA = new Node(nodes[0], new HashSet<>());
            }

            if (nodeB == null) {
                nodeB = new Node(nodes[1], new HashSet<>());
            }

            nodeA.connected.add(nodeB);
            nodeB.connected.add(nodeA);

            result.put(nodeA.name, nodeA);
            result.put(nodeB.name, nodeB);
        }

        return result;
    }


    public record Node(String name, Set<Node> connected) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return name.equals(node.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }


}
