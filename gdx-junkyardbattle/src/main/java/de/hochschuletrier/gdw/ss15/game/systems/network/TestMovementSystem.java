package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.events.SoundEvent;
import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.components.*;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.NormalMapComponent;
import de.hochschuletrier.gdw.ss15.game.network.Packets.MovementPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;

/**
 * Created by lukas on 22.09.15.
 */
public class TestMovementSystem extends IteratingSystem{

    private Game game;
    private Camera camera;
    private MyTimer timer = new MyTimer(true);
    private Vector2 velVector = new Vector2();
    private Vector2 vectorToAdd = new Vector2(0,0);
    private ComponentMapper<InputComponent> input;
    private ComponentMapper<SoundEmitterComponent> soundEmitter;
    private ComponentMapper<InventoryComponent> inventory;
    private ComponentMapper<MoveComponent> move;
    public static boolean interpolate = true;
    public TestMovementSystem(Game game, Camera cam)
    {
        super(Family.all(MoveComponent.class, PlayerComponent.class).get());
        this.game = game;
        this.camera = cam;
        input = ComponentMappers.input;
        soundEmitter = ComponentMappers.soundEmitter;
        inventory = ComponentMappers.inventory;
        move = ComponentMappers.move;

        
    }

	protected void processEntity(Entity entity, float deltaTime) {

	    InventoryComponent inventory = ComponentMappers.inventory.get(entity);
	    MoveComponent move = ComponentMappers.move.get(entity);
		timer.Update();
        if(timer.get_CounterMilliseconds()>50)
        {
            timer.StartCounter();

        	InputComponent input = ComponentMappers.input.get(entity);
	        PositionComponent posc = ComponentMappers.position.get(entity);

            Vector3 mousepos = camera.unproject(new Vector3(input.posX, input.posY,0));
	        Vector2 mousepos2 = new Vector2(mousepos.x, mousepos.y);
	        
	        mousepos2.sub(new Vector2(posc.x,posc.y));
	        float angle = mousepos2.angle();
	        float invtemp = (float)inventory.getMetalShards()/(float)inventory.maxMetalShards;
	        float movetemp = move.speed-move.speed*(invtemp*0.75f);
            vectorToAdd.scl(movetemp);
            System.out.println(inventory.getMetalShards());

	        MovementPacket packet = new MovementPacket(vectorToAdd.x,vectorToAdd.y,angle);
	        SendPacketClientEvent.emit(packet, true);


            vectorToAdd.setZero();
       }


       // velVector.set(input.get(entity).horizontal, input.get(entity).vertical);
       // velVector.nor();
       // velVector.scl(deltaTime);
       // vectorToAdd.add(velVector);

        vectorToAdd.set(input.get(entity).horizontal, input.get(entity).vertical);
        //System.out.println("StreetStepVolume: " + ComponentMappers.soundEmitter.get(entity).emitter.getGlobalVolume());
        if (!vectorToAdd.isZero())
        {
            if (ComponentMappers.soundEmitter.has(entity) && !soundEmitter.get(entity).isPlaying) {

                SoundEvent.emit("streetSteps", entity, true);

                //SoundEvent.emit("shotgun_shoot", entity, false);
                soundEmitter.get(entity).isPlaying = true;
            }
        }
        else
        {
            if (soundEmitter.get(entity).isPlaying) {
                SoundEvent.stopSound(entity);
                soundEmitter.get(entity).isPlaying = false;
            }
        }

        PhysixBodyComponent comp = ComponentMappers.physixBody.get(entity);
        if(comp != null && comp.getLinearVelocity().len()<0.001 && interpolate)
        {
            comp.setLinearVelocity(new Vector2(vectorToAdd).scl(500));
        }
    }
}
