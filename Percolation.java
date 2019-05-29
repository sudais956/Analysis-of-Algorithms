import java.lang.IndexOutOfBoundsException;
import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    /**
     * For the Percolation problem we model a percolation system using an n-by-n
     * grid of sites. Each site is either open or blocked. A full site is an open
     * site that can be connected to an open site in the top row via a chain of
     * neighboring (left, right, up, down) open sites. We say the system percolates
     * if there is a full site in the bottom row. In other words, a system
     * percolates if we fill all open sites connected to the top row and that
     * process fills some open site on the bottom row.

     * In my algorithm, grid is a multi-dimensional boolean array in which all the
     * sites are "blocked" at first. If a site is blocked, it will have the value
     * false
     
     * @author Sudais Moorad
     */

    private int num;
    private int top = 0;
    private int bottom;
    private boolean[][] grid;
    private WeightedQuickUnionUF wqf;
    
    /*
     * create n-by-n grid, with all sites blocked
     */
    public Percolation(int n) {
        if (n > 0) {
        num = n;
        bottom = n * n + 1;
        grid = new boolean[num][num];
        wqf = new WeightedQuickUnionUF((num * num + 2));
        }
        else {
            throw new IllegalArgumentException();
        }
    }
    
    /*
     * takes the row and column number from a 3D array and
     * returns the equivalent index numbers of a 2D array
     */
    private int usableFormat(int p, int q) {
        return (p - 1) * num + q;
    }
    
    /*
     * open site (row, col) if it is not open already
     */
    public void open(int row, int col) {
        if (0 < row && row <= num && 0 < col && col <= num) {
            // changes the value of a site you want to open to true
            grid[row - 1][col - 1] = true;
            if (row == 1) {
                wqf.union(usableFormat(row, col), top);
            }
            if (row == num) {
                wqf.union(usableFormat(row, col), bottom);
            }
            if (row > 1 && isOpen(row - 1, col)) {
                // links the new row and col to an already open one above it
                wqf.union(usableFormat(row, col), usableFormat(row - 1, col));
            }
            if (row < num && isOpen(row + 1, col)) {
                // links the new row and col to an already open one below it
                wqf.union(usableFormat(row, col), usableFormat(row + 1, col));
            }
            if (col > 1 && isOpen(row, col - 1)) {
                // links the new row and col to an already open one on the left
                wqf.union(usableFormat(row, col), usableFormat(row, col - 1));
            }
            if (col < num && isOpen(row, col + 1)) {
                // links the new row and col to an already open one on the right
                wqf.union(usableFormat(row, col), usableFormat(row, col + 1));
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }
    
    /*
     * is site (row, col) open?
     */
    public boolean isOpen(int row, int col) {
        if (0 < row && row <= num && 0 < col && col <= num) {
        // returns true if (row, col) is open
        return grid[row - 1][col - 1];
        }
        else {
            throw new IllegalArgumentException();
        }
    }
    /*
     * is site (row, col) full?
     */
    public boolean isFull(int row, int col) {
        if (0 < row && row <= num && 0 < col && col <= num) {
            return wqf.connected(top, usableFormat(row, col));
        }
        else {
            throw new IllegalArgumentException();
        }
    }
    
    /*
     * number of open sites
     */
    public int numberOfOpenSites() {
        // count is the count of the number of open sites
        int count = 0;
        // goes through the rows of the grid and grid and counts the number of
        // open sites
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                if (grid[i][j] == true) {
                    count++;
                }
            }
        }
        return count;
    }
    
    /*
     * does the system percolate?
     */
    public boolean percolates() {
        return wqf.connected(top, bottom);
    }

    public static void main(String[] args) {
        final int TESTS = 1000;
        final int GRID_SIZE = 20;

        double sum = 0.0;
        int row, col;
        int ctr;

        for (int i = 0; i < TESTS; i++) {
            Percolation a = new Percolation(GRID_SIZE);
            ctr = 0;
            // run a while loop until the system percolates
            while (!a.percolates()) {
                row = StdRandom.uniform(1, GRID_SIZE + 1);
                col = StdRandom.uniform(1, GRID_SIZE + 1);
                if (!a.isOpen(row, col)) {
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
                while (a.isOpen(row, col)) {
                    System.out.println("Please enter another row that (row, col) was already open: ");
                    row = myObj.nextInt();
                    System.out.println("Please enter another col that (row, col) was already open: ");
                    col = myObj.nextInt();
                }
                */
                // open the site (row, col)
                if (a.percolates()) {
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
