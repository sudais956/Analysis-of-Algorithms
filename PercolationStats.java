import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int experiments;
    private Percolation perc;
    private double[] sumExps;
    private double stdDev;
    private double mean;
    private double CONF_95 = 1.96;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("The grid size and number of trials have to be at least 1");
        }
        else {
            experiments = trials;
            sumExps = new double[experiments];
            for (int i = 0; i < experiments; i++) {
                perc = new Percolation(n);
                double sum = 0;
                double openedSites = 0.0;
                int row, col;
                while (!perc.percolates()) {
                    row = StdRandom.uniform(1, n + 1);
                    col = StdRandom.uniform(1, n + 1);
                    if (!perc.isOpen(row, col)) {
                        perc.open(row, col);
                        openedSites++;
                    }
                }
                sum += openedSites;
                sumExps[i] = (openedSites / (n * n));
            }
            mean = StdStats.mean(sumExps);
            stdDev = StdStats.stddev(sumExps);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stdDev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - ((CONF_95 * stdDev) / Math.sqrt(experiments));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + ((CONF_95 * stdDev) / Math.sqrt(experiments));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, trials);

        System.out.println("mean                    = " + percStats.mean());
        System.out.println("stddev                  = " + percStats.stddev());
        String confidence = percStats.confidenceLo() + ", " + percStats.confidenceHi();
        System.out.println("95% confidence interval = [" + confidence + "]");
    }
}
