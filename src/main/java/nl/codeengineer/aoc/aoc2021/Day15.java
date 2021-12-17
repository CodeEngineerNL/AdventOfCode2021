package nl.codeengineer.aoc.aoc2021;

import nl.codeengineer.aoc.AocSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day15  implements AocSolver {

    Node[][] inputMap;

    @Override
    public long part1() throws IOException {
        inputMap = getInput();
        List<Node> nodes = createGraph(inputMap);

        Node startNode = nodes.get(0);
        Node endNode = nodes.get(nodes.size() - 1);

        calcPath(startNode);
        return  endNode.distance;
    }

    public long part2() throws IOException {
        Node[][] actualMap = new Node[inputMap.length * 5][inputMap[0].length * 5];

        int width = inputMap[0].length;
        int height = inputMap.length;

        for (int i = 0; i < 5; i++) {
            for (int k = 0; k < 5; k++) {
                for (int y = 0; y < inputMap.length; y++) {
                    for (int x = 0; x < inputMap.length; x++) {
                        int newX = x + (width * i);
                        int newY = y + (height * k);

                        int newWeight = inputMap[y][x].weight + i + k;

                        while (newWeight > 9) {
                            newWeight -= 9;
                        }

                        actualMap[newY][newX] = new Node(newY, newX, newWeight);
                    }
                }
            }
        }

        List<Node> nodes = createGraph(actualMap);
        Node startNode = nodes.get(0);
        Node endNode = nodes.get(nodes.size() - 1);

        calcPath(startNode);
        return endNode.distance;
    }

    public void printMap(Node[][] map) {
        for (Node[] line : map) {
            StringBuilder lineStr = new StringBuilder();
            for (Node node: line) {
                lineStr.append(node == null ? " " : node.weight );
            }
            System.out.println(lineStr);
        }
    }

    public void calcPath(Node startNode) {
        startNode.distance = 0;

        Set<Node> settledNodes = new HashSet<>();

        PriorityQueue<Node> pq = new PriorityQueue<>(100, Comparator.comparingLong(a -> a.distance));
        pq.add(startNode);

        while (!pq.isEmpty()) {
            Node evalNode = pq.remove();
            settledNodes.add(evalNode);
            evalNeighbors(evalNode, settledNodes, pq);
        }
    }

    public void evalNeighbors(Node evalNode, Set<Node> settledNodes, PriorityQueue<Node> pq) {
        evalNode.connectedNodes.forEach(node -> {
            if (!settledNodes.contains(node)) {
                long distance = node.getWeight();
                long newDist = evalNode.distance + distance;

                if (newDist < node.distance) {
                    node.distance = newDist;
                    pq.add(node);
                }
            }
        });
    }

    private Node[][] getInput() throws IOException {
        List<String> input = Files.readAllLines(Path.of("inputs/day15-1.txt"));

        Node[][] result = new Node[input.size()][input.get(0).length()];

        int y = 0;
        for(String line: input) {
            for (int x = 0; x < line.length(); x++) {
                result[y][x] = new Node(x, y, line.charAt(x) - '0');
            }
            y++;
        }

        return result;
    }

    private List<Node> createGraph(Node[][] result) {
        int y;
        List<Node> nodes = new ArrayList<>();

        for (y = 0; y < result.length; y++) {
            for (int x = 0; x < result[0].length; x++) {
                List<Node> connections = result[y][x].connectedNodes;
                addConnectedNode(connections, x + 1, y, result);
                addConnectedNode(connections, x, y + 1, result);
                addConnectedNode(connections, x - 1, y, result);
                addConnectedNode(connections, x, y - 1, result);

                nodes.add(result[y][x]);
            }
        }

        return nodes;
    }

    public void addConnectedNode(List<Node> connections, int x, int y, Node[][] map) {
        if (isValidPosition(x, y, map)) {
            connections.add(map[y][x]);
        }
    }

    private static boolean isValidPosition(int x, int y, Node[][] map) {
        return x >= 0 && y >=0 && x < map[0].length && y < map.length;
    }


    public static class Node {
        public final int x;
        public final int y;
        public final List<Node> connectedNodes = new ArrayList<>();

        public long distance = Long.MAX_VALUE;

        private final int weight;

        public Node(int x, int y, int weight) {
            this.x = x;
            this.y = y;
            this.weight = weight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        public int getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "x=" + x +
                    ", y=" + y +
                    ", weight=" + weight +
                    '}';
        }
    }
}

