package de.hochschuletrier.gdw.ss15.game.systems;

import de.hochschuletrier.gdw.ss15.events.network.server.SendPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.Highscore;
import de.hochschuletrier.gdw.ss15.game.network.Packets.HighscorePacket;
import de.hochschuletrier.gdw.ss15.game.utils.Timer;
import de.hochschuletrier.gdw.ss15.game.utils.TimerSystem;

import java.util.HashMap;

/**
 * Created by oliver on 26.09.15.
 */
public class SyncHighscoreSystem {
	/*
    Timer t = new Timer(1000); //ms

    public SyncHighscoreSystem(TimerSystem system) {
    	
        system.addTimer(t);
        t.start();
        t.addListener(t::restart);
        t.addListener(()->{
            // sync highscore:

            // teamstats:
            {
                final HighscorePacket pack = new HighscorePacket();
                pack.teamstats = 0;
                Highscore.Get().dirtyTeamStats.forEach((k, v) -> {
                    pack.id.add(v);
                    pack.category.add(k);
                    pack.value.add(Highscore.Get().getTeamStat(v, k));
                });
                SendPacketServerEvent.emit(pack, true);
                Highscore.Get().dirtyTeamStats = new HashMap<>();
            }

            // playerstats:
            {
                final HighscorePacket pack = new HighscorePacket();
                pack.teamstats = 1; //
                Highscore.Get().dirtyPlayerStats.forEach((k, v) -> {
                    pack.id.add(v);
                    pack.category.add(k);
                    pack.value.add(Highscore.Get().getPlayerStat(v, k));
                });
                SendPacketServerEvent.emit(pack, true);
                Highscore.Get().dirtyPlayerStats = new HashMap<>();
            }
        });
    }
*/
}
