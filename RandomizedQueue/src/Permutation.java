import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomQue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            randomQue.enqueue(StdIn.readString());
        }
        for (String str : randomQue) {
            if (k == 0) break;
            StdOut.println(str);
            k--;
        }
    }
}
