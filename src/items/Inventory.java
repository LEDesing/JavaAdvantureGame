package items;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds and manages a player's items.
 * Can only hold up to 10 items at once.
 */
public class Inventory {
    private static final int MAX_CAPACITY = 10;
    private final List<Item> items;

    /**
     * Makes a new empty inventory
     */
    public Inventory() {
        this.items = new ArrayList<>();
    }

    /**
     * Tries to put an item in the inventory
     * @param item The item to add
     * @return true if added, false if inventory is full
     */
    public boolean addItem(Item item) {
        if (items.size() >= MAX_CAPACITY) {
            return false;
        }
        return items.add(item);
    }

    /**
     * Takes an item out of the inventory
     * @param itemName The name of the item to remove
     * @return The item that was removed, or null if not found
     */
    public Item removeItem(String itemName) {
        for (int i = 0; i < items.size(); i++) {
            Item currentItem = items.get(i);
            String currentItemName = currentItem.getName();

            if (currentItemName.equalsIgnoreCase(itemName)) {
                return items.remove(i);
            }
        }
        return null;
    }

    /**
     * Looks for an item in the inventory
     * @param itemName The name of the item to find
     * @return true if the item is found
     */
    public boolean hasItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if inventory has any items
     * @return true if inventory contains items
     */
    public boolean hasItems() {
        return !items.isEmpty();
    }

    /**
     * Shows what items are in the inventory
     */
    public void showContents() {
        if (items.isEmpty()) {
            System.out.println("Your inventory is empty.");
            return;
        }

        int currentSize = items.size();
        System.out.println("\nInventory (" + currentSize + "/" + MAX_CAPACITY + " items):");

        for (Item item : items) {
            String itemName = item.getName();
            System.out.println("- " + itemName);
        }
    }

    /**
     * Makes a copy of all items in inventory
     * @return A new list with all items
     */
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * Checks if inventory can hold more items
     * @return true if inventory is full
     */
    public boolean isFull() {
        int currentSize = items.size();
        return currentSize >= MAX_CAPACITY;
    }
}