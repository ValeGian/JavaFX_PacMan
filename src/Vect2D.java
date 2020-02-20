
import static java.lang.Math.sqrt;
import javafx.geometry.Point2D;


public class Vect2D {
    public int x = 0;
    public int y = 0;
    
//----------------------CONSTRUCTORS-------------------------------------------//
    
    public Vect2D(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    //-------------------------------------------------------------------//
    
    public Vect2D(){
        this(0, 0);
    }
    
//---------------------STATIC_METHODS------------------------------------------//
    
    public static int dist(Vect2D point1, Vect2D point2){
        return (int)sqrt((point1.x - point2.x)*(point1.x - point2.x) + (point1.y - point2.y)*(point1.y - point2.y));
    }
    
//---------------------METHODS-------------------------------------------------//
    
    public void add(Vect2D addV){
        x += addV.x;
        y += addV.y;
    }
    
    //-------------------------------------------------------------------//
    
    public void add(int addX, int addY){
        x += addX;
        y += addY;
    }
    
    //-------------------------------------------------------------------//
    
    public int magnitude(){
        return (int)sqrt(x*x + y*y);
    }
    
    //-------------------------------------------------------------------//
    
    public Vect2D normalize(){
        if(x > 0){
            x = 1;
            y = 0;
        }
        else if(x < 0){
            x = -1;
            y = 0;
        }
        else if(y > 0){
            y = 1;
            x = 0;
        }
        else if(y < 0){
            y = -1;
            x = 0;
        }
        else{
            x = 0;
            y = 0;
        }
                    
        
        return this;
    }
}
