
import javafx.scene.Node;


public abstract class Character {
    protected Vect2D pos; //character position
    protected Vect2D vel; //character velocity
    protected int TILE_SIZE = GameBoard.TILE_SIZE;
    protected GameBoard gameBoard;  //game board wich contains this character instantiation
    
    protected final static int DEFAULT_RADIUS = 12;   //character default dimension
    
    protected int radius;   //character dimension
    
    
//----------------------METHODS-------------------------------------------------//
    
    public abstract Node getCharactertJavafxNode();
    public abstract void move();
    
//----------------------CONSTRUCTORS-------------------------------------------//
    
    public Character(GameBoard gb, Vect2D pos, Vect2D vel, int rad){
        this.pos = pos;
        this.vel = vel;
        radius = rad;
        gameBoard = gb;
    }
    
    //-------------------------------------------------------------------//
    
    public Character(GameBoard gb, int posX, int posY, int velX, int velY, int rad){
        this(gb, new Vect2D(posX, posY), new Vect2D(velX, velY), rad);
    }
    
    //-------------------------------------------------------------------//
    
    public Character(GameBoard gb, int rad){
        this(gb, new Vect2D(), new Vect2D(), rad);
    }
}
