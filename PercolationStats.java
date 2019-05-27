import java.lang.*;
import java.util.*;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class PercolationStats {
    private int experiments;
    private Percolation perc;
    private double[] sumExps;
    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException("The grid size and number of trials have to be at least 1");
        }
        else {
            experiments = trials;
            sumExps = new double[experiments];
            for (int i = 0; i < experiments; i++) {
                perc = new Percolation(n);
                double sum = 0;
                int counter = 0;
                int openedSites = 0;
                int row, col;
                while(!perc.percolates()) {
                    row = (int) (Math.random() * (n));
                    col = (int) (Math.random() * (n));
                    if (!perc.isOpen(row, col)) {
                        perc.open(row, col);
                        openedSites++;
                    }
                    if (perc.percolates()) {
                        counter++;
                        break;
                    }
                }
                sum += openedSites;
                sumExps[i] = openedSites;
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(sumExps);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(sumExps);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(experiments));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return mean() + ((1.96 * stddev()) / Math.sqrt(experiments));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, trials);

        String confidence = percStats.confidenceLo() + ", " + percStats.confidenceHi();
        System.out.println("mean                    = " + percStats.mean());
        System.out.println("stddev                  = " + percStats.stddev());
        System.out.println("95% confidence interval = [" + confidence + "]");
    }
}
