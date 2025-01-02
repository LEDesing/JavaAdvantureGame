package game;

import character.ability.Ability;
import character.player.HeroClass;
import character.player.Player;
import command.CommandProcessor;
import java.util.List;
import java.util.Scanner;
import world.Direction;
import world.DungeonProgress;
import world.Room;
import world.generator.DungeonGeneration;

/**
 * The main game controller that manages the game state and flow.
 * This class handles:
 * - Starting and ending the game
 * - Creating the player character
 * - Managing the game world
 * - Processing player input
 * - Tracking game progress
 */
public class Game {
    /** Text shown when the game starts */
    private static final String INTRO_MESSAGE =
            "Welcome to the Labyrinth of VUB! \n" +
                    "A dark force has taken over the land...\n" +
                    "Only a brave hero can save us now!\n";

    /** Text shown when entering the dungeon */
    private static final String DUNGEON_INTRO =
            "You notice a northern door in your home.\n" +
                    "Beyond it lies the entrance to a dangerous dungeon.\n" +
                    "Many have entered, few have returned...";

    /** Core game components */
    private final Scanner scanner;
    private final CommandProcessor commandProcessor;
    private final DungeonProgress dungeonProgress;
    
    /** Game state tracking */
    private Room currentRoom;
    private List<Room> allRooms;
    private Player player;
    private boolean isGameRunning;

    /**
     * Creates a new game instance and sets up the basic components.
     * This includes the command processor, scanner for input, and dungeon progress tracker.
     */
    public Game() {
        this.scanner = new Scanner(System.in);
        this.dungeonProgress = new DungeonProgress();
        this.commandProcessor = new CommandProcessor(this);
        this.isGameRunning = false;
    }

