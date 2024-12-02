package utils;

import world.Direction;

/**
 * Validates all user input.
 */
public class InputValidator {
    private static final int MAX_INPUT_LENGTH = 20;
    /**
     * I am adding the reference link for this regex here:
     * https://stackoverflow.com/questions/3028642/regular-expression-for-letters-numbers-and
     * I tested it using this website:
     * https://rubular.com/
     */
    private static final String VALID_INPUT_PATTERN = "^[a-zA-Z0-9 ]+$";

    /**
     * Validates raw input.
     * @throws IllegalArgumentException if input is invalid
     */
    public static String validateInput(String input) {
        // Check null or empty
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Hey, you need to type something!");
        }

        String cleanInput = input.trim();

        // Check length
        if (cleanInput.length() > MAX_INPUT_LENGTH) {
            throw new IllegalArgumentException(
                    "That's too long bro! Keep it under " + MAX_INPUT_LENGTH + " characters.");
        }

        // Check characters
        if (!cleanInput.matches(VALID_INPUT_PATTERN)) {
            throw new IllegalArgumentException(
                    "Stick to letters and numbers please!");
        }

        return cleanInput;
    }

    /**
     * Validates a direction input.
     */
    public static Direction validateDirection(String direction) {
        if (direction == null || direction.trim().isEmpty()) {
            throw new IllegalArgumentException("Please specify a direction.");
        }

        try {
            return Direction.valueOf(direction.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid direction. Use: NORTH, SOUTH, EAST, or WEST");
        }
    }
}