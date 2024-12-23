package character.player;

import character.ability.Ability;
import character.ability.AbilityType;

/**
 * The different heroes players can choose to be
 */
public enum HeroClass {
    ROGUE("Rogue",
            "Quick and sneaky fighter",
            new Ability("Invisibility", "Turn invisible to avoid attacks",
                    AbilityType.INVISIBILITY, 3),
            80, 15),

    WIZARD("Wizard",
            "Master of fire magic",
            new Ability("Fireball", "Cast powerful fireball for massive damage",
                    AbilityType.FIREBALL, 3),
            70, 20),

    PALADIN("Paladin",
            "Holy warrior of light",
            new Ability("Holy Light", "Channel divine energy to heal wounds",
                    AbilityType.HEAL, 4),
            100, 12),

    WARRIOR("Warrior",
            "Master of combat",
            new Ability("Berserk", "Enter rage mode for massive damage",
                    AbilityType.BERSERK, 4),
            90, 18),

    KNIGHT("Knight",
            "Armored defender",
            new Ability("Shield Bash", "Bash enemies with your shield",
                    AbilityType.SHIELD_BASH, 3),
            110, 14),

    TRICKSTER("Trickster",
            "Master of deception",
            new Ability("Clone", "Create a confusing clone",
                    AbilityType.CLONE, 3),
            75, 16),

    NECROMANCER("Necromancer",
            "Dark magic user",
            new Ability("Life Drain", "Steal life from enemies",
                    AbilityType.LIFESTEAL, 3),
            85, 17);

    private final String name;
    private final String description;
    private final Ability ability;
    private final int baseHealth;
    private final int baseDamage;

    /**
     * Makes a new hero class
     */
    HeroClass(String name, String description, Ability ability,
              int baseHealth, int baseDamage) {
        this.name = name;
        this.description = description;
        this.ability = ability;
        this.baseHealth = baseHealth;
        this.baseDamage = baseDamage;
    }

    /**
     * Shows information about this class
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