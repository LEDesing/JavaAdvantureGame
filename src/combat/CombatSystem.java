package combat;

import character.player.Player;
import character.enemy.Enemy;


public class CombatSystem {
    private static final String COMBAT_SEPARATOR = "\n═══════════════════";

    public static CombatResult executeCombatRound(Player player, Enemy enemy) {
        CombatLog log = new CombatLog();

        // Start of combat round
        log.addEntry(String.format("\n=== Combat Round ===\n%s HP: %d/%d | %s HP: %d/%d",
                player.getCharacterName(), player.getCurrentHealthPoints(), player.getMaxHealthPoints(),
                enemy.getCharacterName(), enemy.getCurrentHealthPoints(), enemy.getMaxHealthPoints()));

        // Player's turn
        player.performAttack(enemy);

        // Enemy's turn (if still alive)
        if (enemy.isAlive()) {
            enemy.takeTurn(player);
        }

        // End of round status
        log.addEntry(String.format("\nEnd of Round:\n%s HP: %d/%d | %s HP: %d/%d",
                player.getCharacterName(), player.getCurrentHealthPoints(), player.getMaxHealthPoints(),
                enemy.getCharacterName(), enemy.getCurrentHealthPoints(), enemy.getMaxHealthPoints()));

        return new CombatResult(!enemy.isAlive(), log);
    }
}