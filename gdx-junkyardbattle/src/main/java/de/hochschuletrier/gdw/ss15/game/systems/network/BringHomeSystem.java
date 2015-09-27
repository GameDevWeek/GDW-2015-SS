package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import de.hochschuletrier.gdw.ss15.events.ComeToBaseEvent;
import de.hochschuletrier.gdw.ss15.events.network.server.SendPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.Highscore;
import de.hochschuletrier.gdw.ss15.game.components.BasePointComponent;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;

/**
 * Created by Ricardo on 24.09.2015.
 */

public class BringHomeSystem extends EntitySystem implements ComeToBaseEvent.Listener {

    ComponentMapper<InventoryComponent> inventory = ComponentMappers.inventory;
    ComponentMapper<BasePointComponent> basePoint = ComponentMappers.basePoint;
    ComponentMapper<PlayerComponent> player = ComponentMappers.player;

    public BringHomeSystem()
    {
        ComeToBaseEvent.register(this);
    }


    @Override
    public void onComeToBase(Entity playerEntity, Entity basePointEntity) {
        int basePointsToAdd = Math.max(0, inventory.get(playerEntity).getMetalShards() - inventory.get(playerEntity).minMetalShardsForBase);
        inventory.get(playerEntity).subMetalShards(basePointsToAdd);
        int teamid = player.get(playerEntity).teamID;
        Highscore.Get().addTeamPoints(teamid, basePointsToAdd);
    }
}
