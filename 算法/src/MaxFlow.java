import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class MaxFlow {
    private static final int INF = Integer.MAX_VALUE;

    public static void main(String[] args) {
        QReader scanner=new QReader();


        int n = scanner.nextInt(); // Number of vertices
        int m = scanner.nextInt(); // Number of edges
        int s = scanner.nextInt(); // Source vertex
        int t = scanner.nextInt(); // Sink vertex

        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int c = scanner.nextInt();
            graph.get(u - 1).add(new Edge(v - 1, c));
        }

        int maxFlow = fordFulkerson(graph, s - 1, t - 1);
        System.out.println(maxFlow);
    }

    static class Edge {
        int v, c;

        Edge(int v, int c) {
            this.v = v;
            this.c = c;
        }
    }

    private static int fordFulkerson(List<List<Edge>> graph, int source, int sink) {
        int n = graph.size();
        int[][] residualGraph = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (Edge edge : graph.get(i)) {
                residualGraph[i][edge.v] += edge.c;
            }
        }

        int[] parent = new int[n];
        int maxFlow = 0;

        while (bfs(residualGraph, source, sink, parent)) {
            int pathFlow = INF;

            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, residualGraph[u][v]);
            }

            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                residualGraph[u][v] -= pathFlow;
                residualGraph[v][u] += pathFlow;
            }

            maxFlow += pathFlow;
        }

        return maxFlow;
    }

    private static boolean bfs(int[][] graph, int source, int sink, int[] parent) {
        int n = graph.length;
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited[source] = true;
        parent[source] = -1;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v = 0; v < n; v++) {
                if (!visited[v] && graph[u][v] > 0) {
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        return visited[sink];
    }

    static class QReader {
        private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        private StringTokenizer tokenizer = new StringTokenizer("");

        private String innerNextLine() {
            try {
                return reader.readLine();
            } catch (IOException e) {
                return null;
            }
        }

        public boolean hasNext() {
            while (!tokenizer.hasMoreTokens()) {
                String nextLine = innerNextLine();
                if (nextLine == null) {
                    return false;
                }
                tokenizer = new StringTokenizer(nextLine);
            }
            return true;
        }

        public String nextLine() {
            tokenizer = new StringTokenizer("");
            return innerNextLine();
        }

        public String next() {
            hasNext();
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }
}
