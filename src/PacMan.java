import java.awt.*; // imports all classes from this package
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel{
    // set the dimensions of the JPanel (same size as game window)
    private int columnCount = 19;
    private int rowCount = 21;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    // constructor
    PacMan(){

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
    }
}
