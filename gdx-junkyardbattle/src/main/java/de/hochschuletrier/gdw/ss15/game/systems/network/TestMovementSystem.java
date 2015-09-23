package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.components.MoveComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;
import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityUpdatePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.MovementPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;

/**
 * Created by lukas on 22.09.15.
 */
public class TestMovementSystem extends IteratingSystem{

    Game game;
    MyTimer timer = new MyTimer(true);
    Vector2 vectorToAdd = new Vector2();
    private ComponentMapper<MoveComponent> move;
    private ComponentMapper<InputComponent> input;
    public TestMovementSystem(Game game)
    {
        super(Family.all(InputComponent.class, MoveComponent.class).get());
        this.game = game;
        move = ComponentMappers.move;
        input = ComponentMappers.input;
    }

//    @Override
//    public void update(float deltatime)
//    {
//        timer.Update();
//        if(timer.get_CounterMilliseconds()>100)
//        {
//            timer.StartCounter();
//
//            int x=0;
//            int y=0;
//
//            if(Gdx.input.isKeyPressed(Input.Keys.W))
//            {
//                y--;
//            }
//            if(Gdx.input.isKeyPressed(Input.Keys.S))
//            {
//                y++;
//            }
//            if(Gdx.input.isKeyPressed(Input.Keys.A))
//            {
//                x--;
//            }
//            if(Gdx.input.isKeyPressed(Input.Keys.D))
//            {
//                x++;
//            }
//
//            MovementPacket packet = new MovementPacket(x*2,y*2,0);
//            SendPacketClientEvent.emit(packet,true);
//        }
//    }

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		
		timer.Update();
        if(timer.get_CounterMilliseconds()>100)
        {
            timer.StartCounter();

        
//        vectorToAdd.nor();
        vectorToAdd.scl(move.get(entity).speed);
        MovementPacket packet = new MovementPacket(vectorToAdd.x,vectorToAdd.y,0);
        SendPacketClientEvent.emit(packet,true);
        
        System.out.println(vectorToAdd.x);
        System.out.println(vectorToAdd.y);
        vectorToAdd.setZero();
        }
        vectorToAdd.add(input.get(entity).horizontal*deltaTime, input.get(entity).vertical*deltaTime);


		
	}

    }
