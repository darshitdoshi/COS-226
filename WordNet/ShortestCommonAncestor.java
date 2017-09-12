import edu.princeton.cs.algs4.*;

public class ShortestCommonAncestor {
    
    private Digraph G;
    
   // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        if(G == null)
            throw new java.lang.NullPointerException("Digraph to constructor is Empty");
        
        //check if G is rooted
        int V = G.V();
        int no_of_root = 0;
        for(int v = 0; v < V; v++) {
            if(G.outdegree(v)==0) {
                no_of_root++;
            }
        }
        if(no_of_root != 1)
            throw new java.lang.IllegalArgumentException("Digraph is not rooted");
        
        //check if G contains a cycle
        DirectedCycle digraph = new DirectedCycle(G);
        if(digraph.hasCycle())
            throw new java.lang.IllegalArgumentException("Digraph is cyclic");
        
        // creating defensive copy as Digraph is mutable data type
        this.G = new Digraph(G);
    }

   // length of shortest ancestral path between v and w
   /* queue is utilized even for single vertices to make
    the type of parameters passed to calculateSCA method uniform
    */
    public int length(int v, int w) {
        Queue<Integer> a = new Queue<Integer>();
        a.enqueue(v);
        Queue<Integer> b = new Queue<Integer>();
        b.enqueue(w);
        int[] result = calculateSCA(a, b);
        /*result array contains length of path as first element
        the second element is the shortest common ancestor
        */
        return result[0];
    }

   // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        Queue<Integer> a = new Queue<Integer>();
        a.enqueue(v);
        Queue<Integer> b = new Queue<Integer>();
        b.enqueue(w);
        int[] result = calculateSCA(a, b);
        return result[1];
    }
   
    
   // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if(subsetA == null || subsetB == null)
            throw new java.lang.NullPointerException();
        int[] result = calculateSCA(subsetA, subsetB);
        return result[0];
    }

   // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if(subsetA == null || subsetB == null)
            throw new java.lang.NullPointerException();
        int[] result = calculateSCA(subsetA, subsetB);        
        return result[1];
    }
    
    // calculate the shortest common ancestor as well as the legnth of shortest ancestral path
    private int[] calculateSCA(Iterable<Integer> a, Iterable<Integer> b) {
        int[] result = new int[2];
        DeluxeBFS bfs_v =  new DeluxeBFS(G, a);
        DeluxeBFS bfs_w =  new DeluxeBFS(G, b);
        
        int min_dist = Integer.MAX_VALUE;
        int shortest_ancestor = Integer.MAX_VALUE;
        int current_dist = 0;
        
        /*for each vertex visited in the bfs of v (using getMarked() function in DeluxeBFS),
        check if there exists a path from this visited vertex to w
        If true, check if this distace is the least
        */
        for(int x : bfs_v.getMarked()) {
            if(bfs_w.hasPathTo(x)) {
                current_dist = bfs_v.distTo(x) + bfs_w.distTo(x);
                if(current_dist < min_dist) {
                    min_dist = current_dist;
                    shortest_ancestor = x;
                }
            }
        }
        
        result[0] = min_dist;
        result[1] = shortest_ancestor;
        return result;
    }
    

   // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        StdOut.println("Enter vertices v and w");
        int v = StdIn.readInt();
        int w = StdIn.readInt();
        int length   = sca.length(v, w);
        int ancestor = sca.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length,ancestor);
        
        Queue<Integer> a = new Queue<Integer>();
        a.enqueue(5);
        a.enqueue(4);
        Queue<Integer> b = new Queue<Integer>();
        b.enqueue(7);
        b.enqueue(11);
        length   = sca.length(a, b);
        ancestor = sca.ancestor(a, b);
        StdOut.printf("For v = {5,4}, w = {7, 11} length = %d, ancestor = %d\n", length,ancestor);
    }
}