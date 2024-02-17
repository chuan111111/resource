import java.io.*;
import java.util.*;

public class payyear {

    public static void main(String[] args) {

        QReader in=new QReader();
        QWriter out=new QWriter();
       int n= in.nextInt(); int m= in.nextInt();int p= in.nextInt();int q= in.nextInt();
        Queue<Integer> qu=new LinkedList<>();
        Queue<Integer> q1=new LinkedList<>();
        Queue<Integer> q2=new LinkedList<>();//q1出q2入，记录当前天和下一天的node
       int max=0;
       int[] visited=new int[n+1];
        List<List<Integer>> list=new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            list.add(new ArrayList<>());
        }
        for (int i = 0; i < m; i++) {
            int x= in.nextInt();
            int y=in.nextInt();
            list.get(x).add(y);
            list.get(y).add(x);
        }

        for (int i = 0; i < q; i++) {
            int a= in.nextInt();
            qu.add(a);
            if (a>max){
                max=a;
            }
        }

        int[] result=new int[max+1];
        result[0]=1;
        q1.add(p);
        visited[p]=1;
        int day=0;
        for (int i = 0; i < max; i++) {
                while (!q1.isEmpty()){
                    int j=q1.poll();
                    for (int e:list.get(j)
                         ) {
                        if (visited[e]==0){
                        q2.add(e);
                        visited[e]=1;}
                    }
                }
                day++;
                result[day]=result[day-1]+q2.size();
                if (result[day]==n){
                    while (day<max){
                        day++;
                    result[day]=n;}
                    break;
                }
                while (!q2.isEmpty()){
                    q1.add(q2.poll());
                }
        }
        while (!qu.isEmpty()){
            out.print(result[qu.poll()]+" ");
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
