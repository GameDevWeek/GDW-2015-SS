package de.hochschuletrier.gdw.ss15.game.network.Packets;

import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
import javafx.util.Pair;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by oliver on 26.09.15.
 */
public class HighscorePacket extends Packet {

    public byte teamstats = 0; // 1 = playerstats
    public ArrayList<Integer> id = new ArrayList<>();
    public ArrayList<String> category = new ArrayList<>();
    public ArrayList<Integer> value = new ArrayList<>();

    public HighscorePacket() {
        super(PacketIds.Highscore.getValue());
    }

    @Override
    protected void pack(DataOutputStream dataOutput) throws IOException {
        dataOutput.writeInt(this.getSize()); // send package size
        dataOutput.writeByte(teamstats); // send package size
//
//
//        for (Pair<String, Integer> pair : highscorediff) {
//            dataOutput.writeShort(pair.getKey().length());
//            dataOutput.writeChars(pair.getKey());
//            dataOutput.writeInt(pair.getValue());
//        }
    }

    @Override
    protected void unpack(DataInputStream input) throws IOException {
        int size = input.readInt();
        int r = Integer.SIZE / 8;

        teamstats = input.readByte();
        r += Byte.SIZE / 8;

        while (r < size){
            short l = input.readShort();
            r += Short.SIZE / 8;
            StringBuilder builder = new StringBuilder(l);
            for(int i = 0; i < l; ++i){
                builder.append(input.readChar());
                r += Character.SIZE / 8;
            }
            Pair<String, Integer> p = new Pair<>(builder.toString(), input.readInt());
            r += Integer.SIZE / 8;
        }

    }

    @Override
    public int getSize() {
        int size = Integer.SIZE;
        size += Byte.SIZE;
//        for (Pair<String, Integer> pair : highscorediff) {
//            size += Character.SIZE * pair.getKey().length();
//            size += Integer.SIZE;
//        }
        return size / 8;
    }
}
