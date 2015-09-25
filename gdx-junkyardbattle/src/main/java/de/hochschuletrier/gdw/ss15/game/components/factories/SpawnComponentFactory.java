/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.SpawnComponent;

/**
 *
 * @author Julien Saevecke
 */
public class SpawnComponentFactory extends ComponentFactory<EntityFactoryParam> {

	@Override
	public String getType() {
		return "Spawn";
	}

	@Override
	public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        SpawnComponent spawnComponent = new SpawnComponent();
        
        spawnComponent.respawn = false;
        spawnComponent.spawnPoint = null;
        
        spawnComponent.respawnTimer = GameConstants.RESPAWN_TIMER;
        
        entity.add(spawnComponent);
	}
}
