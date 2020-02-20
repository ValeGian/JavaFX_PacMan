
import static java.lang.Math.*;
import java.util.*;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;


public abstract class Ghost extends Character{
    protected Color color;
    protected boolean chase = true;
    protected boolean frightened = false;
    protected boolean returnHome = false;
    protected boolean deadForABit = false;
    protected int chaseCount = 0;
    protected int flashCount = 0;
    protected int deadCount = 0;
    
    protected Path bestPath;    //path of the ghost
    protected ArrayList<GraphNode> ghostNodes = new ArrayList<>();//nodes composing ghost's path
    protected GraphNode start;   //ghost's position
    protected GraphNode end;     //target's position
    
    protected Circle ghostRep;
    protected Vect2D ghostRepPos;
    
    protected Vect2D homePos = new Vect2D(15*TILE_SIZE, 11*TILE_SIZE);  //ghost's base home
    
//----------------------CONSTRUCTORS-------------------------------------------//
    
    public Ghost(GameBoard gb, Vect2D pos, Vect2D vel, Color color){
        super(gb, pos, vel, DEFAULT_RADIUS);
        this.color = color;
        setPath();
    }
    
    public Ghost(GameBoard gb, Vect2D pos, Vect2D vel, Color color, int rad){
        super(gb, pos, vel, rad);
        this.color = color;
        setPath();
    }
    
//---------------------METHODS-------------------------------------------------//
    
    @Override
    public Node getCharactertJavafxNode(){
        ghostRep = new Circle(ghostRepPos.x, ghostRepPos.y, radius, Color.ORANGE);
        return ghostRep;
    }
    
    public Vector<Node> getCharacterPathJavafxNode(){
        return bestPath.getPathJavafxNode(color);
    }
    
    //-------------------------------------------------------------------//
    
    public void show(){
        ghostRepPos = new Vect2D(pos.x + TILE_SIZE/2, pos.y + TILE_SIZE/2);
        ghostRep.setCenterX(ghostRepPos.x);
        ghostRep.setCenterY(ghostRepPos.y);
              
        chaseCount ++;
        if (chase) {
          if (chaseCount > 2000) {
            chase = false;  
            chaseCount = 0;
          }
        } else {
          if (chaseCount > 700) {
            chase = true;
            chaseCount = 0;
          }
        }
        
        if (deadForABit) {
          deadCount ++;
          if (deadCount > 300) {
            deadForABit = false;
          }
        } else { //if not dead, show it
          if (!frightened) {
            ghostRep.setFill(color);
            ghostRep.setOpacity(1);
            
            if (returnHome) {   //if going back home, it's transparent
                ghostRep.setOpacity(0.5);
            }
          } else {  //if frightened
            ghostRep.setFill(Color.rgb(0, 0, 200));
            flashCount ++;
            if (flashCount > 600) { //after 600 frames, the ghost is not frightned anymore
              frightened = false;
              flashCount = 0;
            }

            if (floor(flashCount / 15)%2 ==0)    //it makes the ghost flash each 20 frames
              ghostRep.setOpacity(0);
            else 
                ghostRep.setOpacity(1);
            }
        }
    }
    
    //-------------------------------------------------------------------//
    
    @Override
    public void move(){
        if (!deadForABit) {
            if(((pos.x)%TILE_SIZE == 0 && (pos.y)%TILE_SIZE == 0) && (gameBoard.getTileType(pos.x/TILE_SIZE + vel.x, pos.y/TILE_SIZE + vel.y) == BoardTile.TELEPORT)){
                pos = new Vect2D(gameBoard.getBoardXSize()*TILE_SIZE - pos.x, pos.y);
                pos.add(vel);
                ghostRep.setCenterX(gameBoard.getBoardXSize()*TILE_SIZE - pos.x + TILE_SIZE/2);
                ghostRep.setCenterY(pos.y + TILE_SIZE/2);
            }
            else{
                pos.add(vel);
                ghostRep.setCenterX(pos.x + TILE_SIZE/2);
                ghostRep.setCenterY(pos.y + TILE_SIZE/2);
            }
            checkDirection();
          }
    }
    
