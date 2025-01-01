// DungeonGeneration.java
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
     * Creates the starting area of the dungeon.
     * @return The starting room (home).
     */
    public Room createStartingArea() {
        Room home = new Room(RoomType.HOME);
        Room entrance = new Room(RoomType.ENTRANCE);
        Room firstDungeonRoom = generateNextRoom();

        connectRooms(home, entrance, Direction.NORTH);
        connectRooms(entrance, firstDungeonRoom, Direction.NORTH);

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
     * Creates a new room when player goes deeper.
     */
    public Room generateNextRoom() {
        try {
            RoomType type = determineNextRoomType();
            Room room = new Room(type);
            populateRoom(room);

            // If this is a treasure room, we should still allow forward progress
            if (type == RoomType.TREASURE) {
                Room nextRoom = new Room(RoomType.NORMAL);
                populateRoom(nextRoom);
                connectRooms(room, nextRoom, Direction.NORTH);
            }

            lastGeneratedRoom = room;
            return room;
        } catch (Exception e) {
            System.err.println("Error generating room: " + e.getMessage());
            return new Room(RoomType.NORMAL);
        }
    }

    /**
     * Connects a new room to the north of the current room.
     * @param currentRoom The room the player is currently in.
     * @return The newly connected room.
     */
    public Room connectNewRoomNorth(Room currentRoom) {
        if (currentRoom == null) {
            throw new IllegalArgumentException("Current room cannot be null");
        }

        // If room already has a north exit, use that
        if (currentRoom.hasExit(Direction.NORTH)) {
            return currentRoom.getExit(Direction.NORTH);
        }

        // Only generate new room if current room is cleared
        if (currentRoom.isCleared()) {
            Room newRoom = generateNextRoom();
            connectRooms(currentRoom, newRoom, Direction.NORTH);
            return newRoom;
        }

        return null;
    }

    /**
     * Decides what type the next room should be.
     */
    private RoomType determineNextRoomType() {
        if (progress.canSpawnFinalBoss()) {
            return RoomType.BOSS;
        }
        if (progress.canSpawnSecondBoss()) {
            return RoomType.BOSS;
        }
        if (progress.canSpawnFirstBoss()) {
            return RoomType.BOSS;
        }

        if (random.nextDouble() < TREASURE_ROOM_CHANCE) {
            return RoomType.TREASURE;
        }

        return RoomType.NORMAL;
    }

    /**
     * Adds enemies and items to a room.
     */
    private void populateRoom(Room room) {
        switch (room.getType()) {
            case HOME:
                break;
            case ENTRANCE:
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
     * Adds a boss enemy based on progress.
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

        addTreasureToRoom(room);
    }

    /**
     * Adds valuable items to a room.
     */
    private void addTreasureToRoom(Room room) {
        room.addItem(generateValuableItem());

        if (random.nextDouble() < ITEM_SPAWN_CHANCE) {
            room.addItem(generateValuableItem());
        }
    }

    /**
     * Adds normal enemies to a room.
     */
    private void addRegularEnemies(Room room) {
        int enemyCount = random.nextInt(MAX_ENEMIES_PER_ROOM) + 1;

        for (int i = 0; i < enemyCount; i++) {
            EnemyType type = getRandomEnemyType();
            String enemyName = type.getName() + " " + (i + 1);
            Enemy enemy = new Enemy(enemyName, type, false);
            room.addEnemy(enemy);
        }
    }

    /**
     * Adds random items to a room.
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
     * Creates a valuable item (for treasure/boss rooms).
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
     * Creates a random item.
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
     * Picks a random enemy type.
     */
    private EnemyType getRandomEnemyType() {
        double roll = random.nextDouble();
        if (roll < 0.4) {
            return EnemyType.GOBLIN;
        } else if (roll < 0.7) {
            return EnemyType.SKELETON;
        } else {
            return EnemyType.WITCH;
        }
    }

    /**
     * Connects two rooms together.
     */
    private void connectRooms(Room from, Room to, Direction dir) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Cannot connect null rooms");
        }
        from.setExit(dir, to);
        to.setExit(dir.getOpposite(), from);
    }

    /**
     * Gets the last generated room.
     * @return The most recently generated room.
     */
    public Room getLastGeneratedRoom() {
        return lastGeneratedRoom;
    }

}