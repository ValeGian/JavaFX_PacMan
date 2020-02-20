
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


class Inky extends Ghost{

//----------------------CONSTRUCTORS-------------------------------------------//    
    
    public Inky(GameBoard gb, int rad, int tile_size) {
        super(gb, new Vect2D(8*tile_size, 1*tile_size), new Vect2D(1, 0), Color.rgb(135, 206, 250), rad);
        ghostRepPos = new Vect2D(pos.x, pos.y + TILE_SIZE/2);
        ghostRep = new Circle(ghostRepPos.x, ghostRepPos.y, radius, Color.rgb(135, 206, 250));
    }
    
    public Inky(GameBoard gb) {
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
            if(chase){  //IT'S TARGET IR RELATIVE TO BLINKY'S POSITION
                Vect2D target = new Vect2D(2*gameBoard.pacman.pos.x/TILE_SIZE - gameBoard.blinky.pos.y/TILE_SIZE, 2*gameBoard.pacman.pos.x/TILE_SIZE - gameBoard.blinky.pos.y/TILE_SIZE);
                Vect2D nearestTile = gameBoard.getNearestNonWallTile(target);
                
                if(Vect2D.dist(this.pos, nearestTile) < 1*TILE_SIZE)
                    ghostNodes.add(new GraphNode(gameBoard, gameBoard.pacman.pos));
                else
                    ghostNodes.add(new GraphNode(gameBoard, nearestTile.x*TILE_SIZE, nearestTile.y*TILE_SIZE));
            }
            else
                ghostNodes.add(new GraphNode(gameBoard, 28*TILE_SIZE, 29*TILE_SIZE));
        }
        
        connectMapNodes();
    }
}
