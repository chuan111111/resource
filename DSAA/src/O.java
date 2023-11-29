import java.io.*;
import java.util.*;

public class O {
    static int[] visited;
    static int max=1;
    static int finals;
    static int[] maxs;
    static List<List<Integer>> list=new ArrayList<>();
    public static void main(String[] args) {
        QReader in = new QReader();
        QWriter out = new QWriter();
        int t=in.nextInt();
        int edge=in.nextInt();

            for (int i = 0; i <= t; i++) {
                list.add(new ArrayList<>());
            }
            for (int i = 0; i < edge; i++) {
                int x = in.nextInt();
                int y = in.nextInt();
                list.get(x).add(y);
            }
        for (int i = 0; i < t; i++) {
            if (list.get(i)!=null){
            Collections.reverse(list.get(i));}
        }
            
            maxs=new int[t];
            finals=t;
            visited = new int[t + 1];
            for (int i = 1; i < t ; i++) {
                if (visited[i] == 0) {
                    dfs(i);
                }
                Arrays.fill(visited,0);
                maxs[i-1]=max;
                out.print(max+" ");
                max=0;
            }
        out.print(t);
            out.close();



    }
    public static void dfs(int initial){
        if (initial>max){
            max=initial;
        }
        if (max==finals){
            return;
        }
        visited[initial]=1;
        for (int i = 0; i < list.get(initial).size(); i++) {
            int e=list.get(initial).get(i);
            if (maxs[e-1]!=0) {
                if (max >= maxs[e - 1]){
                return;}else {
                    max=maxs[e-1];
                }
            }
            if (maxs[e-1]==finals){
                max=finals;
                return;
            }
            if (max==finals){
                return;
            }
            if (visited[e]==0){
                dfs(e);
            }
        }

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
