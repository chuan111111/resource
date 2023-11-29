import java.io.*;
import java.util.*;

public class shortestpath {
    static ArrayList<ArrayList<int[]>> list=new ArrayList<>();
    public static void main(String[] args){
        QReader in = new QReader();
        QWriter out = new QWriter();
            int n =in.nextInt();
            int m = in.nextInt();

            int[] dist = new int[n+1];
            Arrays.fill(dist,Integer.MAX_VALUE);
            dist[1]=0;

        for (int i = 0; i <= n; i++) {

            list.add(new ArrayList<>());
        }
            for (int i = 0; i < m; i++) {
                int u = in.nextInt();
                int v = in.nextInt();
                int weigh=in.nextInt();
                list.get(u).add(new int[]{v,weigh});

            }

            int[] visited = new int[n+1];

        PriorityQueue<int[]> pq = new PriorityQueue<>();
        pq.add(new int[]{1, 0});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            if (visited[curr[0]]==0) {
                visited[curr[0]] = 1;
            }

            for (int[] next : list.get(curr[0])) {
                if (dist[next[0]] > dist[curr[0]] + next[1]) {
                    dist[next[0]] = dist[curr[0]] + next[1];
                    pq.add(new int[]{next[0], dist[next[0]]});
                }
            }
        }

            for (int i = 1; i < n; i++) {
                if (dist[i]==Integer.MAX_VALUE){
                    out.print(-1+" ");
                }else {
                out.print(dist[i] + " ");}
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
