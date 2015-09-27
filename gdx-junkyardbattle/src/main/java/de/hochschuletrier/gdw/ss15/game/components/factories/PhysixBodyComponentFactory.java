package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhysixBodyComponentFactory extends ComponentFactory<EntityFactoryParam> {

    private static final Logger logger = LoggerFactory.getLogger(PhysixBodyComponentFactory.class); 
    private PhysixSystem physixSystem;
    
    public static final short ABGRUND = 0x0001;
    public static final short BULLET = 0x0002;
    public static final short LIGHT = 0x0004;
    public static final short LIGHT_PASS_THROUGH = 0x0008;

    @Override
    public String getType() {
        return "PhysixBody";
    }

    @Override
    public void init(PooledEngine engine, AssetManagerX assetManager) {
        super.init(engine, assetManager);

        physixSystem = engine.getSystem(PhysixSystem.class);
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        final PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
        modifyComponent.schedule(() -> {
            String shape = properties.getString("shape", "");
            switch(shape) {
                case "circle": addCircle(param, entity, properties); break;
                case "box": addBox(param, entity, properties); break;
                case "rectangle": addRectangle(param, entity, properties); break;
                default: logger.error("Unknown type: {}", shape); break;
            }
        });
        
        
        entity.add(modifyComponent);
    }

    private void addCircle(EntityFactoryParam param, Entity entity, SafeProperties properties) {
        PhysixBodyComponent bodyComponent = getBodyComponent(param, entity);
        PhysixFixtureDef fixtureDef = getFixtureDef(properties)
                .shapeCircle(properties.getFloat("size", 5), new Vector2(properties.getFloat("offsetX", 0),properties.getFloat("offsetY", 0)));
        bodyComponent.createFixture(fixtureDef);
        bodyComponent.setPosition(param.x,param.y);
        addProperties(entity, properties, bodyComponent);
    }

    private void addBox(EntityFactoryParam param, Entity entity, SafeProperties properties) {
        PhysixBodyComponent bodyComponent = getBodyComponent(param, entity);
        PhysixFixtureDef fixtureDef = getFixtureDef(properties)
                .shapeBox(properties.getFloat("size", 5), properties.getFloat("size", 5));
        bodyComponent.createFixture(fixtureDef);
        bodyComponent.setPosition(param.x,param.y);
        addProperties(entity, properties, bodyComponent);
    }
    
    private void addRectangle(EntityFactoryParam param, Entity entity, SafeProperties properties) {
        PhysixBodyComponent bodyComponent = getBodyComponent(param, entity);
        PhysixFixtureDef fixtureDef = getFixtureDef(properties)
                .shapeBox(properties.getFloat("width", 10), properties.getFloat("height", 5),new Vector2(properties.getFloat("offsetX", 0),properties.getFloat("offsetY", 0)),0);
        bodyComponent.createFixture(fixtureDef);
        bodyComponent.setPosition(param.x,param.y);
        addProperties(entity, properties, bodyComponent);
    }
    
    private void addProperties(Entity entity, SafeProperties properties, PhysixBodyComponent bodyComponent){
    	boolean isfixedRotation = properties.getBoolean("fixedRotation");
        bodyComponent.getBody().setFixedRotation(isfixedRotation);
        if(!bodyComponent.getBody().getFixtureList().get(0).isSensor()){
	        switch(properties.getString("type", "static")){
	        case"dynamic":
	        	bodyComponent.getBody().setType(BodyType.DynamicBody);
	        	break;
	        case"static":
	       	bodyComponent.getBody().setType(BodyType.StaticBody);
	        	break;
	        case"kinematic":
	        	bodyComponent.getBody().setType(BodyType.KinematicBody);
	        	break;
	        }
        }
        
        

        entity.add(bodyComponent);
    }

    private PhysixBodyComponent getBodyComponent(EntityFactoryParam param, Entity entity) {
        PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = getBodyDef(param);
        bodyComponent.init(bodyDef, physixSystem, entity);
        return bodyComponent;
    }

    private PhysixBodyDef getBodyDef(EntityFactoryParam param) {
        return new PhysixBodyDef(BodyDef.BodyType.DynamicBody, physixSystem)
                .position(param.x, param.y).fixedRotation(false);
    }

    private PhysixFixtureDef getFixtureDef(SafeProperties properties) {
    	PhysixFixtureDef PhysixFixDef = new PhysixFixtureDef(physixSystem)
        .density(properties.getFloat("density", 5))
        .friction(properties.getFloat("friction", 5))
        .sensor(properties.getBoolean("sensor", false) )
        .restitution(properties.getFloat("restitution", 0));
    	switch(properties.getString("category", "")){
        case "BULLET":
        	PhysixFixDef.category(BULLET);
        	break;
    	}
        return PhysixFixDef;
    }
}
