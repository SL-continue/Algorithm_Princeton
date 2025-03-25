import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        StringBuilder sb = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            sb.append(BinaryStdIn.readChar());
        }
        String s = sb.toString();
        int n = s.length();
        CircularSuffixArray sfxArr = new CircularSuffixArray(s);
        int start = 0;
        for (int i = 0; i < n; i++) {
            if (sfxArr.index(i) == 0) {
                start = i;
                break;
            }
        }
        BinaryStdOut.write(start);
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(s.charAt((sfxArr.index(i) - 1 + n) % n));
        }
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        StringBuilder sb = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            sb.append(BinaryStdIn.readChar());
        }

        // construct next array
        int n = sb.length();
        char[] t = sb.toString().toCharArray();

        // sort t using key-indexed counting
        int R = 256;
        int[] count = new int[R + 1];
        for (int i = 0; i < n; i++) {
            count[t[i] + 1]++;
        }

        for (int i = 0; i < R; i++) {
            count[i + 1] += count[i];
        }

        char[] col1 = new char[n];

        int[] next = new int[n];
        for (int i = 0; i < n; i++) {
            char c = t[i];
            int pos = count[c]++;
            col1[pos] = c;
            next[pos] = i;
        }

        // invert
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(col1[first]);
            first = next[first];
        }
        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        } else if (args[0].equals("+")) {
            inverseTransform();
        } else {
            throw new IllegalArgumentException("Please select either encoding or decoding");
        }
    }

}
