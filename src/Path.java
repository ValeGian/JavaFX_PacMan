
import java.util.*;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class Path{
    public LinkedList<GraphNode> path = new LinkedList<>();

    public int distance = 0;   //lenght of the path
    public int distToFinish = 0;
    public Vect2D velAtLast;

    private Vector<Node> pathRep;
    
    private int TILE_SIZE = GameBoard.TILE_SIZE;
    private GameBoard gameBoard; 
    
    //----------------------CONSTRUCTORS-------------------------------------------//
    
    public Path(GameBoard gb){
        gameBoard = gb;
    }

    //----------------------METHODS-------------------------------------------------//

    public void addToTail(GraphNode n, GraphNode endNode){
        if (!path.isEmpty()) {
          distance += Vect2D.dist(path.getLast().pos, n.pos);   //adds the distance (from the last current element in the path to the new node) to the total distance
        }

        path.add(n);    //adds the node
        distToFinish = Vect2D.dist(path.getLast().pos, endNode.pos);   //recalculate the distance from finish
    }

    //-------------------------------------------------------------------//

    public Path clone(){    //clones the path
        Path temp = new Path(gameBoard);
        temp.path = (LinkedList)path.clone();
        temp.distance = distance;
        temp.distToFinish = distToFinish;
        temp.velAtLast = new Vect2D(velAtLast.x, velAtLast.y);
        return temp;
    }

    //-------------------------------------------------------------------//

    public void clear(){    //removes all the nodes from the path
        distance =0;
        distToFinish = 0;
        path.clear();
    }

    //-------------------------------------------------------------------//

    public Vector<Node> getPathJavafxNode(Color color){
        pathRep = new Vector<>();

        for(int i = 0; i < path.size() - 1; ++i){
            Line tempL = new Line(path.get(i).pos.x + TILE_SIZE/2, path.get(i).pos.y + TILE_SIZE/2, path.get(i+1).pos.x + TILE_SIZE/2, path.get(i+1).pos.y + TILE_SIZE/2);
            tempL.setStroke(color);
            tempL.setStrokeWidth(2);
            pathRep.add(tempL);
        }

        return pathRep;
    }
}