/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.events.PlayerDiedEvent;
import de.hochschuletrier.gdw.ss15.events.network.server.NetworkNewPlayerEvent;
import de.hochschuletrier.gdw.ss15.events.network.server.SendPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.MapLoader;
import de.hochschuletrier.gdw.ss15.game.MapSpecialEntities;
import de.hochschuletrier.gdw.ss15.game.components.DeathComponent;
import de.hochschuletrier.gdw.ss15.game.components.HealthComponent;
import de.hochschuletrier.gdw.ss15.game.components.HealthComponent.HealthState;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.SpawnComponent;
import de.hochschuletrier.gdw.ss15.game.components.animation.AnimationState;
import de.hochschuletrier.gdw.ss15.game.components.animation.AnimatorComponent;
import de.hochschuletrier.gdw.ss15.game.components.effects.ParticleEffectComponent;
import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityUpdatePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.HealthPacket;
import de.hochschuletrier.gdw.ss15.game.systems.network.TestMovementSystem;

import java.util.ArrayList;

/**
 *
 * @author Julien Saevecke
 */
public class SpawnSystem extends EntitySystem implements PlayerDiedEvent.Listener, NetworkNewPlayerEvent.Listener, MapLoader.TileCreationListener{
    
    float[] orange = new float[]{
        1,
        0.498f,
        0.153f
    };
            
    float[] blue = new float[]{
        0,
        0,
        1
    };
    
    private ImmutableArray<Entity> entities; 

    @Override
    public void onPlayerDied(Entity player) {
        SpawnComponent spawnComponent = ComponentMappers.spawn.get(player);
        spawnComponent.respawn = true;
        spawnComponent.respawnTimer = GameConstants.RESPAWN_TIMER;
        
        HealthComponent healthComponent = ComponentMappers.health.get(player);
        healthComponent.health = GameConstants.START_HEALTH;
        
        player.remove(DeathComponent.class);
        
        PhysixBodyComponent body = ComponentMappers.physixBody.get(player);
        body.setActive(false);
        TestMovementSystem.interpolate = false;
    }
    
    public static class SpawnInfo
    {
        public int teamID = -1;
        public Vector2 spawnPosition = new Vector2();
        public boolean occupied = false;
        
        public SpawnInfo()
        {
            this.teamID = -1;
            this.spawnPosition = new Vector2();
            this.occupied = false;
        }
        
        public SpawnInfo(int teamID, Vector2 spawnPosition, boolean occupied)
        {
            this.teamID = teamID;
            this.spawnPosition = spawnPosition;
            this.occupied = occupied;
        }
    }
    
    @Override
    public void onTileCreate(MapSpecialEntities.CreatorInfo info) {
        if(info.asObject == null)
            return ;
        
        if(info.asObject.getName().equalsIgnoreCase("Spawn")){
            SpawnInfo spawnInfo = new SpawnInfo();
            String team = info.asObject.getProperty("Team", "@");

            spawnInfo.teamID = team.charAt(0) - 'A';
            
            spawnInfo.occupied = false;
            
            spawnInfo.spawnPosition.x = info.asObject.getX();
            spawnInfo.spawnPosition.y = info.asObject.getY();
            
            ParticleEffectComponent particleComponent = ComponentMappers.particleEffect.get(info.entity);
            ParticleEffect spawnEffect = particleComponent.particleEffect;
            Array<ParticleEmitter> emitters = spawnEffect.getEmitters();

            for(ParticleEmitter emitter : emitters){
                if(spawnInfo.teamID == 0)
                    emitter.getTint().setColors(orange);
                else
                    emitter.getTint().setColors(blue);
            }
            
            SpawnSystem.spawnpoints.add(spawnInfo);
        }
    }
    
    public static ArrayList<SpawnInfo> spawnpoints = new ArrayList<>();
    private int spawnsOccupied = 0;
    
    public SpawnSystem()
    {
        NetworkNewPlayerEvent.registerListener(this);
    }
    
    @Override
    public void onNetworkNewPacket(Entity ent) {
        if(spawnsOccupied > spawnpoints.size()-1){
            for(SpawnInfo info : spawnpoints){
                info.occupied = false;
            }
            spawnsOccupied = 0;
        }
        
        for(SpawnInfo info : spawnpoints)
        {
            if(info.occupied)
                continue;
            
            SpawnComponent spawnComponent = ComponentMappers.spawn.get(ent);
            PlayerComponent playerComponent = ComponentMappers.player.get(ent);
            
            if(playerComponent.teamID == info.teamID){
                spawnComponent.spawnPoint = info.spawnPosition;
                spawnComponent.respawn = true;
                spawnComponent.respawnTimer = -1;
                info.occupied = true;
                spawnsOccupied++;
                
                return;
            }
        }
        
        throw new RuntimeException("No spawnpoints available - too many players.");
    }
    
    @Override
    public void addedToEngine(Engine engine)
    {
        entities = engine.getEntitiesFor(Family.all(SpawnComponent.class).get());
    }
    
    @Override
    public void update(float deltaTime)
    {
        for(Entity entity : entities){
            SpawnComponent spawnComponent = ComponentMappers.spawn.get(entity);

            if(spawnComponent.respawn){
                spawnComponent.respawnTimer -= deltaTime;
                
                if(spawnComponent.respawnTimer <= 0.f){
                    PhysixBodyComponent body = ComponentMappers.physixBody.get(entity);
                    
                    body.setActive(true);
                    body.setPosition(spawnComponent.spawnPoint.x, spawnComponent.spawnPoint.y);
                    TestMovementSystem.interpolate = true;
                    
                    HealthPacket healthPacket = new HealthPacket();
                    healthPacket.health = ComponentMappers.health.get(entity).health;
                    healthPacket.id = ComponentMappers.positionSynch.get(entity).networkID;
                    
                    SendPacketServerEvent.emit(healthPacket, true);
                    spawnComponent.respawnTimer=0;
                    //spawnComponent.respawnTimer = GameConstants.RESPAWN_TIMER;
                    spawnComponent.respawn = false;
                }
            }
        }
    }
}
