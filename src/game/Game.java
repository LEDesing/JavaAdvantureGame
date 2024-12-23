package game;

import character.player.Player;
import character.player.HeroClass;
import command.CommandProcessor;
import world.DungeonProgress;
import world.Room;
import world.generator.DungeonGeneration;

import java.util.List;
import java.util.Scanner;

/**
 * The main class that runs the game.
 * Controls how the game works and what players see.
 */
public class Game {
    // Game messages
    private static final String INTRO_MESSAGE =
            "Welcome to the Labyrinth of VUB! \n" +
                    "A dark force has taken over the land...\n" +
                    "Only a brave hero can save us now!\n";

    private static final String DUNGEON_INTRO =
            "You notice a northern door in your home.\n" +
                    "Beyond it lies the entrance to a dangerous dungeon.\n" +
                    "Many have entered, few have returned...";

    // Game state
    private final Scanner scanner;
    private final CommandProcessor commandProcessor;
    private Room currentRoom;
    private final DungeonProgress dungeonProgress;
    private List<Room> allRooms;
    private Player player;
    private boolean isGameRunning;

    /**
     * Creates a new game instance and initializes core components.
     */
    public Game() {
        this.scanner = new Scanner(System.in);
        this.dungeonProgress = new DungeonProgress();
        this.commandProcessor = new CommandProcessor(this);
        this.isGameRunning = false;
    }

    /**
     * Starts the game and handles the main game flow.
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
        while (selectedClass == null) {
            System.out.println("\nChoose your class:");
            for (HeroClass heroClass : HeroClass.values()) {
                System.out.printf("%s - %s%n",
                        heroClass.getName(),
                        heroClass.getDescription());
            }

            String choice = scanner.nextLine().trim();
            try {
                selectedClass = HeroClass.valueOf(choice.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid class. Please try again.");
            }
        }

        // Replace this line:
        // player = new Player(name, selectedClass.getBaseHealth(), selectedClass.getBaseDamage());
        // With this:
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
        System.out.println("(Type 'help' if you need assistance)\n");
        System.out.println(DUNGEON_INTRO);
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
     * Main game loop that handles player input and game state updates.
     * Continues until the player dies or exits the game.
     */
    private void runGameLoop() {
        isGameRunning = true;
        while (isGameRunning && player.isAlive()) {
            try {
                promptAction();
                String input = getValidatedInput();
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