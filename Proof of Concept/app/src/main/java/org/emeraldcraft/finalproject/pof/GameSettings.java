/*
 * Ishaan Sayal && Gavin McClure
 * Period 2
 * 6/7/2023
 */

package org.emeraldcraft.finalproject.pof;

public class GameSettings
{
    public static class StaminaSettings
    {
        public static final double FLY_PUNISHMENT = 0.5;
        public static final double JUMPING_PUNISHMENT = 15;
        public static final double WALKING_REWARD = 0.2;
        public static final double DIVING_PUNISHMENT = 2;
        public static final double EATING_REWARD = 350;
    }

    public static class CoreSettings
    {
        public static final int TICK_TIME = 9;

    }

    public static class HumanSettings
    {
        public static final double HUMAN_MIN_SPAWN_TIME = 1.5;
        public static final double HUMAN_MAX_SPAWN_TIME = 3.5;
    }

    public static class GravityEngine
    {
        public static final int FORCE_DEBOUNCE = 150;
        public static final double GRAVITY_CONSTANT = 49;
        public static final double Y_VEL_MODIFIER = 1.2;
        public static final double X_VEL_MODIFIER = 2;
    }
}
