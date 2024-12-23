package world.generator;

import character.enemy.Enemy;
import character.enemy.EnemyType;
import items.Item;
import items.ItemsType;
import world.Direction;
import world.Room;
import world.RoomType;
import world.DungeonProgress;

import java.util.Random;

/**
 * Creates and connects dungeon rooms.
 * Controls what appears in each room.
 */
public class DungeonGeneration {
    private static final double TREASURE_ROOM_CHANCE = 0.20;
    private static final double ITEM_SPAWN_CHANCE = 0.65;
    private static final int MAX_ITEMS_PER_ROOM = 2;
    private static final int MAX_ENEMIES_PER_ROOM = 2;

    private final DungeonProgress progress;
    private final Random random;
    private Room lastGeneratedRoom;

    public DungeonGeneration(DungeonProgress progress) {
        this.progress = progress;
        this.random = new Random();
        this.lastGeneratedRoom = null;
    }

    /**
     * Creates the starting area of the dungeon
     * @return The starting room (home)
     */
    public Room createStartingArea() {
        // Create rooms
        Room home = new Room(RoomType.HOME);
        Room entrance = new Room(RoomType.ENTRANCE);
        Room firstDungeonRoom = generateNextRoom();  // Create first dungeon room

        // Connect the rooms in sequence
        connectRooms(home, entrance, Direction.NORTH);
        connectRooms(entrance, firstDungeonRoom, Direction.NORTH);

        // Add starter items to entrance
        entrance.addItem(new Item(
                ItemsType.HEALTH_POTION.getDisplayName(ItemsType.HEALTH_POTION.getBasicPower()),
                ItemsType.HEALTH_POTION,
                ItemsType.HEALTH_POTION.getBasicPower(),
                ItemsType.HEALTH_POTION.getDescription()
        ));

        lastGeneratedRoom = firstDungeonRoom;
        return home;
    }

    /**
     * Makes a new room when player goes deeper
     */
    public Room generateNextRoom() {
        try {
            RoomType type = determineNextRoomType();
            Room room = new Room(type);
            populateRoom(room);
            lastGeneratedRoom = room;
            return room;
        } catch (Exception e) {
            System.err.println("Error generating room: " + e.getMessage());
            return new Room(RoomType.NORMAL);
        }
    }

    /**
     * Connects a new room to the north of the current room
     * @param currentRoom The room the player is currently in
     * @return The newly connected room
     */
    public Room connectNewRoomNorth(Room currentRoom) {
        if (currentRoom == null) {
            throw new IllegalArgumentException("Current room cannot be null");
        }

        // If the room is cleared and has no north exit, generate new room
        if (currentRoom.isCleared() && !currentRoom.hasExit(Direction.NORTH)) {
            Room newRoom = generateNextRoom();
            connectRooms(currentRoom, newRoom, Direction.NORTH);
            return newRoom;
        }

        // If there's already a north exit, use it
        return currentRoom.getExit(Direction.NORTH);
    }

    /**
     * Decides what type the next room should be
     */
    private RoomType determineNextRoomType() {
        // Check for boss rooms first
        if (progress.canSpawnFinalBoss()) {
            return RoomType.BOSS;        // Shadow Lord room
        }
        if (progress.canSpawnSecondBoss()) {
            return RoomType.BOSS;        // Frost Sentinel room
        }
        if (progress.canSpawnFirstBoss()) {
            return RoomType.BOSS;        // Flame Warden room
        }

        // Maybe make a treasure room
        if (random.nextDouble() < TREASURE_ROOM_CHANCE) {
            return RoomType.TREASURE;
        }

        // Default to normal dungeon room
        return RoomType.NORMAL;
    }

    /**
     * Adds enemies and items to a room
     */
    private void populateRoom(Room room) {
        switch (room.getType()) {
            case HOME:
                // Home is safe, no enemies
                break;
            case ENTRANCE:
                // Entrance just has starter items (already added in createStartingArea)
                break;
            case TREASURE:
                addTreasureToRoom(room);
                break;
            case BOSS:
                addBossToRoom(room);
                break;
            case NORMAL:
                addRegularEnemies(room);
                addRandomItems(room);
                break;
            default:
                throw new IllegalStateException("Unknown room type: " + room.getType());
        }
    }

    /**
     * Adds a boss enemy based on progress
     */
    private void addBossToRoom(Room room) {
        Enemy boss;
        if (!progress.isFirstBossDefeated()) {
            boss = new Enemy("Flame Warden", EnemyType.FLAME_WARDEN, true);
        } else if (!progress.isSecondBossDefeated()) {
            boss = new Enemy("Frost Sentinel", EnemyType.FROST_SENTINEL, true);
        } else {
            boss = new Enemy("Shadow Lord", EnemyType.SHADOW_LORD, true);
        }
        room.addEnemy(boss);

        // Bosses always drop good items
        addTreasureToRoom(room);
    }

    /**
     * Adds valuable items to a room
     */
    private void addTreasureToRoom(Room room) {
        // Always add at least one good item
        room.addItem(generateValuableItem());

        // Maybe add a second item
        if (random.nextDouble() < ITEM_SPAWN_CHANCE) {
            room.addItem(generateValuableItem());
        }
    }

    /**
     * Adds normal enemies to a room
     */
    private void addRegularEnemies(Room room) {
        int enemyCount = random.nextInt(MAX_ENEMIES_PER_ROOM) + 1;

        for (int i = 0; i < enemyCount; i++) {
            EnemyType type = getRandomEnemyType();
            String enemyName = type.getName() + " " + (i + 1);  // Number enemies for clarity
            Enemy enemy = new Enemy(enemyName, type, false);
            room.addEnemy(enemy);
        }
    }

    /**
     * Adds random items to a room
     */
    private void addRandomItems(Room room) {
        int itemCount = random.nextInt(MAX_ITEMS_PER_ROOM + 1);

        for (int i = 0; i < itemCount; i++) {
            if (random.nextDouble() < ITEM_SPAWN_CHANCE) {
                room.addItem(generateRandomItem());
            }
        }
    }

    /**
     * Creates a valuable item (for treasure/boss rooms)
     */
    private Item generateValuableItem() {
        ItemsType type = random.nextDouble() < 0.7 ?
                ItemsType.HEALTH_POTION : ItemsType.DAMAGE_POTION;

        return new Item(
                type.getDisplayName(type.getValuablePower()),
                type,
                type.getValuablePower(),
                type.getDescription()
        );
    }

    /**
     * Creates a random item
     */
    private Item generateRandomItem() {
        ItemsType type = random.nextDouble() < 0.6 ?
                ItemsType.HEALTH_POTION : ItemsType.DODGE_POTION;

        return new Item(
                type.getDisplayName(type.getBasicPower()),
                type,
                type.getBasicPower(),
                type.getDescription()
        );
    }

    /**
     * Picks a random enemy type
     */
    private EnemyType getRandomEnemyType() {
        double roll = random.nextDouble();
        if (roll < 0.4) {
            return EnemyType.GOBLIN;
        } else if (roll < 0.7) {
            return EnemyType.SKELETON;
        } else {
            return EnemyType.WITCH;  // Add some variety
        }
    }

    /**
     * Connects two rooms together
     */
    private void connectRooms(Room from, Room to, Direction dir) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Cannot connect null rooms");
        }
        from.setExit(dir, to);
        to.setExit(dir.getOpposite(), from);
    }

    /**
     * Gets the last generated room
     * @return The most recently generated room
     */
    public Room getLastGeneratedRoom() {
        return lastGeneratedRoom;
    }
}