
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.*;


public class PacmanGame extends Application {
    
    private ImageView boardImage;
    private GameBoard gameBoard;
    private boolean end = false;
    
    @Override
    public void start(Stage stage) {
        
    //SETTING THE STAGE
        gameBoard = new GameBoard();
        final int TILE_SIZE = GameBoard.TILE_SIZE;
        final int WIDTH = gameBoard.getBoardXSize()*TILE_SIZE;
        final int HEIGHT = gameBoard.getBoardYSize()*TILE_SIZE;
        
        Group root = new Group();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle("PacMan Application"); 
        
        stage.setScene(scene); 
        
    //SETTING AND ADDING THE BOARD IMAGE
        boardImage = new ImageView("file:../../myFiles/map.jpg"); 
        boardImage.setY(0);
        boardImage.setX(2*TILE_SIZE);
        root.getChildren().add(boardImage);
                
    //SETTING PACMAN AND THE GHOSTS TO THE GAMEBOARD
        gameBoard.setCharacter(new Pacman(gameBoard));
        gameBoard.setCharacter(new Blinky(gameBoard));
        gameBoard.setCharacter(new Clyde(gameBoard));
        gameBoard.setCharacter(new Inky(gameBoard));
        gameBoard.setCharacter(new Pinky(gameBoard));
        
    //SETTING AND ADDING GHOSTS PATHS FOR DEBUGGING PURPOUSE
        /*Group pathsGroup = new Group();
        root.getChildren().add(pathsGroup);
        pathsGroup.getChildren().addAll(gameBoard.clyde.getCharacterPathJavafxNode());*/
        
    //SETTING AND ADDING THE GAME BOARD TILES 
        root.getChildren().addAll(gameBoard.getTileJavafxList());
        
    //SETTING THE BORDER COVGERING MASCK
        Rectangle rectLeft = new Rectangle(0, 0, 2*TILE_SIZE, HEIGHT); rectLeft.setFill(Color.BLACK);
        Rectangle rectRight = new Rectangle(WIDTH - 2*TILE_SIZE, 0, 2*TILE_SIZE, HEIGHT); rectLeft.setFill(Color.BLACK);
        root.getChildren().addAll(rectLeft, rectRight);
        
    //GAME LOOP
        AnimationTimer animator;
        animator = new AnimationTimer(){
            
            @Override
            public void handle(long currentNanoTime){
                
                if(gameWon()){
                    
                }
                else if(gameOver()){
                    System.out.println("GAME OVER");
                }
                else{
                    //MOVE AND SHOW THE CHARACTERS
                    //clearGhostsPaths(pathsGroup);
                    gameBoard.pacman.move();
                    //showGhostsPaths(gameBoard, pathsGroup);

                    gameBoard.blinky.show();
                    gameBoard.blinky.move();

                    gameBoard.clyde.show();
                    gameBoard.clyde.move();

                    gameBoard.inky.show();
                    gameBoard.inky.move();

                    gameBoard.pinky.show();
                    gameBoard.pinky.move();
                    
                    stage.show(); 
                }
            }
            
        };
        
        animator.start();
        
    //SETTING onKeyPressed EVENT LISTENER
        scene.setOnKeyPressed((KeyEvent event) -> {
            String key = event.getCode().toString();
            switch(key){
                case "W":
                case "UP":
                    gameBoard.pacman.setTurnTo(0, -1);
                    break;
                    
                case "D":
                case "RIGHT":
                    gameBoard.pacman.setTurnTo(1, 0);
                    break;
                    
                case "S":
                case "DOWN":
                    gameBoard.pacman.setTurnTo(0, 1);
                    break;
                    
                case "A":
                case "LEFT":
                    gameBoard.pacman.setTurnTo(-1, 0);
                    break;
            }
        });
        
    //SETTING onCloseRequest
        stage.setOnCloseRequest((WindowEvent we) -> {
            Platform.exit();
            System.exit(0);
        });
        
    }
    
    //-------------------------------------------------------------------//
    
    private boolean gameWon(){
        return gameBoard.gameWon();
    }
    
    //-------------------------------------------------------------------//
    
    private boolean gameOver(){
        return gameBoard.gameOver();
    }
    
    //-------------------------------------------------------------------//
    
    private void clearGhostsPaths(Group pathsGroup){
        pathsGroup.getChildren().clear();
    }
    
    //-------------------------------------------------------------------//
    
    private void showGhostsPaths(GameBoard gameBoard, Group pathsGroup){
        pathsGroup.getChildren().addAll(gameBoard.blinky.getCharacterPathJavafxNode());
        pathsGroup.getChildren().addAll(gameBoard.clyde.getCharacterPathJavafxNode());
        pathsGroup.getChildren().addAll(gameBoard.inky.getCharacterPathJavafxNode());
        pathsGroup.getChildren().addAll(gameBoard.pinky.getCharacterPathJavafxNode());
    }
    
//----------------------BEST_PATH_ALGORITHMS-----------------------------------//
    public static Path bestPathAlgorithm(GameBoard gb, GraphNode start, GraphNode finish, Vect2D vel){
        //best path algorothm implemented by using AStar algorithm for best path
        return AStar(gb, start, finish, vel);
    }
    
