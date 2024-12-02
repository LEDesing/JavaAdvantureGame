package game;

import command.Command;
import world.Direction;

public class GameController {
    private final Game game;

    public GameController(Game game) {
        this.game = game;
    }

    /**
     * Handles all the game commands
     */
    public void handleCommand(Command command, String argument) {
        try {
            switch (command) {
                case HELP:
                    showHelp();
                    break;
                case LOOK:
                    handleLook();
                    break;
                case MOVE:
                    handleMove(argument);
                    break;
                case ATTACK:
                    handleAttack(argument);
                    break;
                case TAKE:
                    handleTake(argument);
                    break;
                case USE:
                    handleUse(argument);
                    break;
                case INVENTORY:
                    handleInventory();
                    break;
                case QUIT:
                    handleQuit();
                    break;
                default:
                    System.out.println("That command does not exist");
                    break;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Bro this is error: " + e.getMessage());
        }
    }

    private void showHelp() {
        System.out.println("\nHere's what you can do:");
        for (Command cmd : Command.values()) {
            System.out.printf("• %s - %s%n", cmd.getName(), cmd.getDescription());
            System.out.printf("  (like this: %s)%n", cmd.getExample());
        }
    }

    private void handleLook() {
        // Will add actual room description later
        System.out.println("You take a good look around...");
    }

    private void handleMove(String direction) {
        if (direction == null || direction.trim().isEmpty()) {
            throw new IllegalArgumentException("Which way do you want to go?");
        }

        Direction dir = Direction.fromString(direction);
        if (dir == null) {
            throw new IllegalArgumentException(
                    "I don't know that direction! Try north(n), south(s), east(e), or west(w)");
        }

        // Will add actual movement later
        System.out.println("You try to go " + dir.name().toLowerCase() + "...");
    }

    private void handleAttack(String target) {
        if (target == null || target.trim().isEmpty()) {
            throw new IllegalArgumentException(" Tell me what to attack!");
        }
        // Will add actual combat later
        System.out.println("You try to attack the " + target + "...");
    }

    private void handleTake(String item) {
        if (item == null || item.trim().isEmpty()) {
            throw new IllegalArgumentException("What do you want to take?");
        }
        // Will add actual item pickup later
        System.out.println("You try to take the " + item + "...");
    }

    private void handleUse(String item) {
        if (item == null || item.trim().isEmpty()) {
            throw new IllegalArgumentException("What do you want to use?");
        }
        // Will add actual item usage later
        System.out.println("You try to use the " + item + "...");
    }

    private void handleInventory() {
        // Will add actual inventory later
        System.out.println("You check your pockets...");
    }

    private void handleQuit() {
        System.out.println("See ya later! Thanks for playing!");
        game.setIsGameRunning(false);
    }
}