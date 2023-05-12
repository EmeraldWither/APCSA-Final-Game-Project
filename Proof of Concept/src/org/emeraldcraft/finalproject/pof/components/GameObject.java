package org.emeraldcraft.finalproject.pof.components;

import org.emeraldcraft.finalproject.pof.SegalGame;
import org.emeraldcraft.finalproject.pof.utils.Logger;

import java.awt.*;

public abstract class GameObject {
	private final String name;
	private final Rectangle hitbox;
	private final int renderPriority;
	private boolean isRemoved = false;
	
	public GameObject(String name, Rectangle hitbox, int renderPriority) {
		this.name = name;
		this.hitbox = hitbox;
		this.renderPriority = renderPriority;
		SegalGame.getInstance().registerGameObject(this);
		Logger.log("Created a new GameObject of: " + this);
	}
	public Rectangle getHitbox() {
		return this.hitbox;
	}
	public boolean isRemoved() {
		return isRemoved;
	}
	public void remove() {
		Logger.log(this + " has been deregistered and has been removed.");
		SegalGame.getInstance().deRegisterGameObject(this);
		isRemoved = true;
	}
	public String getName() {
		return this.name;
	}
	@Override
	public String toString() {
		return "GameObject { name: \"" + name + ", Hitbox: " + hitbox.toString() + ", IsRemoved: " + isRemoved + "}";
	}
	
	public abstract void render(Graphics g);

	public int getRenderPriority() {
		return renderPriority;
	}
}
