package world;

/**
 * Defines the different types of rooms in the game.
 * Each room type has its own name and description.
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
     * Creates a new room type
     * @param name Display name of the room
     * @param description Room description (null for random description)
     */
    RoomType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Gets room description
     * @return Fixed description or random one for normal rooms
     */
    public String getDescription() {
        if (this == NORMAL) {
            return RoomDescription.getRandomDescription();
        }
        return description;
    }

    /**
     * Gets room display name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if this is a special room type
     */
    public boolean isSpecialRoom() {
        return this != NORMAL;
    }

    /**
     * Checks if enemies can spawn here
     */
    public boolean canHaveEnemies() {
        return this != HOME && this != ENTRANCE;
    }

    /**
     * Checks if items can spawn here
     */
    public boolean canHaveItems() {
        return this != ENTRANCE;
    }
}