package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
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
public class BringHomeSystem extends EntitySystem implements ComeToBaseEvent.listener {

    ComponentMapper<InventoryComponent> inventory = ComponentMappers.inventory;
    ComponentMapper<BasePointComponent> basePoint = ComponentMappers.basePoint;
    ComponentMapper<PlayerComponent> player = ComponentMappers.player;

    public void onComeToBase(PhysixContact contact) {
        if (player.has(contact.getOtherComponent().getEntity())) {
            Entity playerEntity = contact.getOtherComponent().getEntity();
            Entity basePointEntity = contact.getMyComponent().getEntity();

            basePoint.get(basePointEntity).points += inventory.get(playerEntity).getMetalShards();
            inventory.get(playerEntity).setMetalShards(0);
            SimplePacket packet = new SimplePacket(SimplePacket.SimplePacketId.BasePointsUpdate.getValue(), basePoint.get(basePointEntity).points);
            SendPacketServerEvent.emit(packet, true);
        }
    }

}
