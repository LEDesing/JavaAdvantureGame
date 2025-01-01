package command;

/**
 * Represents all available commands that players can use in the game.
 * Each command has a name, description, and example of how to use it.
 * The commands control player movement, combat, inventory management, and game flow.
 */
public enum Command {
    /** Move to a different room in a specified direction */
    MOVE("move", "Move to another room in a direction", "move north"),
    
    /** Look around the current room to see what's there */
    LOOK("look", "See what is in your current room", "look"),
    
    /** Attack an enemy in the current room */
    ATTACK("attack", "Fight an enemy in the room", "attack boss"),
    
    /** Pick up an item from the current room */
    TAKE("take", "Pick up an item from the room", "take potion"),
    
    /** Use an item from your inventory */
    USE("use", "Use an item from your inventory", "use potion"),

    /** Use your special ability */
    ABILITY("ability", "Use your special ability", "ability"),

    /** Check what items you are carrying */
    INVENTORY("inventory", "See what items you are carrying", "inventory"),
    
    /** Drop an item from your inventory */
    DROP("drop", "Drop an item from your inventory", "drop sword"),
    
    /** Exit the game */
    QUIT("quit", "Leave the game", "quit");

    private final String name;
    private final String description;
    private final String example;

    /**
     * Creates a new command with its details.
     * 
     * @param name The command word that players type
     * @param description What the command does
     * @param example How to use the command
     */
    Command(String name, String description, String example) {
        this.name = name;
        this.description = description;
        this.example = example;
    }

    /**
     * Gets the word that players type to use this command.
     * @return The command's name
     */
    public String getName() { return name; }

    /**
     * Gets what this command does.
     * @return The command's description
     */
    public String getDescription() { return description; }

    /**
     * Gets an example of how to use this command.
     * @return Example usage of the command
     */
    public String getExample() { return example; }

    /**
     * Finds the matching command for what the player typed.
     * Ignores letter case and extra spaces.
     * 
     * @param input The text that the player typed
     * @return The matching command, or null if no match found
     */
    public static Command fromString(String input) {
        if (input == null || input.isEmpty()) return null;

        String normalizedInput = input.toLowerCase().trim();
        for (Command cmd : values()) {
            if (cmd.name.equals(normalizedInput)) {
                return cmd;
            }
        }
        return null;
    }
}
