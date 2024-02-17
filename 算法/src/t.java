import java.util.*;

public class t {
    static final int inf = (int) 1e9 + 7;

    static class Edge {
        int to, weight;

        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    static int n, m, k, p;
    static List<List<Edge>> graph;
    static int[] shortestPath;
    static long[][] dp;
    static boolean[] visited;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int t = scanner.nextInt();

        while (t-- > 0) {
            n = scanner.nextInt();
            m = scanner.nextInt();
            k = scanner.nextInt();
            p = scanner.nextInt();

            graph = new ArrayList<>();
            shortestPath = new int[n + 1];
            dp = new long[n + 1][k + 1];
            visited = new boolean[n + 1];

            Arrays.fill(shortestPath, inf);
            for (int i = 0; i <= n; i++) {
                Arrays.fill(dp[i], -1);
                graph.add(new ArrayList<>());
            }

            for (int i = 0; i < m; i++) {
                int u = scanner.nextInt();
                int v = scanner.nextInt();
                int w = scanner.nextInt();
                graph.get(u).add(new Edge(v, w));
            }

            dijkstra(1);
            long result = dfs(1, 0);

            System.out.println(result);
        }
    }

    static void dijkstra(int start) {
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(edge -> edge.weight));
        pq.add(new Edge(start, 0));
        shortestPath[start] = 0;

        while (!pq.isEmpty()) {
            Edge current = pq.poll();

            if (visited[current.to]) {
                continue;
            }

            visited[current.to] = true;

            for (Edge neighbor : graph.get(current.to)) {
                int newDistance = current.weight + neighbor.weight;

                if (newDistance < shortestPath[neighbor.to]) {
                    shortestPath[neighbor.to] = newDistance;
                    pq.add(new Edge(neighbor.to, newDistance));
                }
            }
        }
    }

    static long dfs(int node, int remainingDistance) {
        if (remainingDistance > k) {
            return 0;
        }

        if (node == n) {
            return 1;
        }

        if (dp[node][remainingDistance] != -1) {
            return dp[node][remainingDistance];
        }

        long paths = 0;

        for (Edge neighbor : graph.get(node)) {
            paths += dfs(neighbor.to, remainingDistance + neighbor.weight);
            paths %= p;
        }

        dp[node][remainingDistance] = paths;

        return paths;
    }
}
