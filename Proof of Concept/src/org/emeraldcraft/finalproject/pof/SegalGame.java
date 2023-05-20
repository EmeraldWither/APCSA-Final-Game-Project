package org.emeraldcraft.finalproject.pof;

import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.gameobjects.Human;
import org.emeraldcraft.finalproject.pof.gameobjects.Player;
import org.emeraldcraft.finalproject.pof.gameobjects.TEMPORARYObstacle;
import org.emeraldcraft.finalproject.pof.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	
	private boolean isRunning = false;
	//destroy queue
	List<GameObject> removeObjectsQueue = new ArrayList<GameObject> ();  
	List<GameObject> addObjectsQueue = new ArrayList<GameObject> ();  

	
	public void init(){
		Logger.log("Game init Called");
		try {
			gameObjects.clear();
			removeObjectsQueue.clear();
			addObjectsQueue.clear();
			System.gc();
			
			player = new Player();
			background = new Background();
			new TEMPORARYObstacle(300);
			new TEMPORARYObstacle(800);
			new TEMPORARYObstacle(1300);
			new TEMPORARYObstacle(1600);

		} catch (IOException e) {
			Logger.log("Failed to load images.");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	public void stop() {
		isRunning = false;
	}


	public Human createHuman() {
		Human human = new Human();
		humans.add(human);
		return human;
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
		addObjectsQueue.add(gameObject);
	}
	public boolean isMainMenu() {
		return this.isMainMenu;
	}

	public void start() {
		Logger.log("STARTING A NEW GAME");
		isMainMenu = false;
		isRunning = true;
		new Thread(() -> {
			//move the player
			long lastTickTime;
			while(isRunning){
				lastTickTime = System.currentTimeMillis();
				for(GameObject gameObject: addObjectsQueue) {
					if(gameObject.getRenderPriority() >= gameObjects.size()) gameObjects.add(gameObject);
					else gameObjects.add(gameObject.getRenderPriority() - 1, gameObject);
				}
				addObjectsQueue.clear();
				
				//actual stuff to do				
				for(GameObject gameObject : gameObjects) {
					gameObject.tick();
					if(gameObject.shouldRemove()) gameObject.remove();
				}
				
				//queue removing any gameobjects
				gameObjects.removeAll(removeObjectsQueue);
				removeObjectsQueue.clear();

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
		removeObjectsQueue.add(gameObject);
	}

	public Player getPlayer() {
		return player;
	}
}
