package org.emeraldcraft.finalproject.pof.gravity;

import static org.emeraldcraft.finalproject.pof.GameSettings.GravityEngine.*;

public class Gravity {
	private double xVel = 0;
	private double yVel = 0;
	
	private long startTime = System.currentTimeMillis();
	
	
	private final double[] pos = new double[2];
	private final double[] prePos = new double[2];

	private boolean enabled = true;

	public void setVel(double x, double y) {
		yVel = y * Y_VEL_MODIFIER;
		xVel = x * X_VEL_MODIFIER;
		pos[0] = -1;
		pos[1] = -1;
		startTime = System.currentTimeMillis();
	}
	public void setVelX(double x){
		xVel = x * X_VEL_MODIFIER;
		pos[0] = xVel;
	}
	public void setVelY(double y){
		yVel = y * Y_VEL_MODIFIER;
		pos[1] = -1;
		startTime = System.currentTimeMillis();
	}
	public void setGravityEnabled(boolean enabled){
		this.enabled = enabled;
		startTime = System.currentTimeMillis();
	}
	public boolean isGravityDisabled() {
		return !enabled;
	}

	public void tickGravity() {
		if(!enabled){
			pos[0] = 0;
			pos[1] = 0;
			return;
		}
		double time = (double) (System.currentTimeMillis() - startTime) / 1000.0;
		double yDistance = (yVel*time)-(0.5*((GRAVITY_CONSTANT)*time*time));
		if(pos[0] == -1 || pos[1] == -1){
			prePos[0] = xVel;
			prePos[1] = yDistance;

		}
		pos[0] = xVel;
		pos[1] = -(yDistance - prePos[1]) * 50;
		prePos[1] = yDistance;
	}
	public double getXVel() {
		return pos[0];
	}
	public double getYVel() {
		return pos[1];
	}

}
