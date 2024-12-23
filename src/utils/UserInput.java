package utils;

/**
 * Makes sure user input is clean and safe to use.
 */
public class UserInput {
    /**
     * Clean up user input by:
     * - Removing extra spaces
     * - Converting to lowercase
     * - Removing special characters
     */
    public static String clean(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Please type something!");
        }

        String cleaned = input.trim().toLowerCase();

        // Only allow letters, numbers and spaces
        // Reference on the regex: https://stackoverflow.com/questions/3028642/regular-expression-for-letters-numbers-and
        // Tested on: https://rubular.com/
        if (!cleaned.matches("[a-zA-Z0-9 ]+")) {
            throw new IllegalArgumentException("Please only use letters and numbers!");
        }

        return cleaned;
    }
}