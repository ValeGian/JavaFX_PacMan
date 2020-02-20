
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


class Blinky extends Ghost{

//----------------------CONSTRUCTORS-------------------------------------------//    
    
    public Blinky(GameBoard gb, int rad, int tile_size) {
        super(gb, new Vect2D(15*tile_size, 11*tile_size), new Vect2D(1, 0), Color.RED, rad);
        ghostRepPos = new Vect2D(pos.x + TILE_SIZE/2, pos.y + TILE_SIZE/2);
        ghostRep = new Circle(ghostRepPos.x, ghostRepPos.y, radius, Color.RED);
    }
    
    public Blinky(GameBoard gb) {
        this(gb, DEFAULT_RADIUS, GameBoard.TILE_SIZE);
    }
    
//---------------------METHODS-------------------------------------------------//
   
    @Override
    protected void setNodes() {
        ghostNodes.add(new GraphNode(gameBoard, pos.x , pos.y)); //adding current position
        
        setMapNodes();
        
        if(returnHome)  //if going back home, sets the target to home
            ghostNodes.add(new GraphNode(gameBoard, 15*TILE_SIZE, 11*TILE_SIZE));
        else{
            if(chase)   //HIS TARGET IS PACMAN
                ghostNodes.add(new GraphNode(gameBoard, gameBoard.pacman.pos));
            else
                ghostNodes.add(new GraphNode(gameBoard, 3*TILE_SIZE, 1*TILE_SIZE));
        }
        
        connectMapNodes();
    }
}
