import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;


public class Solver {
    private static class ManhattanState implements Comparable<ManhattanState> {
        public final Board board;
        public final int move;
        public final int dis;
        public final int priority;
        public final ManhattanState prev;

        public ManhattanState(Board board, int move, ManhattanState prev) {
            this.board = board;
            this.move = move;
            this.dis = board.manhattan(); // Using Manhattan heuristic
            this.priority = this.move + this.dis;
            this.prev = prev;
        }

        public int compareTo(ManhattanState other) {
            return Integer.compare(this.priority, other.priority);
        }
    }

    private boolean solvable;
    private int moves;
    private List<Board> solutionPath;

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Board cannot be null");

        solvable = false;
        solutionPath = null;

        MinPQ<ManhattanState> pq = new MinPQ<>((a, b) -> Integer.compare(a.priority, b.priority));
        MinPQ<ManhattanState> pqRev = new MinPQ<>((a, b) -> Integer.compare(a.priority, b.priority));
        pq.insert(new ManhattanState(initial, 0, null));
        pqRev.insert(new ManhattanState(initial.twin(), 0, null));


        while (!pq.isEmpty() && !pqRev.isEmpty()) {
            ManhattanState current = pq.delMin();
            ManhattanState currentRev = pqRev.delMin();

            if (current.board.isGoal()) {
                reconstructPath(current);
                solvable = true;
                moves = current.move;
                return;
            }
            if (currentRev.board.isGoal()) {
                solvable = false;
                moves = -1;
                return;
            }

            for (Board neighbor : current.board.neighbors()) {
                if (current.prev != null && neighbor.equals(current.prev.board)) continue;  // Avoid backtracking
                pq.insert(new ManhattanState(neighbor, current.move + 1, current));
            }
            for (Board neighbor : currentRev.board.neighbors()) {
                if (currentRev.prev != null && neighbor.equals(currentRev.prev.board)) continue;  // Avoid backtracking
                pqRev.insert(new ManhattanState(neighbor, currentRev.move + 1, currentRev));
            }
        }
    }

    private void reconstructPath(ManhattanState goal) {
        solutionPath = new ArrayList<>();
        for (ManhattanState node = goal; node != null; node = node.prev) {
            solutionPath.add(0, node.board);
        }
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        return solutionPath;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
