package bearmaps;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;

public class TestNaivePoint {

   @Test
   public void testNaivePoint(){
       ArrayList<Point> points = new ArrayList<>(5);
       points.add(new Point(1.1,2.2));
       points.add(new Point(3.3,4.4));
       points.add(new Point(-2.9,4.2));
       NaivePointSet nn = new NaivePointSet(points);
       assertTrue(nn.nearest(3.0,4.0).equals(new Point(3.3,4.4)));
       assertTrue(nn.nearest(1.1,2.1).equals(new Point(1.1,2.2)));
       assertFalse(nn.nearest(-2.9,4.1).equals(new Point(1.0,2.0)));

  }








}
