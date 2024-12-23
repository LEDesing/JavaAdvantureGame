package items;

/**
 * Defines the different types of items available in the game.
 * Each item type has basic and valuable versions with different power levels.
 */
public enum ItemsType {
    /**
     * Restores health to the player.
     * Basic version restores 20 health.
     * Valuable version restores 50 health.
     */
    HEALTH_POTION(
            "Health Potion",
            "Restores health when consumed",
            20,
            50
    ) {
        @Override
        public String getDisplayName(int value) {
            return value >= getValuablePower() ? "Large Health Potion" : "Small Health Potion";
        }
    },

    /**
     * Increases attack damage temporarily.
     * Basic version increases damage by 30%.
     * Valuable version increases damage by 75%.
     */
    DAMAGE_POTION(
            "Damage Potion",
            "Temporarily increases attack damage",
            30,
            75
    ) {
        @Override
        public String getDisplayName(int value) {
            return value >= getValuablePower() ? "Strong Damage Potion" : "Damage Potion";
        }
    },

    /**
     * Allows dodging incoming attacks.
     * Basic version allows dodging one attack.
     * Valuable version allows dodging two attacks.
     */
    DODGE_POTION(
            "Dodge Potion",
            "Grants ability to dodge attacks",
            1,
            2
    ) {
        @Override
        public String getDisplayName(int value) {
            return value >= getValuablePower() ? "Greater Dodge Potion" : "Dodge Potion";
        }
    };

    private static final int VALUABLE_THRESHOLD = 50;
    private final String baseName;
    private final String description;
    private final int basicPower;
    private final int valuablePower;

    /**
     * Creates a new item type with specified properties.
     * @param baseName The base name of the item type
     * @param description Description of what the item does
     * @param basicPower Power level for basic version
     * @param valuablePower Power level for valuable version
     */
    ItemsType(String baseName, String description, int basicPower, int valuablePower) {
        this.baseName = baseName;
        this.description = description;
        this.basicPower = basicPower;
        this.valuablePower = valuablePower;
    }

    /**
     * Gets the display name based on item's power value.
     * @param value The power value of the item
     * @return The appropriate display name indicating item quality
     */
    public abstract String getDisplayName(int value);

    /**
     * Gets the base name of the item type.
     * @return The base name
     */
    public String getName() {
        return baseName;
    }

    /**
     * Gets the description of what the item does.
     * @return The item description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the power level for basic version.
     * @return The basic power value
     */
    public int getBasicPower() {
        return basicPower;
    }

    /**
     * Gets the power level for valuable version.
     * @return The valuable power value
     */
    public int getValuablePower() {
        return valuablePower;
    }
}