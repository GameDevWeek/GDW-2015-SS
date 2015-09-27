package de.hochschuletrier.gdw.ss15.game.systems.renderers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.ss15.events.WeaponCharging;
import de.hochschuletrier.gdw.ss15.events.WeaponUncharged;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.GameGlobals;
import de.hochschuletrier.gdw.ss15.game.components.ClientIsShootingComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.animation.AnimationState;
import de.hochschuletrier.gdw.ss15.game.components.animation.AnimatorComponent;
import de.hochschuletrier.gdw.ss15.game.components.effects.AttachedEntityComponent;
import de.hochschuletrier.gdw.ss15.game.components.effects.ChargeEffectComponent;
import de.hochschuletrier.gdw.ss15.game.components.effects.MagneticBeamEffectComponent;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.ConeLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.PointLightComponent;
import de.hochschuletrier.gdw.ss15.game.rendering.ZoomingModes;

public class EffectAddSystem extends IteratingSystem implements EntityListener, WeaponCharging.Listener, WeaponUncharged.Listener {
    private final PooledEngine engine;
    private Entity player;
    private final float CHARGE_EFFECT_START = 0.1f;
    private final float gatherTime = 0.3f;
    
    @SuppressWarnings("unchecked")
    public EffectAddSystem(PooledEngine engine) {
        super(Family.all(PlayerComponent.class).one(ConeLightComponent.class).get(), GameConstants.PRIORITY_ADD_EFFECT_SYSTEM);
        this.engine = engine;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(Family.all(PlayerComponent.class, ClientIsShootingComponent.class).get(), this);
        
        WeaponCharging.register(this);
        WeaponUncharged.register(this);
    }
    
    public void removedFromEngine(Engine engine) {
        WeaponCharging.unregister(this);
        WeaponUncharged.unregister(this);
    }
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent posComp = ComponentMappers.position.get(entity);
//        ConeLightComponent coneLightComp = ComponentMappers.coneLight.get(entity);
        AttachedEntityComponent particleEntityComp = ComponentMappers.attachedEntity.get(entity);
        InputComponent inputComp = ComponentMappers.input.get(entity);
        ClientIsShootingComponent shootingComp = ComponentMappers.ClientIsSchooting.get(entity);
        
