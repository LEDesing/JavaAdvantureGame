package command;

/**
 * All the commands you can use in the game.
 */

public enum Command {
    MOVE("move", "Move in a direction", "move north"),
    LOOK("look", "Look around the room cuz why not", "look"),
    ATTACK("attack", "Attack an enemy", "attack boss"),
    TAKE("take", "Pick up an item", "take potion"),
    USE("use", "Use an item", "use potion"),
    INVENTORY("inventory", "Check your inventory", "inventory"),
    HELP("help", "Show available commands", "help"),
    DROP("drop", "Drop an item from inventory", "drop sword"),
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
     * Find a command from user input.
     * Ex: "move" returns Command.MOVE
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
