
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.shape.Circle;


public class Pacman extends Character{
    
    private int lives = 2;  //default lives number
    
    private Vect2D turnTo = new Vect2D(-1, 0);
    private final int DEFAULT_TURN_COUNT = 5;
    private int turnCount = DEFAULT_TURN_COUNT;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private int scoreDOT = 0;
    private int score = 0;
    
    private Circle pacmanRep;   //pacman node representation
    
//----------------------DEBUG--------------------------------------------------//
    
    public Vect2D getPos() { return pos; } 
    public Vect2D getVel() { return vel; }
    public Vect2D getTurn() { return turnTo; }
    
//----------------------CONSTRUCTORS-------------------------------------------//
    
    public Pacman(GameBoard gb, int rad, int tile_size) {
        super(gb, new Vect2D(16*tile_size, 23*tile_size), new Vect2D(-1, 0), rad);
        pacmanRep = new Circle(pos.x, pos.y + TILE_SIZE/2, radius, Color.YELLOW);
    }
    
    //-------------------------------------------------------------------//
    
    public Pacman(GameBoard gb) {
        this(gb, DEFAULT_RADIUS, GameBoard.TILE_SIZE);
    }
    
//---------------------METHODS-------------------------------------------------//

    public void setTurnTo(int x, int y) { turnTo = new Vect2D(x, y); }
    public boolean gameWon() { return gameWon; }
    public boolean gameOver() { return gameOver; }
    
    //-------------------------------------------------------------------//
    
    @Override
    public Node getCharactertJavafxNode() {
        pacmanRep = new Circle(pos.x, pos.y + TILE_SIZE/2, radius, Color.YELLOW);
        return pacmanRep;
    }
    
    //-------------------------------------------------------------------//
    
    @Override
    public void move(){
        if(checkPosition()){ //if pacman is not blocked by a wall
            if(((pos.x)%TILE_SIZE == 0 && (pos.y)%TILE_SIZE == 0) && (gameBoard.getTileType(pos.x/TILE_SIZE + vel.x, pos.y/TILE_SIZE + vel.y) == BoardTile.TELEPORT)){
                pos = new Vect2D(gameBoard.getBoardXSize()*TILE_SIZE - pos.x, pos.y);
                pos.add(vel);
                pacmanRep.setCenterX(gameBoard.getBoardXSize()*TILE_SIZE - pos.x + TILE_SIZE/2);
                pacmanRep.setCenterY(pos.y + TILE_SIZE/2);
            }
            else{
                pos.add(vel);
                pacmanRep.setCenterX(pos.x + TILE_SIZE/2);
                pacmanRep.setCenterY(pos.y + TILE_SIZE/2);
            }
        }
        
        if(scoreDOT >= 250){
            gameWon = true;
        }
    }
    
    //-------------------------------------------------------------------//
    
    public boolean hit(Vect2D ghostPos){
        return (Vect2D.dist(this.pos, ghostPos) < TILE_SIZE/2);
    }
    
    //-------------------------------------------------------------------//
    
    public void kill(){
        lives -= 1;
        if(lives < 0)
            gameOver = true;
        else{
            pos = new Vect2D(16*TILE_SIZE, 23*TILE_SIZE);
            vel = new Vect2D(-1, 0);
            turnTo = new Vect2D(-1, 0);
            
            resetGhosts();
            //System.out.println("NEW GAME --- LIVES: " + lives);
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Pacman.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void resetGhosts(){
        gameBoard.blinky.pos = new Vect2D(15*TILE_SIZE, 11*TILE_SIZE);  gameBoard.blinky.vel = new Vect2D(1, 0);
        gameBoard.clyde.pos = new Vect2D(3*TILE_SIZE, 29*TILE_SIZE);  gameBoard.clyde.vel = new Vect2D(1, 0);
        gameBoard.inky.pos = new Vect2D(8*TILE_SIZE, 1*TILE_SIZE);  gameBoard.inky.vel = new Vect2D(1, 0);
        gameBoard.pinky.pos = new Vect2D(26*TILE_SIZE, 26*TILE_SIZE);  gameBoard.pinky.vel = new Vect2D(1, 0);
    }
    
    //-------------------------------------------------------------------//
    
    private boolean checkPosition(){    //returns true if pacman can move (if there is not a wall in the moving direction)

        if ((pos.x)%TILE_SIZE == 0 && (pos.y)%TILE_SIZE == 0){  
            
            Vect2D pacmanPosition = new Vect2D((pos.x)/TILE_SIZE, (pos.y)/TILE_SIZE); //pacman position in the matrix representation of the board
            Vect2D positionToCheck = new Vect2D(pacmanPosition.x + turnTo.x, pacmanPosition.y + turnTo.y);
            
            //resetta i percorsi per tutti i fantasmi 
            gameBoard.blinky.setPath();
            gameBoard.pinky.setPath();
            gameBoard.clyde.setPath();
            gameBoard.inky.setPath();
            
            BoardTile currentTile = gameBoard.getTileType(pacmanPosition.x, pacmanPosition.y);
            BoardTile toCheckTile = gameBoard.getTileType(positionToCheck.x, positionToCheck.y);
            switch(currentTile){
                
                case DOT:
                    gameBoard.setTileEmpty(pacmanPosition.x, pacmanPosition.y);
                    scoreDOT += 1;    //add a point
                    score += 1;
                    break;
                    
                case BIGDOT:
                    gameBoard.setTileEmpty(pacmanPosition.x, pacmanPosition.y);
                    scoreDOT += 2;    //add two points
                    score += 2;
                    
                    //setta tutti i fantasmi in frightened mode
                    gameBoard.blinky.frightened = true;
                    gameBoard.blinky.flashCount = 0;
                    gameBoard.clyde.frightened = true;
                    gameBoard.clyde.flashCount = 0;
                    gameBoard.pinky.frightened = true;
                    gameBoard.pinky.flashCount = 0;
                    gameBoard.inky.frightened = true;
                    gameBoard.inky.flashCount = 0;
            }
            
            //System.out.println(score);
            
            switch(toCheckTile){
                case WALL:
                    if(--turnCount == 0){
                        turnTo = vel;
                        turnCount = DEFAULT_TURN_COUNT;
                    }
                    if(gameBoard.getTileType(pacmanPosition.x + vel.x, pacmanPosition.y + vel.y) == BoardTile.WALL)
                        return false;
                    
                    return true;
                 
                default:
                    vel = new Vect2D(turnTo.x, turnTo.y);
                    return true;
            }
        }
        
        return true;
    }
    
}