        if(particleEntityComp != null) {
            for(Entity attachedEntity : particleEntityComp.entities) {
                AnimatorComponent animatorComp = ComponentMappers.animator.get(attachedEntity);
                MagneticBeamEffectComponent beamComp = ComponentMappers.magneticBeamEffect.get(attachedEntity);
                if(beamComp != null && animatorComp != null) {
                    beamComp.gatherStateTime += deltaTime;
                    animatorComp.draw = (beamComp.gatherStateTime <= gatherTime);
                    if(shootingComp.onGather)
                        beamComp.gatherStateTime = 0.f;
                }
                
                if(ComponentMappers.chargeEffect.has(attachedEntity) && animatorComp != null) {
                    ChargeEffectComponent chargeEffectComp = ComponentMappers.chargeEffect.get(attachedEntity);
                    PositionComponent attachedEntityPosComp = ComponentMappers.position.get(attachedEntity);
                    if(inputComp != null && inputComp.shoot) {
                        chargeEffectComp.stateTime += deltaTime;
                        if(chargeEffectComp.stateTime >= CHARGE_EFFECT_START) {
                            animatorComp.draw = true;
                            attachedEntityPosComp.x = posComp.x;
                            attachedEntityPosComp.y = posComp.y;
                        }
                    } else {
                        animatorComp.draw = false;
                        chargeEffectComp.stateTime = 0.f;
                    } 
                }
            }
        }
    }

    @Override
    public void entityAdded(Entity entity) {
        if(ComponentMappers.player.get(entity).isLocalPlayer)
            player = entity;
        
        if(!ComponentMappers.coneLight.has(entity)) {
            ConeLightComponent coneLightComp = engine.createComponent(ConeLightComponent.class);
            coneLightComp.set(new Color(0.f, 1.f, 1.f, 0.9f), 15.f, 0.f, 40.f);
            entity.add(coneLightComp);
        }
        
        if(!ComponentMappers.pointLight.has(entity)) {
            PointLightComponent pointLightComponent = engine.createComponent(PointLightComponent.class);
            pointLightComponent.set(new Color(1.f, 1.f, 0.f, GameConstants.PLAYER_POINT_LIGHT_ALPHA), 
                    GameConstants.PLAYER_POINT_LIGHT_DISTANCE);
            pointLightComponent.pointLight.setXray(true);
            entity.add(pointLightComponent);
        }
        
        addChargeEffect(entity);
        addMagneticBeamEffect(entity);
    }
    
    private void addMagneticBeamEffect(Entity entity) {
        PositionComponent entityPos = ComponentMappers.position.get(entity);
        if(!ComponentMappers.attachedEntity.has(entity)) 
            entity.add(engine.createComponent(AttachedEntityComponent.class));
        
        Entity attachedMagneticBeamEntity = engine.createEntity();
        
        AnimatorComponent animatorComp = engine.createComponent(AnimatorComponent.class);
        AnimationExtended anim = GameGlobals.assetManager.getAnimation("traktor_strahl");
        animatorComp.animationStates.put(AnimationState.IDLE, anim);
        animatorComp.positionOffsetX = 0.f;
        animatorComp.positionOffsetY = -170.f;
        animatorComp.initialRotation = 90.f;
        animatorComp.scaleX = 0.5f;
        animatorComp.scaleY = 0.5f;
        animatorComp.draw = false;
        
        AttachedEntityComponent attachedEntityComp = ComponentMappers.attachedEntity.get(entity);
        attachedEntityComp.entities.add(attachedMagneticBeamEntity);
        attachedMagneticBeamEntity.add(animatorComp);
        attachedMagneticBeamEntity.add(entityPos);
        attachedMagneticBeamEntity.add(engine.createComponent(MagneticBeamEffectComponent.class));
        engine.addEntity(attachedMagneticBeamEntity);
        entity.add(attachedEntityComp);
    }
    
    public void addChargeEffect(Entity entity) {
        PositionComponent entityPos = ComponentMappers.position.get(entity);
        PlayerComponent playerComp = ComponentMappers.player.get(entity);
        if(!ComponentMappers.attachedEntity.has(entity)) 
            entity.add(engine.createComponent(AttachedEntityComponent.class));
        
        Entity chargeEffectEntity = engine.createEntity();
        PositionComponent posComp = engine.createComponent(PositionComponent.class);
        posComp.x = entityPos.x;
        posComp.y = entityPos.y;
        posComp.layer = entityPos.layer - 1;
        
        AnimatorComponent animatorComp = engine.createComponent(AnimatorComponent.class);
        AnimationExtended animExtended = null; 
        switch(playerComp.teamID) {
        case 0:
            animExtended = GameGlobals.assetManager.getAnimation("charge_red");
            break;
        case 1:
            animExtended = GameGlobals.assetManager.getAnimation("charge_blue");
            break;
        }

        animatorComp.animationStates.put(AnimationState.IDLE, animExtended);
        animatorComp.draw = false;
        
        AttachedEntityComponent attachedEntityComp = ComponentMappers.attachedEntity.get(entity);
        attachedEntityComp.entities.add(chargeEffectEntity);
        chargeEffectEntity.add(animatorComp);
        chargeEffectEntity.add(posComp);
        chargeEffectEntity.add(engine.createComponent(ChargeEffectComponent.class));
        engine.addEntity(chargeEffectEntity);
        entity.add(attachedEntityComp);
    }

    @Override
    public void entityRemoved(Entity entity) {
        if(player == entity)
            player = null;
    }

    @Override
    public void onWeaponUncharged() {
        if(player != null) {
            PointLightComponent pointLightComp = ComponentMappers.pointLight.get(player);
            pointLightComp.pointLight.setDistance(GameConstants.PLAYER_POINT_LIGHT_DISTANCE);
        }
    }

    @Override
    public void onWeaponCharging(float fireChannelAmount) {
        if(player != null) {
            PointLightComponent pointLightComp = ComponentMappers.pointLight.get(player);
            AttachedEntityComponent attachedEntityComp = ComponentMappers.attachedEntity.get(player);
            if(pointLightComp != null) {
                float distance = ZoomingModes.interpolate(GameConstants.ZOOM_MODE, GameConstants.PLAYER_POINT_LIGHT_DISTANCE, 
                        GameConstants.PLAYER_POINT_LIGHT_DISTANCE_CHARGED, fireChannelAmount);
                pointLightComp.pointLight.setDistance(distance);
                pointLightComp.pointLight.getColor().a = GameConstants.PLAYER_POINT_LIGHT_ALPHA 
                        + fireChannelAmount;
            }
            
            if(attachedEntityComp != null) {
                for(Entity attachedEntity : attachedEntityComp.entities) {
                    AnimatorComponent animComp = ComponentMappers.animator.get(attachedEntity);
                    if(animComp != null && ComponentMappers.chargeEffect.has(attachedEntity)) {
                        animComp.scaleX = animComp.scaleY = ZoomingModes.interpolate(GameConstants.ZOOM_MODE, 0.1f, 
                                0.5f, fireChannelAmount);
                    }
                }
            }
        }
    }

}
