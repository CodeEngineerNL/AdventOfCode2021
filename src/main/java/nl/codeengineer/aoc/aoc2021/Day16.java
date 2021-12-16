package nl.codeengineer.aoc.aoc2021;

import nl.codeengineer.aoc.AocSolver;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day16 implements AocSolver {

    String packetString;

    @Override
    public long part1() throws IOException {
        packetString = getInput();

        Packet result = parsePacket(packetString);

        return calcVersionSum(result);
    }

    public int calcVersionSum(Packet packet) {
        int total = 0;

        total = total + packet.version;
        for (Packet sub: packet.subPackets) {
            total = total + calcVersionSum(sub);
        }

        return total;
    }

    @Override
    public long part2() throws IOException {
        return 0;
    }

    public Packet parsePacket(String packetString) {
        StringBuilder bitString = new StringBuilder();

        for (int i = 0; i < packetString.length(); i++) {
            char c = packetString.charAt(i);
            String str = Integer.toBinaryString(Integer.parseInt("" + c, 16));
            bitString.append(StringUtils.leftPad(str, 4, "0"));
        }

        List<Packet> result = new ArrayList<>();
        parseOnePacket(bitString.toString(), 0, result);

        return result.get(0);
    }

    public int parseOnePacket(String packetString, int startIndex, List<Packet> result) {
        int i = startIndex;
        int version = Integer.parseInt(packetString.substring(i, i + 3), 2);
        i+=3;
        int typeID = Integer.parseInt(packetString.substring(i, i + 3), 2);
        i+=3;

        Packet packet = new Packet(version, typeID);
        result.add(packet);

        if (typeID == 4) {
            // num packet
            StringBuilder numStr = new StringBuilder();
            boolean keepGoing = true;
            while (keepGoing) {
                keepGoing = packetString.charAt(i) == '1';
                i++;
                numStr.append(packetString.substring(i, i+4));
                i+=4;
            }
            packet.setNum(Long.parseLong(numStr.toString(), 2));
        } else {
            // operator packet
            String lengthTypeId = packetString.substring(i, i+1);
            i++;
            if (lengthTypeId.equals("0")) {
                String lengthString = packetString.substring(i, i+15);
                int bitLength = Integer.parseInt(lengthString, 2);
                i = i + 15;
                int end = i + bitLength;
                while (i < end) {
                    i = parseOnePacket(packetString, i, packet.subPackets);
                }


            } else {
                String numPacketsString = packetString.substring(i, i+11);
                int numPackets = Integer.parseInt(numPacketsString, 2);
                i = i+ 11;
                for (int k = 0; k < numPackets; k++) {
                    i = parseOnePacket(packetString, i, packet.subPackets);
                }
            }
        }

        return i;

    }

    private String getInput() throws IOException {
        return Files.readAllLines(Path.of("inputs/day16-1.txt")).get(0);
    }

    public static class Packet {
        public final int version;
        public final int typeID;
        public final List<Packet> subPackets = new ArrayList<>();

        private String operator;
        private long num;

        public Packet(int version, int typeID) {
            this.version = version;
            this.typeID = typeID;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public long getNum() {
            return num;
        }

        public void setNum(long num) {
            this.num = num;
        }
    }
}
