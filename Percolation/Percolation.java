import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;

public class Percolation {
    
    private int N;
    private boolean grid[][];
    private WeightedQuickUnionUF uf;
    private int open_cells_count;
    
    /* Data structures used are of size N^2 + N.
     * Additional N sites for an imaginary row above the N-by-N grid
     * 1D data structure of type WeightedQuickUnionUF for tracking connected components
     * 2D boolean array to keep track of open sites
     */
    public Percolation(int N) {               
        this.N = N;
        if(N <= 0)
            throw new IndexOutOfBoundsException();
        
        grid = new boolean[N+1][N];   //by default, all values are false
        for(int i = 0; i < N; i++)    //setting the imaginary row to true
            grid[0][i] = true;
        
        uf = new WeightedQuickUnionUF(N + (N*N));
        for(int i = 0; i < N-1; i++)   // first N imaginary sites belong to same component
            uf.union(i, i+1);         
        
        open_cells_count = 0;
    }
    
    /*Note: row+1 operation is performed as an adjustment step
     * to counter the inclusion of an imaginary row above the NxN grid
     */
    
    //calculate the index for UF of site(row, col)
    public int index(int row, int col) {
        return(((row+1) * N) + col); 
    }
    
    /* open the site (row, col) if it is not open already
       check the neighbouring sites(up, down, right, left)
       perform union of components if neighbouring sites are open
     */
    public void open(int row, int col) {       
        checkRange(row, col); 
            if(!grid[row + 1][col]) {
                grid[row + 1][col] = true;
                open_cells_count++;
            
                if(col < (N - 1) && isOpen(row, col+1))               //check right
                    uf.union(index(row, col), index(row, col+1));
                
                if(col > 0 && isOpen(row, col-1))                   //check left
                    uf.union(index(row, col), index(row, col-1));
                
                if(row < (N - 1) && isOpen(row+1, col))               //check down 
                    uf.union(index(row, col), index(row+1, col));
                
                if(row >= 0 && isOpen(row-1, col))                  //check up 
                    uf.union(index(row, col), index(row-1, col));
            }
        
    }
    
    //check if the site(row, col) is valid and open
    public boolean isOpen(int row, int col) {  
        checkRange(row, col);
        return(grid[row + 1][col] == true);
    }
    
    /*check if the site(row, col) is full
     * by comparing the component index of the imaginary sites
     * and the current site(row, col)
     */
    public boolean isFull(int row, int col) { 
        checkRange(row, col);
        if(isOpen(row, col)) {
            if(uf.connected(index(row, col), 0))
                return true;
            return false;
        }
        return false;
    }
    
    //no. of open sites
    public int numberOfOpenSites() {           
        return open_cells_count;
    }
    
    
    /* check if the system percolates by checking if 
     * atleast one of the last row sites is full
     */
    public boolean percolates() {             
        for(int i = 0; i < N; i++) {
            if(isFull(N-1, i))
                return true;
        }
        return false;
    }
    
    // check if the row, col index are within range
    public void checkRange(int row, int col) {
        row += 1;
        if(row < 0 || col < 0 || row > N || col >= N)
            throw new IndexOutOfBoundsException();
        
    }
    
    // unit testing
    public static void main(String[] args) { 
        int N = 4;
        
        Percolation p = new Percolation(N);
        p.open(0,0);
        p.open(1,0);
        p.open(1,1);
        p.open(2,1);
        p.open(2,2);
        p.open(3,2);
        p.open(2,2);
        
        StdOut.println(p.percolates());
        StdOut.println(p.numberOfOpenSites());
    }
}