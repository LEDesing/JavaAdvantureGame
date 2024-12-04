package world;

import java.util.Random;

public enum RoomDescription {
    LAB("Glass breaks under your feet. Strange tools and bottles are on the shelves."),
    LIBRARY("Big bookshelves are all around you. Most books are old and falling apart."),
    ARMORY("Old weapons hang on the walls, most are rusty. Training dolls stand in the corner."),
    PRISON("Empty cells with open doors. Old chains hang on the walls.");

    private final String description;
    private static final Random random = new Random();

    RoomDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static String getRandomRoomDescription() {
        RoomDescription[] descriptions = RoomDescription.values();
        return descriptions[random.nextInt(descriptions.length)].getDescription();
    }
}

