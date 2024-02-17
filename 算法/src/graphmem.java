import java.util.*;

public class graphmem {
    static final int inf = (int) 1e9 + 7;

    static class Edge {
        int to, weight;

        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    static class Node {
        int vertex, distance;

        public Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }

    static int n, m, k, p;
    static List<List<Edge>> graph;
    static int[] shortestPath;
    static long[] pathCount;
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
            pathCount = new long[n + 1];
            visited = new boolean[n + 1];

            Arrays.fill(shortestPath, inf);
            Arrays.fill(pathCount, -1);

            for (int i = 0; i <= n; i++) {
                graph.add(new ArrayList<>());
            }

            for (int i = 0; i < m; i++) {
                int u = scanner.nextInt();
                int v = scanner.nextInt();
                int w = scanner.nextInt();
                graph.get(u).add(new Edge(v, w));
            }

            dijkstra();

            long result = dfs(1, k);

            if (result == -1) {
                System.out.println("-1");
            } else {
                System.out.println(result % p);
            }
        }
    }

    static void dijkstra() {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.distance));
        pq.add(new Node(1, 0));
        shortestPath[1] = 0;

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (visited[current.vertex]) {
                continue;
            }

            visited[current.vertex] = true;

            for (Edge neighbor : graph.get(current.vertex)) {
                int newDistance = current.distance + neighbor.weight;

                if (newDistance < shortestPath[neighbor.to]) {
                    shortestPath[neighbor.to] = newDistance;
                    pq.add(new Node(neighbor.to, newDistance));
                }
            }
        }
    }

    static long dfs(int node, int remainingDistance) {
        if (remainingDistance < 0) {
            return 0;
        }

        if (pathCount[node] != -1) {
            return pathCount[node];
        }

        if (node == n) {
            return 1;
        }

        long paths = 0;

        for (Edge neighbor : graph.get(node)) {
            paths += dfs(neighbor.to, remainingDistance - neighbor.weight);

        }

        pathCount[node] = paths;

        return paths;
    }
}
