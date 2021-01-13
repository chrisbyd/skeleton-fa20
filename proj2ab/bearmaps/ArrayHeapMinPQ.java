package bearmaps;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    private class Node{
        private T item;
        private double priority;
        private int index;
        private Node left,right;

        public Node(T item, double priority){
            this.item  = item;
            this.priority = priority;
            this.left = null;
            this.right = null;
            this.index = -2;
        }

        public void setPriority(double priority){
            this.priority = priority;
        }

        public int getIndex(){
            return this.index;
        }

    }

    private class Bst{
        private Node root;

        public Bst(){
            this.root = null;
        }

        public void insert(Node node){
            this.root = insert(this.root,node);
        }

        public Node insert(Node thisNode,Node toInsert){
            if(thisNode == null) return toInsert;
            if (((Comparable<T>) toInsert.item).compareTo(thisNode.item) >= 0){
                thisNode.right = insert(thisNode.right, toInsert);
                return thisNode;
            }else {
                thisNode.left = insert(thisNode.left, toInsert);
                return thisNode;
            }
        }

        public Node contains(T item){
           return this.contains(this.root,item);
        }

        public Node contains(Node thisNode, T item){
            if(thisNode == null) return null;
            if (((Comparable<T>) thisNode.item).compareTo(item) == 0){
                return thisNode;
            }else if(((Comparable<T>) thisNode.item).compareTo(item) >0){
                return contains(thisNode.left,item);
            }else {
                return contains(thisNode.right,item);
            }
        }

        public void delete(T item){
            if (this.contains(item) == null) throw new IllegalArgumentException("item is not contained in the tree");
            else {
                this.root = delete(this.root,item);
            }
        }

        public Node delete(Node thisNode, T item ){
            if (this.Compare(item,thisNode.item) == 0){
                if(thisNode.left == null && thisNode.right == null){
                    return null;
                }else if(thisNode.left == null && thisNode.right != null){
                    return thisNode.right;
                }else if(thisNode.left != null && thisNode.right == null){
                    return thisNode.left;
                }else {
                    Node smallest = getSmallest(thisNode);
                    delete(thisNode,smallest.item);
                    smallest.left = thisNode.left;
                    smallest.right = thisNode.right;
                    thisNode.left = null;
                    thisNode.right = null;
                    return smallest;
                }
            }else if(this.Compare(item, thisNode.item) > 0){
                return delete(thisNode.right, item);
            }else {
                return delete(thisNode.left, item);
            }

        }

        private Node getSmallest(Node thisNode){
            if(thisNode.left == null) return null;
            else {
                return getSmallest(thisNode.left);
            }

        }

        private int Compare(T item1, T item2){
            if(((Comparable<T>) item1).compareTo(item2) >0) return 1;
            else if(((Comparable<T>) item1).compareTo(item2) <0) return -1;
            else return 0;
        }

    }

    private class MinPQ{
        private ArrayList<Node> items;
        private int size;
        public MinPQ(){
            this.items = new ArrayList<>(1000);
            this.size = 0;
        }

        public void insert(Node node){
            this.size += 1;
            this.items.set(size,node);
            int index = swim(size);
            node.index = index;
        }

        public int parent(int k){
         if(k == 1) return -1;
         else {
             return k/2;
         }
        }

        public int leftChild(int k){
            return k*2;
        }

        public int rightChild(int k){
            return k*2 +1;
        }

        public void swap(int k1, int k2){
            Node interim = null;
            interim = this.items.get(k1);
            this.items.set(k1,this.items.get(k2));
            this.items.set(k2,interim);
        }

        public Node getSmallest(){
            return this.items.get(1);
        }

        public int swim(int k){
          if(k == 1) return 1;
          if(this.items.get(parent(k)).priority > this.items.get(k).priority)
          {
              this.swap(k,parent(k));
              return swim(parent(k));
          }
          return k;

        }

        public Node removeSmallest(){
            Node smallest = this.items.get(1);
            this.items.set(1,this.items.get(size));
            this.items.set(size,null);
            sink(1);
            return smallest;
        }

        public void sink(int index){
         if(this.leftChild(index) > size){
             return;
         }else if(this.leftChild(index) == size){
             if (this.items.get(index).priority > this.items.get(this.leftChild(index)).priority ){
                 this.swap(index,this.leftChild(index));
                 sink(this.leftChild(index));
             }
         }
         else {
             int smallerChild;
             if(this.items.get(this.leftChild(index)).priority < this.items.get(this.rightChild(index)).priority) smallerChild = this.leftChild(index);
             else {
                 smallerChild = this.rightChild(index);
             }
             if (this.items.get(index).priority > this.items.get(smallerChild).priority){
                 this.swap(index,smallerChild);
                 this.sink(smallerChild);
             }
             return;
         }
        }
    }

    /** The official implementation of the arrayHeapMinPQ starts here**/
    private MinPQ mpq;
    private Bst bst;
    private int size;


    public ArrayHeapMinPQ(){
        this.mpq = new MinPQ();
        this.bst = new Bst();
        this.size = 0;
    }

    @Override
    public void add(T item, double priority) {
             Node node = new Node(item,priority);
             this.mpq.insert(node);
             this.bst.insert(node);
             this.size +=1;
    }

    @Override
    public boolean contains(T item) {
        if (this.bst.contains(item) == null){
            return false;
        }
        return true;
    }

    @Override
    public T getSmallest() {
        return this.mpq.getSmallest().item;
    }

    @Override
    public T removeSmallest() {
        Node node = this.mpq.removeSmallest();
        T item = node.item;
        this.bst.delete(item);
        this.size -=1;
        return item;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) throw new NoSuchElementException("The element doesnt exists");
        Node node = this.bst.contains(item);
        int index = node.getIndex();
        node.setPriority(priority);
        this.mpq.swim(index);
        this.mpq.sink(node.getIndex());
    }
}
