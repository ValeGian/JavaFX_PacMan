
import static java.lang.Math.*;
import java.util.*;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GraphNode{
        
        public LinkedList<GraphNode> edges = new LinkedList<>();  //all the nodes connected to this instance
        
        public Vect2D pos;
        public int smallestDistToPoint = 10000000;  //shortest path distance from start to this node
        public int degree;
        public int value;
        public boolean checked = false;
        
        private Circle graphNodeRep;
        
        private static final int GN_RADIUS = 5;
        
        private int TILE_SIZE = GameBoard.TILE_SIZE;
        private GameBoard gameBoard; 
        
        //----------------------CONSTRUCTORS-------------------------------------------//  
        
        GraphNode(GameBoard gb, Vect2D pos) {
            gameBoard = gb;
            this.pos = pos;
            graphNodeRep = new Circle(pos.x + TILE_SIZE/2, pos.y + TILE_SIZE/2, GN_RADIUS, Color.rgb(0, 100, 100));
        }
        
        GraphNode(GameBoard gb, int posX, int posY) {
            this(gb, new Vect2D(posX, posY));
        }
        
        //----------------------METHODS-------------------------------------------------//
        
        public Node getGraphNodeJavafxNode(){
            return graphNodeRep;
        }
        
        //-------------------------------------------------------------------//
        
        public void addEdges(ArrayList<GraphNode> nodes) {
            for (GraphNode node : nodes) {
                if(gameBoard.tiles[this.pos.x/TILE_SIZE][this.pos.x/TILE_SIZE].getTileType() == BoardTile.TELEPORT
                  && gameBoard.tiles[node.pos.x/TILE_SIZE][node.pos.x/TILE_SIZE].getTileType() == BoardTile.TELEPORT){
                    edges.add(node);    //add the node to the adjacents
                }
                else if (node.pos.y == pos.y || node.pos.x == pos.x) {
                    if (node.pos.y == pos.y) {  //if the node is in the same orizzontal line
                        int mostLeft = min(node.pos.x, pos.x)/TILE_SIZE + 1;    //dividing by TILE_SIZE to get the index in the matrix representation of the board
                        int max = max(node.pos.x, pos.x)/TILE_SIZE;
                        boolean edge = true;
                        
                        while (mostLeft < max && mostLeft >= 0 && mostLeft < gameBoard.tiles[0].length) {    //check if there is a wall between
                            if (gameBoard.tiles[pos.y/TILE_SIZE][mostLeft].getTileType() == BoardTile.WALL) {
                                edge = false;   //not adjacent because there is a wall between
                                break;
                            }
                            mostLeft ++;
                        }     
                        
                        if (edge) {
                            edges.add(node);    //add the node to the adjacents
                        }
                    } else if (node.pos.x == pos.x) {   //if the node is in the same vertical line
                        int mostUp = min(node.pos.y, pos.y)/TILE_SIZE + 1;
                        int max = max(node.pos.y, pos.y)/TILE_SIZE;
                        boolean edge = true;
                        
                        while (mostUp < max && mostUp >= 0 && mostUp < gameBoard.tiles.length) {
                            if (gameBoard.tiles[mostUp][pos.x/TILE_SIZE].getTileType() == BoardTile.WALL) {
                                edge = false;
                                break;
                            }
                            mostUp ++;
                        }     
                        
                        if (edge) {
                            edges.add(node);
                        }
                    }
                }
            }
        }
    }