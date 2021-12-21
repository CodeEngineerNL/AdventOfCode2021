package nl.codeengineer.aoc.aoc2021;

import nl.codeengineer.aoc.AocSolver;

import java.io.IOException;
import java.util.*;

public class Day21 implements AocSolver {
    @Override
    public long part1() throws IOException {
        int playerI = 0;
        int dice = 0;
        int numRols = 0;

        List<Player> players = new ArrayList<>();
        players.add(new Player(5, 0));
        players.add(new Player(8, 0));

        Player winner = null;
        while (winner == null) {
            Player currentPlayer = players.get(playerI);
            for (int i = 0; i < 3; i++) {
                numRols++;
                dice = 1 + (dice) % 100;
                currentPlayer.position += dice;

            }

            currentPlayer.position = 1 + (currentPlayer.position - 1) % 10;

            currentPlayer.score += currentPlayer.position;
            if (currentPlayer.score >= 1000) {
                winner = currentPlayer;
            }
            playerI = (playerI + 1) % players.size();

        }

        return players.get(playerI).score * numRols;
    }

    @Override
    public long part2() throws IOException {
       Map<Contest, Long> contestCounts = new HashMap<>();

        Contest start= new Contest(
                5,0,8,0,true
        );

        contestCounts.put(start, 1L);

        long[] winCounts = new long[2];
        int[] diceroll = new int[]{1, 2, 3};

        while (!contestCounts.isEmpty()) {
            ArrayList<Map.Entry<Contest, Long>> entries = new ArrayList<>(contestCounts.entrySet());

            for (Map.Entry<Contest, Long> entry : entries) {
                Contest current = entry.getKey();
                Long value = entry.getValue();

                contestCounts.remove(current);

                for (int d1 : diceroll) {
                    for (int d2 : diceroll) {
                        for (int d3 : diceroll) {
                            int position = current.p1sTurn ? current.p1pos : current.p2pos;
                            int score = current.p1sTurn ? current.p1score : current.p2score;

                            position = position + d1 + d2 + d3;
                            position = 1 + (position - 1) % 10;
                            score += position;

                            if (score >=21) {
                                if (current.p1sTurn) {
                                    winCounts[0] += value;
                                } else {
                                    winCounts[1] += value;
                                }
                                continue;
                            }

                            Contest contest;
                            if (current.p1sTurn) {
                                contest = new Contest(position, score, current.p2pos, current.p2score, false);
                            } else {
                                contest = new Contest(current.p1pos, current.p1score, position, score, true);
                            }

                            contestCounts.merge(contest, value, Long::sum);
                        }
                    }
                }
            }
        }

        return Long.max(winCounts[0], winCounts[1]);
    }

    public static class Player {
        public int position;
        public int score;

        public Player(int position, int score) {
            this.position = position;
            this.score = score;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Player player = (Player) o;
            return position == player.position && score == player.score;
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, score);
        }

        @Override
        public String toString() {
            return "Player{" +
                    "position=" + position +
                    ", score=" + score +
                    '}';
        }
    }

    public record Contest(int p1pos, int p1score, int p2pos, int p2score, boolean p1sTurn){}

}