    //-------------------------------------------------------------------//
    
    public static Path AStar(GameBoard gb, GraphNode start, GraphNode finish, Vect2D vel){
        LinkedList<Path> big = new LinkedList<>();  //contains all the paths
        Path extend = new Path(gb); //a temp path has to be extended adding to it other nodes
        Path winningPath = new Path(gb);  //the final path
        Path extended = new Path(gb); //the extended path
        LinkedList<Path> sorting = new LinkedList<>();  //used to sort the paths based on their distance from the target

        extend.addToTail(start, finish);
        extend.velAtLast = new Vect2D(vel.x, vel.y);
        big.add(extend);


        boolean winner = false; //has it been found a path from start to finish?

        while (true) //repeats the procedure until when the ideal path is been found or there isn't a possible path
        {
          extend = big.pop();
          if (extend.path.getLast().equals(finish)) //if it finds the target
          {
            if (!winner) //if it is the first time that the target has been found, sets the winning path
            {
              winner = true;
              winningPath = extend.clone(); //takes the first path in big
            } else { //if the current path is shorter than the previous one
              if (winningPath.distance > extend.distance)
              {
                winningPath = extend.clone();   //sets this path as the winning one
              }
            }
            if (big.isEmpty()) //if the current path is the last one, returns the winner
            {
              return winningPath.clone();
            } else {
              extend = big.pop();   //discards the path
            }
          } 


          //if the final node in the path is already been checked and the distance from it is shorter than the distance that this path expended to arrive to that node then this path isn't good
          if (!extend.path.getLast().checked || extend.distance < extend.path.getLast().smallestDistToPoint)
          {     
            if (!winner || extend.distance + Vect2D.dist(extend.path.getLast().pos, finish.pos)  < winningPath.distance)
            {
              //if this is the first path reaching this node or the shortest path reaching it then set the smallest distance to this point as the lenght of the current path
              extend.path.getLast().smallestDistToPoint = extend.distance;

              //clones all the paths in sorting from big and adds to them the new paths (in the for loop) and puts them back in big sorted
              sorting = (LinkedList)big.clone();
              GraphNode tempN = new GraphNode(gb, 0, 0);    //resets the temp node
              if (extend.path.size() >1) {
                tempN = extend.path.get(extend.path.size() -2); //sets the temp node to be the second-last in the path
              }

              for (int i =0; i< extend.path.getLast().edges.size(); i++)    //for each node adjacent to the last node of the path that has to be extended
              {
                if (tempN != extend.path.getLast().edges.get(i))    //if the direction isn't backwards (the new node isn't the previous one)
                {     

                  Vect2D directionToNode = new Vect2D( extend.path.getLast().edges.get(i).pos.x -extend.path.getLast().pos.x, extend.path.getLast().edges.get(i).pos.y - extend.path.getLast().pos.y );
                  directionToNode.normalize();
                  if (directionToNode.x == -1* extend.velAtLast.x && directionToNode.y == -1* extend.velAtLast.y ) {
                  } else {
                    extended = extend.clone();
                    extended.addToTail(extend.path.getLast().edges.get(i), finish);
                    extended.velAtLast = new Vect2D(directionToNode.x, directionToNode.y);
                    sorting.add(extended.clone());  //add this extended list to the path's list to be sorted
                  }
                }
              }

              //sorting now contains all of the paths from big plus the new ones that have to be extended
              //adding as first the paths that had the bigger size from big so that they go in big's bottom
              //apply a Selection Sort
              big.clear();
              while (!sorting.isEmpty())
              {
                float max = -1;
                int iMax = 0;
                for (int i = 0; i < sorting.size(); i++)
                {
                  if (max < sorting.get(i).distance + sorting.get(i).distToFinish)  //A* (the used algorithm) uses the distance from the target and the lenght of the path to determine the sorting order
                  {
                    iMax = i;
                    max = sorting.get(i).distance + sorting.get(i).distToFinish;
                  }
                }
                big.addFirst(sorting.remove(iMax).clone()); //add it on the top so that the farther will end on the bottom and the nearest on the top
              }
            }
            extend.path.getLast().checked = true;
          }
          //if there are no other paths to be checked
          if (big.isEmpty()) {
            if (winner == false) //if there isn't a valid path
            {
              System.out.println("AStar ERROR!!!"); //error message
              return null;
            } else {
              return winningPath.clone();
            }
          }
        }
    }
}
