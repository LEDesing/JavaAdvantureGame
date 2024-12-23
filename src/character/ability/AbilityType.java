package character.ability;

import character.Character;
import character.player.Player;

/**
 * Defines all possible special abilities in the game.
 * Each ability has unique effects and can be used by players or enemies.
 */
public enum AbilityType {
    INVISIBILITY {
        @Override
        public void execute(Character user, Character target) {
            if (user instanceof Player) {
                ((Player)user).setDodgeNextAttack(true);
                System.out.printf("%s turns invisible, avoiding the next attack!%n", user.getCharacterName());
            }
        }
    },

    FIREBALL {
        @Override
        public void execute(Character user, Character target) {
            if (target != null) {
                int damage = user.getAttackDamage() * 2;
                target.receiveAttackDamage(damage);
                System.out.printf("%s casts a powerful fireball for %d damage!%n",
                        user.getCharacterName(), damage);
            }
        }
    },

    HEAL {
        @Override
        public void execute(Character user, Character target) {
            int healAmount = user.getMaxHealthPoints() / 3; // Heals 1/3 of max health
            user.restoreHealth(healAmount);
            System.out.printf("%s glows with healing light, recovering %d health!%n",
                    user.getCharacterName(), healAmount);
        }
    },

    SHIELD_BASH {
        @Override
        public void execute(Character user, Character target) {
            if (target != null) {
                int damage = user.getAttackDamage() + 5;
                target.receiveAttackDamage(damage);
                System.out.printf("%s bashes with their shield for %d damage!%n",
                        user.getCharacterName(), damage);
            }
        }
    },

    BERSERK {
        @Override
        public void execute(Character user, Character target) {
            if (target != null) {
                int damage = user.getAttackDamage() * 3;
                target.receiveAttackDamage(damage);
                System.out.printf("%s goes berserk, dealing %d massive damage!%n",
                        user.getCharacterName(), damage);
            }
        }
    },

    CLONE {
        @Override
        public void execute(Character user, Character target) {
            if (user instanceof Player) {
                ((Player)user).setDodgeNextAttack(true);
                System.out.printf("%s creates a confusing clone, avoiding the next attack!%n",
                        user.getCharacterName());
            }
        }
    },

    LIFESTEAL {
        @Override
        public void execute(Character user, Character target) {
            if (target != null) {
                int damage = user.getAttackDamage();
                target.receiveAttackDamage(damage);
                user.restoreHealth(damage / 2);
                System.out.printf("%s steals %d life from their target!%n",
                        user.getCharacterName(), damage);
            }
        }
    };

    /**
     * Executes the ability's effect
     * @param user Character using the ability
     * @param target Character being targeted (can be null for self-buffs)
     */
    public abstract void execute(Character user, Character target);
}