    /**
     * Starts the game and manages the main game loop.
     * This method:
     * - Shows the introduction
     * - Creates the player character
     * - Generates the game world
     * - Runs the main game loop
     * - Handles cleanup when the game ends
     */
    public void start() {
        try {
            initializeGame();
            runGameLoop();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    /**
     * Initializes all game components and displays introduction.
     */
    private void initializeGame() {
        displayIntroduction();
        createPlayerCharacter();
        generateDungeonLayout();
    }

    /**
     * Displays the game's introduction message.
     */
    private void displayIntroduction() {
        System.out.printf(INTRO_MESSAGE);
    }

    /**
     * Creates the player character by getting user input.
     */
    private void createPlayerCharacter() {
        Scanner scanner = new Scanner(System.in);
        String name;
        HeroClass selectedClass = null;

        // Get player name
        System.out.print("\nEnter your character's name: ");
        name = scanner.nextLine().trim();
        while (name.isEmpty()) {
            System.out.print("Name cannot be empty. Try again: ");
            name = scanner.nextLine().trim();
        }

        // Select character class
        System.out.println("\nChoose your class:");
        for (HeroClass heroClass : HeroClass.values()) {
            System.out.printf("%s - %s%n",
                    heroClass.getName(),
                    heroClass.getDescription());
        }

        while (selectedClass == null) {
            System.out.print("\nI'd like to pick: ");
            String choice = scanner.nextLine().trim();
            try {
                selectedClass = HeroClass.valueOf(choice.toUpperCase());
                System.out.printf("\nYou have chosen the path of the %s!%n", selectedClass.getName());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid class. Please try again.");
            }
        }

        player = new Player(name, selectedClass);
        displayWelcomeMessage();
    }


    /**
     * Displays welcome message after character creation.
     */
    private void displayWelcomeMessage() {
        System.out.printf("\nWelcome, %s the %s!%n",
                player.getCharacterName(),
                player.getHeroClass().getName());
        System.out.println("Your adventure is about to begin...");
        System.out.println(DUNGEON_INTRO);
        displayAvailableCommands(true);
        System.out.println("\nWhat would you like to do?"); // Move this here
    }

    /**
     * Generates the dungeon layout using the dungeon generator.
     */
    private void generateDungeonLayout() {
        DungeonGeneration generator = new DungeonGeneration(dungeonProgress);
        Room startingRoom = generator.createStartingArea();
        this.currentRoom = startingRoom;
    }


    /**
     * Shows the player what they can do right now.
     * Different options appear based on where they are and what they can see.
     *
     * @param isInitial true if player is at the start, false if they moved
     */
    private void displayAvailableCommands(boolean isInitial) {
        System.out.println("\nAvailable commands:");

        if (isInitial) {
            // At the start, you can only go into the dungeon
            System.out.println("- move north    : Enter the dungeon");
        } else {
            // Show which ways you can walk
            for (Direction dir : currentRoom.getExits().keySet()) {
                // Give more descriptive movement information
                String moveDescription;
                if (dir == Direction.NORTH) {
                    moveDescription = "Go deeper into the dungeon";
                } else if (dir == Direction.SOUTH) {
                    moveDescription = "Return to previous room";
                } else {
                    moveDescription = "Move to next room";
                }
                System.out.printf("- move %-8s: %s%n", dir.name().toLowerCase(), moveDescription);
            }

            // Show what you can do in this room
            if (currentRoom.hasEnemies()) {
                System.out.println("- attack <name> : Attack an enemy");

                // Show your special power and when you can use it again
                Ability ability = player.getSpecialAbility();
                String cooldownInfo = ability.getCurrentCooldown() > 0 ?
                        String.format(" (Cooldown: %d)", ability.getCurrentCooldown()) : "";
                System.out.printf("- ability      : %s - %s%s%n",
                        ability.getName(),
                        ability.getDescription(),
                        cooldownInfo);
            }
            if (currentRoom.hasItems()) {
                System.out.println("- take <item>   : Pick up an item");
            }
            // Show if you can use items from your bag
            if (player.getInventory().hasItems()) {
                System.out.println("- use <item>    : Use an item from inventory");
            }
        }

        // Things you can always do
        System.out.println("- look          : Examine your surroundings");
        System.out.println("- inventory     : Check your items");
        System.out.println("- help          : Show all commands");
        System.out.println("- quit          : Exit the game");
    }

    /**
     * Main game loop that handles player input and game state updates.
     * Continues until the player dies or exits the game.
     */
    private void runGameLoop() {
        isGameRunning = true;
        String input = getValidatedInput(); // Get first input after welcome message
        processCommand(input);              // Process first command

        while (isGameRunning && player.isAlive()) {
            try {
                promptAction();
                input = getValidatedInput();
                processCommand(input);
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
        handleGameEnd();
    }

    /**
     * Gets and validates user input.
     * @return validated user input
     * @throws IllegalArgumentException if input is invalid
     */
    private String getValidatedInput() {
        String input = scanner.nextLine().trim().toLowerCase();
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        }
        return input;
    }

    /**
     * Processes a player command.
     * @param input the player's input command
     */
    private void processCommand(String input) {
        isGameRunning = commandProcessor.processInput(input);
    }

    /**
     * Prompts the player for their next action.
     */
    private void promptAction() {
        displayAvailableCommands(false);
        System.out.println("\nWhat would you like to do?");
    }

    /**
     * Handles the end of the game, displaying appropriate messages.
     */
    private void handleGameEnd() {
        if (!player.isAlive()) {
            System.out.println("Game Over! You have been defeated...");
        } else {
            System.out.println("Thanks for playing! Goodbye.");
        }
    }

    /**
     * Cleans up resources when the game ends.
     */
    private void cleanup() {
        if (scanner != null) {
            scanner.close();
        }
    }

    // Getters and setters
    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public Player getPlayer() {
        return player;
    }

    public void setGameRunning(boolean running) {
        this.isGameRunning = running;
    }

    public List<Room> getAllRooms() {
        return allRooms;
    }

    public DungeonProgress getDungeonProgress() {
        return dungeonProgress;
    }
}