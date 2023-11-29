import java.io.*;
import java.util.*;

public class tuopu {
    static int[] inde;
    static int cos=0;

    static int[] dist;
    static int[] time;
    static List<List<Integer>> list=new ArrayList<>();
    public static void main(String[] args) {
        QReader in = new QReader();
        QWriter out = new QWriter();
        int t= in.nextInt();
        time=new int[t+1];
        inde=new int[t+1];
        for (int i = 0; i <= t; i++) {
            list.add(new ArrayList<>());
        }
        for (int i = 1; i <= t; i++) {
         int x=in.nextInt();
            time[x]= in.nextInt();
            int pre= in.nextInt();
            while (pre!=0){
            list.get(pre).add(x);
            inde[x]++;
            pre= in.nextInt();
            }
        }

        dist = new int[t + 1];
        PriorityQueue<Integer> q=new PriorityQueue<>();
        for (int i = 1; i <=t ; i++) {
            if (inde[i]==0){
                q.add(i);
                dist[i]=time[i];
            }
        }

        while (!q.isEmpty()){
            int x=q.poll();

            for (int i = 0; i < list.get(x).size(); i++) {
                int k=list.get(x).get(i);
                inde[k]--;
                if (dist[k]<dist[x]+time[k]){
                    dist[k]=dist[x]+time[k];
                }
                if (cos<dist[k]){
                    cos=dist[k];
                }
                if (inde[k]==0){
                    q.add(k);
                }
            }
        }


        out.println(cos);
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
