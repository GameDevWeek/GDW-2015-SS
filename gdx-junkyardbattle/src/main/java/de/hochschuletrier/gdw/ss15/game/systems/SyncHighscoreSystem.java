package de.hochschuletrier.gdw.ss15.game.systems;

import de.hochschuletrier.gdw.ss15.game.Highscore;
import de.hochschuletrier.gdw.ss15.game.network.Packets.HighscorePacket;
import de.hochschuletrier.gdw.ss15.game.utils.Timer;
import de.hochschuletrier.gdw.ss15.game.utils.TimerSystem;
import javafx.util.Pair;

/**
 * Created by oliver on 26.09.15.
 */
public class SyncHighscoreSystem {

    Timer t = new Timer(1000); //ms

    public SyncHighscoreSystem(TimerSystem system) {
        system.addTimer(t);
        t.start();
        t.addListener(t::restart);
        t.addListener(()->{
            // sync highscore:
//                // send team pack:
//            for(short team = 0; team <= 1; ++team) {
//                HighscorePacket pack = new HighscorePacket();
//                pack.teamstats = 0;
//                Highscore.Get().dirtyTeamStats.forEach((k) -> {
//                    pack.highscorediff.add(new Pair<>(k, Highscore.Get().getTeamStat(pack.teamstats, k)));
//                });
//            }
//
//            HighscorePacket pack = new HighscorePacket();
//            pack.teamstats = -1;
//            Highscore.Get().dirtyPlayerStats.forEach((k) -> {
//                pack.highscorediff.add(new Pair<>(k, Highscore.Get().getTeamStat(pack.teamstats, k)));
//            });

        });
    }

}
