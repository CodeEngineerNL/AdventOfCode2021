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

        Map<Node, Long> result = calcPath(startNode, nodes);
        return result.get(endNode);
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


                        int newWeight = inputMap[y][x].weight + (1 * i) + k;

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

        Map<Node, Long> result = calcPath(startNode, nodes);
        return result.get(endNode);
    }

    public void printMap(Node[][] map) {
        for (int y = 0; y < map.length; y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < map[0].length; x++) {
                line.append(map[y][x] == null ? " " : map[y][x].weight );
            }
            System.out.println(line);
        }
    }

    public Map<Node, Long> calcPath(Node startNode, List<Node> nodes) {
        Map<Node, Long> distMap = new HashMap<>();
        nodes.forEach(n -> distMap.put(n, Long.MAX_VALUE));
        distMap.put(startNode, 0L);

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unSettledNodes = new HashSet<>();
        unSettledNodes.add(startNode);

        while (!unSettledNodes.isEmpty()) {
            Node evalNode = getNodeWithLowestDistance(unSettledNodes, distMap);
            unSettledNodes.remove(evalNode);
            settledNodes.add(evalNode);
            evalNeighbors(evalNode, settledNodes, unSettledNodes, distMap);
        }

        return distMap;
    }

    public void evalNeighbors(Node evalNode, Set<Node> settledNodes, Set<Node> unsettledNodes, Map<Node, Long> distMap) {
        evalNode.connectedNodes.forEach(node -> {
            if (!settledNodes.contains(node)) {
                long distance = node.getWeight();
                long newDist = distMap.get(evalNode) + distance;

                if (newDist < distMap.get(node)) {
                    distMap.put(node, newDist);
                    unsettledNodes.add(node);
                }
            }
        });
    }


    public Node getNodeWithLowestDistance(Set<Node> nodes, Map<Node, Long> distMap) {
        List<Node> sorted = nodes.stream().sorted(Comparator.comparingLong(distMap::get)).toList();
        return sorted.get(0);
    }

    private Node[][] getInput() throws IOException {
        List<String> input = Files.readAllLines(Path.of("inputs/day15-test.txt"));

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

