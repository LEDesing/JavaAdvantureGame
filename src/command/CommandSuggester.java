package command;

import world.Room;
import world.Direction;
import character.player.Player;
import character.enemy.Enemy;
import items.Item;

public class CommandSuggester {
    private static final String SUGGESTION_SEPARATOR = "\n─────────────────";

    public static void suggestAvailableCommands(Room currentRoom, Player player) {
        StringBuilder suggestions = new StringBuilder("\nAvailable Actions:");

        // Always available commands
        suggestions.append("\n• General:");
        suggestions.append("\n  - look: Look around");
        suggestions.append("\n  - inventory: Check your items");
        suggestions.append("\n  - help: Show all commands");

        // Movement options
        appendMovementSuggestions(suggestions, currentRoom);

        // Combat options
        appendCombatSuggestions(suggestions, currentRoom);

        // Item interactions
        appendItemSuggestions(suggestions, currentRoom, player);

        System.out.println(suggestions.toString());
        System.out.println(SUGGESTION_SEPARATOR);
    }

    private static void appendMovementSuggestions(StringBuilder sb, Room room) {
        if (!room.getExits().isEmpty()) {
            sb.append("\n\n• Movement:");
            for (Direction dir : room.getExits().keySet()) {
                sb.append(String.format("\n  - move %s", dir.getName()));
            }
        }
    }

    private static void appendCombatSuggestions(StringBuilder sb, Room room) {
        if (!room.getEnemies().isEmpty()) {
            sb.append("\n\n• Combat:");
            for (Enemy enemy : room.getEnemies()) {
                sb.append(String.format("\n  - attack %s", enemy.getCharacterName()));
            }
        }
    }

    private static void appendItemSuggestions(StringBuilder sb, Room room, Player player) {
        // Items in room
        if (!room.getItems().isEmpty()) {
            sb.append("\n\n• Items here:");
            for (Item item : room.getItems()) {
                sb.append(String.format("\n  - take %s", item.getName()));
            }
        }

        // Inventory items
        if (player.getInventory().hasItems()) {
            sb.append("\n\n• Your items:");
            for (Item item : player.getInventory().getItems()) {
                sb.append(String.format("\n  - use %s", item.getName()));
                sb.append(String.format("\n  - drop %s", item.getName()));
            }
        }
    }
}