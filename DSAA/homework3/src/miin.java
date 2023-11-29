import java.io.*;
import java.util.*;

public class miin {

    static List<List<Integer>> list=new ArrayList<>();
    static int[] inde;
    static List<Integer> res=new ArrayList<>();

    public static void main(String[] args) {
        QReader in = new QReader();
        QWriter out = new QWriter();
        int n= in.nextInt();
        int m= in.nextInt();

            inde=new int[n+1];
            for (int i = 0; i <= n; i++) {
                list.add(new ArrayList<>());
            }
            for (int i = 0; i < m; i++) {
                int x = in.nextInt();
                int y = in.nextInt();
                list.get(x).add(y);
               inde[y]++;
            }
            PriorityQueue<Integer> q=new PriorityQueue<>();
            for (int i = 1; i <=n ; i++) {
                if (inde[i]==0){
                    q.add(i);
                }
            }
            while (!q.isEmpty()){
                int x=q.poll();
                res.add(x);
                for (int i = 0; i < list.get(x).size(); i++) {
                    inde[list.get(x).get(i)]--;
                    if (inde[list.get(x).get(i)]==0){
                        q.add(list.get(x).get(i));
                    }
                }
            }
            if (res.size()<n){
                out.print(-1);
            }else {
                for (int i = 0; i < res.size(); i++) {
                    out.print(res.get(i)+" ");
                }
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
