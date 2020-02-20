
import java.util.*;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;



public class GameBoard {
    private int[][] tilesRepresentation = loadMap(1);  //game board tiles matrix
    
    private int BOARD_X_SIZE = tilesRepresentation[0].length;
    private int BOARD_Y_SIZE = tilesRepresentation.length;
    
    public static int TILE_SIZE = 16;
    
    //ACCESSING THIS MATRIX, THE y GOES BEFORE THE x due to the way I initialized the tilesRepresentation
    public Tile[][] tiles = new Tile[BOARD_Y_SIZE][BOARD_X_SIZE]; 
    
    public Vector<Node> gameBoardRep = null;
    
    public Pacman pacman;
    public Blinky blinky;
    public Clyde clyde;
    public Inky inky;
    public Pinky pinky;
//----------------------CONSTRUCTORS-------------------------------------------//
    
    public GameBoard(){
        
        for (int y = 0; y < tilesRepresentation.length; y++) {
            for (int x = 0; x < tilesRepresentation[y].length; x++) {
              switch(tilesRepresentation[y][x]) {
              case 1: //1 is a WALL
                tiles[y][x] = new Tile(BoardTile.WALL, TILE_SIZE*x, TILE_SIZE*y);
                break;
              case 0: // 0 is a DOT
                tiles[y][x] = new Tile(BoardTile.DOT, TILE_SIZE*x, TILE_SIZE*y);
                break;
              case 6://6 is an EMPTY space
                tiles[y][x] = new Tile(BoardTile.EMPTY, TILE_SIZE*x, TILE_SIZE*y);
                break;
              case 8: // 8 is a BIG DOT
                tiles[y][x] = new Tile(BoardTile.BIGDOT, TILE_SIZE*x, TILE_SIZE*y);
                break;
              case 9://9 is a TELEPORT
                tiles[y][x] = new Tile(BoardTile.TELEPORT, TILE_SIZE*x, TILE_SIZE*y);
                break;
              default:
                  tiles[y][x] = new Tile(BoardTile.EMPTY, 0, 0);
              }
            }
        }
    }
    
//---------------------METHODS-------------------------------------------------//    
    
    public int getTileSize() { return TILE_SIZE; } 
    public int getBoardXSize() { return BOARD_X_SIZE; }
    public int getBoardYSize() { return BOARD_Y_SIZE; }
    public BoardTile getTileType(int x, int y) { return tiles[y][x].getTileType(); }
    public boolean gameWon() { return pacman.gameWon(); }
    public boolean gameOver() { return pacman.gameOver(); }
    
    //-------------------------------------------------------------------//
    
    public void setTileEmpty(int x, int y){ 
        tiles[y][x].setTileType(BoardTile.EMPTY); 
        gameBoardRep.elementAt(y*BOARD_X_SIZE + x).setVisible(false);
    }
    
    //-------------------------------------------------------------------//
    
    public Vect2D getNearestNonWallTile(Vect2D target){
    float min = 1000000;
    int minIndexY = 0;
    int minIndexX = 0;
    for (int y = 0; y < tiles.length; y++) {   //for each tile
      for (int x = 0; x < tiles[y].length; x++) {
        if (tiles[y][x].tileType != BoardTile.WALL) {    //if not a wall
          if (Vect2D.dist(new Vect2D(x, y), target) < min) { //if it is the nearest target
            min =  Vect2D.dist(new Vect2D(x, y), target);
            minIndexY = y;
            minIndexX = x;
          }
        }
      }
    }
    //returns a Vect2D pointing to a tile
    return new Vect2D(minIndexX, minIndexY);
    }
    
    //-------------------------------------------------------------------//
    
