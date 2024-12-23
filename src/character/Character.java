package character;

import character.ability.Ability;

/**
 * Base class for all characters in the game (heroes and enemies).
 * Handles basic stats, combat actions, and abilities.
 */
public abstract class Character {
    private final String name;
    private int currentHealth;
    private int maxHealthPoints;
    private int attackDamage;
    private Ability specialAbility;

    /**
     * Creates a new character
     * @param name Character's display name
     * @param maxHealth Starting/maximum health points
     * @param baseDamage Base attack damage
     */
    public Character(String name, int maxHealth, int baseDamage) {
        this.name = name;
        this.maxHealthPoints = maxHealth;
        this.currentHealth = maxHealth;
        this.attackDamage = baseDamage;
    }

    /**
     * Performs a basic attack against another character
     * @param target Character to attack
     */
    public void performAttack(Character target) {
        if (target != null && isAlive()) {
            target.receiveAttackDamage(attackDamage);
            System.out.printf("%s attacks %s for %d damage!%n",
                    name, target.getCharacterName(), attackDamage);
        }
    }

    /**
     * Receives and processes incoming attack damage
     * @param incomingDamage Amount of damage to take
     */
    public void receiveAttackDamage(int incomingDamage) {
        if (incomingDamage > 0) {
            currentHealth = Math.max(0, currentHealth - incomingDamage);
            System.out.printf("%s takes %d damage! (%d/%d HP)%n",
                    name, incomingDamage, currentHealth, maxHealthPoints);

            if (!isAlive()) {
                System.out.printf("%s has been defeated!%n", name);
            }
        }
    }

    /**
     * Activates character's special ability
     * @param target Target of the ability (can be null for self-buffs)
     */
    public void activateSpecialAbility(Character target) {
        if (specialAbility != null && isAlive()) {
            specialAbility.execute(this, target);
        }
    }

    /**
     * Restores health points to the character
     * @param healAmount Amount of health to restore
     */
    public void restoreHealth(int healAmount) {
        if (healAmount > 0) {
            int previousHealth = currentHealth;
            currentHealth = Math.min(maxHealthPoints, currentHealth + healAmount);
            int actualHealAmount = currentHealth - previousHealth;

            if (actualHealAmount > 0) {
                System.out.printf("%s recovers %d health! (%d/%d HP)%n",
                        name, actualHealAmount, currentHealth, maxHealthPoints);
            }
        }
    }

    /**
     * Checks if character is still alive
     * @return true if current health > 0
     */
    public boolean isAlive() {
        return currentHealth > 0;
    }

    /**
     * Assigns a new special ability to the character
     * @param ability New ability to use
     */
    public void setSpecialAbility(Ability ability) {
        this.specialAbility = ability;
    }

    // Getters with clear, descriptive names
    public String getCharacterName() {
        return name;
    }

    public int getCurrentHealthPoints() {
        return currentHealth;
    }

    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public Ability getSpecialAbility() {
        return specialAbility;
    }

    // Protected setters for subclasses with clear names
    protected void updateMaxHealthPoints(int newMaxHealth) {
        this.maxHealthPoints = newMaxHealth;
        this.currentHealth = Math.min(currentHealth, maxHealthPoints);
    }

    protected void updateAttackDamage(int newAttackDamage) {
        this.attackDamage = newAttackDamage;
    }
}