// Main file where the code will be run
import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        // set the dimensions of the game window
        int columnCount = 19;
        int rowCount = 21;
        int tileSize = 32;
        int boardWidth = columnCount * tileSize;
        int boardHeight = rowCount * tileSize;

        // create the game window
        JFrame frame = new JFrame("Pac Man Game");
        //frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add the JPanel to the game window
        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);
        frame.pack();
        pacmanGame.requestFocus();
        frame.setVisible(true);
    }
}