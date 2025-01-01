package combat;

import character.player.Player;
import character.enemy.Enemy;
import character.ability.Ability;
import world.Room;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages combat between player and enemies.
 * Handles combat rounds, damage calculation, and combat results.
 */
public class CombatSystem {
    /** Set of enemies currently engaged in combat */
    private final Set<Enemy> engagedEnemies;

    public CombatSystem() {
        this.engagedEnemies = new HashSet<>();
    }

    /**
     * Handles an attack command from the player
     * @param player The player attacking
     * @param currentRoom Current room with enemies
     * @param targetName Name of enemy to attack
     */
    public void handleAttack(Player player, Room currentRoom, String targetName) {
        if (targetName.isEmpty()) {
            System.out.println("Attack what? Type 'attack' and the enemy's name!");
            System.out.println(currentRoom.getAttackableEnemiesInfo());
            return;
        }

        Enemy enemy = findTargetEnemy(currentRoom, targetName);
        if (enemy == null) return;

        executeCombatRound(player, enemy);
    }

    /**
     * Handles the player using their special ability
     * @param player The player using ability
     * @param currentRoom Current room with enemies
     */
    public void handleAbility(Player player, Room currentRoom) {
        if (!validateCombatRoom(currentRoom)) return;

        Ability ability = player.getSpecialAbility();
        if (!validateAbilityUse(ability)) return;

        Enemy target = currentRoom.getEnemies().get(0);
        ability.execute(player, target);

        engagedEnemies.addAll(currentRoom.getEnemies());
        CombatResult result = executeCombatRound(player, engagedEnemies);
        result.getCombatLog().display();

        if (result.isEnemyDefeated()) {
            handleEnemyDefeat(target);
        }
    }

    /**
     * Handles post-action combat responses
     * @param player The player who acted
     * @param currentRoom Current room with enemies
     */
    public void handlePostAction(Player player, Room currentRoom) {
        Ability playerAbility = player.getSpecialAbility();
        if (playerAbility != null) {
            playerAbility.reduceCooldown();
        }

        if (shouldExecuteEnemyTurns(currentRoom, playerAbility)) {
            executeEnemyTurns(player);
        }
    }

    /**
     * Executes a complete combat round
     * @param player The player in combat
     * @param enemies Set of enemies in combat
     * @return Results of the combat round
     */
    public CombatResult executeCombatRound(Player player, Set<Enemy> enemies) {
        CombatLog log = new CombatLog();

        showCombatStart(player, enemies, log);
        Enemy target = enemies.iterator().next();
        boolean enemyDefeated = executePlayerTurn(player, target, log);

        if (!enemyDefeated) {
            executeEnemiesTurns(player, enemies, log);
        }

        showCombatEnd(player, enemies, log);
        return new CombatResult(enemyDefeated, log);
    }

    private Enemy findTargetEnemy(Room currentRoom, String targetName) {
        Enemy enemy = currentRoom.findEnemy(targetName);
        if (enemy == null) {
            System.out.println("There's no " + targetName + " here to attack!");
            return null;
        }
        return enemy;
    }

    private void executeCombatRound(Player player, Enemy enemy) {
        engagedEnemies.add(enemy);
        CombatResult result = executeCombatRound(player, engagedEnemies);
        result.getCombatLog().display();

        if (result.isEnemyDefeated()) {
            handleEnemyDefeat(enemy);
        }
    }

    private boolean validateCombatRoom(Room room) {
        if (!room.hasEnemies()) {
            System.out.println("No enemies here to fight!");
            return false;
        }
        return true;
    }

    private boolean validateAbilityUse(Ability ability) {
        if (ability.getCurrentCooldown() > 0) {
            System.out.println("Ability on cooldown: " +
                    ability.getCurrentCooldown() + " turns remaining");
            return false;
        }
        return true;
    }

    private void handleEnemyDefeat(Enemy enemy) {
        engagedEnemies.remove(enemy);
    }

    private boolean shouldExecuteEnemyTurns(Room currentRoom, Ability playerAbility) {
        return (playerAbility == null || playerAbility.getCurrentCooldown() == 0) &&
                !currentRoom.getEnemies().isEmpty() &&
                !engagedEnemies.isEmpty();
    }

    private void executeEnemyTurns(Player player) {
        for (Enemy enemy : engagedEnemies) {
            if (enemy.isAlive()) {
                enemy.takeTurn(player);
                if (!player.isAlive()) return;
            }
        }
    }

    private void showCombatStart(Player player, Set<Enemy> enemies, CombatLog log) {
        log.addEntry("\n=== Combat Round ===");
        logCombatStatus(player, enemies, log);
    }

    private boolean executePlayerTurn(Player player, Enemy target, CombatLog log) {
        player.performAttack(target);
        return !target.isAlive();
    }

    private void executeEnemiesTurns(Player player, Set<Enemy> enemies, CombatLog log) {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                enemy.takeTurn(player);
            }
        }
    }

    private void showCombatEnd(Player player, Set<Enemy> enemies, CombatLog log) {
        log.addEntry("\nEnd of Round:");
        logCombatStatus(player, enemies, log);
    }

    private void logCombatStatus(Player player, Set<Enemy> enemies, CombatLog log) {
        log.addEntry(String.format("%s HP: %d/%d",
                player.getCharacterName(),
                player.getCurrentHealthPoints(),
                player.getMaxHealthPoints()));

        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                log.addEntry(String.format("%s HP: %d/%d",
                        enemy.getCharacterName(),
                        enemy.getCurrentHealthPoints(),
                        enemy.getMaxHealthPoints()));
            }
        }
    }
}