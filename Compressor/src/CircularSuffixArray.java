import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {

    private final int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("null string input");
        int n = s.length();
        Integer[] indexTemp = new Integer[n];
        index = new int[n];
        for (int i = 0; i < n; i++) {
            indexTemp[i] = i;
        }

        Arrays.sort(indexTemp, (a, b) -> {
            for (int i = 0; i < n; i++) {
                char c1 = s.charAt((a + i) % n);
                char c2 = s.charAt((b + i) % n);
                if (c1 != c2) return c1 - c2;
            }
            return 0;
        });

        for (int i = 0; i < n; i++) {
            index[i] = indexTemp[i];
        }
    }

    // length of s
    public int length() {
        return index.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= index.length) throw new IllegalArgumentException("index invalid.");
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray test = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < test.length(); i++) {
            StdOut.println(test.index(i));
        }
    }

}
