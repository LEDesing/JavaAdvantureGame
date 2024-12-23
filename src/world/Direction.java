package world;

/**
 * Shows which ways you can move in the game.
 * Helps understand short names like 'n' for 'north'.
 */
public enum Direction {
    NORTH("north", "n") {
        @Override
        public Direction getOpposite() {
            return SOUTH;
        }
    },
    SOUTH("south", "s") {
        @Override
        public Direction getOpposite() {
            return NORTH;
        }
    };

    private final String name;
    private final String shortName;

    /**
     * Makes a new direction
     * @param name The full name (like "north")
     * @param shortName The short name (like "n")
     */
    Direction(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    /**
     * Finds the opposite direction
     * @return The direction that goes the other way
     */
    public abstract Direction getOpposite();

    /**
     * Changes text into a direction
     * @param input The text to check (like "north" or "n")
     * @return The matching direction, or null if no match
     */
    public static Direction fromString(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        String cleanInput = input.toLowerCase().trim();
        for (Direction dir : Direction.values()) {
            if (dir.name.equals(cleanInput) || dir.shortName.equals(cleanInput)) {
                return dir;
            }
        }
        return null;
    }

    /**
     * Gets the full name
     * @return The direction's full name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the short name
     * @return The direction's short name
     */
    public String getShortName() {
        return shortName;
    }
}