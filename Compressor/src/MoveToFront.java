import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static void shift(char[] chars, int idx) {
        if (idx == 0) return;
        char tgt = chars[idx];
        for (int i = idx; i > 0; i--) {
            chars[i] = chars[i - 1];
        }
        chars[0] = tgt;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] chars = new char[256];
        for (int i = 0; i < 256; i++) {
            chars[i] = ((char) i);
        }
        while (!BinaryStdIn.isEmpty()) {
            char tgt = BinaryStdIn.readChar();
            int idx = 0;
            while (chars[idx] != tgt) {
                idx++;
            }
            BinaryStdOut.write(idx, 8);
            shift(chars, idx);
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] chars = new char[256];
        for (int i = 0; i < 256; i++) {
            chars[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            int idx = BinaryStdIn.readInt(8);
            char out = chars[idx];
            BinaryStdOut.write(out);
            shift(chars, idx);
        }
        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        } else {
            throw new IllegalArgumentException("Please select either encoding or decoding");
        }
    }

}
