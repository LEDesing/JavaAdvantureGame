package character.enemy;

import character.ability.Ability;
import character.ability.AbilityType;

/**
 * Different types of enemies in the game
 */
public enum EnemyType {
    // Regular enemies
    SKELETON(
            "Skeleton",
            "An undead warrior wielding rusty weapons",
            new Ability("Bone Crush", "Powerful bone-crushing attack",
                    AbilityType.BERSERK, 3),
            50, 10
    ),

    GOBLIN(
            "Goblin",
            "A small but fierce creature",
            new Ability("Sneaky Stab", "Quick stabbing attack",
                    AbilityType.LIFESTEAL, 3),
            40, 8
    ),

    WITCH(
            "Witch",
            "A corrupted magic user",
            new Ability("Dark Magic", "Powerful dark spell",
                    AbilityType.FIREBALL, 3),
            45, 15
    ),

    // Mini-bosses
    FLAME_WARDEN(
            "Flame Warden",
            "First boss, master of fire magic",
            new Ability("Inferno Blast", "Massive fire damage",
                    AbilityType.FIREBALL, 2),
            150, 20
    ),

    FROST_SENTINEL(
            "Frost Sentinel",
            "Second boss, master of ice magic",
            new Ability("Blizzard", "Freezing ice storm",
                    AbilityType.SHIELD_BASH, 2),
            175, 25
    ),

    SHADOW_LORD(
            "Shadow Lord",
            "Final boss, master of dark magic",
            new Ability("Death Ray", "Ultimate dark magic",
                    AbilityType.LIFESTEAL, 1),
            250, 30
    );

    private final String name;
    private final String description;
    private final Ability ability;
    private final int baseHealth;
    private final int baseDamage;

    /**
     * Makes a new enemy type
     */
    EnemyType(String name, String description, Ability ability,
              int baseHealth, int baseDamage) {
        this.name = name;
        this.description = description;
        this.ability = ability;
        this.baseHealth = baseHealth;
        this.baseDamage = baseDamage;
    }

    /**
     * Shows information about this enemy
     */
    public void displayInfo() {
        System.out.println(name + " - " + description);
        System.out.println("Special: " + ability.getDescription());
    }

    // Simple getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Ability getAbility() {
        return ability;
    }

    public int getBaseHealth() {
        return baseHealth;
    }

    public int getBaseDamage() {
        return baseDamage;
    }
}