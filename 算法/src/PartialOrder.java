import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class PartialOrder {

    public static void main(String[] args) {
        QReader scanner=new QReader();
        QWriter out=new QWriter();

        // Read the length of the sequence
        int n = scanner.nextInt();

        // Read the three-dimensional sequence A
        int[][] sequence = new int[n][3];
        for (int i = 0; i < n; i++) {
            sequence[i][0] = scanner.nextInt();
            sequence[i][1] = scanner.nextInt();
            sequence[i][2] = scanner.nextInt();
        }

        // Calculate the count of pairs for each element in the sequence
        for (int i = 0; i < n; i++) {
            int count = 0;
            for (int j = 0; j < n; j++) {
                if (i != j && 
                    sequence[j][0] <= sequence[i][0] &&
                    sequence[j][1] <= sequence[i][1] &&
                    sequence[j][2] <= sequence[i][2]) {
                    count++;
                }
            }
            out.println(count);
        }

      out.close();
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

}
