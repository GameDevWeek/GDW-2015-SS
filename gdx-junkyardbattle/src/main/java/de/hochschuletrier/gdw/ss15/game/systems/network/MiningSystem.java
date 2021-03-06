package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import de.hochschuletrier.gdw.ss15.events.MiningEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.*;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;

/**
 * Created by Ricardo on 25.09.2015.
 */
public class MiningSystem extends EntitySystem implements MiningEvent.Listener {

    ComponentMapper<InventoryComponent> inventoryComp = ComponentMappers.inventory;
    ComponentMapper<InputComponent> inputComp = ComponentMappers.input;
    ComponentMapper<SoundEmitterComponent> soundEmitterComp = ComponentMappers.soundEmitter;

    public MiningSystem()
    {
        MiningEvent.register(this);
    }

    @Override
    public void onMiningEvent(Entity playerEnt, Entity mineableEnt, float channelTime, float deltaTime) {
    	
    	//THIS IS WHERE DARK MAGIC HAPPENS
    	float chosenMiningPerSeconds = 0.f;
    	if(channelTime <= GameConstants.MINING_TIME_NEEDED_1)
    		chosenMiningPerSeconds = GameConstants.MINING_PER_SECOND_STAGE_1;
    	
    	
    	else if(channelTime > GameConstants.MINING_TIME_NEEDED_1 && channelTime <=GameConstants.MINING_TIME_NEEDED_2)
    		chosenMiningPerSeconds = GameConstants.MINING_PER_SECOND_STAGE_2;
    	
    	
    	else if(channelTime > GameConstants.MINING_TIME_NEEDED_2 && channelTime <= GameConstants.MINING_TIME_NEEDED_3)
    		chosenMiningPerSeconds = GameConstants.MINING_PER_SECOND_STAGE_3;
    	
    	
    	else if(channelTime > GameConstants.MINING_TIME_NEEDED_3)
    		chosenMiningPerSeconds = GameConstants.MINING_PER_SECOND_STAGE_4;
    	
    	
        int minedMetalShards = (int)Math.ceil(chosenMiningPerSeconds*deltaTime);

        transferMines(mineableEnt, playerEnt, minedMetalShards);

        int actuallyMinedMetalShards = inventoryComp.get(mineableEnt).subMetalShards(minedMetalShards);
        int collected = inventoryComp.get(playerEnt).addMetalShards(actuallyMinedMetalShards);



    }

    private void transferMines(Entity givingEntity, Entity takingEntity, int minedMetalShards)
    {
        int givingEntityHas = inventoryComp.get(givingEntity).getMetalShards();
        int givingEntityMin = inventoryComp.get(givingEntity).minMetalShards;

        int takingEntityHas = inventoryComp.get(takingEntity).getMetalShards();
        int takingEntityMax = inventoryComp.get(takingEntity).maxMetalShards;

        int givingEntityCanGive = givingEntityHas - givingEntityMin;
        int takingEntityCanTake = takingEntityMax - takingEntityHas;

        int shardsToTransfer = Math.min(givingEntityCanGive, Math.min(takingEntityCanTake, minedMetalShards));

        inventoryComp.get(givingEntity).subMetalShards(shardsToTransfer);
        inventoryComp.get(takingEntity).addMetalShards(shardsToTransfer);
    }
}
