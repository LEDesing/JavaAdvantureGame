package character;

/**
 * Represents the available special classes for both enemies and boss.
 * Each class has unique stats and abilities.
 */
public enum CharacterClass {
    ROGUE("Rogue", "Quick and sneaky fighter who can turn invisible", 80, 15),
    WIZARD("Wizard", "Magic user who can throw fireballs", 70, 20),
    PALADIN("Paladin", "Holy knight who can heal wounds", 120, 12),
    KNIGHT("Knight", "Strong defender who can bash with shield", 100, 14),
    WARRIOR("Warrior", "Brave fighter who gets stronger when angry", 90, 16),
    TRICKSTER("Trickster", "Clever fighter who can make fake copies", 75, 17),
    NECROMANCER("Necromancer", "Dark wizard who can steal life force", 85, 18);

    private final String name;
    private final String description;
    private final int baseHealth;
    private final int baseDamage;

    /**
     * Creates a new character class with specified attributes
     * @param name The display name of the class
     * @param description Brief description of the class
     * @param baseHealth Starting health points
     * @param baseDamage Base damage output
     */
    CharacterClass(String name, String description, int baseHealth, int baseDamage) {
        this.name = name;
        this.description = description;
        this.baseHealth = baseHealth;
        this.baseDamage = baseDamage;
    }

    // Getters

    /**
     * @return The class name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Brief description of the class
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Starting health value
     */
    public int getBaseHealth() {
        return baseHealth;
    }

    /**
     * @return Base damage value
     */
    public int getBaseDamage() {
        return baseDamage;
    }


    /**
     * Converts a string input to a CharacterClass
     * @param input The string to convert
     * @return The matching CharacterClass, or null
     * Example would be: so like if we said Ninja, it would return null
     */

    public static CharacterClass fromString(String input) {
        if (input == null || input.isEmpty()) return null;
        try {
            return valueOf(input.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}