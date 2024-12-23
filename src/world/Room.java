package world;

import character.enemy.Enemy;
import items.Item;
import java.util.*;

/**
 * Represents a single room in the dungeon.
 * Each room can:
 * - Have a type (normal, boss, treasure, etc.)
 * - Connect to other rooms (north/south)
 * - Contain enemies and items
 * - Track if it has been cleared of enemies
 */
public class Room {
    private final RoomType type;
    private final List<Item> items;
    private final List<Enemy> enemies;
    private final Map<Direction, Room> exits;
    private boolean isCleared;

    /**
     * Creates a new room
     * @param type What kind of room this is
     */
    public Room(RoomType type) {
        this.type = type;
        this.items = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.exits = new HashMap<>();
        this.isCleared = false;
    }

    /**
     * Shows room description and contents to player
     */
    public void describeRoom() {
        // Display room name and description
        System.out.println("\n=== " + type.getName() + " ===");
        System.out.println(type.getDescription());

        // List enemies if any are present
        if (hasEnemies()) {
            System.out.println("\nEnemies here:");
            for (Enemy enemy : enemies) {
                System.out.println("- " + enemy.getCharacterName());
            }
        }

        // List items if any are present
        if (hasItems()) {
            System.out.println("\nItems here:");
            for (Item item : items) {
                System.out.println("- " + item.getName());
            }
        }

        // Show available exits
        System.out.println("\nPossible exits:");
        for (Direction dir : exits.keySet()) {
            System.out.println("- " + dir.name().toLowerCase());
        }
    }

    /**
     * Checks if room has any enemies
     * @return true if room contains enemies
     */
    public boolean hasEnemies() {
        return !enemies.isEmpty();
    }

    /**
     * Checks if room has any items
     * @return true if room contains items
     */
    public boolean hasItems() {
        return !items.isEmpty();
    }

    /**
     * Connects this room to another room
     * @param direction Which way the other room is
     * @param room The room to connect to
     */
    public void setExit(Direction direction, Room room) {
        exits.put(direction, room);
    }

    /**
     * Gets the room connected in a direction
     * @param direction Which way to look
     * @return The connected room, or null if no room that way
     */
    public Room getExit(Direction direction) {
        return exits.get(direction);
    }

    /**
     * Checks if there's an exit in a direction
     * @param direction Direction to check
     * @return true if an exit exists that way
     */
    public boolean hasExit(Direction direction) {
        return exits.containsKey(direction);
    }

    /**
     * Adds an item to the room
     * @param item Item to add
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Removes an item from the room by name
     * @param itemName Name of item to remove
     * @return The removed item, or null if not found
     */
    public Item removeItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                items.remove(item);
                return item;
            }
        }
        return null;
    }

    /**
     * Adds an enemy to the room
     * @param enemy Enemy to add
     */
    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    /**
     * Removes an enemy from the room
     * @param enemy Enemy to remove
     */
    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
        if (enemies.isEmpty()) {
            isCleared = true;
        }
    }

    /**
     * Finds an enemy by name
     * @param name Name of enemy to find
     * @return The enemy, or null if not found
     */
    public Enemy findEnemy(String name) {
        for (Enemy enemy : enemies) {
            if (enemy.getCharacterName().equalsIgnoreCase(name)) {
                return enemy;
            }
        }
        return null;
    }

    /**
     * Gets the room's type
     * @return The type of this room
     */
    public RoomType getType() {
        return type;
    }

    /**
     * Checks if room is cleared of enemies
     * @return true if room is cleared
     */
    public boolean isCleared() {
        return isCleared;
    }

    /**
     * Sets the cleared status of the room
     * @param cleared true if room is cleared of enemies, false otherwise
     */
    public void setCleared(boolean cleared) {
        this.isCleared = cleared;
    }

    /**
     * Gets a copy of room's items
     * @return A new list containing the room's items
     */
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * Gets a copy of room's enemies
     * @return A new list containing the room's enemies
     */
    public List<Enemy> getEnemies() {
        return new ArrayList<>(enemies);
    }

    /**
     * Gets available exits
     * @return A new map containing the room's exits
     */
    public Map<Direction, Room> getExits() {
        return new HashMap<>(exits);
    }
}