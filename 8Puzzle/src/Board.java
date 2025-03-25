import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Board {
    private final int dimension;
    private final int [][] board;
    private int emptyX;
    private int emptyY;
    // create a board from a dimension-by-dimension array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        dimension = tiles.length;
        board = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tiles[i][j] == 0) {
                    emptyX = i;
                    emptyY = j;
                }
                board[i][j] = tiles[i][j];
            }
        }
    }


    // string representation of this board
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(dimension);
        str.append(System.lineSeparator());
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension - 1; col++) {
                str.append(String.format("%d ", board[row][col]));
            }
            str.append(board[row][dimension - 1]);
            str.append(System.lineSeparator());
        }
        return str.toString();
    }

    // board dimension dimension
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        int miss = 0;
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                if (board[row][col] != 0 && row * dimension + col + 1 != board[row][col])
                    miss++;
            }
        }
        return miss;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int totalDistance = 0;
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                if (board[row][col] == 0) continue;
                int goalRow = (board[row][col] - 1) / dimension, goalCol = (board[row][col] - 1) % dimension;
                totalDistance += Math.abs(row - goalRow) + Math.abs(col - goalCol);
            }
        }
        return totalDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null || y.getClass() != this.getClass()) return false;

        Board other = (Board) y;
        if (this.dimension != other.dimension) return false;
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                if (this.board[row][col] != other.board[row][col])
                    return false;
            }
        }
        return true;
    }

    private int[][] copyBoard() {
        int[][] copy = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++)
            System.arraycopy(board[i], 0, copy[i], 0, dimension);
        return copy;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> boards = new ArrayList<>();
        int[] helper = {1, 0, -1, 0, 1};
        for (int dir = 0; dir < 4; dir++) {
            int newX = emptyX + helper[dir], newY = emptyY + helper[dir + 1];
            if (newX < 0 || newX >= dimension || newY < 0 || newY >= dimension) continue;
            int[][] newBoard = copyBoard();
            newBoard[newX][newY] = 0;
            newBoard[emptyX][emptyY] = board[newX][newY];
            boards.add(new Board(newBoard));
        }
        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] newBoard = copyBoard();

        // Swap first two non-zero tiles
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension - 1; col++) {
                if (newBoard[row][col] != 0 && newBoard[row][col + 1] != 0) {
                    newBoard[row][col] = board[row][col + 1];
                    newBoard[row][col + 1] = board[row][col];
                    return new Board(newBoard);
                }
            }
        }
        return new Board(newBoard);
    }


    // unit testing (not graded)
    public static void main(String[] args) {
        int [][] some = {{1, 2, 3}, {4, 5, 6}, {7, 0, 8}};
        int [][] idea = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        Board board1 = new Board(some);
        Board board2 = new Board(idea);
        StdOut.println(String.format("Dimension: %d", board1.dimension()));
        StdOut.println(board1.toString());
        StdOut.println(String.format("Hamming distance: %d", board1.hamming()));
        StdOut.println(String.format("Manhattan distance: %d", board1.manhattan()));
        if (board1.isGoal()) StdOut.println("Board1 is goal");
        if (board2.isGoal()) StdOut.println("Board2 is goal");
        if (board1.equals(board2)) StdOut.println("Board1 equals board2");
        for (Board neighbor : board1.neighbors()) {
            if (neighbor.isGoal()) {
                StdOut.println(neighbor.toString());
            }
        }
        StdOut.println(board2.twin().toString());



    }

}
