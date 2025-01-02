package world;

import character.enemy.EnemyType;

/**
 * Controls the player's journey through the dungeon.
 * The dungeon has a fixed structure:
 * 1. Home -> Entrance -> Normal Room
 * 2. First Boss (Flame Warden)
 * 3. Two rooms (Normal/Treasure)
 * 4. Second Boss (Frost Sentinel)
 * 5. Two rooms (Normal/Treasure)
 * 6. Final Boss (Shadow Lord)
 */
public class DungeonProgress {
    // Room sequence constants
    private static final int ROOMS_BEFORE_FIRST_BOSS = 1;
    private static final int ROOMS_BETWEEN_BOSSES = 2;

    // Boss depths (fixed positions)
    private static final int FIRST_BOSS_DEPTH = 2;   // After entrance + 1 room
    private static final int SECOND_BOSS_DEPTH = 5;  // After 2 more rooms
    private static final int FINAL_BOSS_DEPTH = 8;   // After 2 final rooms

    private int currentDepth;
    private int roomsSinceLastBoss;
    private boolean firstBossDefeated;
    private boolean secondBossDefeated;
    private boolean finalBossDefeated;

    /**
     * Creates a new dungeon progress tracker
     */
    public DungeonProgress() {
        this.currentDepth = 0;
        this.roomsSinceLastBoss = 0;
        this.firstBossDefeated = false;
        this.secondBossDefeated = false;
        this.finalBossDefeated = false;
    }

    /**
     * Moves player one room deeper into the dungeon
     * @return The new depth
     */
    public int moveDeeper() {
        currentDepth++;
        roomsSinceLastBoss++;
        return currentDepth;
    }

    /**
     * Checks if the next room should be a boss room
     * @return true if a boss should appear
     */
    public boolean shouldSpawnBoss() {
        if (!firstBossDefeated && currentDepth == FIRST_BOSS_DEPTH) {
            return true;
        }
        if (firstBossDefeated && !secondBossDefeated &&
                currentDepth == SECOND_BOSS_DEPTH) {
            return true;
        }
        if (secondBossDefeated && !finalBossDefeated &&
                currentDepth == FINAL_BOSS_DEPTH) {
            return true;
        }
        return false;
    }

    /**
     * Gets which boss should appear next
     * @return The type of boss to spawn, or null if no boss should spawn
     */
    public EnemyType getCurrentBossType() {
        if (!firstBossDefeated && currentDepth == FIRST_BOSS_DEPTH) {
            return EnemyType.FLAME_WARDEN;
        }
        if (firstBossDefeated && !secondBossDefeated &&
                currentDepth == SECOND_BOSS_DEPTH) {
            return EnemyType.FROST_SENTINEL;
        }
        if (secondBossDefeated && !finalBossDefeated &&
                currentDepth == FINAL_BOSS_DEPTH) {
            return EnemyType.SHADOW_LORD;
        }
        return null;
    }

    /**
     * Records that a boss was defeated
     * @param bossType The type of boss that was defeated
     */
    public void recordBossDefeat(EnemyType bossType) {
        switch (bossType) {
            case FLAME_WARDEN:
                firstBossDefeated = true;
                break;
            case FROST_SENTINEL:
                secondBossDefeated = true;
                break;
            case SHADOW_LORD:
                finalBossDefeated = true;
                break;
            default:
                throw new IllegalArgumentException("Not a boss type: " + bossType);
        }
        roomsSinceLastBoss = 0;
    }

    /**
     * Checks if more rooms can be generated
     * @return true if dungeon continues, false if at the end
     */
    public boolean canGenerateNextRoom() {
        return currentDepth < FINAL_BOSS_DEPTH ||
                (currentDepth == FINAL_BOSS_DEPTH && !finalBossDefeated);
    }

    /**
     * Gets the current section of the dungeon
     * @return Description of current area
     */
    public String getCurrentSection() {
        if (!firstBossDefeated) {
            return "Fire Section";
        } else if (!secondBossDefeated) {
            return "Ice Section";
        } else {
            return "Shadow Section";
        }
    }

    // Getters
    public int getCurrentDepth() { return currentDepth; }
    public boolean isFirstBossDefeated() { return firstBossDefeated; }
    public boolean isSecondBossDefeated() { return secondBossDefeated; }
    public boolean isFinalBossDefeated() { return finalBossDefeated; }
    public int getRoomsSinceLastBoss() { return roomsSinceLastBoss; }
}