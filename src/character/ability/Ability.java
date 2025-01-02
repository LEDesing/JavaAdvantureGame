package character.ability;

import character.Character;

public class Ability {
    private final String name;
    private final String description;
    private final AbilityType type;
    private final int cooldown;
    private int currentCooldown;


    public Ability(String name, String description, AbilityType type, int cooldown) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.cooldown = cooldown;
        this.currentCooldown = 0;
    }

    public void execute(Character user, Character target) {
        if (currentCooldown > 0) {
            System.out.println("Ability on cooldown: " + currentCooldown + " turns remaining");
            return;
        }

        type.execute(user, target);
    }

    public void startCooldown() {
        currentCooldown = cooldown;
    }

    public void reduceCooldown() {
        if (currentCooldown > 0) {
            currentCooldown--;
        }
    }

    public AbilityType getType() {
        return type;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getCooldown() { return cooldown; }
    public int getCurrentCooldown() { return currentCooldown; }
}