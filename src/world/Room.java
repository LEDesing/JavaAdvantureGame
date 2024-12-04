package world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    private  List<Enemy> enemies;
    private  List<String> items;
    private  RoomType type;
    private  String description;
    private  Map<Direction, Room> exits;

    private boolean isVisited;
    private boolean isCleared;

    public Room(RoomType type){
        this.type = type;
        this.description = type.getDescription();
        this.exits = new HashMap<>();
        this.enemies = new ArrayList<>();
        this.items = new ArrayList<>();
        this.isVisited = false;
        this.isCleared = false;
    }

    public void describeRoom(){
        System.out.printf(" %s ", type.getName());
        System.out.println(description);

        if (!exits.isEmpty()){
            System.out.println();
        }

        if (!isCleared && !enemies.isEmpty()) {
            System.out.println("Enemies here:");
            for (Enemy enemy : enemies) {
                System.out.printf("- %s\n", enemy.getName());
            }
        }

        if (!items.isEmpty()) {
            System.out.println("Items here:");
            for (String item : items) {
                System.out.printf("- %s\n", item);
            }
        }

        isVisited = true;

    }

    public void clearRoom(){
        isCleared = true;
        enemies.clear();
    }

    public RoomType getType() {
        return type;
    }

    public Map<Direction, Room> getExits() {
        return exits;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<String> getItems() {
        return items;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public boolean isCleared() {
        return isCleared;
    }

    public void addExit(Direction direction, Room room) {
        exits.put(direction, room);
    }

    public Room getExit(Direction direction) {
        return exits.get(direction);
    }
}
