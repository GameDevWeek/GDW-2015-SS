package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.systems.IteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.ss15.events.ComeToBaseEvent;
import de.hochschuletrier.gdw.ss15.events.network.server.SendPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
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
        //System.out.println("ComeBackToBase");
        int basePointsToAdd = Math.max(0, inventory.get(playerEntity).getMetalShards() - inventory.get(playerEntity).minMetalShardsForBase);
        inventory.get(playerEntity).subMetalShards(basePointsToAdd);
        System.out.println(basePoint.get(basePointEntity));
        basePoint.get(basePointEntity).points += basePointsToAdd;
        //System.out.println("InventarPlayer: " + inventory.get(playerEntity).getMetalShards());
        //System.out.println("BasePoints: " + basePoint.get(basePointEntity).points);
        SimplePacket packet = new SimplePacket(SimplePacket.SimplePacketId.BasePointsUpdate.getValue(), basePoint.get(basePointEntity).points);
        SendPacketServerEvent.emit(packet, true);
    }
}
