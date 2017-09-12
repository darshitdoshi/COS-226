import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
        
    private double[] threshold;
    public static final double k = 1.96;
    private int T;
    
    /* perform T independent experiments on an N-by-N grid
       randomly open sites till the grid percolates
       count the no. of open sites and store it an array
     */
    public PercolationStats(int N, int T) {                        
        if (N < 1 || T < 1) throw new IllegalArgumentException();
        
        this.T = T;
        threshold = new double[T];
 
        for(int i = 0; i < T; i++) {
            
            int row, col;
            int open_sites = 0;
            Percolation percolate = new Percolation(N);
            while(!percolate.percolates()) {
                row = StdRandom.uniform(N);
                col = StdRandom.uniform(N);
    
                percolate.open(row, col);
            }
            
             open_sites = percolate.numberOfOpenSites();   
             threshold[i] = (double) open_sites/(N*N);
             
            }
        }
    
    
    // sample mean of percolation threshold
    public double mean() {                   
        return(StdStats.mean(threshold));
    }
    
    // sample standard deviation of percolation threshold
    public double stddev() {                 
        return(StdStats.stddev(threshold));
    }
    
    // low  endpoint of 95% confidence interval
    public double confidenceLow() {           
        return(mean() - (k * stddev())/Math.sqrt(T));
    }
    
    // high endpoint of 95% confidence interval
    public double confidenceHigh() {          
        return(mean() + (k * stddev())/Math.sqrt(T));
    }
    
    // testing this class
    public static void main(String args[]) {
    
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        Stopwatch sw = new Stopwatch();
        PercolationStats ps = new PercolationStats(N, T);
        StdOut.println("mean =            " + ps.mean());
        StdOut.println("std deviation =   " + ps.stddev());
        StdOut.println("confidence low =  " + ps.confidenceLow());
        StdOut.println("confidence high = " + ps.confidenceHigh());
        StdOut.println("elapsed time =    " + sw.elapsedTime());
    
    }
    
}