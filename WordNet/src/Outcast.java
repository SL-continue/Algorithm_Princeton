public class Outcast {
    private final WordNet wn;
    public Outcast(WordNet wordnet) {        // constructor takes a WordNet object
        wn = wordnet;
    }
    public String outcast(String[] nouns) {  // given an array of WordNet nouns, return an outcast
        int largestTotalDistance = 0;
        int outlier = 0;
        int [][] dist = new int[nouns.length][nouns.length];
        for (int i = 0; i < nouns.length; i++) {
            int totalDistance = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (i == j) continue;
                if (dist[i][j] != 0) {
                    totalDistance += dist[i][j];
                    continue;
                }
                int distance = wn.distance(nouns[i], nouns[j]);
                dist[i][j] = distance;
                dist[j][i] = distance;
                totalDistance += distance;
            }
            if (largestTotalDistance < totalDistance) {
                largestTotalDistance = totalDistance;
                outlier = i;
            }
        }
        return nouns[outlier];

    }
    public static void main(String[] args) { // see test client below

    }
}
