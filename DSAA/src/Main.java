import java.io.*;
import java.util.*;

public class Main {
    static ArrayList<ArrayList<int[]>> list=new ArrayList<>();
    static List<Integer> arr = new ArrayList<>();
    public static void main(String[] args){
        QReader in = new QReader();
        QWriter out = new QWriter();
        int n =in.nextInt();
        int m = in.nextInt();

        for (int i = 0; i <= n; i++) {

            list.add(new ArrayList<>());
        }
        int min=Integer.MAX_VALUE;
        int start=0;
        for (int i = 0; i < m; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            int weigh=in.nextInt();
            list.get(u).add(new int[]{v,weigh});
            if (min>weigh){
                min=weigh;
                start=u;
            }
        }

        int[] vis = new int[n+1];

        int ans = 0;
        for (int i = 0; i < n-1; i++) {
            //将该节点加入到集合中
            arr.add(start);
            //标记
            vis[start] = 1;
            int val = Integer.MAX_VALUE;
            //寻找最小边
            for (int row : arr) {
                for (int j = 0; j < list.get(row).size(); j++) {
                    if (vis[list.get(row).get(j)[0]]==0){
                        if (list.get(row).get(j)[1]<val){
                            start = list.get(row).get(j)[0];
                            val = list.get(row).get(j)[1];
                        }
                    }else {
                        list.get(row).remove(list.get(row).get(j));
                        j--;
                        if (list.get(row).size()==0){
                            break;
                        }
                    }
                }


            }
            ans += val;
        }
        out.println(ans);

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
