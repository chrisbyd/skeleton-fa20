package bearmaps;

import java.util.List;

public class KDTree {

    private class Node{
        private Boolean flag;
        private Node left, right;
        private Point value;
        public Node(Point value, Node left, Node right, Boolean flag){
            this.flag = flag;
            this.left = left;
            this.right = right;
            this.value =value;
        }
    }

    private Node root;

    public KDTree(){
        this.root = null;
    }

    public KDTree(List<Point> points){
        for (Point p : points){
            Node insertNode = new Node(p,null,null,true);
            this.insert(insertNode);
        }
    }

    public void insert(Node node){
        node.flag = false;
        this.root = insert(this.root,node);
    }

    public void insert(Point point){
        Node n = new Node(point,null,null,true);
        n.flag = false;
        this.root = insert(this.root, n);

    }

    public Node insert(Node thisNode,Node newnode){
        newnode.flag = !newnode.flag;
        if(thisNode == null) return newnode;
        else if(thisNode.value.equals(newnode.value)){
            return thisNode;
        }
        else if (thisNode.flag && ( newnode.value.getX()  < thisNode.value.getX())){
            thisNode.left = insert(thisNode.left, newnode);
            return thisNode;
        }
        else if (thisNode.flag && ( newnode.value.getX()  >=thisNode.value.getX())){
            thisNode.right = insert(thisNode.right, newnode);
            return thisNode;
        }
        else if(!thisNode.flag && ( newnode.value.getY()  < thisNode.value.getY())){
            thisNode.left = insert(thisNode.left,newnode);
            return thisNode;
        }
        else {
            thisNode.right = insert(thisNode.right, newnode);
            return thisNode;
        }
    }

    public Point nearest(double x, double y){
     Point goal = new Point(x,y);
     Node best = nearest(this.root, goal, this.root);
     return best.value;
    }

    private Node nearestNaive(Node n, Point goal, Node best){
        if (n == null) return best;
        if(Point.distance(n.value,goal) < Point.distance(goal,best.value)){
            best = n;
            best = nearest(n.left, goal, best);
            best = nearest(n.right, goal, best);
            return best;
        }else {
            best = nearest(n.left, goal, best);
            best = nearest(n.right, goal, best);
            return best;
        }
    }

    private Node nearest(Node n, Point goal, Node best){
        if (n== null) return best;
        Node goodSide = null;
        Node badSide = null;
        Point badSideBestPoint = null;
        if(Point.distance(n.value, goal) < Point.distance(goal,best.value)) best = n;
        if (n.flag && goal.getX() >= n.value.getX()) {
            goodSide = n.right;
            badSide = n.left;
            badSideBestPoint = new Point(n.value.getX(),goal.getY());
        }
        else if(n.flag && goal.getX() < n.value.getX()){
            goodSide = n.left;
            badSide = n.right;
            badSideBestPoint = new Point(n.value.getX(),goal.getY());

        }
        else if(!n.flag && goal.getY() >= n.value.getY()){
            goodSide = n.right;
            badSide = n.left;
            badSideBestPoint = new Point(goal.getX(), n.value.getY());
        }
        else {
            goodSide = n.left;
            badSide = n.right;
            badSideBestPoint = new Point(goal.getX(), n.value.getY());
        }
        best = nearest(goodSide, goal, best);
        if(Point.distance(badSideBestPoint, goal) < Point.distance(best.value, goal) ){
            best = nearest(badSide, goal, best);
        }
        return best;

    }


}
