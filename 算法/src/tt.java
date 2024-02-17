import java.util.*;

class tt {
    static final int INF = Integer.MAX_VALUE;

    static class Edge {
        int to, capacity, flow;

        Edge(int to, int capacity) {
            this.to = to;
            this.capacity = capacity;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt(); // Number of projects
        int m = scanner.nextInt(); // Number of prerequisites

        int[] revenue = new int[n];
        for (int i = 0; i < n; i++) {
            revenue[i] = scanner.nextInt();
        }

        ArrayList<Edge>[] graph = new ArrayList[n + 2]; // Graph representation
        for (int i = 0; i < n + 2; i++) {
            graph[i] = new ArrayList<>();
        }

        int source = 0;
        int sink = n + 1;

        // Add edges to the graph
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            graph[u].add(new Edge(v, INF));
        }

        // Add edges from source to projects and projects to sink
        for (int i = 1; i <= n; i++) {
            if (revenue[i - 1] > 0) {
                graph[source].add(new Edge(i, revenue[i - 1]));
            } else if (revenue[i - 1] < 0) {
                graph[i].add(new Edge(sink, -revenue[i - 1]));
            }
        }

        // Find maximum flow using Edmonds-Karp algorithm
        int maxRevenue = maxFlow(graph, source, sink);
        System.out.println(maxRevenue);
    }

    static int maxFlow(ArrayList<Edge>[] graph, int source, int sink) {
        int n = graph.length;
        int[][] capacity = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (Edge edge : graph[i]) {
                capacity[i][edge.to] += edge.capacity;
            }
        }

        int[] parent = new int[n];
        int maxFlow = 0;

        while (true) {
            int[] visited = new int[n];
            Queue<Integer> queue = new LinkedList<>();
            queue.add(source);
            visited[source] = 1;

            while (!queue.isEmpty()) {
                int current = queue.poll();

                for (Edge edge : graph[current]) {
                    int next = edge.to;
                    if (visited[next] == 0 && capacity[current][next] > 0) {
                        queue.add(next);
                        visited[next] = 1;
                        parent[next] = current;
                    }
                }
            }

            if (visited[sink] == 0) {
                break;
            }

            int pathFlow = INF;
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, capacity[u][v]);
            }

            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                capacity[u][v] -= pathFlow;
                capacity[v][u] += pathFlow;
            }

            maxFlow += pathFlow;
        }

        return maxFlow;
    }
}
