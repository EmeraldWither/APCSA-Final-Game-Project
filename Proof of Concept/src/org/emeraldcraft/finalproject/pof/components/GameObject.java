package org.emeraldcraft.finalproject.pof.components;

import org.emeraldcraft.finalproject.pof.SegalGame;
import org.emeraldcraft.finalproject.pof.utils.Logger;

import java.awt.*;

public abstract class GameObject {
	private final String name;
	private final Rectangle location;
	private final int renderPriority;

	public GameObject(String name, Rectangle hitbox, int renderPriority) {
		this.name = name;
		this.location = hitbox;
		this.renderPriority = renderPriority;
		SegalGame.getInstance().registerGameObject(this);
		Logger.log("Created a new GameObject of: " + this);
	}
	public Rectangle getLocation() {
		return this.location;
	}
	public abstract boolean shouldRemove();
	public void remove() {
		Logger.log(this + " has been deregistered and has been removed.");
		SegalGame.getInstance().deRegisterGameObject(this);
	}
	public String getName() {
		return this.name;
	}
	@Override
	public String toString() {
		return "GameObject { name: \"" + name + "\", Location: " + getLocation().toString() + "}";
	}
	
	public abstract void render(Graphics g);
	public abstract void tick();

	public boolean canCollide(){
		return false;
	}

	public int getRenderPriority() {
		return renderPriority;
	}
}
