package org.emeraldcraft.finalproject.pof;

import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.gameobjects.Human;
import org.emeraldcraft.finalproject.pof.gameobjects.Player;
import org.emeraldcraft.finalproject.pof.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class SegalGame {
	private static final SegalGame instance = new SegalGame();

	public static SegalGame getInstance() {
		return instance;
	}

	private final ArrayList<Human> humans = new ArrayList<>();
	private final ArrayList<GameObject> gameObjects = new ArrayList<>();

	private Player player;
	private Background background;
	private boolean isMainMenu = true;
	public void init(){
		Logger.log("Game init Called");
		try {
			player = new Player();
			background = new Background();
		} catch (IOException e) {
			Logger.log("Failed to load images.");
			e.printStackTrace();
			System.exit(-1);
		}
	}


	public void addHuman(Human human) {
		Logger.log("Added human: " + human);
		humans.add(human);
	}

	public ArrayList<Human> getHumans() {
		return new ArrayList<>(humans);
	}

	public void removeHuman(Human human) {
		humans.remove(human);
	}

	public ArrayList<GameObject> getGameObjects() {
		return new ArrayList<>(gameObjects);
	}
	public void registerGameObject(GameObject gameObject){
		Logger.log("Registering a game object");
		if(gameObject.getRenderPriority() >= gameObjects.size()) gameObjects.add(gameObject);
		else gameObjects.add(gameObject.getRenderPriority() - 1, gameObject);
	}
	public boolean isMainMenu() {
		return this.isMainMenu;
	}

	public void start() {
		Logger.log("STARTING A NEW GAME");
		isMainMenu = false;
		new Thread(() -> {
			//move the player
			long lastTickTime;
			while(true){
				lastTickTime = System.currentTimeMillis();

				
				//actual stuff to do				
				for(GameObject gameObject : gameObjects) {
					gameObject.tick();
				}




				//Tick calculations
				long timeElapsed = System.currentTimeMillis() - lastTickTime;
				if(timeElapsed > 25) Logger.warn("Is the main thread lagging? Thread took " + timeElapsed + "ms to successfully tick.");
				else {
					try {
						Thread.sleep(25-timeElapsed);
					} catch (InterruptedException e) {
						Logger.warn("Failed to sleep the main thread. Crashing with stacktrace");
						e.printStackTrace();
						System.exit(-1);
					}
				}
			}
		}).start();
	}

	public void deRegisterGameObject(GameObject gameObject) {
		gameObjects.remove(gameObject);
	}

	public Player getPlayer() {
		return player;
	}
}
