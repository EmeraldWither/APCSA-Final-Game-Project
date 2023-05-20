package org.emeraldcraft.finalproject.pof.gravity;

import org.emeraldcraft.finalproject.pof.gameobjects.Player;
import org.emeraldcraft.finalproject.pof.utils.Logger;

public class Gravity {
	private final Player player;
	private double xVel = 0;
	private double yVel = 0;
	
	private long startTime = 0;
	
	
	private final double[] pos = new double[2];
	private final double[] prePos = new double[2];
	public Gravity(Player player) {
		startTime = System.currentTimeMillis();
		this.player = player;
	}
	public void setVel(double x, double y) {	
		yVel = y * 1.2;
		xVel = x;
		pos[0] = -1;
		pos[1] = -1;
		startTime = System.currentTimeMillis();
	}
	public void tickGravity() {
		//calculate vertical distance
		double time = (double) (System.currentTimeMillis() - startTime) / 1000.0;
		Logger.log("Time: " + time);
		double yDistance = (yVel*time)-(0.5*((9.81 * 5)*time*time));
//		if(yDistance < 0) yDistance = 0;

		if(pos[0] == -1 || pos[1] == -1){
			prePos[0] = xVel;
			prePos[1] = yDistance;

		}

		//(yVel * time) - (1.0/2 * 9.8 * Math.pow((time/1000.0), 2));
		Logger.log("ydistance: " + yDistance);

		pos[0] = xVel;
		Logger.log("corrected ydistance: " + (yDistance - prePos[1]));
//		pos[1] = (900 - (yDistance * 55));
		pos[1] = -(yDistance - prePos[1]) * 50;
		prePos[0] = xVel;
		prePos[1] = yDistance;
	}
	public double getXVel() {
		return pos[0];
	}
	public double getYVel() {
		return pos[1];
	}

}
