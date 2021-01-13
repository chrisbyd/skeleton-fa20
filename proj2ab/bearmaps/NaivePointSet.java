package bearmaps;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class NaivePointSet implements PointSet{
    private List<Point> points;
    /**
     * The constructor which takes a list of points as input and store them
     * **/
    public NaivePointSet(List<Point> points){
        this.points = points;
    }

    public NaivePointSet(){
        this.points = new ArrayList<>(1000);
    }

    public void insert(Point p){
        this.points.add(p);
    }


    @Override
    public Point nearest(double x, double y) {
        double shortest = Double.MAX_VALUE;
        Point nearest = null;
        Point p = new Point(x,y);
        for(Point item : this.points){
            if(Point.distance(item,p) < shortest)
            {
                shortest = Point.distance(item,p);
                nearest = item;
            }
        }
        return nearest;
    }
}
