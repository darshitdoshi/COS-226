import edu.princeton.cs.algs4.*;
public class KdTreeST<Value> {
    
    private class Node {
    private Point2D p;      // the point, associated with each node in KdTreeST
    private Value val;    // the symbol table maps the point to this value
    private RectHV rect;    // the axis-aligned rectangle corresponding to this node
    private Node lb;        // the left/bottom subtree
    private Node rt;        // the right/top subtree
    
    private int size;       //number of nodes in subtree
    private boolean type;       // true- Horizontal, false - Vertical
    
    public Node(Point2D p, Value val, boolean type, RectHV rect) {  //Data type-Node constructor
        this.p = p;
        this.val = val;
        this.type = type;
        this.rect = rect;
        this.size = 1;
    }
    
    public int compare(Double p1, Double p2) { //compare points p1 and p2
        if(p1 > p2) return 1;
        if(p1 < p2) return -1;
        else return 0;
    }
    public int compareTo(Point2D point) {
        if(this.type == false)        //type : Vertical
        {
            int cmp = compare(this.p.x(), point.x());
            if(cmp != 0)
                return cmp;
            return compare(this.p.y(), point.y());
        }
        //type " horizontal
        int cmp = compare(this.p.y(), point.y());
        if(cmp != 0)
            return cmp;
        return compare(this.p.x(), point.x());
        
    }
    
    public int compareTo(RectHV rect) {    
        if(this.type == false) {
            if(this.p.x() > rect.xmax())   //point lies to the right of the rectangle
                return 1;
            if(this.p.x() < rect.xmin())   //point lies to the left of the rectangle
                return -1;
        }
        else {
            if(this.p.y() > rect.ymax())   //point lies above the rectangle
                return 1;
            if(this.p.y() < rect.ymin())   //point lies below the rectangle
                return -1;
        }
        return 0;                         //point intersects the rectangle
        
    }
    
    private RectHV findRect(Point2D p) {  //find rectangle of point p from this node
        double xmin = this.rect.xmin();
        double xmax = this.rect.xmax();
        double ymin = this.rect.ymin();
        double ymax = this.rect.ymax();
        
        if(this.type)                   //type : horizontal 
        {
            if(this.compareTo(p) > 0)
                ymax = this.p.y();
            else
                ymin = this.p.y();
        }
        else                           //type : vertical
        {
            if(this.compareTo(p) > 0)
                xmax = this.p.x();
            else
                xmin = this.p.x();
        }
        return new RectHV(xmin, ymin, xmax, ymax);
    }
    
    }
    
    private Node root = null;
    
    public KdTreeST() {                               // construct an empty symbol table of points 
        
    }
    
    public boolean isEmpty() {                     // is the symbol table empty? 
        return size() == 0;
    }
    
    public int size() {                        // number of points 
        return size(root);
    }
    
    private int size(Node node) {
        if(node == null) 
            return 0;
        else
            return node.size;
    }
    
    public void put(Point2D p, Value val) {     // associate the value (x,y) coordinate with point p
        if (p == null) throw new java.lang.NullPointerException();
        
        root = put(root, p, val, null, false);
    }
    
    
    private Node put(Node node, Point2D p, Value val, Node parent, boolean type) {
        if(node == null) {
            RectHV rect;
            if(parent == null)
                rect = new RectHV(0.0, 0.0, 1.0, 1.0);
            else 
            {
                rect = parent.findRect(p); 
            }
            return new Node(p, val, type, rect);
        }
        
        int cmp = node.compareTo(p);
        if(cmp > 0)                                // point is smaller than node; check left/bottom
            node.lb = put(node.lb, p, val, node, !type);
        else if(cmp < 0)                           // point is greater than node; check right/top
            node.rt = put(node.rt, p, val, node, !type);
        else {
            node.p = p;
            node.val = val;
        }
        
        node.size = 1 + size(node.lb) + size(node.rt); //set size parameter of node
        return node;
    }
    
    public Value get(Point2D p) {                //get value associated with point p
        if(p == null)
            throw new java.lang.NullPointerException();
        return get(root, p);
    }
    
    private Value get(Node node, Point2D p) {
        if(node == null || p == null)
            throw new java.lang.NullPointerException();;
        
        int cmp = node.compareTo(p);
        if(cmp < 0)                               // point is smaller than node; check left/bottom
            return get(node.lb, p);
        if(cmp > 0)                               // point is greater than node; check right/top
            return get(node.rt, p);
        else                                      // desired node found
            return node.val;
    }
    
