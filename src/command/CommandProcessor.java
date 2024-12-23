package command;

import character.enemy.Enemy;
import character.player.Player;
import combat.CombatResult;
import combat.CombatSystem;
import game.Game;
import items.Inventory;
import items.Item;
import utils.UserInput;
import world.Direction;
import world.Room;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles what players type and makes the game do things.
 * Makes sure commands work and tells players if something goes wrong.
 */
public class CommandProcessor {
    private final Game game;
    private Set<Enemy> engagedEnemies = new HashSet<>();

    // Simple messages for players
    private static final String INVALID_DIRECTION_MSG = "Try: north (n) or south (s)";
    private static final String ENEMIES_PRESENT_MSG = "You can't run away while enemies are here!";
    private static final String NO_EXIT_MSG = "You can't go that way!";

    /**
     * Makes a new command handler
     * @param game The game to handle commands for
     */
    public CommandProcessor(Game game) {
        this.game = game;
    }

    /**
     * Takes what player typed and does the right action
     * @param rawInput What the player typed
     * @return false if player wants to quit, true if not
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

            String argument = "";
            if (parts.length > 1) {
                argument = parts[1];
            }

            return executeCommand(command, argument);

        } catch (IllegalArgumentException e) {
            System.out.println("Oops: " + e.getMessage());
            return true;
        }
    }

    /**
     * Does what the command says
     * @param command Which command to do
     * @param argument Extra information for the command
     * @return false if game should end, true if not
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
                handleAttack(argument);
                break;
            case TAKE:
                handleTake(argument);
                handlePostActionCombat();
                break;
            case USE:
                handleUse(argument);
                handlePostActionCombat();
                break;
            case INVENTORY:
                handleInventory();
                handlePostActionCombat();
                break;
            case HELP:
                handleHelp();
                handlePostActionCombat();
                break;
            case DROP:
                handleDrop(argument);
                handlePostActionCombat();
                break;
            case QUIT:
                return handleQuit();
        }
        return true;
    }

    /**
     * Tries to move the player in a direction
     */
    private void handleMove(String direction) {
        if (direction.isEmpty()) {
            System.out.println("Which way? " + INVALID_DIRECTION_MSG);
            return;
        }

        try {
            String cleanDirection = UserInput.clean(direction);
            Direction dir = Direction.fromString(cleanDirection);

            if (dir == null) {
                System.out.println("That's not a valid direction!");
                System.out.println(INVALID_DIRECTION_MSG);
                return;
            }

            Room currentRoom = game.getCurrentRoom();
            checkAndMove(currentRoom, dir);

        } catch (IllegalArgumentException e) {
            System.out.println("Oops: " + e.getMessage());
        }
    }

    /**
     * Checks if movement is possible and moves player
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

        Room nextRoom = currentRoom.getExit(dir);
        game.setCurrentRoom(nextRoom);
        engagedEnemies.clear(); // Clear engaged enemies when moving rooms
        handleLook();
    }

    /**
     * Shows what's in the room
     */
    private void handleLook() {
        Room currentRoom = game.getCurrentRoom();
        currentRoom.describeRoom();
    }

    /**
     * Handles fighting with enemies
     */
    private void handleAttack(String targetName) {
        if (targetName.isEmpty()) {
            System.out.println("Attack what? Type 'attack' and the enemy's name!");
            CommandSuggester.suggestAvailableCommands(game.getCurrentRoom(), game.getPlayer());
            return;
        }

        Room currentRoom = game.getCurrentRoom();
        Enemy enemy = currentRoom.findEnemy(targetName);

        if (enemy == null) {
            System.out.println("There's no " + targetName + " here to attack!");
            return;
        }

        // Add enemy to engaged set when attacked
        engagedEnemies.add(enemy);

        CombatResult result = CombatSystem.executeCombatRound(game.getPlayer(), enemy);
        result.getCombatLog().display();

        if (result.isEnemyDefeated()) {
            handleEnemyDefeat(enemy);
            engagedEnemies.remove(enemy);  // Remove defeated enemy from engaged set
        }
    }

