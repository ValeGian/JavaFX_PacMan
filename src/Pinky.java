
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


class Pinky extends Ghost{

//----------------------CONSTRUCTORS-------------------------------------------//    
    
    public Pinky(GameBoard gb, int rad, int tile_size) {
        super(gb, new Vect2D(26*tile_size , 26*tile_size), new Vect2D(1, 0), Color.PINK, rad);
        ghostRepPos = new Vect2D(pos.x, pos.y + TILE_SIZE/2);
        ghostRep = new Circle(ghostRepPos.x, ghostRepPos.y, radius, Color.PINK);
    }
    
    public Pinky(GameBoard gb) {
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
            if(chase){  //TARGET 4 POSITIONS AHEAD OF PACMAN
                int lookAhead = 4;
                Vect2D pacmanMatrixPosAhead = new Vect2D(gameBoard.pacman.pos.x/TILE_SIZE + gameBoard.pacman.vel.x*lookAhead, gameBoard.pacman.pos.y/TILE_SIZE + gameBoard.pacman.vel.y*lookAhead);
                
                while(!(pacmanMatrixPosAhead.x > 2 && pacmanMatrixPosAhead.y > 0
                        && pacmanMatrixPosAhead.x < 30 && pacmanMatrixPosAhead.y < 31
                        && gameBoard.tiles[pacmanMatrixPosAhead.y][pacmanMatrixPosAhead.x].getTileType() != BoardTile.WALL)){
                    
                    lookAhead -= 1;
                    pacmanMatrixPosAhead = new Vect2D(gameBoard.pacman.pos.x/TILE_SIZE + gameBoard.pacman.vel.x*lookAhead, gameBoard.pacman.pos.y/TILE_SIZE + gameBoard.pacman.vel.y*lookAhead);
                }
                
                if(Vect2D.dist(this.pos, pacmanMatrixPosAhead) < 1*TILE_SIZE)
                    ghostNodes.add(new GraphNode(gameBoard, gameBoard.pacman.pos));
                else
                    ghostNodes.add(new GraphNode(gameBoard, pacmanMatrixPosAhead.x*TILE_SIZE, pacmanMatrixPosAhead.y*TILE_SIZE));
            }
            else
                ghostNodes.add(new GraphNode(gameBoard, 28*TILE_SIZE, 1*TILE_SIZE));
        }
        
        connectMapNodes();
    }
}
