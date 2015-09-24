package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.events.SoundEvent;
import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.components.*;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityUpdatePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.MovementPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.Tools;

/**
 * Created by lukas on 22.09.15.
 * With code of Ricardo Helms and Dominik Petersdorf
 */
public class TestMovementSystem extends IteratingSystem{

    Game game;
    Camera camera;
    MyTimer timer = new MyTimer(true);
    Vector2 velVector = new Vector2();
    Vector2 vectorToAdd = new Vector2(0,0);
    private ComponentMapper<MoveComponent> move;
    private ComponentMapper<InputComponent> input;
    private ComponentMapper<InventoryComponent> inventory;
    private ComponentMapper<SoundEmitterComponent> soundEmitter;
    public TestMovementSystem(Game game, Camera cam)
    {
        super(Family.all(InputComponent.class, MoveComponent.class, InventoryComponent.class).get());
        this.game = game;
        this.camera = cam;
        move = ComponentMappers.move;
        input = ComponentMappers.input;
        inventory = ComponentMappers.inventory;
        soundEmitter = ComponentMappers.soundEmitter;
    }

    protected void processEntity(Entity entity, float deltaTime) {

        timer.Update();
        if(timer.get_CounterMilliseconds()>100)
        {
            InputComponent input = ComponentMappers.input.get(entity);
            PositionComponent posc = ComponentMappers.position.get(entity);
            InventoryComponent inventory = ComponentMappers.inventory.get(entity);
            //System.out.println(inventory);
            timer.StartCounter();
            if(inventory.getMetalShards()<= GameConstants.MAX_METALSHARDS && inventory.getMetalShards()>0)
            {
                float invtemp = inventory.getMetalShards()/GameConstants.MAX_METALSHARDS;
                vectorToAdd.scl(move.get(entity).speed-move.get(entity).speed*(invtemp*0.75f));
            }
            else
            {
                vectorToAdd.scl(move.get(entity).speed);
            }
            if (!vectorToAdd.isZero())
            {
                if (ComponentMappers.soundEmitter.has(entity) && !soundEmitter.get(entity).isPlaying) {

                    SoundEvent.emit("streetSteps", entity, true);
                    soundEmitter.get(entity).isPlaying = true;
                }
            }
            else
            {
                SoundEvent.stopSound(entity);
                soundEmitter.get(entity).isPlaying = false;
            }


            Vector3 mousepos = camera.unproject(new Vector3(input.posX, input.posY,0));
            Vector2 mousepos2 = new Vector2(mousepos.x, mousepos.y);

            mousepos2.sub(new Vector2(posc.x,posc.y));
            float angle = mousepos2.angle();
            //System.out.println(angle);

//	        float rotation = (float)Math.atan2(mousepos2.y - posc.y,mousepos2.x - posc.x);

            MovementPacket packet = new MovementPacket(vectorToAdd.x,vectorToAdd.y,angle);
            SendPacketClientEvent.emit(packet,false);
            vectorToAdd.setZero();
        }

        velVector.set(input.get(entity).horizontal, input.get(entity).vertical);
        velVector.nor();
        velVector.scl(deltaTime);
        vectorToAdd.add(velVector);

    }

}
