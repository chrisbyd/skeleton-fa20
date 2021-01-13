package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class KDTreeTest {
    @Test
    public void testNearest(){
        List<Point> points = new ArrayList<>(6);
        points.add(new Point(1.1,1.1));
        points.add(new Point(1.1,2.2));
        points.add(new Point(4.1,1.1));
        points.add(new Point(4.1,2.2));
        points.add(new Point(3.1,1.1));
        points.add(new Point(3.1,2.2));

        NaivePointSet correctNearest = new NaivePointSet(points);
        KDTree kdt = new KDTree(points);

        assertEquals(correctNearest.nearest(0.0,1.1),kdt.nearest(0.0,1.1));
        assertEquals(correctNearest.nearest(0.0,2.3),new Point(1.1,2.2));
        assertEquals(correctNearest.nearest(0.0,2.3),kdt.nearest(0.0,2.3));
        assertEquals(correctNearest.nearest(3.1,1.0),kdt.nearest(3.1,1.0));
        assertEquals(correctNearest.nearest(4.1,1.0),kdt.nearest(4.1,1.1));
        assertEquals(correctNearest.nearest(5.1,3.3),kdt.nearest(5.1,3.3));
        assertEquals(correctNearest.nearest(4.1,1.2),kdt.nearest(4.1,1.2));

    }

    private static void printTimingTable(List<Integer> Ns, List<Double> times, List<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    @Test
    public void main(){
        long RANDOM_SEED = (long) 1.22445555;
        Random rand = new Random(RANDOM_SEED);
        List<Point> points = new ArrayList<>(6);
        points.add(new Point(2.0,3.0));
        points.add(new Point(4.0,2.0));
        points.add(new Point(4.0,2.0));
        points.add(new Point(4.0,5.0));
        points.add(new Point(3.0,3.0));
        points.add(new Point(1.0,5.0));

       // NaivePointSet correctNearest = new NaivePointSet(points);
        KDTree kdt = new KDTree(points);
        NaivePointSet correctNearest = new NaivePointSet(points);
        assertEquals(kdt.nearest(0.0,7.0),correctNearest.nearest(0.0,7.0));
        assertEquals(kdt.nearest(0.0,7.0),new Point(1.0,5.0));

        KDTree kdtt = new KDTree();
        int startNum = 31250;

        // test for kd-tree construction
        System.out.printf("Timing table for kd-Tree Construction\n");
        ArrayList<Integer> ns = new ArrayList<>();
        ArrayList<Double> times = new ArrayList<>();
        ArrayList<Integer> opCounts = new ArrayList<>();

        ArrayList<Point> npoints = new ArrayList<>(5000);
        for(int i =0; i< 1000000; i++){
            Point np = new Point(rand.nextDouble(),rand.nextDouble());
            npoints.add(np);
            }
        NaivePointSet targetSolver = new NaivePointSet(npoints);
        int count = 0;

        Stopwatch sw = new Stopwatch();
        for (Point p : npoints)
        {
            kdtt.insert(p);
            if (count == startNum) {
                ns.add(count);
                times.add(sw.elapsedTime());
                opCounts.add(count);
                startNum = startNum * 2;

            }
            count += 1;
        }

        printTimingTable(ns,times,opCounts);
        //test for kd-tree nearest
        npoints.clear();
        ns.clear();
        times.clear();
        opCounts.clear();
        startNum = 125;
        sw = new Stopwatch();
        for(int i=0; i<2000; i++){
            Point np = new Point(rand.nextDouble(),rand.nextDouble());
            npoints.add(np);
            if(i== startNum){
                NaivePointSet solver = new NaivePointSet(npoints);
                opCounts.add(1000000);
                ns.add(i);
                for (int j=0; j<1000000; j++){
                    Point ret = solver.nearest(rand.nextDouble(),rand.nextDouble());
                }
                times.add(sw.elapsedTime());
                startNum = startNum*2;
            }

        }
        printTimingTable(ns,times,opCounts);

        //timiong test for kdtree

        npoints.clear();
        ns.clear();
        times.clear();
        opCounts.clear();
        startNum = 31250;
        sw = new Stopwatch();
        for(int i=0; i<1000000; i++){
            Point np = new Point(rand.nextDouble(),rand.nextDouble());
            npoints.add(np);
            if(i== startNum){
                KDTree solver = new KDTree(npoints);
                opCounts.add(1000000);
                ns.add(i);
                for (int j=0; j<1000000; j++){
                    Point ret = solver.nearest(rand.nextDouble(),rand.nextDouble());
                }
                times.add(sw.elapsedTime());
                startNum = startNum*2;
            }

        }
        printTimingTable(ns,times,opCounts);



    // test correctiveness of kddtree using the naivePointSet

       npoints = new ArrayList<>(1000);
        for(int i =0; i< 1000000; i++){
            Point np = new Point(rand.nextDouble(),rand.nextDouble());
            npoints.add(np);
        }


    }

    @Test
    public void randomizedTest(){
        List<Point> points = new ArrayList<>(1000);
        Random rand = new Random();
        for(int i =0; i< 1000000; i++){
            Point np = new Point(rand.nextDouble(),rand.nextDouble());
            points.add(np);
        }
        KDTree kd = new KDTree(points);
        NaivePointSet target = new NaivePointSet(points);
        for (int i = 0; i<10000; i++){
            double x = rand.nextDouble();
            double y = rand.nextDouble();
            assertEquals(kd.nearest(x,y),target.nearest(x,y));
        }
    }
}
