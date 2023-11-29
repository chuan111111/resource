import java.io.*;
import java.util.*;

public class bipargraph {
    public static void main(String[] args) {
        QReader in=new QReader();
        QWriter out=new QWriter();
        int t= in.nextInt();
        for (int i = 0; i < t; i++) {
            int n= in.nextInt();
            int m= in.nextInt();
            List<List<Integer>> list=new ArrayList<>();
            for (int j = 0; j <= n; j++) {
                list.add(new ArrayList<>());
            }
            boolean result=true;
            for (int j = 0; j < m; j++) {
                int x= in.nextInt();
                int y=in.nextInt();

                list.get(x).add(y);
                list.get(y).add(x);
            }

            int[] colored=new int[n+1];
            loop:  for (int j = 1; j <=n ; j++) {
                if (colored[j]==0){
                    Queue<Integer> qu=new LinkedList<>();
                    qu.add(j);
                    colored[j]=1;
                      while (!qu.isEmpty()){
                        int q= qu.poll();
                        for (int e:list.get(q)) {
                            if (colored[e]==0){
                                if (colored[q]==1){
                                    colored[e]=2;
                                }else {
                                    colored[e]=1;
                                }
                                qu.add(e);
                            }else if (colored[e]==colored[q]){
                                result=false;
                                break loop;
                            }
                        }
                    }
                }

            }

            out.println(result);
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
                if (nextLine == null ) {
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
