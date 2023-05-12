package org.emeraldcraft.finalproject.pof;

import java.util.ArrayList;

public class SegalGame {
	private ArrayList<Human> humans = new ArrayList<>();
	private final Player thePlayer = new Player();
	public void addHuman(Human human) {
		humans.add(human);
	}
	public void removeHuman(Human human) {
		humans.remove(human);
	}
}
