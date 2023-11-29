import java.io.*;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class miniSpaningTree {
    public static void main(String[] args) {
        QReader in=new QReader();
        QWriter out=new QWriter();
        int t= in.nextInt();
        for (int i = 0; i < t; i++) {
            int vertice= in.nextInt();
            int edge= in.nextInt();
            PriorityQueue<edge> queue=new PriorityQueue<>(Comparator.comparingInt(o -> o.weight));
            for (int j = 0; j < edge; j++) {
                queue.add(new edge(in.nextInt(), in.nextInt(), in.nextInt()));
            }
            int[] set=new int[vertice+1];
            for (int j = 1; j <= vertice; j++) {
                set[j]=j;
            }
            int total=0;
            while (!queue.isEmpty()){
              edge edge1=queue.poll();
                int u = edge1.u;
                int v = edge1.v;

                int uSet = findset(set, u);
                int vSet = findset(set, v);

                if (uSet != vSet) {
                    total += edge1.weight;
                    union(set, uSet, vSet);
                }
            }
            out.println(total);

        }
        out.close();

    }
    static int findset(int[] set,int i){
        if (set[i] == i)
            return i;
        return set[i]=findset(set, set[i]);
    }
    static  void union(int[] set, int x, int y){
        int xSet = findset(set, x);
        int ySet = findset(set, y);
        set[xSet] = ySet;
    }
    static class edge{
        int u, v, weight;

        public edge(int u, int v, int weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }
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
