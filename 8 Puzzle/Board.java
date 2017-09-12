import edu.princeton.cs.algs4.*;
public class Board {
    
    private int N;
    private int[] current_board;
    
    public Board(int[][] tiles) {            // construct a board from an N-by-N array of tiles
        N = tiles.length;                // (where tiles[i][j] = tile at row i, column j)
        current_board = new int[N*N];
        
        int k = 0;
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                current_board[k++] = tiles[i][j];  //storing the board in a 1D array
            }
        }
        
    }
                                           
    public int tileAt(int i, int j) {        // return tile at row i, column j (or 0 if blank)
        if( i <0 || i > N-1 || j < 0 || j > N-1)
            throw new java.lang.IndexOutOfBoundsException();
        return current_board[(i * N) + j];
    }
    
    public int size() {                     // board size N
        return N;
    }
    
    public int hamming() {                  // number of tiles out of place
        int out_of_place = 0;
        for(int i = 0; i < N*N; i++) {
            if(current_board[i] != i+1)
                out_of_place ++;
        }
        return out_of_place-1;             //subtract 1 as we do not consider '0'
    }
    
    public int manhattan() {                // sum of Manhattan distances between tiles and goal excluding '0'
        int manhattan_dist = 0;
        
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                int value = current_board[(i * N) + j];
                if(value != 0) {
                    int dx = ((value-1) / N) - i;
                    int dy = ((value-1) % N) - j;
                
                    manhattan_dist += Math.abs(dx) + Math.abs(dy);
                }
            }
        }
        return manhattan_dist;
    }
    
    public boolean isGoal() {               // is this board the goal board?
        for(int i = 0; i < N*N - 1; i++){   // disregard the last tile as it must be 0 
            if(current_board[i] != i+1)
                return false;
        }
        return true;
    }
    
    public boolean isSolvable() {           // is this board solvable?
        if(N % 2 == 1) {                    // if N is odd
            int inversions = 0;
            for(int i = 0; i < N*N - 1; i++) {
                for(int j = i+1; j < N*N; j++) {
                    if((current_board[i] != 0) && (current_board[j] != 0) && (current_board[i] > current_board[j]))
                        inversions++;
                }
            }
            if(inversions % 2 == 0)       //if the no of inversions is even
                return true;
            return false;
        }
        else {
            int inversions = 0;
            int sum, blank_row = 0;
            
            for(int i = 0; i < N*N; i++) {
                if(current_board[i] == 0)
                    blank_row = i / N;
            }
            
            for(int i = 0; i < N*N - 1; i++) {
                for(int j = i+1; j < N*N; j++) {
                    if((current_board[i] != 0) && (current_board[j] != 0) && (current_board[i] > current_board[j]))
                        inversions++;
                }
            }
            sum = blank_row + inversions;
            if(sum % 2 == 0)
                return false;
            return true;
        }
    }
    
    public boolean equals(Object y) {       // does this board equal y?
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return (this.current_board.equals(that.current_board));
    }
    
    public Iterable<Board> neighbors() {    // all neighboring boards
        int[][] swap_left = new int[N][N];
        int[][] swap_right = new int[N][N];
        int[][] swap_up = new int[N][N];
        int[][] swap_down = new int[N][N];
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++) {
                int val = current_board[(i * N) + j];
                swap_left[i][j] = val;
                swap_right[i][j] = val;
                swap_up[i][j] = val;
                swap_down[i][j] = val;
            }
        }
        
        int zero_pos = 0;
        for(int i = 0; i < N*N; i++) {
            if(current_board[i] == 0) {
                zero_pos = i;
                break;
            }
        }
        int zero_x = zero_pos / N;
        int zero_y = zero_pos % N;
        
        
        Stack<Board> stack = new Stack<Board>();
        
        if(zero_y > 0) {                     //swap left
            //int[][] swap_left = current;
            int temp = swap_left[zero_x][zero_y];
            swap_left[zero_x][zero_y] = swap_left[zero_x][zero_y - 1];
            swap_left[zero_x][zero_y - 1] = temp;
            Board left_board = new Board(swap_left);
            stack.push(left_board);
        }
        
        if(zero_y < N-1) {                   //swap right 
            //int[][] swap_right = current;
            int temp = swap_right[zero_x][zero_y];
            swap_right[zero_x][zero_y] = swap_right[zero_x][zero_y + 1];
            swap_right[zero_x][zero_y + 1] = temp;
            Board right_board = new Board(swap_right);
            stack.push(right_board); 
        }
        
        if(zero_x > 0) {                  //swap up
            //int[][] swap_up = current;
            int temp = swap_up[zero_x][zero_y];
            swap_up[zero_x][zero_y] = swap_up[zero_x - 1][zero_y];
            swap_up[zero_x - 1][zero_y] = temp;
            Board up_board = new Board(swap_up);
            stack.push(up_board); 
        }
        
        if(zero_x < N-1) {                    //swap down
            //int[][] swap_down = current;
            int temp = swap_down[zero_x][zero_y];
            swap_down[zero_x][zero_y] = swap_down[zero_x + 1][zero_y];
            swap_down[zero_x + 1][zero_y] = temp;
            Board down_board = new Board(swap_down);
            stack.push(down_board);
        }
        return stack;
        
    }
    
    public String toString() {              // string representation of this board (in the output format specified below)
       StringBuilder s = new StringBuilder();
       s.append(N + "\n");
       for (int i = 0; i < N; i++) {
           for (int j = 0; j < N; j++) {
               s.append(String.format("%2d ", tileAt(i, j)));
           }
           s.append("\n");
       }
       return s.toString(); 
    }

    public static void main(String[] args) { // unit testing (required)
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        StdOut.println(initial.toString());
        StdOut.println(initial.isSolvable());
        StdOut.println(initial.manhattan());
        StdOut.println(initial.hamming());
        StdOut.println(initial.isGoal());
        
        Board neighbor = new Board(tiles);
        Stack<Board> test = (Stack<Board>) initial.neighbors();
        while(!test.isEmpty()) {
            neighbor = (Board) test.pop();
            StdOut.println(neighbor.toString());
        }
    }
}