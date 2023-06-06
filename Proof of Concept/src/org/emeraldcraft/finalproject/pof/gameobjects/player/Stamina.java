package org.emeraldcraft.finalproject.pof.gameobjects.player;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.emeraldcraft.finalproject.pof.components.GameObject;

public class Stamina extends GameObject {

	private Player player;
	private double stamina = 1500;
	public Stamina(Player player) {
		super("Stamina Bar", new Rectangle(1920-450, 40, 400, 50), 1);
		this.player = player;
	}

	@Override
	public boolean shouldRemove() {
		return false;
	}

	@Override
	public void render(Graphics g) {
        ((Graphics2D) g).setStroke(new BasicStroke(5));
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Stamina", getLocation().x, getLocation().y - 15);
		g.drawRect(getLocation().x, getLocation().y, getLocation().width, getLocation().height);
		g.setColor(Color.white);
		g.fillRect(getLocation().x + 1, getLocation().y + 1, getLocation().width - 1, getLocation().height - 1);
		g.setColor(Color.orange);
		g.fillRect(getLocation().x + 1, getLocation().y + 1, (int) (stamina/3.75), getLocation().height - 1);
	}

	@Override
	public void tick() {
		
	}

	public double getStamina() {
		return stamina;
	}

	public void setStamina(double stamina) {
		this.stamina = stamina;
	}
	public void increase(double amount) {
		if(stamina + amount > 1500) this.stamina = 1500;
		else this.stamina += amount;
	}
	public void decrease(double amount) {
		if(stamina - amount < 0) this.stamina = 0;
		else this.stamina -= amount;
	}

}
