import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // matrix[i] = 1 means blocked = 0 means open
    private boolean [] matrix;
    private int openSites;
    private int ncol;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF backwash;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(String.format("Initialization failed with n %d", n));
        }
        matrix = new boolean[n * n];
//        for (int i = 0; i < n * n; i ++) {
//            matrix[i] = false;
//        }
        // n * n represents top
        // n * n + 1 represents bottom
        this.uf = new WeightedQuickUnionUF(n * n + 2);
        this.backwash = new WeightedQuickUnionUF(n * n + 1);
        openSites = 0;
        ncol = n;
    }

    private void check(int row, int col) {
        if (row < 1 || row > ncol || col < 1 || col > ncol) {
            throw new IllegalArgumentException(String.format("Can not open site (%d, %d)", row, col));
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        check(row, col);
        row = row - 1;
        col = col - 1;
        if (!matrix[row * ncol + col]) {
            matrix[row * ncol + col] = true;
            openSites++;
            int [] helper = {1, 0, -1, 0, 1};
            for (int i = 0; i < 4; i++) {
                int newRow = row + helper[i];
                int newCol = col + helper[i + 1];
                int pos = row * ncol + col;
                if (newRow < 0) {
                    uf.union(ncol * ncol, pos);
                    backwash.union(ncol * ncol, pos);
                } else if (newRow == ncol) {
                    uf.union(ncol * ncol + 1, pos);
                } else if (newRow >= 0 && newRow < ncol && newCol < ncol && newCol >= 0) {
                    if (isOpen(newRow + 1, newCol + 1)) {
                        uf.union(newRow * ncol + newCol, pos);
                        backwash.union(newRow * ncol + newCol, pos);
                    }
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        check(row, col);
        return matrix[(row - 1) * ncol + (col - 1)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        check(row, col);
        return backwash.find((row - 1) * ncol + (col - 1)) == backwash.find(ncol * ncol);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(ncol * ncol) == uf.find(ncol * ncol + 1);
    }

    // test client (optional)
    public static void main(String[] args) {

    }

}
