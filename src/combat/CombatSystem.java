package combat;

import character.ability.AbilityType;
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
        CombatLog log = new CombatLog();

        // Start combat round
        log.addEntry("\n=== Combat Round ===");
        logCombatStatus(player, new HashSet<>(currentRoom.getEnemies()), log);

        // Execute player's ability
        ability.execute(player, target);

        // For offensive abilities, we're done with player's turn
        // For defensive abilities (Invisibility/Clone), also perform normal attack
        if (ability.getType() == AbilityType.INVISIBILITY ||
                ability.getType() == AbilityType.CLONE) {
            executePlayerTurn(player, target, log);
        }

        // Enemy turns
        engagedEnemies.addAll(currentRoom.getEnemies());
        for (Enemy enemy : engagedEnemies) {
            if (enemy.isAlive()) {
                enemy.takeTurn(player);
                if (!player.isAlive()) break;
            }
        }

        // Apply cooldown
        ability.startCooldown();

        // End combat round
        log.addEntry("\nEnd of Round:");
        logCombatStatus(player, engagedEnemies, log);
        log.display();

        if (!target.isAlive()) {
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
    /**
     * Finds a specific enemy in the current room by name
     * @param currentRoom Room to search in
     * @param targetName Name of enemy to find
     * @return The found enemy, or null if not found
     */
    private Enemy findTargetEnemy(Room currentRoom, String targetName) {
        Enemy enemy = currentRoom.findEnemy(targetName);
        if (enemy == null) {
            System.out.println("There's no " + targetName + " here to attack!");
            return null;
        }
        return enemy;
    }

    /**
     * Executes a single combat round between player and enemy
     * @param player The player in combat
     * @param enemy The enemy being fought
     */
    private void executeCombatRound(Player player, Enemy enemy) {
        engagedEnemies.add(enemy);
        CombatResult result = executeCombatRound(player, engagedEnemies);
        result.getCombatLog().display();

        if (result.isEnemyDefeated()) {
            handleEnemyDefeat(enemy);
        }
    }

    /**
     * Checks if combat can occur in the given room
     * @param room Room to check for enemies
     * @return true if combat is possible
     */
    private boolean validateCombatRoom(Room room) {
        if (!room.hasEnemies()) {
            System.out.println("No enemies here to fight!");
            return false;
        }
        return true;
    }

    /**
     * Validates if an ability can be used
     * @param ability Ability to check
     * @return true if ability can be used
     */
    private boolean validateAbilityUse(Ability ability) {
        if (ability == null) {
            System.out.println("You don't have any special ability!");
            return false;
        }

        if (ability.getCurrentCooldown() > 0) {
            System.out.printf("Ability on cooldown: %d turns remaining%n",
                    ability.getCurrentCooldown());
            return false;
        }

        return true;
    }

    /**
     * Processes an enemy's defeat
     * Updates room status and removes enemy from combat
     * @param enemy The defeated enemy
     */
    private void handleEnemyDefeat(Enemy enemy) {
        engagedEnemies.remove(enemy);
        Room currentRoom = enemy.getCurrentRoom();
        if (currentRoom != null) {
            currentRoom.removeEnemy(enemy);
            if (!currentRoom.hasEnemies()) {
                currentRoom.setCleared(true);
            }
        }
    }

    /**
     * Determines if enemies should take their turns
     * @param currentRoom Room containing enemies
     * @param playerAbility Player's current ability status
     * @return true if enemies should act
     */
    private boolean shouldExecuteEnemyTurns(Room currentRoom, Ability playerAbility) {
        return (playerAbility == null || playerAbility.getCurrentCooldown() == 0) &&
                !currentRoom.getEnemies().isEmpty() &&
                !engagedEnemies.isEmpty();
    }

    /**
     * Executes turns for all engaged enemies
     * @param player The player being attacked
     */
    private void executeEnemyTurns(Player player) {
        for (Enemy enemy : engagedEnemies) {
            if (enemy.isAlive()) {
                enemy.takeTurn(player);
                if (!player.isAlive()) return;
            }
        }
    }

    /**
     * Shows the start of combat status
     * @param player The player in combat
     * @param enemies Set of enemies in combat
     * @param log Combat log to update
     */
    private void showCombatStart(Player player, Set<Enemy> enemies, CombatLog log) {
        log.addEntry("\n=== Combat Round ===");
        logCombatStatus(player, enemies, log);
    }

    /**
     * Executes the player's combat turn
     * @param player The player taking action
     * @param target Enemy being targeted
     * @param log Combat log to update
     * @return true if enemy was defeated
     */
    private boolean executePlayerTurn(Player player, Enemy target, CombatLog log) {
        player.performAttack(target);
        return !target.isAlive();
    }

    /**
     * Executes turns for a set of enemies
     * @param player The player being attacked
     * @param enemies Set of enemies taking turns
     * @param log Combat log to update
     */
    private void executeEnemiesTurns(Player player, Set<Enemy> enemies, CombatLog log) {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                enemy.takeTurn(player);
            }
        }
    }

    /**
     * Shows the end of combat status
     * @param player The player in combat
     * @param enemies Set of enemies in combat
     * @param log Combat log to update
     */
    private void showCombatEnd(Player player, Set<Enemy> enemies, CombatLog log) {
        log.addEntry("\nEnd of Round:");
        logCombatStatus(player, enemies, log);
    }

    /**
     * Logs the current combat status
     * @param player The player to log
     * @param enemies Set of enemies to log
     * @param log Combat log to update
     */
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