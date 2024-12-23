package world;

import java.util.Random;

/**
 * Tracks player's progress through the dungeon.
 * Controls boss spawning based on player depth and previous encounters.
 */
public class DungeonProgress {
    // Depth requirements
    private static final int FIRST_BOSS_MIN_DEPTH = 5;
    private static final int SECOND_BOSS_MIN_DEPTH = 10;
    private static final int FINAL_BOSS_MIN_DEPTH = 15;

    // Spawn chances
    private static final double MINI_BOSS_SPAWN_CHANCE = 0.20;
    private static final double FINAL_BOSS_SPAWN_CHANCE = 0.30;

    private final Random random;
    private int currentDepth;
    private boolean firstBossDefeated;
    private boolean secondBossDefeated;

    public DungeonProgress() {
        this.random = new Random();
        this.currentDepth = 0;
        this.firstBossDefeated = false;
        this.secondBossDefeated = false;
    }

    /**
     * Moves player deeper into dungeon
     * @return New depth after movement
     */
    public int moveDeeper() {
        return ++currentDepth;
    }

    /**
     * Moves player back towards entrance
     * @return New depth after movement
     */
    public int moveBack() {
        if (currentDepth > 0) {
            currentDepth--;
        }
        return currentDepth;
    }

    /**
     * Checks if first mini-boss can spawn here
     */
    public boolean canSpawnFirstBoss() {
        return !firstBossDefeated &&
                currentDepth >= FIRST_BOSS_MIN_DEPTH &&
                random.nextDouble() < MINI_BOSS_SPAWN_CHANCE;
    }

    /**
     * Checks if second mini-boss can spawn here
     */
    public boolean canSpawnSecondBoss() {
        return firstBossDefeated &&
                !secondBossDefeated &&
                currentDepth >= SECOND_BOSS_MIN_DEPTH &&
                random.nextDouble() < MINI_BOSS_SPAWN_CHANCE;
    }

    /**
     * Checks if final boss can spawn here
     */
    public boolean canSpawnFinalBoss() {
        return firstBossDefeated &&
                secondBossDefeated &&
                currentDepth >= FINAL_BOSS_MIN_DEPTH &&
                random.nextDouble() < FINAL_BOSS_SPAWN_CHANCE;
    }

    /**
     * Records defeat of first boss
     */
    public void defeatFirstBoss() {
        firstBossDefeated = true;
    }

    /**
     * Records defeat of second boss
     */
    public void defeatSecondBoss() {
        secondBossDefeated = true;
    }

    public int getCurrentDepth() {
        return currentDepth;
    }

    public boolean isFirstBossDefeated() {
        return firstBossDefeated;
    }

    public boolean isSecondBossDefeated() {
        return secondBossDefeated;
    }
}