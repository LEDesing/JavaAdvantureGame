package combat;

public class CombatResult {
    private final boolean enemyDefeated;
    private final CombatLog combatLog;

    public CombatResult(boolean enemyDefeated, CombatLog log) {
        this.enemyDefeated = enemyDefeated;
        this.combatLog = log;
    }

    public boolean isEnemyDefeated() { return enemyDefeated; }
    public CombatLog getCombatLog() { return combatLog; }
}