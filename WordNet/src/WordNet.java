import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;

public class WordNet {
    private final Digraph graph;
    private final ST<String, SET<Integer>> wordIDs;
    private final ST<Integer, SET<String>> words;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();
        In synsetsFile = new In(synsets);
        while (synsetsFile.hasNextLine()) {
            String line = synsetsFile.readLine();
            String[] lineSegments = line.split(",");
            ids.add(Integer.parseInt(lineSegments[0]));
            lines.add(lineSegments[1]);
        }

        int size = lines.size();
        graph = new Digraph(size);
        wordIDs = new ST<>();
        words = new ST<>();

        for (int i = 0; i < size; i++) {
            int id = ids.get(i);
            String synset = lines.get(i);
            String[] synsetWords = synset.split(" ");
            SET<Integer> wordToID;
            SET<String> idToWord = new SET<>();
            for (String word : synsetWords) {
                idToWord.add(word);
                if (wordIDs.contains(word)) {
                    wordToID = wordIDs.get(word);
                    wordToID.add(id);
                }else {
                    wordToID = new SET<>();
                    wordToID.add(id);
                    wordIDs.put(word, wordToID);
                }
            }
            words.put(id, idToWord);
        }

        In hypernymsFile = new In(hypernyms);
        while (hypernymsFile.hasNextLine()) {
            String line = hypernymsFile.readLine();
            String[] lineSegments = line.split(",");
            int id = Integer.parseInt(lineSegments[0]);
            for (int i = 1; i < lineSegments.length; i++) {
                graph.addEdge(id, Integer.parseInt(lineSegments[i]));
            }
        }

        // check rooted
        Digraph reverse = graph.reverse();
        int rootID = -1;
        boolean rootFound = false;
        for (int i = 0; i < graph.V(); i++) {
            if (reverse.indegree(i) == 0) {
                if (rootFound) {
                    throw new IllegalArgumentException("Has multiple roots");
                } else {
                    rootFound = true;
                    rootID = i;
                }
            }
        }
        boolean[] checked = new boolean[graph.V()];
        Stack<Integer> st = new Stack<>();
        st.push(rootID);
        while (!st.isEmpty()) {
            int id = st.pop();
            if (checked[id]) continue;
            checked[id] = true;
            for (int neighbor : reverse.adj(id)) {
                st.push(neighbor);
            }
        }
        for (int i = 0; i < graph.V(); i++) {
            if (!checked[i]) {
                throw new IllegalArgumentException("Not rooted");
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordIDs.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return wordIDs.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        SAP sap = new SAP(graph);
        return sap.length(wordIDs.get(nounA), wordIDs.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        SAP sap = new SAP(graph);
        return String.join(" ", words.get(sap.ancestor(wordIDs.get(nounA), wordIDs.get(nounB))));
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
