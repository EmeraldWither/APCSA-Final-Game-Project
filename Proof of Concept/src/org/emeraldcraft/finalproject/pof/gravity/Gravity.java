package org.emeraldcraft.finalproject.pof.gravity;

import org.emeraldcraft.finalproject.pof.gameobjects.Player;
import org.emeraldcraft.finalproject.pof.utils.Logger;

public class Gravity {
	private final Player player;
	private double xVel = 0;
	private double yVel = 0;
	
	private long startTime = 0;
	
	
	private final double[] vel = new double[2];
	public Gravity(Player player) {
		startTime = System.currentTimeMillis();
		this.player = player;
	}
	public void setVel(double x, double y) {	
		yVel = y;
		xVel = x;
		startTime = System.currentTimeMillis();
	}
	public void tickGravity() {
		//calculate vertical distance
		double time = (double) (System.currentTimeMillis() - startTime) / 1000.0;
		Logger.log("Time: " + time);
		double yDistance = (yVel*time)-(0.5*(9.81*time*time));
		if(yDistance < 0) yDistance = 0;

				//(yVel * time) - (1.0/2 * 9.8 * Math.pow((time/1000.0), 2));
		Logger.log("YVel * time: " + (yVel*time));
		Logger.log("Gravity: " + (9.81*time*time));
		Logger.log("Half gravity " + (0.5*(9.81*time*time)));
		Logger.log("Subtract it all " + ((yVel*time)-(0.5*(9.81*time*time))));
		Logger.log("\n");
		Logger.log("ydistance: " + yDistance);
		vel[0] = xVel;
		vel[1] = (900 - (yDistance * 50));
		Logger.log("corrected ydistance: " + vel[1]);
	}
	public double getXVel() {
		return vel[0];
	}
	public double getYVel() {
		return vel[1];
	}

}
