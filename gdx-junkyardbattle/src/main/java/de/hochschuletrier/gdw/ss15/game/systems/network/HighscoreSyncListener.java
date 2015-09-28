package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import de.hochschuletrier.gdw.ss15.events.network.client.NetworkReceivedNewPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by oliver on 26.09.15.
 */
public class HighscoreSyncListener extends EntitySystem implements NetworkReceivedNewPacketClientEvent.Listener{

    public HighscoreSyncListener(){
        NetworkReceivedNewPacketClientEvent.registerListener(PacketIds.Highscore, this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        NetworkReceivedNewPacketClientEvent.unregisterListener(PacketIds.Highscore, this);
    }

    @Override
    public void onReceivedNewPacket(Packet _pack, Entity ent) {
    	/*
        try {
            HighscorePacket packet = (HighscorePacket) _pack;

            if(packet.teamstats == 0){ // teamstats
                for (int i = 0; i < packet.id.size(); i++) {
                    Highscore.Get().addTeam(packet.id.get(i));  // add categorys if not exist
                    Highscore.Get().addTeamCategory(packet.category.get(i));

                    Highscore.Get().setTeamStat(packet.id.get(i), packet.category.get(i), packet.value.get(i));
                }
            }else{ // playerstats
                for (int i = 0; i < packet.id.size(); i++) {
                    Highscore.Get().addPlayer(packet.id.get(i));  // add categorys if not exist
                    Highscore.Get().addPlayerCategory(packet.category.get(i));

                    Highscore.Get().setPlayerStat(packet.id.get(i), packet.category.get(i), packet.value.get(i));
                }
            }

        }catch (ClassCastException ex){}
        */
    }
}
