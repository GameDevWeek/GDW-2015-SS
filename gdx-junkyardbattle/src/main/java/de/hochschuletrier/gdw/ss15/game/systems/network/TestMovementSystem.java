package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityUpdatePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.MovementPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;

/**
 * Created by lukas on 22.09.15.
 */
public class TestMovementSystem extends EntitySystem{

    Game game;
    MyTimer timer = new MyTimer(true);

    public TestMovementSystem(Game game)
    {
        super(53);
        this.game = game;
    }

    @Override
    public void update(float deltatime)
    {
        timer.Update();
        if(timer.get_CounterMilliseconds()>100)
        {
            timer.StartCounter();

            int x=0;
            int y=0;

            if(Gdx.input.isKeyPressed(Input.Keys.W))
            {
                y--;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.S))
            {
                y++;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.A))
            {
                x--;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.D))
            {
                x++;
            }

            MovementPacket packet = new MovementPacket(x*10,y*10,0);
            SendPacketClientEvent.emit(packet,true);
        }
    }

    }