    //-------------------------------------------------------------------//
    
    //calculates a path from the first node in ghostNodes to the last node in ghostNodes and sets it as the best path
    protected void setPath(){   
        ghostNodes.clear();
        setNodes();
        start  = ghostNodes.get(0);
        end = ghostNodes.get(ghostNodes.size()-1);
        
        //----------DEBUG------------//
        /*System.out.println("PACMAN POS: (" + gameBoard.pacman.pos.x/TILE_SIZE + ", " + gameBoard.pacman.pos.y/TILE_SIZE + ")");
        System.out.println("GHOST POS: (" + pos.x/TILE_SIZE + ", " + pos.y/TILE_SIZE + ")");
        System.out.println("START POS: (" + start.pos.x/TILE_SIZE + ", " + start.pos.y/TILE_SIZE + ")");
        System.out.println("END POS: (" + end.pos.x/TILE_SIZE + ", " + end.pos.y/TILE_SIZE + ")");*/
        //---------------------------//
        
        Path temp = PacmanGame.bestPathAlgorithm(gameBoard, start, end, vel);
        if (temp != null) {  //if no path is been found, then do not change the best path
          bestPath = temp.clone();
        }
    }
    
    //-------------------------------------------------------------------//
    
    //sets all the nodes and their adjacents
    //also sets the target node
    protected abstract void setNodes();
    
    //sets all the map nodes
    protected void setMapNodes(){
        //checking each tile of the map
        for(int y = 1; y < gameBoard.tiles.length - 1; y++) {
            for(int x = 1; x < gameBoard.tiles[y].length - 1; x++) {
            //if the current tile isn't a wall and there isn't a WALL tile above or below, on the right or on the left, this tile is a graph node
                if(gameBoard.tiles[x][y].getTileType() == BoardTile.TELEPORT)
                    ghostNodes.add(new GraphNode(gameBoard, y*TILE_SIZE, x*TILE_SIZE));
                if(gameBoard.tiles[x][y].getTileType() != BoardTile.WALL)
                    //checking above and below
                    if(gameBoard.tiles[x-1][y].getTileType() != BoardTile.WALL || gameBoard.tiles[x+1][y].getTileType() != BoardTile.WALL)
                        //checking on the right and on the left
                        if(gameBoard.tiles[x][y-1].getTileType() != BoardTile.WALL || gameBoard.tiles[x][y+1].getTileType() != BoardTile.WALL)
                            ghostNodes.add(new GraphNode(gameBoard, y*TILE_SIZE, x*TILE_SIZE));
            }
        }
        //----------DEBUG------------//
        /*for(GraphNode node : ghostNodes){
            System.out.println("ADDING NODE: (" + node.pos.x/TILE_SIZE + ", " + node.pos.y/TILE_SIZE + ")");
        }
        System.out.println("------FINISHED ADDING " + ghostNodes.size() + " NODES------");*/
    }
    
    //connects all the map nodes together
    protected void connectMapNodes(){
        for(GraphNode node : ghostNodes)
            node.addEdges(ghostNodes);
    }
    //-------------------------------------------------------------------//
    
