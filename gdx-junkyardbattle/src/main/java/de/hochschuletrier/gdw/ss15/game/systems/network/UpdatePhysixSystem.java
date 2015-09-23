package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.events.network.client.NetworkReceivedNewPacketClientEvent;
import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityUpdatePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.MovementPacket;
import de.hochschuletrier.gdw.ss15.game.utils.Timer;
import de.hochschuletrier.gdw.ss15.game.utils.TimerSystem;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by oliver on 23.09.15.
 */
public class UpdatePhysixSystem extends IteratingSystem
        implements NetworkReceivedNewPacketClientEvent.Listener{

    Timer timer = new Timer(200); // 200 ms timer

    public UpdatePhysixSystem(TimerSystem timerSystem)
    {
        super(Family.all(PhysixBodyComponent.class, PlayerComponent.class).get());
        NetworkReceivedNewPacketClientEvent.registerListener(PacketIds.EntityUpdate, this);

        //timer.addListener(timer::restart); // timer is restarted after expired
        timerSystem.addTimer(timer);
        timer.start();
    }


    /////////////////// CLIENT
    /////// send every x miliseconds the player velocity and angle to the server
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
       /* if(timer.isExpired()) {
            PlayerComponent plc = ComponentMappers.player.get(entity);
            if(! plc.isLocalPlayer) return;
            PhysixBodyComponent phxc = ComponentMappers.physixBody.get(entity);
//            System.out.println("velocity-x: " + phxc.getLinearVelocity().x + "velocity-y: " + phxc.getLinearVelocity().y);
            MovementPacket packet = new MovementPacket(phxc.getLinearVelocity().x, phxc.getLinearVelocity().y, phxc.getAngle());
            SendPacketClientEvent.emit(packet, true);
            timer.restart();
        }*/

    }

    /////////////////// CLIENT
    @Override
    public void onReceivedNewPacket(Packet pack, Entity entity) {
        try{
            EntityUpdatePacket p = (EntityUpdatePacket)pack;
            PhysixBodyComponent phxc = entity.getComponent(PhysixBodyComponent.class);
            phxc.setPosition(p.xPos, p.yPos);
            phxc.setAngle(p.rotation*MathUtils.degreesToRadians);
          
            //System.out.println("used new rotation: "+p.rotation);
        }catch(ClassCastException ex){}

    }
}
