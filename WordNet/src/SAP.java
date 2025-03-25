import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

public class SAP {
    private final Digraph graph;

    // Constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        graph = new Digraph(G);
    }

    // Single-source BFS
    private int[] bfs(int id) {
        int[] dist = new int[graph.V()];
        Arrays.fill(dist, -1);
        Queue<Integer> qu = new Queue<>();
        qu.enqueue(id);
        dist[id] = 0;
        while (!qu.isEmpty()) {
            int cur = qu.dequeue();
            for (int neighbor : graph.adj(cur)) {
                if (dist[neighbor] == -1) {
                    dist[neighbor] = dist[cur] + 1;
                    qu.enqueue(neighbor);
                }
            }
        }
        return dist;
    }

    private void checkNull(Object obj) {
        if (obj == null) throw new IllegalArgumentException("input is null");
    }

    // Check if v and w are in valid range
    private void checkvw(int v, int w) {
        checkNull(v);
        checkNull(w);
        if (v < 0 || w < 0 || v >= graph.V() || w >= graph.V()) {
            throw new IllegalArgumentException("v, w out of range");
        }
    }

    // Helper: compute minimum combined distance for two distance arrays.
    private int minLength(int[] distv, int[] distw) {
        int minDistance = Integer.MAX_VALUE;
        for (int i = 0; i < graph.V(); i++) {
            if (distv[i] > -1 && distw[i] > -1) {
                minDistance = Math.min(minDistance, distv[i] + distw[i]);
            }
        }
        return minDistance == Integer.MAX_VALUE ? -1 : minDistance;
    }

    // Helper: find common ancestor with minimum combined distance.
    private int commonAncestor(int[] distv, int[] distw) {
        int minDistance = Integer.MAX_VALUE;
        int ancest = -1;
        for (int i = 0; i < graph.V(); i++) {
            if (distv[i] > -1 && distw[i] > -1) {
                if (distv[i] + distw[i] < minDistance) {
                    minDistance = distv[i] + distw[i];
                    ancest = i;
                }
            }
        }
        return ancest;
    }

    // Length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkvw(v, w);
        int[] distv = bfs(v);
        int[] distw = bfs(w);
        return minLength(distv, distw);
    }

    // A common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        checkvw(v, w);
        int[] distv = bfs(v);
        int[] distw = bfs(w);
        return commonAncestor(distv, distw);
    }

    // Multi-source BFS
    private int[] bfs(Iterable<Integer> vs) {
        int[] dist = new int[graph.V()];
        Arrays.fill(dist, -1);
        Queue<Integer> qu = new Queue<>();
        for (int v : vs) {
            qu.enqueue(v);
            dist[v] = 0;
        }
        while (!qu.isEmpty()) {
            int cur = qu.dequeue();
            for (int neighbor : graph.adj(cur)) {
                if (dist[neighbor] == -1) {
                    dist[neighbor] = dist[cur] + 1;
                    qu.enqueue(neighbor);
                }
            }
        }
        return dist;
    }

    // Check iterable arguments for valid vertex indices.
    private void checkIterable(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException("v, w null");
        for (int vid : v) {
            checkNull(vid);
            if (vid < 0 || vid >= graph.V()) throw new IllegalArgumentException("v out of range");
        }
        for (int wid : w) {
            checkNull(wid);
            if (wid < 0 || wid >= graph.V()) throw new IllegalArgumentException("w out of range");
        }
    }

    // Length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkIterable(v, w);
        int[] distv = bfs(v);
        int[] distw = bfs(w);
        return minLength(distv, distw);
    }

    // A common ancestor that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkIterable(v, w);
        int[] distv = bfs(v);
        int[] distw = bfs(w);
        return commonAncestor(distv, distw);
    }

    // Unit testing of this class.
    public static void main(String[] args) {
        // Testing code here if needed.
    }
}
