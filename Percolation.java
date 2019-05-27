import java.lang.*;
import java.util.*;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    /*
    For the Percolation problem we model a percolation system using an n-by-n
    grid of sites. Each site is either open or blocked. A full site is an open
    site that can be connected to an open site in the top row via a chain of
    neighboring (left, right, up, down) open sites. We say the system percolates
    if there is a full site in the bottom row. In other words, a system
    percolates if we fill all open sites connected to the top row and that
    process fills some open site on the bottom row.

    In my algorithm, grid is a multi-dimensional integer array in which all the
    sites are "blocked" at first. If a site is blocked it will have a value
    which is equal to the index of that site, so site 0 has value 0 etc.

    wqf makes use of the WeightedQuickUnionUF algorithm from Princetons algs4
    file
    */

    private int num;
    private int top = 0;
    private int bottom;
    private int[][] grid;
    private WeightedQuickUnionUF wqf;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        num = n;
        bottom = n - 1;
        grid = new int[num][num];
        wqf = new WeightedQuickUnionUF((num * num + 2));
        for (int row = 0; row < n; row++){
            for (int col = 0; col < n; col++){
                grid[row][col] = usableFormat(row, col);
            }
        }
    }

    private int usableFormat(int p, int q){
        // usable format takes the row and column number from a 3D array and
        // returns the equivalent index numbers of a 2D array
        return p * num + q;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        // changes the value of a site you want to open to (num * num + 1)
        grid[row][col] = (num * num + 1);
        // checks if (row - 1) is a valid row and if ((row -1), col) is open
        if ((row - 1) > -1 && isOpen((row - 1), col)){
            // links the new row and col to an already open one
            wqf.union(usableFormat(row, col), usableFormat((row - 1), col));
        }
        // checks if (row + 1) is a valid row and if ((row -1), col) is open
        if ((row + 1) < num && isOpen((row + 1), col)){
            // links the new row and col to an already open one
            wqf.union(usableFormat(row, col), usableFormat((row + 1), col));
        }
        // checks if (col - 1) is a valid row and if (row, (col - 1)) is open
        if ((col - 1) > -1 && isOpen(row, (col - 1))){
            // links the new row and col to an already open one
            wqf.union(usableFormat(row, col), usableFormat(row, (col - 1)));
        }
        // checks if (col + 1) is a valid row and if (row, (col + 1)) is open
        if ((col + 1) < num && isOpen(row, (col + 1))){
            // links the new row and col to an already open one
            wqf.union(usableFormat(row, col), usableFormat(row, (col + 1)));
        }
        // System.out.println("You opened grid number " + usableFormat(row, col) + " and its value is now " + grid[row][col]);
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        // returns true if (row, col) is open
        return grid[row][col] == (num * num + 1);
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        boolean isfull = false;
        // runs a for loop through the columns in the first row
        for (int i = 0; i < num; i++){
            // checks to see if (row, col) is connected to any col in the first
            // row
            if (wqf.connected(usableFormat(row, col), usableFormat(top, i))){
                // runs a for loop through the columns in the last row
                for (int j = 0; j < num; j++){
                    // checks to see if (row, col) is connected to any col in
                    // the last row
                    if (wqf.connected(usableFormat(row, col), usableFormat((bottom), j))){
                        // if (row, col) is connected to both; a col in the top
                        // row and a col in the bottom row, then it is full
                        isfull = true;
                        break;
                    }
                }
            }
        }
        // return true if the grid is full
        return isfull;

    }

    // number of open sites
    public int numberOfOpenSites() {
        // count is the count of the number of open sites
        int count = 0;
        // goes through the rows of the grid and grid and counts the number of
        // open sites
        for (int i = 0; i < num; i++){
            for (int j = 0; j < num; j++){
                if(grid[i][j] == (num * num + 1)){
                    count++;
                }
            }
        }
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        //return wqf.connected(top, bottom);
        boolean isF = false;
        // goes through columns of the first row and tells if the system
        // percolates or not
        for (int i = 0; i < num; i++){
            if (isFull(top, i)){
                isF = true;
                break;
            }
        }
        return isF;

    }

    public static void main(String[] args) {
        final int TESTS = 1000;
        final int GRID_SIZE = 20;

        Percolation a = new Percolation(GRID_SIZE);

        double sum = 0.0;
        int row, col;
        int counter = 0;
        int ctr;

        for (int i = 0; i < TESTS; i++){
            a = new Percolation(GRID_SIZE);
            ctr = 0;
            // run a while loop until the system percolates
            while(!a.percolates()){
                row = (int) (Math.random() * (GRID_SIZE));
                col = (int) (Math.random() * (GRID_SIZE));
                if (!a.isOpen(row, col)){
                    /*
                    // if you want to see the row and columns printed out,
                    // uncomment the following line of code
                    System.out.println("row: " + row + " col: " + col);
                    */
                    a.open(row, col);
                    ctr++;
                }
                /*
                // to manually check the algorithm use the following code
                Scanner myObj = new Scanner(System.in);
                System.out.println("Please enter row: ");
                row = myObj.nextInt();
                System.out.println("Please enter col: ");
                col = myObj.nextInt();
                while(a.isOpen(row, col)){
                    System.out.println("Please enter another row that (row, col) was already open: ");
                    row = myObj.nextInt();
                    System.out.println("Please enter another col that (row, col) was already open: ");
                    col = myObj.nextInt();
                }
                */
                // open the site (row, col)
                if (a.percolates()){
                    counter++;
                    break;
                }
            }
            sum += ctr;
        }
        // print out test results
        System.out.println("After " + TESTS + " attempts, the average number of sites");
        System.out.printf("opened was %.2f", sum/TESTS);
        System.out.printf(" or %.2f", ((sum/TESTS)/(GRID_SIZE * GRID_SIZE)) * 100);
        System.out.println("%.");
    }
}