    public boolean contains(Point2D p) {           // does the symbol table contain point p? 
        if(p == null)
            throw new java.lang.NullPointerException();
        return get(p) != null;
    }
    
    public Iterable<Point2D> points() {                      // all points in the symbol table 
        Queue<Point2D> point_queue = new Queue<Point2D>();   //level order traversal
        Queue<Node> node_queue = new Queue<Node>();
        node_queue.enqueue(root);
        while(!node_queue.isEmpty())
        {
            Node top = node_queue.dequeue();
            if(top == null) continue;
            point_queue.enqueue(top.p);
            node_queue.enqueue(top.lb);
            node_queue.enqueue(top.rt);
        }
        return point_queue;
    }

   
    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle 
        if(rect == null) throw new java.lang.NullPointerException();
        Queue<Point2D> range_queue = new Queue<Point2D>();
        return range(root, rect, range_queue);
    }
    
    private Iterable<Point2D> range(Node node, RectHV rect, Queue<Point2D> range_queue) {            // all points that are inside the rectangle 
        if(node == null) return range_queue;   
        int cmp = node.compareTo(rect);
        if(cmp == 1)                               //current point is greater than rect; check left subtree
            range(node.lb, rect, range_queue);
        else if(cmp == -1)                         //current point is less than rect; check right subtree
            range(node.rt, rect, range_queue);
        else {                                    //current point lies inside or on the rect
            if(rect.contains(node.p))
                range_queue.enqueue(node.p);
            range(node.lb, rect, range_queue);
            range(node.rt, rect, range_queue);
        }
        return range_queue;
    }
    
     public Point2D nearest(Point2D point) {            // a nearest neighbor to point p; null if the symbol table is empty
        if(isEmpty()) return null;
        if(point == null)
            throw new java.lang.NullPointerException();
        double distance_to_root = root.p.distanceSquaredTo(point); 
        return nearest(point, root, root.p, Double.POSITIVE_INFINITY); //set initial node to root and min_distance to infinity
    }
     
     private Point2D nearest(Point2D point, Node current_node, Point2D closest, double min_distance) {
         
         if(current_node == null) return closest;
         double rect_distance = current_node.rect.distanceSquaredTo(point);
         //check if the rect of current node is closer to query point than closest point
         if(rect_distance < min_distance) {
             //check if current node is closest to query point
             double point_distance = current_node.p.distanceSquaredTo(point);
             if(point_distance < min_distance) {                 
                 min_distance = point_distance;
                 closest = current_node.p;
             }
             //Check for exploring left/right subtree depending on type of current node
                 if(current_node.compareTo(point) == 1) {     
                     closest = nearest(point, current_node.lb, closest, min_distance);
                     closest = nearest(point, current_node.rt, closest, closest.distanceSquaredTo(point));
                 }
                 else if(current_node.compareTo(point) == -1){                                 
                     closest = nearest(point, current_node.rt, closest, min_distance);
                     closest = nearest(point, current_node.lb, closest, closest.distanceSquaredTo(point));
                 }
               
         }
         return closest;
     }
    
    public static void main(String[] args) {                 // unit testing (required)
        KdTreeST<Integer> kdtree = new KdTreeST<Integer>();
        double vals[] = StdIn.readAllDoubles();
        for(int i = 0, j = 1; i < vals.length; j++) {
            double x = vals[i++];
            double y = vals[i++];
            kdtree.put(new Point2D(x, y), j);
        }
        int size = kdtree.size();
        StdOut.println("Size of the KdTreeST : " + size);
        Iterable<Point2D> point_list = kdtree.points();
        StdOut.println("Points contained in KdTreeST are : ");
        for(Point2D point: point_list)
            StdOut.println(point.toString());
        
        RectHV rect = new RectHV(0.0, 0.0, 0.5, 0.5);
        Iterable<Point2D> range_list = kdtree.range(rect);
        StdOut.println("Points contained in the rectangle (0, 0, 0.5, 0.5) are : ");
        for(Point2D point: range_list)
            StdOut.println(point.toString());
        
        Point2D closest = kdtree.nearest(new Point2D(0.5, 0.75));
        StdOut.println("Closest point to (0.5, 0.5) is : " + closest.toString());
    }
    
}