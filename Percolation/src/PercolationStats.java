import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double [] trialResults;
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException(String.format("Initialization failed with n %d, trails %d", n, trials));
        }
        trialResults = new double [trials];
        for (int i = 0; i < trials; i++) {
            Percolation pc = new Percolation(n);
            while (!pc.percolates()) {
                int row = StdRandom.uniformInt(n) + 1;
                int col = StdRandom.uniformInt(n) + 1;
                if (!pc.isOpen(row, col)) {
                    pc.open(row, col);
                }
            }
            trialResults[i] = (double) pc.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(trialResults);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(trialResults);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(trialResults.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(trialResults.length);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats pcs = new PercolationStats(n, t);
        StdOut.println(String.format("mean = %.8f", pcs.mean()));
        StdOut.println(String.format("stddev = %.8f", pcs.stddev()));
        StdOut.println(String.format("95%% confidence interval = [%.8f, %.8f]", pcs.confidenceLo(), pcs.confidenceHi()));
    }

}
