import edu.princeton.cs.algs4.*;
import java.awt.Color;

public class SeamCarver {
    static final double INF = Double.MAX_VALUE;
    private Picture picture;
    private int[][][] picture_RGB;  // a 3D matrix which stores RGB values of each pixel of the picture
    
    public SeamCarver(Picture picture) {               
    // creates a seam carver object based on the given picture
        this.picture = picture;
        int width = picture.width();
        int height = picture.height();
        //enter values into picture_RGB 
        picture_RGB = new int[width][height][3];
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                Color color = picture.get(i, j);
                picture_RGB[i][j][0] = color.getRed();
                picture_RGB[i][j][1] = color.getGreen();
                picture_RGB[i][j][2] = color.getBlue();
            }
        }
    }
    
    public Picture picture() {                         // current picture
        return picture;
    }
    
    public int width() {                           // width of current picture
        return picture.width();
    }
    
    public int height() {                          // height of current picture
        return picture.height();
    }
    
    private double[][] findEnergyMatrix() {       
    //generates and returns a matrix containing
    //energy value of each pixel of the current picture
        
        int width = width();
        int height = height();
        double[][] energy_matrix = new double[width][height];
        
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                energy_matrix[i][j] = energy(i, j);
            }
        }
        
        return energy_matrix;
    }
    
    public  double energy(int x, int y) {              
    // energy of pixel at column x and row y
        
        int width = width();
        int height = height();
        if(x < 0 || x >= width || y < 0 || y >= height)  //check for valid range of arguments
            throw new IndexOutOfBoundsException();
        
        /*calculate the x indices of left and right pixels
         *and y indices of top and bottom pixels,
         *wrapping around image required for border pixels
         */
        int x_left = (x + width - 1) % width;
        int x_right = (x + 1) % width;
        int y_top = (y + height -1) % height;
        int y_bottom = (y + 1) % height;
        
        //fetch the RGB values of the neighbouring pixels
        int[] left = picture_RGB[x_left][y];
        int[] right = picture_RGB[x_right][y];
        int[] top = picture_RGB[x][y_top];
        int[] bottom = picture_RGB[x][y_bottom];
        
        //calculate energy using dual-gradient energy function
        double x_gradient_sq = Math.pow((left[0] - right[0]), 2) + Math.pow((left[1] - right[1]), 2) + Math.pow((left[2] - right[2]), 2); 
        double y_gradient_sq = Math.pow((top[0] - bottom[0]), 2) + Math.pow((top[1] - bottom[1]), 2) + Math.pow((top[2] - bottom[2]), 2);
        
        double energy = Math.sqrt(x_gradient_sq + y_gradient_sq);
        return energy;
    }
    
    public   int[] findHorizontalSeam() {              // sequence of indices for horizontal seam
        int[] horizontal_seam = findSeam(false);
        return horizontal_seam;
    }
    
    public   int[] findVerticalSeam() {                // sequence of indices for vertical seam
        int[] vertical_seam = findSeam(true);
        return vertical_seam;
    }

    private int [] findSeam(boolean isVertical) {
    /*this function returns an array containing indices of 
     *pixels to be removed from the picture depending on 
     * whether the seam is horizontal or vertical
     */
        int width = width();
        int height = height();
        double[][] energy_matrix = findEnergyMatrix();
        
        //if the seam is horizontal, transpose the energy matrix
        if(!isVertical) {
            double[][] temp_matrix = new double[height][width];
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++)
                    temp_matrix[i][j] = energy_matrix[j][i];
            }
            energy_matrix = temp_matrix;
            width = height();
            height = width();
        }
        
        //find the seam using the shortest path algorithm
        double[][] minDistance = new double[width][height];  //matrix to store the shortest distace to that pixel
        int[][] parent = new int[width][height];  //matrix to store the shortest path parent of each pixel 
        
        //intialize the distance of source pixels in first row to their energy values
        //and distance to rest of the pixels as infinity
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                if(j == 0)
                    minDistance[i][j] = energy_matrix[i][j];
                else
                    minDistance[i][j] = INF;
            }
        }
        
        //for each pixel which is not the source, 
        //calculate the distance to each parent
        //and store the least distance and its corresponding parent index
        for(int row = 1; row < height; row++) {
            for(int col = 0; col < width; col++) {
                
                //parent just above the pixel(col, row)
                double minEnergy = minDistance[col][row-1];
                int column = col;
                
                //top-right parent pixel
                if(col != width-1) {
                    if(minDistance[col+1][row-1] < minEnergy) {
                        minEnergy = minDistance[col+1][row-1];
                        column = col+1;
                    }
                }
                
                //top-left parent pixel
                if(col != 0) {
                    if(minDistance[col-1][row-1] < minEnergy) {
                        minEnergy = minDistance[col-1][row-1];
                        column = col-1;
                    }
                }
                
                if(minDistance[col][row] > (energy_matrix[col][row] + minEnergy)) {
                    minDistance[col][row] = energy_matrix[col][row] + minEnergy;
                    parent[col][row] = column;
                }
            }
           
        }
        
        //find the pixel in the last row which has the least distance
        //and track the seam using the parent[][] matrix
        double min = Double.MAX_VALUE;
        int index_min = 0;
        for(int col = 0; col < width; col++) {
            if(minDistance[col][height-1] < min) {
                min = minDistance[col][height-1];
                index_min = col;
            }
        }
        
        int [] seam = new int [height];
        for(int row = height-1; row >= 0; row--) {
            seam[row] = index_min;
            index_min = parent[index_min][row];
        }
        
        
        return seam;
    }
    
    public void removeHorizontalSeam(int[] seam) {  // remove horizontal seam from current picture        
        //check if seam is valid
        if(!validSeam(seam, false))
            throw new IllegalArgumentException();
        
        int width = width();
        int height = height();
        Picture new_pic = new Picture(width, height-1);
        
        //for each pixel that lies below the horizontal seam,
        //shift it up by 1 pixel
        for(int row = 0; row < height; row++) {
            for(int col = 0; col < width; col++) {
                if(seam[col] == row)
                    continue;
                
                if(row < seam[col])
                    new_pic.set(col, row, picture.get(col, row));
                else {
                    new_pic.set(col, row-1, picture.get(col, row));
                    picture_RGB[col][row-1] = picture_RGB[col][row];
                }
            }
        }
        picture = new_pic;
    }
    
    public void removeVerticalSeam(int[] seam) {    // remove vertical seam from current picture
        //check if seam is valid
        if(!validSeam(seam, true))
            throw new IllegalArgumentException();
        
        int width = width();
        int height = height();
        Picture new_pic = new Picture(width-1, height);
        
        //for each pixel that lies to the right of the vertical seam,
        //shift it to the left by 1 pixel
        for(int row = 0; row < height; row++) {
            for(int col = 0; col < width; col++) {
                if(seam[row] == col) {
                    continue;
                }
                
                if(col < seam[row])
                    new_pic.set(col, row, picture.get(col, row));
                else {
                    new_pic.set(col-1, row, picture.get(col, row));
                    picture_RGB[col-1][row] = picture_RGB[col][row];
                }
            }
        }
        this.picture = new_pic;
    }
    
    private boolean validSeam(int[] seam, boolean isVertical) { //check if the seam is valid
        if(seam == null)
            throw new NullPointerException();
        
        int width = width();
        int height = height();
        
        if(isVertical) {
            if(width <= 1)
                throw new IllegalArgumentException();
            for(int i = 0; i < height; i++) {
                //check if the entries lie within range
                if(seam[i] < 0 || seam[i] >= width)
                    return false;
                //check if adjacent pixels in seam do not differ by more than 1
                if(i==0) continue;
                if(Math.abs(seam[i] - seam[i-1]) > 1)
                    return false;
            }
        }
        else {
            if(height <= 1)
                throw new IllegalArgumentException();
            for(int i = 0; i < width; i++) {
                //check if the entries lie within range
                if(seam[i] < 0 || seam[i] >= height)
                    return false;
                //check if adjacent pixels in seam do not differ by more than 1
                if(i==0) continue;
                if(Math.abs(seam[i] - seam[i-1]) > 1)
                    return false;
            }
        }
        return true;
    }
    
    public static void main(String[] args) {           // do unit testing of this class
        Picture pic = new Picture("6x5.png");
        SeamCarver sc = new SeamCarver(pic);
        int [] seam = sc.findSeam(false);
        for(int i : seam)
            StdOut.println(i);
    }
    

}