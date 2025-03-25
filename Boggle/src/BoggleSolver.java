import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver
{
    private static class Node {
        boolean end;
        Node[] next;

        Node() {
            end = false;
            next = new Node[26];
        }

        void put(String str) {
            put(str, 0);
        }

        void put(String str, int d) {
            int index = str.charAt(d) - 'A';
            if (next[index] == null) next[index] = new Node();
            if (d == str.length() - 1) {
                next[index].end = true;
                return;
            }
            next[index].put(str, d + 1);
        }

        boolean valid(String str) {
            return valid(str, 0);
        }

        boolean valid(String str, int d) {
            int index = str.charAt(d) - 'A';
            Node child = next[index];
            if (child == null) return false;
            if (d == str.length() - 1) return child.end;
            return child.valid(str, d + 1);
        }
    }

    private Node root;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        root = new Node();
        for (String str : dictionary) {
            root.put(str);
        }
    }

    private class Solver {
        boolean[][] marked;
        int m, n;

        SET<String> validWords;

        Solver(BoggleBoard board, Node root) {
            m = board.rows();
            n = board.cols();
            marked = new boolean[m][n];
            validWords = new SET<>();

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    dfs(board, root, i, j, "");
                }
            }
        }

        public Iterable<String> getValidWords() {
            return validWords;
        }

        private void dfs(BoggleBoard board, Node root, int x, int y, String prefix) {
            marked[x][y] = true;
            char c = board.getLetter(x, y);
            Node nextNode = root.next[c - 'A'];
            if (nextNode == null) {
                marked[x][y] = false;
                return;
            }
            String newPrefix = prefix + c;
            if (c == 'Q') {
                nextNode = nextNode.next['U' - 'A'];
                if (nextNode == null) {
                    marked[x][y] = false;
                    return;
                }
                newPrefix = newPrefix + 'U';
            }
            if (nextNode.end && newPrefix.length() > 2) validWords.add(newPrefix);

            int[] helper1 = {-1, 0, 1, 0, -1};
            int[] helper2 = {-1, -1, 1, 1, -1};
            for (int h1 = 0; h1 < 4; h1++) {
                int newX = x + helper1[h1], newY = y + helper1[h1 + 1];
                if (inBoard(newX, newY)) {
                    dfs(board, nextNode, newX, newY, newPrefix);
                }
            }
            for (int h2 = 0; h2 < 4; h2++) {
                int newX = x + helper2[h2], newY = y + helper2[h2 + 1];
                if (inBoard(newX, newY)) {
                    dfs(board, nextNode, newX, newY, newPrefix);
                }
            }
            marked[x][y] = false;
        }

        private boolean inBoard(int i, int j) {
            return i >= 0 && i < m && j >= 0 && j < n && !marked[i][j];
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Solver solver = new Solver(board, root);
        return solver.getValidWords();
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (root.valid(word)) {
            int l = word.length();
            switch (l) {
                case 3:
                case 4:
                    return 1;
                case 5:
                    return 2;
                case 6:
                    return 3;
                case 7:
                    return 5;
                default:
                    return l < 3 ? 0 : 11;
            }
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
