package de.hochschuletrier.gdw.ss15.game.network.Packets;

import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
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

        for (int i = 0; i < id.size(); i++) {
            dataOutput.writeInt(id.get(i));
            dataOutput.writeShort(category.get(i).length());
            dataOutput.writeChars(category.get(i));
            dataOutput.writeInt(value.get(i));
        }
    }

    @Override
    protected void unpack(DataInputStream input) throws IOException {
        int size = input.readInt(); // maximum bytes
        teamstats = input.readByte();

        int r = Integer.SIZE / 8 + Byte.SIZE / 8; // read bytes
        while (r < size){
            id.add(input.readInt());

            short l = input.readShort();    r += Short.SIZE / 8;
            StringBuilder builder = new StringBuilder(l);
            for(int i = 0; i < l; ++i){
                builder.append(input.readChar());   r += Character.SIZE / 8;
            }
            category.add(builder.toString());

            value.add(input.readInt());
            r += Integer.SIZE / 8;
        }

    }

    @Override
    public int getSize() {
        int size = Integer.SIZE;
            size += Byte.SIZE;
        for (int i = 0; i < id.size(); i++) {
            size += Integer.SIZE;
            size += Short.SIZE;
            size += Character.SIZE * category.get(i).length();
            size += Integer.SIZE;
        }
        return size / 8;
    }
}
