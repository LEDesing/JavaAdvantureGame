package character.enemy;

import java.util.Random;
import character.Character;
import character.player.Player;
import character.ability.Ability;

/**
 * Represents an enemy character in the game.
 * Enemies can be regular monsters or bosses with special abilities.
 */
public class Enemy extends Character {
    private static final double ABILITY_USE_CHANCE = 0.3;  // 30% chance to use ability

    private final Random random;
    private final EnemyType type;
    private final boolean isBoss;

    /**
     * Creates a new enemy
     * @param name Enemy's name
     * @param type Type of enemy (determines stats and abilities)
     * @param isBoss Whether this is a boss enemy
     */
    public Enemy(String name, EnemyType type, boolean isBoss) {
        super(name, type.getBaseHealth(), type.getBaseDamage());
        this.random = new Random();
        this.type = type;
        this.isBoss = isBoss;
        setSpecialAbility(type.getAbility());

        // Bosses get bonus stats
        if (isBoss) {
            updateMaxHealthPoints(getMaxHealthPoints() * 2);
            updateAttackDamage(getAttackDamage() * 2);
        }
    }

    /**
     * Decides what action to take during combat
     * @param player The player being fought
     */
    public void takeTurn(Player player) {
        if (!isAlive()) {
            return;
        }

        // Try to use ability (higher chance for bosses)
        double abilityChance = isBoss ? ABILITY_USE_CHANCE * 1.5 : ABILITY_USE_CHANCE;
        if (getSpecialAbility() != null && random.nextDouble() < abilityChance) {
            activateSpecialAbility(player);
            return;
        }

        // Default to normal attack
        performAttack(player);
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
}