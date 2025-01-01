package command;

import character.player.Player;
import combat.CombatSystem;
import game.Game;
import items.Item;
import utils.UserInput;
import world.Direction;
import world.Room;

/**
 * Processes and executes player commands in the game.
 * This class handles all user input and converts it into game actions.
 * It ensures commands are valid and provides feedback to the player.
 *
 * @since 11.0
 */
public class CommandProcessor {
    /** Main game instance */
    private final Game game;

    /** Combat system for handling battles */
    private final CombatSystem combatSystem;

    /** Messages shown to players when certain actions fail */
    private static final String INVALID_DIRECTION_MSG = "Try: north (n) or south (s)";
    private static final String ENEMIES_PRESENT_MSG = "You can't run away while enemies are here!";
    private static final String NO_EXIT_MSG = "You can't go that way!";
    private static final String EMPTY_INVENTORY_MSG = "There are no items in your inventory.";

    /**
     * Creates a new command processor
     * @param game The main game instance
     */
    public CommandProcessor(Game game) {
        this.game = game;
        this.combatSystem = new CombatSystem();
    }

    /**
     * Takes player input and performs the matching game action.
     * Handles invalid input by showing helpful error messages.
     *
     * @param rawInput The text that the player typed
     * @return true if the game should continue, false if the player wants to quit
     */
    public boolean processInput(String rawInput) {
        try {
            String cleanInput = UserInput.clean(rawInput);
            String[] parts = cleanInput.split(" ", 2);
            Command command = Command.fromString(parts[0]);

            if (command == null) {
                System.out.println("I don't know that command. Type 'help' to see what you can do!");
                return true;
            }

            String argument = parts.length > 1 ? parts[1] : "";
            return executeCommand(command, argument);

        } catch (IllegalArgumentException e) {
            System.out.println("Oops: " + e.getMessage());
            return true;
        }
    }

    /**
     * Executes the given command with its argument
     * @param command Which command to execute
     * @param argument Extra information for the command
     * @return false if game should end, true otherwise
     */
    private boolean executeCommand(Command command, String argument) {
        switch (command) {
            case MOVE:
                handleMove(argument);
                break;
            case LOOK:
                handleLook();
                break;
            case ATTACK:
                combatSystem.handleAttack(game.getPlayer(), game.getCurrentRoom(), argument);
                break;
            case TAKE:
                handleTake(argument);
                combatSystem.handlePostAction(game.getPlayer(), game.getCurrentRoom());
                break;
            case USE:
                handleUse(argument);
                combatSystem.handlePostAction(game.getPlayer(), game.getCurrentRoom());
                break;
            case ABILITY:
                combatSystem.handleAbility(game.getPlayer(), game.getCurrentRoom());
                break;
            case INVENTORY:
                handleInventory();
                combatSystem.handlePostAction(game.getPlayer(), game.getCurrentRoom());
                break;
            case DROP:
                handleDrop(argument);
                combatSystem.handlePostAction(game.getPlayer(), game.getCurrentRoom());
                break;
            case QUIT:
                return handleQuit();
            default:
                System.out.println("Command not implemented yet!");
        }
        return true;
    }

    /**
     * Handles player movement in a direction
     * @param direction The direction to move in
     */
    private void handleMove(String direction) {
        if (direction.isEmpty()) {
            System.out.println("Which way? " + INVALID_DIRECTION_MSG);
            return;
        }

        try {
            Direction dir = Direction.fromString(UserInput.clean(direction));
            if (dir == null) {
                System.out.println("That's not a valid direction!\n" + INVALID_DIRECTION_MSG);
                return;
            }

            checkAndMove(game.getCurrentRoom(), dir);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid direction: " + e.getMessage());
        }
    }

    /**
     * Validates and executes movement to a new room
     * @param currentRoom The room the player is currently in
     * @param dir Direction to move in
     */
    private void checkAndMove(Room currentRoom, Direction dir) {
        if (!currentRoom.hasExit(dir)) {
            System.out.println(NO_EXIT_MSG);
            return;
        }

        if (!currentRoom.getEnemies().isEmpty()) {
            System.out.println(ENEMIES_PRESENT_MSG);
            return;
        }

        // Move to new room and look around
        game.setCurrentRoom(currentRoom.getExit(dir));
        handleLook();
    }

    /**
     * Shows the current room description
     */
    private void handleLook() {
        game.getCurrentRoom().describeRoom();
    }

    /**
     * Handles picking up items from the current room
     * @param itemName Name of item to take
     */
    private void handleTake(String itemName) {
        if (itemName.isEmpty()) {
            System.out.println("Take what? Type 'take' and the item's name!");
            showTakeableItems();
            return;
        }

        Room currentRoom = game.getCurrentRoom();
        Player player = game.getPlayer();
        Item item = currentRoom.removeItem(itemName);

        if (item != null) {
            if (player.addToInventory(item)) {
                System.out.println("You picked up the " + itemName);
            } else {
                currentRoom.addItem(item);
                System.out.println("Your inventory is full!");
            }
        } else {
            System.out.println("There's no " + itemName + " here to take!");
        }
    }

    /**
     * Shows items available to take in current room
     */
    private void showTakeableItems() {
        Room currentRoom = game.getCurrentRoom();
        if (currentRoom.getItems().isEmpty()) {
            System.out.println("There are no items here to take.");
            return;
        }

        System.out.println("\nItems you can take:");
        currentRoom.getItems().forEach(item ->
                System.out.printf("- %s: %s%n", item.getName(), item.getDescription()));
    }

    /**
     * Handles dropping items from inventory
     * @param itemName Name of item to drop
     */
    private void handleDrop(String itemName) {
        if (itemName.isEmpty()) {
            System.out.println("Drop what? Type 'drop' and the item's name!");
            return;
        }

        Player player = game.getPlayer();
        Item item = player.removeFromInventory(itemName);

        if (item != null) {
            game.getCurrentRoom().addItem(item);
            System.out.println("You dropped the " + itemName);
        } else {
            System.out.println("You don't have a " + itemName + " to drop!");
        }
    }

    /**
     * Handles using items from inventory
     * @param itemName Name of item to use
     */
    private void handleUse(String itemName) {
        if (itemName.isEmpty()) {
            showInventoryContents("Use what? Type 'use' and the item's name!");
            return;
        }

        if (!game.getPlayer().useItem(itemName)) {
            showInventoryContents("You don't have a " + itemName + " to use!");
        }
    }

    /**
     * Shows the player's inventory contents
     */
    private void handleInventory() {
        showInventoryContents(null);
    }

    /**
     * Displays inventory contents with optional message
     * @param message Optional message to display before contents
     */
    private void showInventoryContents(String message) {
        if (message != null) {
            System.out.println(message);
        }
        System.out.println("\nAvailable items in your inventory:");
        game.getPlayer().getInventory().showContents();
    }

    /**
     * Handles quitting the game
     * @return false to indicate game should end
     */
    private boolean handleQuit() {
        game.setGameRunning(false);
        return false;
    }
}