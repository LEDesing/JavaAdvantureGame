/**
 * Base class for all characters in the game, this is for enemies and players.
*/

public abstract class GameCharacter {
    //Constant for default values
    private static final int DEFAULT_MAX_HEALTH = 100;
    private static final int DEFAULT_BASE_DAMAGE = 10;

    //Core traits of characters
    private final String name;
    private int maxHealth;
    private int currentHealth;
    private int baseDamage;

    /**
     * Creates a new character with specific trait
     * @param name Character's name
     * @param maxHealth Maximum health points
     * @param baseDamage Base damage dealt
     * @throws IllegalArgumentException if health or damage are negative since that doesn't make sense
     */

    private GameCharacter (String name, int maxHealth, int baseDamage ) {
        if(maxHealth <= 0){
            throw new IllegalArgumentException("maxHealth must be greater than 0");
        }

        if(baseDamage <= 0){
            throw new IllegalArgumentException("baseDamage must be greater than 0");
        }

        this.name = name;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth; //we start the current health with full health.
        this.baseDamage = baseDamage;
    }
}
