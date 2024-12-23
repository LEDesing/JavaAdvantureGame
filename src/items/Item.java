package items;

import character.player.Player;

/**
 * Things players can find and use in the game.
 * Each item has a type, value, and special use.
 */
public class Item {
    private final String name;
    private final ItemsType type;
    private final int value;
    private final String description;

    /**
     * Makes a new item
     * @param name What the item is called
     * @param type What kind of item it is
     * @param value How strong the item is
     * @param description What the item looks like or does
     */
    public Item(String name, ItemsType type, int value, String description) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.description = description;
    }

    /**
     * Uses the item on a player
     * @param player The player using the item
     */
    public void use(Player player) {
        switch (type) {
            case HEALTH_POTION:
                useHealthPotion(player);
                break;
            case DAMAGE_POTION:
                useDamagePotion(player);
                break;
            case DODGE_POTION:
                useDodgePotion(player);
                break;
            default:
                System.out.println("This item can't be used right now.");
        }
    }

    /**
     * Heals the player
     */
    private void useHealthPotion(Player player) {
        player.heal(value);
        System.out.println("You drink the health potion and feel better!");
        System.out.println("Healed for " + value + " health!");
    }

    /**
     * Makes the player's next attack stronger
     */
    private void useDamagePotion(Player player) {
        player.setDamageMultiplier(1.0 + (value / 100.0));
        System.out.println("Your next attack will be " + value + "% stronger!");
    }

    /**
     * Lets the player dodge the next attack
     */
    private void useDodgePotion(Player player) {
        player.setDodgeNextAttack(true);
        System.out.println("You will dodge the next attack!");
    }

    // Simple getters
    public String getName() {
        return name;
    }

    public ItemsType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}