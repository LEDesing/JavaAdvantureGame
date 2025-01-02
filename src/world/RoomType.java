// RoomType.java
package world;

/**
 * A list of all the different room types in the game.
 * Each room has a name and a description.
 */
public enum RoomType {
    HOME("Home",
            "This is your safe place. A warm fire burns in the corner. " +
                    "You can rest here to heal."),

    NORMAL("Room", null),  // Description comes from RoomDescription

    ENTRANCE("Dungeon Entrance",
            "A dark stone archway marks the entrance to the dungeon.\n" +
                    "Your home lies to the SOUTH, but adventure waits ahead..."),

    TREASURE("Treasure Room",
            "Your eyes light up! Gold coins and gems sparkle everywhere.\n" +
                    "This room holds many valuable items."),

    MINI_BOSS("Strong Enemy Room",
            "The air feels heavy. A powerful enemy guards this room.\n" +
                    "Be ready for a tough fight!"),

    BOSS("Boss Room",
            "A huge dark chamber stretches before you.\n" +
                    "The most dangerous enemy awaits in the shadows.");

    private final String name;
    private final String description;

    /**
     * Makes a new room type.
     * @param name The name shown for the room
     * @param description What the room looks like (can be null for normal rooms)
     */
    RoomType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Gets the room's description.
     * @return The room's description (random for normal rooms)
     */
    public String getDescription() {
        if (this == NORMAL) {
            return RoomDescription.getRandomDescription();
        }
        return description;
    }

    /**
     * Gets the room's name.
     * @return The name of the room
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if this is a special room.
     * @return true if it's special, false if it's normal
     */
    public boolean isSpecialRoom() {
        return this != NORMAL;
    }

    /**
     * Checks if enemies can appear in this room.
     * @return true if enemies can appear, false if they can't
     */
    public boolean canHaveEnemies() {
        return this != HOME && this != ENTRANCE;
    }

    /**
     * Checks if items can appear in this room.
     * @return true if items can appear, false if they can't
     */
    public boolean canHaveItems() {
        return this != ENTRANCE;
    }
}