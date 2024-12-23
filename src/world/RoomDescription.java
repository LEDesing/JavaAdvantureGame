package world;

import java.util.Random;

/**
 * Contains descriptions for normal rooms.
 * Provides random selection of room descriptions.
 */
public enum RoomDescription {
    LAB("You see broken glass on the floor. There are tools and bottles on the shelves."),
    LIBRARY("You see big bookshelves everywhere. The books look old and broken."),
    ARMORY("You see old weapons on the walls. They are rusty. There are training dolls in the corner."),
    PRISON("You see empty cells with open doors. Old chains hang on the walls."),
    DINING("You see long tables with old plates and cups. Chairs are knocked over."),
    KITCHEN("You see a big fireplace and cooking pots. There are knives and tools on the tables."),
    BEDROOM("You see old beds with torn sheets. Broken mirrors hang on the walls."),
    GARDEN("You see dead plants and dry fountains. Stone paths lead through the weeds.");

    private final String description;
    private static final Random random = new Random();

    /**
     * Creates a new room description
     * @param description Text shown to player
     */
    RoomDescription(String description) {
        this.description = description;
    }

    /**
     * Gets this description's text
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets a random room description
     * Used for normal room types
     */
    public static String getRandomDescription() {
        RoomDescription[] descriptions = values();
        int index = random.nextInt(descriptions.length);
        return descriptions[index].getDescription();
    }
}