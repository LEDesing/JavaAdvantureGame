import game.Game;

/**
 * Main entry point for the Labyrinth of VUB game.
 * This class initializes and starts the game session.
 */
public class Main {

    /**
     * Main method that starts the game.
     * Handles any unexpected errors during gameplay.
     *
     * @param args Command line arguments (not used)
     */

    public static void main(String[] args) {
        try{
            Game game  = new Game();
            game.start();
        } catch(Exception e){
            System.out.println("Error message : " + e.getMessage());
        }
    }
}