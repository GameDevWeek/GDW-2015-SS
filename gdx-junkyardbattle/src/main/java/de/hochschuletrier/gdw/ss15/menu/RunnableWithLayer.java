package de.hochschuletrier.gdw.ss15.menu;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;

public class RunnableWithLayer implements Runnable {

	Runnable runnable;
	public RunnableWithLayer(MenuPage page, MenuManager menuManager,Runnable runnable)
	{
		menuManager.addLayer(page);
		this.runnable=runnable;
	}
	@Override
	public void run() {
		runnable.run();
		
	}
	
}
