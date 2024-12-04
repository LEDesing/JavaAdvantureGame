package game;

import character.CharacterClass;
import command.Command;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import utils.InputValidator;
import world.Direction;
import world.Room;
import world.RoomType;

public class Game {
    private final Scanner scanner;
    private final GameController controller;
    private static final int ROOMS_BEFORE_BOSS = 7;
    private static final int ROOMS_BEFORE_MINIBOSS = 2;
    private Room currentRoom;
    private List<Room> allRooms;

    private boolean isGameRunning;
    private String playerName;
    private CharacterClass playerClass;

    public Game(){
        this.scanner = new Scanner(System.in);
        this.controller = new GameController(this);
        this.isGameRunning = false;
        this.allRooms = new ArrayList<>();
    }

    public void start() {
        showIntroduction();
        createCharacter();
        generateRooms();
        gameLoop();
    }

    private void generateRooms() {
        allRooms = new ArrayList<>();

        currentRoom = new Room(RoomType.HOME);
        allRooms.add(currentRoom);

        for (int i = 0; i < ROOMS_BEFORE_BOSS; i++) {
            Room newRoom = new Room(RoomType.NORMAL);
            if (i == ROOMS_BEFORE_MINIBOSS) {
                newRoom = new Room(RoomType.MINI_BOSS);
            }
            allRooms.add(newRoom);
        }

        Room bossRoom = new Room(RoomType.BOSS);
        allRooms.add(bossRoom);

        // Connect rooms (we'll implement this next)
        connectRooms();
    }

    private void connectRooms() {
        for (int i = 0; i < allRooms.size() - 1; i++) {
            Room currentRoom = allRooms.get(i);
            Room nextRoom = allRooms.get(i + 1);

            if (Math.random() < 0.5) {
                if (Math.random() < 0.5) {
                    currentRoom.addExit(Direction.EAST, nextRoom);
                    nextRoom.addExit(Direction.WEST, currentRoom);
                } else {
                    currentRoom.addExit(Direction.WEST, nextRoom);
                    nextRoom.addExit(Direction.EAST, currentRoom);
                }
            } else {
                if (Math.random() < 0.5) {
                    currentRoom.addExit(Direction.NORTH, nextRoom);
                    nextRoom.addExit(Direction.SOUTH, currentRoom);
                } else {
                    currentRoom.addExit(Direction.SOUTH, nextRoom);
                    nextRoom.addExit(Direction.NORTH, currentRoom);
                }
            }
        }
    }

    private void showIntroduction() {
        System.out.println("\n=== Welcome to the VUB Game! ===");
        System.out.println("Hey there! Ready for an adventure?");
        System.out.println("Bad things are happening in our world...");
        System.out.println("We need a hero to save us!");
        System.out.println("Could that hero be you?");
        System.out.println("===========================\n");
    }

    private void createCharacter() {
        while (playerName == null || playerName.trim().isEmpty()) {
            System.out.print("First things first - what's your name? ");
            try{
                String input = scanner.nextLine();
                String validatedName = InputValidator.validateInput(input);
                if(validatedName.length() < 2){
                    System.out.println("Your name needs to be longer than that!");
                    continue;
                }
                playerName = validatedName;

            } catch (IllegalArgumentException e){
                System.out.println("Oops! " + e.getMessage());
            }
        }

        // Get player class
        while (playerClass == null) {
            System.out.println("\nNow, pick what kind of hero you want to be:");
            System.out.println("(Each hero is good at different things!)\n");

            for (CharacterClass c : CharacterClass.values()) {
                System.out.printf("• %s - %s%n", c.getName(), c.getDescription());
                System.out.printf("  Health: %d (how much damage you can take)%n", c.getBaseHealth());
                System.out.printf("  Damage: %d (how hard you hit)%n", c.getBaseDamage());
                System.out.println(); // Empty line between classes
            }

            System.out.print("Which one do you like? Just type the name > ");
            try {
                String input = scanner.nextLine();
                String validatedInput = InputValidator.validateInput(input);
                playerClass = CharacterClass.fromString(validatedInput);
                if (playerClass == null) {
                    System.out.println("Sorry, that's not one of the choices. Try again!");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.printf("\nAwesome! You are now %s the %s!%n",
                playerName, playerClass.getName());
        System.out.println("Your adventure is about to begin...");
        System.out.println("(If you need help, just type 'help')\n");
    }

    private void gameLoop(){
        isGameRunning = true;

        while (isGameRunning) {
            System.out.println(" Tell me man, what do you want to do? ");
            String input = scanner.nextLine();

            try {
                String cleanInput = InputValidator.validateInput(input);
                String[] parts = cleanInput.split("\\s+", 2);

                Command command = Command.fromString(parts[0]);
                if (command == null) {
                    System.out.println("I don't know that command!");
                    System.out.println("Type 'help' to see what you can do.");
                    continue;
                }

                String argument = parts.length > 1 ? parts[1] : "";
                controller.handleCommand(command, argument);

            }catch(IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }
    }




    public void setIsGameRunning(boolean gameRunning) {
        this.isGameRunning = gameRunning;
    }

    public String getPlayerName() {
        return playerName;
    }

    public CharacterClass getPlayerClass() {
        return playerClass;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public List<Room> getAllRooms(){
        return allRooms;
    }
}
