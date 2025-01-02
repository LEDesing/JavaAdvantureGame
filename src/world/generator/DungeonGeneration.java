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
 * The dungeon follows a fixed structure:
 * 1. Home -> Entrance -> Normal Room
 * 2. First Boss (Flame Warden)
 * 3. Two rooms (Normal/Treasure)
 * 4. Second Boss (Frost Sentinel)
 * 5. Two rooms (Normal/Treasure)
 * 6. Final Boss (Shadow Lord)
 */
public class DungeonGeneration {
    private static final double TREASURE_ROOM_CHANCE = 0.40;
    private static final double ITEM_SPAWN_CHANCE = 0.65;
    private static final int MAX_ITEMS_PER_ROOM = 2;
    private static final int MAX_ENEMIES_PER_ROOM = 2;
    private static final int ROOMS_BEFORE_FIRST_BOSS = 1;

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

        // Give player a starting health potion
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
        progress.moveDeeper();

        RoomType type = determineNextRoomType();
        Room room = new Room(type);
        populateRoom(room);

        // If this is a treasure room and we're not at max depth,
        // ensure there's a path forward
        if (type == RoomType.TREASURE && progress.canGenerateNextRoom()) {
            Room nextRoom = new Room(RoomType.NORMAL);
            populateRoom(nextRoom);
            connectRooms(room, nextRoom, Direction.NORTH);
        }

        lastGeneratedRoom = room;
        return room;
    }

    /**
     * Decides what type the next room should be based on progression.
     */
    private RoomType determineNextRoomType() {
        // Force normal combat room before first boss
        if (!progress.isFirstBossDefeated() &&
                progress.getCurrentDepth() == ROOMS_BEFORE_FIRST_BOSS) {
            return RoomType.NORMAL;
        }

        // Check for boss rooms at appropriate depths
        if (progress.shouldSpawnBoss()) {
            return RoomType.BOSS;
        }

        // Between bosses, chance for treasure rooms
        if (random.nextDouble() < TREASURE_ROOM_CHANCE) {
            return RoomType.TREASURE;
        }

        return RoomType.NORMAL;
    }

    /**
     * Adds appropriate contents to a room based on its type.
     */
    private void populateRoom(Room room) {
        switch (room.getType()) {
            case HOME:
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
     * Adds the appropriate boss based on progression.
     */
    private void addBossToRoom(Room room) {
        EnemyType bossType = progress.getCurrentBossType();
        if (bossType == null) {
            throw new IllegalStateException("Tried to create boss room without valid boss type");
        }

        Enemy boss = new Enemy(bossType.getName(), bossType, true);
        room.addEnemy(boss);
        addTreasureToRoom(room); // Boss rooms always have treasure
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
        // Force single weak enemy for the first combat room
        if (!progress.isFirstBossDefeated() &&
                progress.getCurrentDepth() == ROOMS_BEFORE_FIRST_BOSS) {
            Enemy enemy = new Enemy("Goblin Trainee", EnemyType.GOBLIN, false);
            room.addEnemy(enemy);
            return;
        }

        // Normal enemy generation for other rooms
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