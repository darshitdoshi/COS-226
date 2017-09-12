import edu.princeton.cs.algs4.*;
public class Solver {

    private SearchNode min_priority_node;
        
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private SearchNode previous;
        private int manhattan_priority;
        
        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            manhattan_priority = board.manhattan() + moves;
            
            }
        
        @Override
        public int compareTo(SearchNode another) {                             //Overriding the default compareTo method
            if(this.manhattan_priority < another.manhattan_priority) return -1;
            if(this.manhattan_priority > another.manhattan_priority) return 1;
            return 0;
        }
            
        }
    
        public Solver(Board initial) {          // find a solution to the initial board (using the A* algorithm)
        if(initial == null)
            throw new java.lang.NullPointerException();
        
        if(!initial.isSolvable())
            throw new java.lang.IllegalArgumentException();
        
        //Till the goal board is not reached, remove the node with least priority
        // from priority queue and enqueue its neighbours which isn't the previous node
        MinPQ<SearchNode> minpq = new MinPQ<SearchNode>();
        SearchNode initial_node = new SearchNode(initial, 0, null);
        minpq.insert(initial_node);
        min_priority_node  = initial_node;
        while(!min_priority_node.board.isGoal()) {
            min_priority_node = minpq.delMin();
            
            for(Board board: min_priority_node.board.neighbors())
            {
                if(min_priority_node.previous == null || !board.equals(min_priority_node.previous.board))
                    minpq.insert(new SearchNode(board, min_priority_node.moves+1, min_priority_node));
            }
            
        }
        
    }
    
    public int moves() {                     // min number of moves to solve initial board
        if(min_priority_node.board.isSolvable())
            return min_priority_node.moves;
        return -1;
    }
    
    public Iterable<Board> solution() {     // sequence of boards in a shortest solution
        if(!min_priority_node.board.isSolvable())
            return null;
        
        Stack<Board> solution = new Stack<Board>();
        SearchNode current = min_priority_node;
        while(current != null) {
            solution.push(current.board);
            current = current.previous;
        }
        return solution;
    }
    
    public static void main(String[] args) { // solve a slider puzzle (given below)
        // create initial board from file
    In in = new In(args[0]);
    int N = in.readInt();
    int[][] tiles = new int[N][N];
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
            tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);

    // check if puzzle is solvable; if so, solve it and output solution
    if (initial.isSolvable()) {
        Solver solver = new Solver(initial);
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution())
            StdOut.println(board);
    }

    // if not, report unsolvable
    else {
        StdOut.println("Unsolvable puzzle");
    }
    }
}