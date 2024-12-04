package world;

public enum RoomType {
    HOME("Home", "Your safe place. A nice fire burns in the corner."),
    NORMAL("Room", null),
    TREASURE("Treasure Room", "Wow! Gold and shiny stones are everywhere. You can get rich here!"),
    MINI_BOSS("Strong Enemy Room", "This room feels scary. A strong enemy is waiting..."),
    BOSS("Boss Room", "A huge dark room. The most dangerous enemy is here!");

    private final String name;
    private final String description;
    
    RoomType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription(){
        return description != null ? description : RoomDescription.getRandomRoomDescription();
    }

}