    /**
     * Handles what happens when an enemy dies
     */
    private void handleEnemyDefeat(Enemy enemy) {
        Room currentRoom = game.getCurrentRoom();
        currentRoom.removeEnemy(enemy);

        String enemyName = enemy.getCharacterName();
        System.out.println("You defeated the " + enemyName + "!");

        if (currentRoom.getEnemies().isEmpty()) {
            System.out.println("Room cleared!");
            currentRoom.setCleared(true);
        }
    }

    private void handlePostActionCombat() {
        Room currentRoom = game.getCurrentRoom();

        // Only proceed if there are enemies in the room and we're engaged with some
        if (!currentRoom.getEnemies().isEmpty() && !engagedEnemies.isEmpty()) {
            Player player = game.getPlayer();

            // Let each engaged enemy take their turn
            for (Enemy enemy : engagedEnemies) {
                if (enemy.isAlive()) {
                    enemy.takeTurn(player);

                    // Check if player died
                    if (!player.isAlive()) {
                        return;
                    }
                }
            }
        }
    }

    /**
     * Tries to pick up an item
     */
    private void handleTake(String itemName) {
        if (itemName.isEmpty()) {
            System.out.println("Take what? Type 'take' and the item's name!");
            showTakeableItems(game.getCurrentRoom());
            return;
        }

        Room currentRoom = game.getCurrentRoom();
        Player player = game.getPlayer();

        Item item = currentRoom.removeItem(itemName);

        if (item != null) {
            boolean added = player.addToInventory(item);
            if (!added) {
                currentRoom.addItem(item);
                System.out.println("Your inventory is full!");
            } else {
                System.out.println("You picked up the " + itemName);
            }
        } else {
            System.out.println("There's no " + itemName + " here to take!");
        }
    }

    /**
     * Shows available items to take in the current room
     */
    private void showTakeableItems(Room room) {
        if (room.getItems().isEmpty()) {
            System.out.println("There are no items here to take.");
            return;
        }

        System.out.println("\nItems you can take:");
        for (Item item : room.getItems()) {
            System.out.printf("- %s: %s%n", item.getName(), item.getDescription());
        }
    }

    /**
     * Tries to drop an item
     */
    private void handleDrop(String itemName) {
        if (itemName.isEmpty()) {
            System.out.println("Drop what? Type 'drop' and the item's name!");
            return;
        }

        Player player = game.getPlayer();
        Room currentRoom = game.getCurrentRoom();

        Item item = player.removeFromInventory(itemName);

        if (item != null) {
            currentRoom.addItem(item);
            System.out.println("You dropped the " + itemName);
        } else {
            System.out.println("You don't have a " + itemName + " to drop!");
        }
    }

    /**
     * Tries to use an item
     */
    private void handleUse(String itemName) {
        if (itemName.isEmpty()) {
            System.out.println("Use what? Type 'use' and the item's name!");

            // Show available items
            Player player = game.getPlayer();
            Inventory inventory = player.getInventory();
            System.out.println("\nAvailable items in your inventory:");
            inventory.showContents();
            return;
        }

        Player player = game.getPlayer();
        boolean used = player.useItem(itemName);

        if (!used) {
            System.out.println("You don't have a " + itemName + " to use!");
            // Show available items here too
            System.out.println("\nAvailable items in your inventory:");
            player.getInventory().showContents();
        }
    }

    /**
     * Shows what the player is carrying
     */
    private void handleInventory() {
        Player player = game.getPlayer();
        Inventory inventory = player.getInventory();
        inventory.showContents();
    }

    /**
     * Shows all commands the player can use
     */
    private void handleHelp() {
        System.out.println("\nHere's what you can do:");
        for (Command cmd : Command.values()) {
            String name = cmd.getName();
            String description = cmd.getDescription();
            String example = cmd.getExample();

            System.out.printf("• %s - %s%n  Example: %s%n%n",
                    name,
                    description,
                    example);
        }
    }

    /**
     * Ends the game
     */
    private boolean handleQuit() {
        System.out.println("Thanks for playing! Goodbye!");
        game.setGameRunning(false);
        return false;
    }
}