    //returns a javafx list containing the javafx Node representations of the game board tiles
    public Vector<Node> getTileJavafxList(){   
        
        gameBoardRep = new Vector<>();
        for (Tile[] tile : tiles) {
            for (Tile tile1 : tile) {
                gameBoardRep.add(tile1.getTileJavafxNode());
            }
        }
        
        gameBoardRep.add(blinky.getCharactertJavafxNode()); //adding Blinky ghost
        gameBoardRep.add(clyde.getCharactertJavafxNode());  //adding Clyde ghost
        gameBoardRep.add(inky.getCharactertJavafxNode());   //adding Inky ghost
        gameBoardRep.add(pinky.getCharactertJavafxNode());  //adding Pinky ghost
        gameBoardRep.add(pacman.getCharactertJavafxNode()); //adding Pacman as last, so that it is showed above other character if collision
        
        
        //-------------VISUALIZING THE GRAPH NODES FOR DEBUGGING---------------//
        /*for(GraphNode node : clyde.ghostNodes){
            gameBoardRep.add(node.getGraphNodeJavafxNode());
        }*/
        
        return gameBoardRep;
    }
    
    //-------------------------------------------------------------------//
    
    public void setCharacter(Character ch) { 
        ch.TILE_SIZE = TILE_SIZE;
        
        if(ch instanceof Pacman)
            pacman = (Pacman) ch;
        else if(ch instanceof Blinky)
            blinky = (Blinky) ch;
        else if(ch instanceof Clyde)
            clyde = (Clyde) ch;
        else if(ch instanceof Inky)
            inky = (Inky) ch;
        else if(ch instanceof Pinky)
            pinky = (Pinky) ch;
    }
    
    //-------------------------------------------------------------------//
    
    public int[][] loadMap(int level){
        switch(level){
            case 1:
                int[][] temp = {     //32x31
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, 
                {1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1}, 
                {1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1}, 
                {1, 1, 1, 8, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 8, 1, 1, 1}, 
                {1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1}, 
                {1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1}, 
                {1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1}, 
                {1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1}, 
                {1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1}, 
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 6, 1, 1, 6, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1}, 
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 6, 1, 1, 6, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1}, 
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1}, 
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1}, 
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1}, 
                {9, 6, 6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 6, 9}, 
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1}, 
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1}, 
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1}, 
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1}, 
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1}, 
                {1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1}, 
                {1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1}, 
                {1, 1, 1, 8, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 8, 1, 1, 1}, 
                {1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1}, 
                {1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1}, 
                {1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1}, 
                {1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1}, 
                {1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1}, 
                {1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1}, 
                {1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1}, 
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};  
                
                return temp;
                
            default:
                return null;
        }
    }
    
//---------------------SUB_CLASSES---------------------------------------------//    
    
    public class Tile {
        private BoardTile tileType;
        private final Vect2D pos; //Tile position

        protected int TILE_SIZE;

        private Node tileRep = null;

    //----------------------CONSTRUCTORS-------------------------------------------//        

        public Tile(BoardTile tileType, Vect2D pos){
            this.tileType = tileType;
            this.pos = pos;
            TILE_SIZE = getTileSize();
        }
        
        //-------------------------------------------------------------------//

        public Tile(BoardTile tileType, int x, int y){
            this(tileType, new Vect2D(x, y));
        }

    //---------------------METHODS-------------------------------------------------//

        public int getX() { return pos.x; }
        public int getY() { return pos.y; }
        public BoardTile getTileType() { return tileType; }
        public void setTileType(BoardTile tileType) { this.tileType = tileType; }
        
        //-------------------------------------------------------------------//

        public Node getTileJavafxNode(){ //returns a javafx Node representatione of the tile
            if(tileType == BoardTile.DOT)
                tileRep = new Circle(pos.x + TILE_SIZE/2, pos.y + TILE_SIZE/2, 3, Color.YELLOW);
            else if(tileType == BoardTile.BIGDOT)
                tileRep = new Circle(pos.x + TILE_SIZE/2, pos.y + TILE_SIZE/2, 6, Color.YELLOW);
            else
                tileRep = new Circle(0, 0, 0, Color.TRANSPARENT);

            return tileRep;
        }
    }

}