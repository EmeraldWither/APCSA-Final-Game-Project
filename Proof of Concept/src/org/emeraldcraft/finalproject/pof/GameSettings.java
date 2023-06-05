package org.emeraldcraft.finalproject.pof;

public class GameSettings {
	public static class StaminaSettings{
		public static int FLY_PUNISHMENT = 1;
		public static int JUMPING_PUNISHMENT = 4;
		public static int WALKING_REWARD = 2;
		public static int DIVING_PUNISHMENT = 4;
		public static int EATING_REWARD = 400;
	}
	public static class CoreSettings {
		public static int TICK_TIME = 10;
	}
	public static class HumanSettings {
		public static int HUMAN_MIN_SPAWN_TIME = 1;
		public static int HUMAN_MAX_SPAWN_TIME = 3;
	}
	public static class GravityEngine {
		public static final int FORCE_DEBOUNCE = 150;
		public static final double GRAVITY_CONSTANT = 49;
		public static final double Y_VEL_MODIFIER = 1.2;
		public static final double X_VEL_MODIFIER = 2;
	}
}
