package org.emeraldcraft.finalproject.pof;

public class GameSettings {
	public static class StaminaSettings{
		public static double FLY_PUNISHMENT = 1;
		public static double JUMPING_PUNISHMENT = 5;
		public static double WALKING_REWARD = 1;
		public static double DIVING_PUNISHMENT = 2;
		public static double EATING_REWARD = 350;
	}
	public static class CoreSettings {
		public static int TICK_TIME = 9;
	}
	public static class HumanSettings {
		public static int HUMAN_MIN_SPAWN_TIME = 2;
		public static int HUMAN_MAX_SPAWN_TIME = 6;
	}
	public static class GravityEngine {
		public static final int FORCE_DEBOUNCE = 150;
		public static final double GRAVITY_CONSTANT = 49;
		public static final double Y_VEL_MODIFIER = 1.2;
		public static final double X_VEL_MODIFIER = 2;
	}
}
