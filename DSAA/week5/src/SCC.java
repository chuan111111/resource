import java.io.*;
import java.util.*;

public class SCC {
    public static int[] count;

    public static List<List<Integer>> graph = new ArrayList<>();

    public static  int n;
    // A class to represent a node in the graph
    static class Node {
        int id; // node id
        int dist; // distance from node 1

        public Node(int id, int dist) {
            this.id = id;
            this.dist = dist;
        }


    }


    public static void dijkstra() {
        // Create an array to store the distances
        int[] dist = new int[n + 1];

        // Create an array to store the number of shortest paths
        count = new int[n + 1];

        // Initialize all distances to infinity except node 1
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[1] = 0;

        // Initialize the count of shortest paths to 0 except node 1
        Arrays.fill(count, 0);
        count[1] = 1;

        // Create a priority queue to store the nodes
        PriorityQueue<Node> pq = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.dist - o2.dist;
            }
        });

        // Add node 1 to the queue with distance 0
        pq.add(new Node(1, 0));

        // Loop until the queue is empty
        while (!pq.isEmpty()) {

            Node node = pq.poll();

            int u = node.id;
            int d = node.dist;
            if (d<=dist[u]){
                for (int v : graph.get(u)) {

                    if (dist[v] > dist[u] + 1) {

                        dist[v] = dist[u] + 1;

                        count[v] = count[u];

                        pq.add(new Node(v, dist[v]));
                    }
                    // If the distance to v is equal to the current distance + 1
                    else if (dist[v] == dist[u] + 1) {
                        // Add the count of shortest paths from u to v
                        count[v] = (count[v] + count[u]) ;
                    }
                }
            }
        }

        // Print the number of shortest paths for each node

    }

    public static void main(String[] args) {
        QReader in = new QReader();
        QWriter out = new QWriter();

        n = in.nextInt();
        int m = in.nextInt();


        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            int x = in.nextInt();
            int y = in.nextInt();
            graph.get(x).add(y);
            graph.get(y).add(x);
        }


        dijkstra();

        for (int i = 1; i <= n; i++) {
            out.println(count[i]);
        }
        out.close();
    }

    static   class QReader {
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

    static class QWriter implements Closeable {
        private BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        public void print(Object object) {
            try {
                writer.write(object.toString());
            } catch (IOException e) {
                return;
            }
        }

        public void println(Object object) {
            try {
                writer.write(object.toString());
                writer.write("\n");
            } catch (IOException e) {
                return;
            }
        }

        @Override
        public void close() {
            try {
                writer.close();
            } catch (IOException e) {
                return;
            }
        }
    }
}
