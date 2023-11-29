import java.io.*;
import java.util.*;

public class prim {

    // A class to represent an edge in the graph
    static class Edge {
        int src, dest, weight;

        public Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }
    }

    // A class to represent a graph
    static class Graph {
        int V; // number of vertices
        List<Edge>[] adj; // adjacency list of edges

        public Graph(int V) {
            this.V = V;
            adj = new ArrayList[V];
            for (int i = 0; i < V; i++) {
                adj[i] = new ArrayList<>();
            }
        }

        // A method to add an edge to the graph
        public void addEdge(int src, int dest, int weight) {
            adj[src].add(new Edge(src, dest, weight));
            adj[dest].add(new Edge(dest, src, weight));
        }

        // A method to find the minimum spanning tree using Prim's algorithm
        public int primMST() {
            // Create a boolean array to store the visited vertices
            boolean[] mstSet = new boolean[V];

            // Create an array to store the key value of each vertex
            int[] key = new int[V];

            // Initialize all the arrays
            for (int i = 0; i < V; i++) {
                key[i] = Integer.MAX_VALUE;
                mstSet[i] = false;
            }

            // Start from the first vertex and assign it a key value of 0
            key[0] = 0;

            // Loop until all the vertices are visited
            for (int count = 0; count < V - 1; count++) {
                // Find the vertex with the minimum key value that is not visited
                int u = -1;
                int minKey = Integer.MAX_VALUE;
                for (int v = 0; v < V; v++) {
                    if (!mstSet[v] && key[v] < minKey) {
                        u = v;
                        minKey = key[v];
                    }
                }

                // Mark the vertex as visited
                mstSet[u] = true;

                // Iterate over all the adjacent edges of the vertex
                for (Edge edge : adj[u]) {
                    // If the destination vertex is not visited and the edge weight is smaller than the current key value
                    if (!mstSet[edge.dest] && edge.weight < key[edge.dest]) {
                        // Update the key value of the destination vertex
                        key[edge.dest] = edge.weight;
                    }
                }
            }

            // Calculate the sum of weights of all the edges in the MST
            int sum = 0;
            for (int i = 0; i < V; i++) {
                sum += key[i];
            }

            // Return the sum as the answer
            return sum;
        }
    }

    public static void main(String[] args) {
        QReader in = new QReader();
        QWriter out = new QWriter();

        // Read the number of vertices and edges from input
        int n = in.nextInt();
        int m = in.nextInt();

        // Create a graph object with n vertices
        Graph g = new Graph(n);

        // Read and add m edges to the graph from input
        for (int i = 0; i < m; i++) {
            int x = in.nextInt() - 1; // subtract 1 to make it zero-based indexing
            int y = in.nextInt() - 1; // subtract 1 to make it zero-based indexing
            int z = in.nextInt();
            g.addEdge(x, y, z);
        }
        out.println(g.primMST());
        out.close();
        // Find and print the value of the minimum spanning tree using Prim's algorithm
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