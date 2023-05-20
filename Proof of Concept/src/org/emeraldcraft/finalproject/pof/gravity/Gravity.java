package org.emeraldcraft.finalproject.pof.gravity;

import org.emeraldcraft.finalproject.pof.gameobjects.Player;

public class Gravity {
	private final Player player;
	private double xVel = 0;
	private double yVel = 0;
	
	private long startTime = 0;
	
	
	private double[] vel = new double[2];
	public Gravity(Player player) {
		this.player = player;
	}
	public void setVel(double x, double y) {	
		yVel = y;
		xVel = x;
		startTime = System.currentTimeMillis();
	}
	public void tickGravity() {
		//calculate vertical distance
		long time = System.currentTimeMillis() - startTime;
		double yDistance = (yVel * time) - (1/2 * 9.8 * Math.pow((time/1000), 2));
		vel[0] = xVel;
		vel[1] = yDistance;
	}
	public double getYVel() {
		return vel[1];
	}
	public double getXVel() {
		return vel[0];
	}

}
