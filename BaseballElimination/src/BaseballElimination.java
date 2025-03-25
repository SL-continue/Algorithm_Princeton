import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;


public class BaseballElimination {
    private final int teams;
    private final ST<String, Integer> teamNames;
    private final ST<Integer, String> idToTeam;
    private final int[] wins;
    private final int[] losses;
    private final int[] left;
    private final int[][] games;

    public BaseballElimination(String filename) {                   // create a baseball division from given filename in format specified below
        In file = new In(filename);
        String[] lines = file.readAllLines();
        teams = Integer.parseInt(lines[0]);
        teamNames = new ST<>();
        idToTeam = new ST<>();
        wins = new int[teams];
        losses = new int[teams];
        left = new int[teams];
        games = new int[teams][teams];
        for (int row = 1; row <= teams; row++) {
            int id = row - 1;
            String[] subset = lines[row].strip().split("\\s+");
            teamNames.put(subset[0], id);
            idToTeam.put(id, subset[0]);
            wins[id] = Integer.parseInt(subset[1]);
            losses[id] = Integer.parseInt(subset[2]);
            left[id] = Integer.parseInt(subset[3]);
            for (int col = 0; col < teams; col++) {
                games[id][col] = Integer.parseInt(subset[4 + col]);
            }
        }
    }
    private void checkTeam(String team) {
        if (!teamNames.contains(team))
            throw new IllegalArgumentException(String.format("%s not in teams", team));
    }
    public              int numberOfTeams() {                      // number of teams
        return teams;
    }
    public Iterable<String> teams() {                               // all teams
        return teamNames.keys();
    }
    public              int wins(String team) {                     // number of wins for given team
        checkTeam(team);
        return wins[teamNames.get(team)];
    }
    public              int losses(String team) {                   // number of losses for given team
        checkTeam(team);
        return losses[teamNames.get(team)];
    }
    public              int remaining(String team) {                // number of remaining games for given team
        checkTeam(team);
        return left[teamNames.get(team)];
    }
    public              int against(String team1, String team2) {   // number of remaining games between team1 and team2
        checkTeam(team1);
        checkTeam(team2);
        return games[teamNames.get(team1)][teamNames.get(team2)];
    }

    private FordFulkerson buildMaxFlow(String team) {
        int curTeam = teamNames.get(team);
        int teamV = teams - 1;
        int gameV = teamV * (teamV - 1) / 2; // (1 + teams - 2) * (teams - 2) / 2
        int v = teamV + gameV + 2;
        FlowNetwork graph = new FlowNetwork(v);
        // build network
        // team vertices to t
        ST<Integer, Integer> teamToVertex = new ST<>();
        int offset = 1;
        for (int teamID = 0; teamID < teams; teamID++) {
            if (teamID == curTeam) continue;
            teamToVertex.put(teamID, gameV + offset);
            double capacity = wins[curTeam] + left[curTeam] - wins[teamID];
            FlowEdge teamToT = new FlowEdge(gameV + offset, v - 1, capacity);
            graph.addEdge(teamToT);
            offset++;
        }
        // s to game vertices and game vertices to team vertices
        int curGameV = 1;
        for (int i = 0; i < teams; i++) {
            if (i == curTeam) continue;
            for (int j = i + 1; j < teams; j++) {
                if (j == curTeam) continue;
                FlowEdge toGameEdge = new FlowEdge(0, curGameV, games[i][j]);
                int teamIVertex = teamToVertex.get(i), teamJVertex = teamToVertex.get(j);
                FlowEdge toTeamIEdge = new FlowEdge(curGameV, teamIVertex, Double.POSITIVE_INFINITY);
                FlowEdge toTeamJEdge = new FlowEdge(curGameV, teamJVertex, Double.POSITIVE_INFINITY);
                graph.addEdge(toGameEdge);
                graph.addEdge(toTeamIEdge);
                graph.addEdge(toTeamJEdge);
                curGameV++;
            }
        }
        return new FordFulkerson(graph, 0, v - 1);
    }

    public          boolean isEliminated(String team) {             // is given team eliminated?
        checkTeam(team);
        int maxWin = wins(team) + remaining(team);
        for (int win : wins) {
            if (maxWin < win) {
                return true;
            }
        }
        FordFulkerson maxFlow = buildMaxFlow(team);
        int curTeam = teamNames.get(team);
        double flowValue = maxFlow.value();
        double sumOfGameCaps = 0;
        for (int i = 0; i < teams; i++) {
            if (i == curTeam) continue;
            for (int j = i + 1; j < teams; j++) {
                if (j == curTeam) continue;
                sumOfGameCaps += games[i][j];    // games between i, j
            }
        }
        return !(flowValue == sumOfGameCaps);
    }
    public Iterable<String> certificateOfElimination(String team) { // subset R of teams that eliminates given team; null if not eliminated
        checkTeam(team);
        Bag<String> ret = new Bag<>();
        int curTeam = teamNames.get(team);
        int maxWin = wins[curTeam] + left[curTeam];
        for (String teamName : teamNames.keys()) {
            if (maxWin < wins(teamName)) {
                ret.add(teamName);
            }
        }
        if (!ret.isEmpty()) return ret;

        FordFulkerson maxFlow = buildMaxFlow(team);

        double flowValue = maxFlow.value();
        double sumOfGameCaps = 0;
        for (int i = 0; i < teams; i++) {
            if (i == curTeam) continue;
            for (int j = i + 1; j < teams; j++) {
                if (j == curTeam) continue;
                sumOfGameCaps += games[i][j];    // games between i, j
            }
        }
        if (flowValue == sumOfGameCaps) return null;

        int teamV = teams - 1;
        int gameV = teamV * (teamV - 1) / 2;
        for (int i = gameV + 1, j = 0; j < teams; j++) {
            if (j == curTeam) continue;
            if (maxFlow.inCut(i)) {
                ret.add(idToTeam.get(j));
            }
            i++;
        }
        return ret;
    }
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
