import java.awt.*; // imports all classes from this package
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener{

    class Block{
        int x;
        int y;
        int width;
        int height;
        Image image;
        
        // for the ghosts and Pacman (always have the same starting position)
        int startX;
        int startY;
        char direction = 'U'; // U D L R
        int velocityX = 0;
        int velocityY = 0;

        // constructor
        Block(Image image, int x, int y, int width, int height){
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

        void updateDirection(char direction){
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;

            for(Block wall : walls){

                if(collision(this, wall)){
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();

                }
            }
        }

        void updateVelocity(){

            if(this.direction == 'U'){
                this.velocityX = 0;
                this.velocityY = -tileSize/4;

            }else if(this.direction == 'D'){
                this.velocityX = 0;
                this.velocityY = tileSize/4;

            }else if(this.direction == 'L'){
                this.velocityX = -tileSize/4;
                this.velocityY = 0;

            }else if(this.direction == 'R'){
                this.velocityX = tileSize/4;
                this.velocityY = 0;

            }
        }
    }

    // set the dimensions of the JPanel (same size as game window)
    private int columnCount = 19;
    private int rowCount = 21;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    // variables for the images of the game
    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    // Tile map: array of strings
    //X = wall, O = skip, P = pac man, ' ' = food, b = blue ghost, o = orange ghost, p = pink ghost, r = red ghost
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };

    // declare/initialize the HashSets which are composed of "Blocks"
    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    Timer gameLoop;

    // constructor
    PacMan(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
 
        // load game images
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

        loadMap();

        // how long it takes to start the timer (miliseconds gone between frames)
        gameLoop = new Timer(50, this);
        gameLoop.start();
    }

    // function to create the map with the loaded images
    public void loadMap(){
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        for(int r = 0; r < rowCount; r++){
            for(int c = 0; c < columnCount; c++){
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c*tileSize;
                int y = r*tileSize;

                if(tileMapChar == 'X'){ // block wall
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);

                }else if(tileMapChar == 'b'){ // blue ghost
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);

                }else if(tileMapChar == 'o'){ // orange ghost
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);

                }else if(tileMapChar == 'p'){ // pink ghost
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);

                }else if(tileMapChar == 'r'){ // red ghost
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);

                }else if(tileMapChar == 'P'){ // Pacman
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                    ghosts.add(pacman);

                }else if(tileMapChar == ' '){ // Food
                    Block food = new Block(null, x + 14, y + 14, 4, 4); // so that it is in the middle
                    foods.add(food);

                }
            }
        }
    }

    // function
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        // draw Pacman
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        // draw the map walls
        for(Block wall : walls){
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        } 

        // draw the 4 ghosts
        for(Block ghost : ghosts){
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        // draw the food
        g.setColor(Color.WHITE);
        for(Block food : foods){
            g.fillRect(food.x, food.y, food.width, food.height);
        }
    }

    public void move(){
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        // check walls collisions
        for(Block wall : walls){

            if(collision(pacman, wall)){
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }
    }

    // collision function between pacman, ghsots, walls, food
    public boolean collision(Block a, Block b){

        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move(); // update the positions of pacman
        repaint(); // and then "paint" the game board
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        //System.out.println("KeyEvent: "+ e.getKeyCode());

        if(e.getKeyCode() == KeyEvent.VK_UP){
            pacman.updateDirection('U');

        }else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            pacman.updateDirection('D');
            
        }else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            pacman.updateDirection('L');
            
        }else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            pacman.updateDirection('R');
            
        }

        if(pacman.direction == 'U'){
            pacman.image = pacmanUpImage;

        }else if(pacman.direction == 'D'){
            pacman.image = pacmanDownImage;

        }else if(pacman.direction == 'L'){
            pacman.image = pacmanLeftImage;

        }else if(pacman.direction == 'R'){
            pacman.image = pacmanRightImage;

        }
    }

}
