package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;

public class MetalShardSpawnComponent extends Component implements Pool.Poolable{

	//wie häufig soll gespawnt werden
	public MyTimer timer;
	//wieviel soll gespawnt werden
	public int shards;
	
	public MetalShardSpawnComponent() {
		timer = new MyTimer(true);
		shards = 10;
		System.out.println("MetalShardSpawn");
	}
	
	@Override
	public void reset() {
		timer.ResetTimer();
		shards = 10;
	}

}
