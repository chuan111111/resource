import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Bellman_Ford {
    static List<List<int[]>> list=new ArrayList<>();
    public static void main(String[] args) {
        QWriter out=new QWriter();
        QReader in=new QReader();
        int t= in.nextInt();
        for (int i = 0; i < t; i++) {
            int vertices= in.nextInt();
            int edges= in.nextInt();
            list=new ArrayList<>();
            for (int j = 0; j <= vertices; j++) {
                list.add(new ArrayList<>());
            }
            int[] update=new int[vertices+1];
            int[] m=new int[vertices+1];
            int des=1;
            Arrays.fill(m,Integer.MAX_VALUE);
            for (int j = 0; j < edges; j++) {
                int x= in.nextInt();
                int y=in.nextInt();
                int weight= in.nextInt();
                list.get(y).add(new int[]{x, weight});

                if (y==des){
                    m[x]=weight;
                    update[x]=1;
                }
            }


            int[] successor=new int[vertices+1];
            for (int j = 1; j <vertices ; j++) {
                for (int k = 1; k <vertices+1 ; k++) {
                    if (update[k]==1){
                        for (int[] e:list.get(k)
                             ) {
                            if (m[e[0]]>m[k]+e[1]){
                                m[e[0]]=m[k]+e[1];
                                successor[e[0]]=k;
                            }
                        }
                    }
                }

            }
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