    //check if the ghost needs to change direction
    protected void checkDirection(){
        if (gameBoard.pacman.hit(pos)) {  //if the ghost hits PacMan
            if (frightened) {   //if eaten by PacMan
              returnHome = true;
              frightened = false;
            } else if (!returnHome) {   //kill PacMan
              gameBoard.pacman.kill();
            }
          }


          //checks if the ghost returned home
          if (returnHome) {
            if (Vect2D.dist(this.pos, homePos) < TILE_SIZE/2) {
              //sets the ghost as dead for a bit
              returnHome = false;
              deadForABit = true;
              deadCount = 0;
            }
          }

          if ((pos.x)%TILE_SIZE == 0 && (pos.y)%TILE_SIZE == 0) {

            Vect2D matrixPosition = new Vect2D((pos.x)/TILE_SIZE, (pos.y)/TILE_SIZE);   //ghost position in the matrix representation of the board

            if (frightened) {   //no path has to be generated if in frightened mode
              boolean isNode = false;
                for (GraphNode ghostNode : ghostNodes) {
                    if (matrixPosition.x == (ghostNode.pos.x)/TILE_SIZE && matrixPosition.y == (ghostNode.pos.y)/TILE_SIZE) {
                        isNode = true;
                    }
                }
              if (isNode) {
                //sets a random velocity (direction)
                Vect2D newVel = new Vect2D();
                int rand = (int)(Math.random()*4);
                switch(rand) {
                case 0:
                  newVel = new Vect2D(1, 0);
                  break;
                case 1:
                  newVel = new Vect2D(0, 1);
                  break;
                case 2:
                  newVel = new Vect2D(-1, 0);
                  break;
                case 3:
                  newVel = new Vect2D(0, -1);
                  break;
                }
                //if the random velocity goes towards a wall or backwards, chose another one
                while (gameBoard.tiles[(matrixPosition.y + newVel.y)][(matrixPosition.x + newVel.x)].getTileType() == BoardTile.WALL 
                        || (newVel.x + 2*vel.x == 0 && newVel.y + 2*vel.y == 0)) {
                  rand = (int)(Math.random()*4);
                  switch(rand) {
                  case 0:
                    newVel = new Vect2D(1, 0);
                    break;
                  case 1:
                    newVel = new Vect2D(0, 1);
                    break;
                  case 2:
                    newVel = new Vect2D(-1, 0);
                    break;
                  case 3:
                    newVel = new Vect2D(0, -1);
                    break;
                  }
                }
                vel = newVel;
              }
            } else {    //not frightened

              setPath();

              for (int i = 0; i < bestPath.path.size() - 1; i++) {
                //if the ghost is on a node, it has to go towards the next node in the path
                if (matrixPosition.x ==  bestPath.path.get(i).pos.x/TILE_SIZE && matrixPosition.y == bestPath.path.get(i).pos.y/TILE_SIZE) {
                  vel = new Vect2D(bestPath.path.get(i+1).pos.x/TILE_SIZE - matrixPosition.x, bestPath.path.get(i+1).pos.y/TILE_SIZE - matrixPosition.y);
                  vel.normalize();
                  
                    if(vel.x == 0 && vel.y == 0){
                        Vect2D newVel = new Vect2D();
                        int rand = (int)(Math.random()*4);
                        switch(rand) {
                        case 0:
                          newVel = new Vect2D(1, 0);
                          break;
                        case 1:
                          newVel = new Vect2D(0, 1);
                          break;
                        case 2:
                          newVel = new Vect2D(-1, 0);
                          break;
                        case 3:
                          newVel = new Vect2D(0, -1);
                          break;
                        }
                        //if the random velocity goes towards a wall or backwards, chose another one
                        while (gameBoard.tiles[(matrixPosition.y + newVel.y)][(matrixPosition.x + newVel.x)].getTileType() == BoardTile.WALL 
                                || (newVel.x + 2*vel.x == 0 && newVel.y + 2*vel.y == 0)) {
                          rand = (int)(Math.random()*4);
                          switch(rand) {
                          case 0:
                            newVel = new Vect2D(1, 0);
                            break;
                          case 1:
                            newVel = new Vect2D(0, 1);
                            break;
                          case 2:
                            newVel = new Vect2D(-1, 0);
                            break;
                          case 3:
                            newVel = new Vect2D(0, -1);
                            break;
                          }
                        }
                        vel = newVel;
                    }
                }
              }
            }
        }
    } 
}
