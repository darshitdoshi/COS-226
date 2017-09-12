import edu.princeton.cs.algs4.*;

public class PointST<Value> {
    RedBlackBST<Point2D, Value> bst;
    public PointST() {                                // construct an empty symbol table of points 
        bst = new RedBlackBST<Point2D, Value>();
    }
    
    public boolean isEmpty() {                     // check if symbol table is empty 
        return bst.isEmpty();
    }
    
    public int size() {                        // number of points 
        return bst.size();
    }
    
    public void put(Point2D p, Value val) {     // associate the value val with point p
        if(p == null || val == null)
            throw new java.lang.NullPointerException();
        
        bst.put(p, val);
        return;
    }
    
    public Value get(Point2D p) {                // value associated with point p
        if(p == null)
            throw new java.lang.NullPointerException();
        
        return bst.get(p);
    }
    
    public boolean contains(Point2D p) {           // does the symbol table contain point p? 
        if(p == null)
            throw new java.lang.NullPointerException();
        return bst.contains(p);
    }
    
    public Iterable<Point2D> points() {                      // all points in the symbol table 
       return bst.keys(); 
    }
    
    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle 
        if(rect == null)
            throw new java.lang.NullPointerException();
        
        Queue<Point2D> queue = new Queue<Point2D>();
        for(Point2D point: points())
        {
            if(rect.contains(point))
                queue.enqueue(point);
        }
        return queue;
    }
    
    public Point2D nearest(Point2D p) {            // a nearest neighbor to point p; null if the symbol table is empty 
        if(bst.isEmpty())
            return null;
        
        if(p == null)
            throw new java.lang.NullPointerException();
        
        Point2D nearest_point = bst.min();
        double min_dist = p.distanceSquaredTo(nearest_point);
        for(Point2D point : points())
        {
            double distance = p.distanceSquaredTo(point);
            if((distance<min_dist) && (!p.equals(point)))
            {
                min_dist = distance;
                nearest_point = point;
            }
        }
        return nearest_point;
    }
    
    public static void main(String[] args) {                 // unit testing (required) 
        PointST<Integer> symbol_table = new PointST<Integer>();
        for (int i = 0; i < 10; i++) {
            int x = StdRandom.uniform(10);
            int y = StdRandom.uniform(10);
            int z = StdRandom.uniform(10);
            symbol_table.put(new Point2D(x, y),z);
        }
        StdOut.print("Is the symbol table empty ? : ");
        StdOut.println(symbol_table.isEmpty());
        StdOut.print("Size of the table is : ");
        StdOut.println(symbol_table.size());
        Iterable<Point2D> list = symbol_table.points();
        for(Point2D key: list) {
            StdOut.print(key.toString());
            StdOut.println(symbol_table.get(key));
        }
        Point2D test_point = new Point2D(5.0, 5.0);
        StdOut.print("The closest point to (5, 5) is ");
        StdOut.println(symbol_table.nearest(test_point).toString());
        
        RectHV rect = new RectHV(2, 2, 4, 4);
        Iterable<Point2D> range_list = symbol_table.range(rect);
        StdOut.println("Points contained within the rectangle (2,2,4,4) are : ");
        for(Point2D key: range_list)
            StdOut.println(key.toString());
    }
}