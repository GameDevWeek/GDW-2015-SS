﻿package de.hochschuletrier.gdw.ss15.game.systems.network;

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

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.components.HealthComponent;
import de.hochschuletrier.gdw.ss15.game.components.MoveComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;
import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityUpdatePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.MovementPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.Tools;

/**
 * Created by lukas on 22.09.15.
 */
public class TestMovementSystem extends IteratingSystem{

    Game game;
    Camera camera;
    MyTimer timer = new MyTimer(true);
    Vector2 velVector = new Vector2();
    Vector2 vectorToAdd = new Vector2(0,0);
    private ComponentMapper<MoveComponent> move;
    private ComponentMapper<InputComponent> input;
    public TestMovementSystem(Game game, Camera cam)
    {
        super(Family.all(InputComponent.class, MoveComponent.class).get());
        this.game = game;
        this.camera = cam;
        move = ComponentMappers.move;
        input = ComponentMappers.input;
    }

	protected void processEntity(Entity entity, float deltaTime) {
		
		timer.Update();
		Tools.Sleep(100);
        if(timer.get_CounterMilliseconds()>100)
        {
            timer.StartCounter();
        
	        vectorToAdd.nor();
	        vectorToAdd.scl(move.get(entity).speed);
	        InputComponent input = ComponentMappers.input.get(entity);
	        PositionComponent posc = ComponentMappers.position.get(entity);
	        
	        Vector3 mousepos = camera.unproject(new Vector3(input.posX, input.posY,0));
	
	        float rotation = (float)Math.atan2(mousepos.y - posc.y,mousepos.x - posc.y);
	        MovementPacket packet = new MovementPacket(vectorToAdd.x,vectorToAdd.y,rotation);
	        SendPacketClientEvent.emit(packet,true);
	        vectorToAdd.setZero();
        }
        
        velVector.set(input.get(entity).horizontal, input.get(entity).vertical);
        velVector.nor();
        velVector.scl(deltaTime);
        vectorToAdd.add(velVector);
		

    }
