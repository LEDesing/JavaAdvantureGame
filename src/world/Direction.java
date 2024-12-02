package world;

public enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST;

    /**
     * Converts string input to a Direction.
     * Accepts full names (e.g., "north") or shortcuts (e.g., "n")
     * @param input The direction string to convert
     * @return Direction enum if valid, null if not
     */
    public static Direction fromString(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        // Try exact enum name first
        try {
            return Direction.valueOf(input.toUpperCase().trim());
        } catch (IllegalArgumentException ignored) {
            System.out.println("Not an exact enum match: " + input);

        }

        // Check shortcuts
        switch (input.toLowerCase().trim()) {
            case "n":
            case "north":
                return NORTH;
            case "s":
            case "south":
                return SOUTH;
            case "e":
            case "east":
                return EAST;
            case "w":
            case "west":
                return WEST;
            default:
                System.out.println("Not an exact enum match: " + input);
                return null;
        }
    }
}