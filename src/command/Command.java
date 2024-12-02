package command;

/**
 * Available game commands.
 */

public enum Command {
    MOVE("move", "Move in a direction", "move north"),
    LOOK("look", "Look around the room cuz why not", "look"),
    ATTACK("attack", "Attack an enemy", "attack boss"),
    TAKE("take", "Pick up an item", "take potion"),
    USE("use", "Use an item", "use potion"),
    INVENTORY("inventory", "Check your inventory", "inventory"),
    HELP("help", "Show available commands", "help"),
    QUIT("quit", "Exit the game", "quit");

    private final String name;
    private final String description;
    private final String example;

    Command(String name, String description, String example) {
        this.name = name;
        this.description = description;
        this.example = example;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getExample() { return example; }

    /**
     * Converts input to a Command. We convert to lowercase and trim space
     * @return Command if valid, null if not
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
