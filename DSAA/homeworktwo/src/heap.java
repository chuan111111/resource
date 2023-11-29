import java.io.*;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class heap {
    public static void main(String[] args) {
        QReader in=new QReader();
        QWriter out=new QWriter();
        int n= in.nextInt();
        PriorityQueue<Integer> pri=new PriorityQueue<>();
        for (int i = 0; i < n; i++) {
            int x=in.nextInt();
            if (x==1){
                pri.add(in.nextInt());
            }else if (x==2){
                out.println(pri.peek());
            }else {
                pri.poll();
            }
        }
        out.close();
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
        public String next() {
            hasNext();
            return tokenizer.nextToken();
        }
        public int nextInt() {
            return Integer.parseInt(next());
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

