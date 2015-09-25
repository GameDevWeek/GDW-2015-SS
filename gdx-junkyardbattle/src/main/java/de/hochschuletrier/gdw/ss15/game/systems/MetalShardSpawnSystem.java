package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.ServerGame;
import de.hochschuletrier.gdw.ss15.game.components.MetalShardSpawnComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;

public class MetalShardSpawnSystem extends IteratingSystem{

    private float countEntities=0;
    private float MaxCountEntities = 20;

    private ServerGame game;
    public MetalShardSpawnSystem(ServerGame game) {
        super(Family.all(PositionComponent.class, MetalShardSpawnComponent.class).get());
        this.game = game;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        float minTimeBetweenSpawns = 2.f;
        MyTimer timer = ComponentMappers.metalShardSpawn.get(entity).timer;
        timer.Update();
        if (timer.get_CounterSeconds() > minTimeBetweenSpawns)
        {
            timer.StartCounter();
            if(countEntities<MaxCountEntities)
            {
                //only sapwn when nto entowh element
                //System.out.println("timer getcalled");
                countEntities++;//todo decrease when skarp is removed <-- Importatnt
                //spawn
                float angleBetweenShards = 360 / ComponentMappers.metalShardSpawn.get(entity).shards;
                for (int i = 0; i < ComponentMappers.metalShardSpawn.get(entity).shards; i++) {
                    Vector2 center = new Vector2(ComponentMappers.position.get(entity).x, ComponentMappers.position.get(entity).y);
                    float angle = i * angleBetweenShards;
                    //Vector2 pos = new Vector2(center);
                    //pos.add(20.f, 0.f);

                    //System.out.println("Spawned entity "+ pos);
                    game.createEntity("metalServer", center.x, center.y);
                }
            }
        }
    }
}
