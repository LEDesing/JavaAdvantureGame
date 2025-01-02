package character.enemy;

import java.util.Random;
import character.Character;
import character.player.Player;
import character.ability.Ability;
import world.Room;

/**
 * Represents an enemy character in the game.
 * Enemies can be regular monsters or bosses with special abilities.
 */
public class Enemy extends Character {
    private static final double ABILITY_USE_CHANCE = 0.3;  // 30% chance to use ability
    private final Random random;
    private final EnemyType type;
    private final boolean isBoss;
    private final Ability ability;
    private Room currentRoom;


     /**
     * Creates a new enemy
     * @param name Enemy's name
     * @param type Type of enemy
     * @param isBoss Whether this is a boss enemy
     */
    public Enemy(String name, EnemyType type, boolean isBoss) {
        super(name, type.getBaseHealth(), type.getBaseDamage());
        this.type = type;
        this.random = new Random();
        this.isBoss = isBoss;
        this.ability = type.getAbility();
        this.currentRoom = null;  // Initialize room as null
    }



    /**
     * Sets the room this enemy is currently in
     * @param room The room containing this enemy
     */
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    /**
     * Gets the room this enemy is currently in
     * @return The current room, or null if not in a room
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }


    /**
     * Takes a turn in combat
     * @param player The player being fought
     */
    public void takeTurn(Player player) {
        if (!isAlive() || player.isInvisible()) {
            return;
        }

        // Only one action per turn - either ability or normal attack
        if (ability != null && random.nextDouble() < ABILITY_USE_CHANCE) {
            ability.execute(this, player);
        } else {
            performAttack(player);
        }
    }

    /**
     * Gets a description of this enemy
     * @return String describing the enemy
     */
    public String getDescription() {
        String bossPrefix = isBoss ? "Boss: " : "";
        return String.format("%s%s (%d/%d HP) - %s",
                bossPrefix, getCharacterName(), getCurrentHealthPoints(),
                getMaxHealthPoints(), type.getDescription());
    }

    // Getters
    public EnemyType getType() { return type; }
    public boolean isBoss() { return isBoss; }
    public Ability getSpecialAbility() { return ability; }
}