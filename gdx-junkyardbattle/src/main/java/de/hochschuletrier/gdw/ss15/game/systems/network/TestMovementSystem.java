package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.hochschuletrier.gdw.ss15.events.SoundEvent;
import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.components.*;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;
import de.hochschuletrier.gdw.ss15.game.network.Packets.MovementPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;

/**
 * Created by lukas on 22.09.15.
 */
public class TestMovementSystem extends IteratingSystem{

    Game game;
    Camera camera;
    MyTimer timer = new MyTimer(true);
    Vector2 velVector = new Vector2();
    Vector2 vectorToAdd = new Vector2(0,0);
    private ComponentMapper<InputComponent> input;
    private ComponentMapper<SoundEmitterComponent> soundEmitter;
    public TestMovementSystem(Game game, Camera cam)
    {
        super(Family.all(MoveComponent.class).get());
        this.game = game;
        this.camera = cam;
        input = ComponentMappers.input;
        soundEmitter = ComponentMappers.soundEmitter;

        
    }

	protected void processEntity(Entity entity, float deltaTime) {

		
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

            //System.out.println("Client vel: "+vectorToAdd);
	        MovementPacket packet = new MovementPacket(vectorToAdd.x,vectorToAdd.y,angle);
	        SendPacketClientEvent.emit(packet,true);
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


    }

}
