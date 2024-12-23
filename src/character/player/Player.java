package character.player;

import character.Character;
import items.Inventory;
import items.Item;

/**
 * The character that the player controls in the game.
 * Can fight, use items, and has special powers based on their class.
 */
public class Player extends Character {
    private final Inventory inventory;
    private final HeroClass heroClass;
    private boolean canDodgeNextAttack;
    private double extraDamage;

    /**
     * Makes a new player
     * @param name The player's name
     * @param heroClass What kind of hero they want to be
     */
    public Player(String name, HeroClass heroClass) {
        super(name, heroClass.getBaseHealth(), heroClass.getBaseDamage());
        this.heroClass = heroClass;
        this.inventory = new Inventory();
        this.canDodgeNextAttack = false;
        this.extraDamage = 1.0;
        setSpecialAbility(heroClass.getAbility());
    }

    /**
     * Takes damage from an attack, might dodge it
     * @param damage How much damage is coming
     */
    @Override
    public void receiveAttackDamage(int damage) {
        if (canDodgeNextAttack) {
            System.out.println("You dodge the attack!");
            canDodgeNextAttack = false;
            return;
        }
        super.receiveAttackDamage(damage);
    }

    /**
     * Figures out how much damage to deal
     * @return The final damage number
     */
    @Override
    public int getAttackDamage() {
        int baseDamage = super.getAttackDamage();
        int finalDamage = (int)(baseDamage * extraDamage);
        extraDamage = 1.0;  // Reset after use
        return finalDamage;
    }

    /**
     * Gets better (heals health)
     * @param amount How much health to get back
     */
    public void heal(int amount) {
        restoreHealth(amount);
    }

    /**
     * Tries to add an item to the bag
     * @param item The item to add
     * @return true if added, false if bag is full
     */
    public boolean addToInventory(Item item) {
        return inventory.addItem(item);
    }

    /**
     * Takes an item out of the bag
     * @param itemName Name of item to remove
     * @return The item, or null if not found
     */
    public Item removeFromInventory(String itemName) {
        return inventory.removeItem(itemName);
    }

    /**
     * Tries to use an item from the bag
     * @param itemName Name of item to use
     * @return true if used, false if not found
     */
    public boolean useItem(String itemName) {
        if (!inventory.hasItem(itemName)) {
            return false;
        }

        Item item = inventory.removeItem(itemName);
        if (item != null) {
            item.use(this);
            return true;
        }
        return false;
    }

    // Simple getters and setters
    public Inventory getInventory() {
        return inventory;
    }

    public void setDodgeNextAttack(boolean canDodge) {
        this.canDodgeNextAttack = canDodge;
    }

    public HeroClass getHeroClass() {
        return heroClass;
    }

    public void setDamageMultiplier(double multiplier) {
        this.extraDamage = multiplier;
    }
}