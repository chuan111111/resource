import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class force {
    public static void main(String[] args) {
        QReader in=new QReader();
        int n= in.nextInt();
        int l= in.nextInt();
        int r= in.nextInt();
        int[] arr=new int[n];
        for (int i = 0; i < n; i++) {
            arr[i]= in.nextInt();
        }
        int re=0;
        for (int i = 0; i <n ; i++) {
            for (int j = i; j <n ; j++) {
                int count=0;
                for (int k = i; k <=j ; k++) {
                    count+=arr[k];
                }
                if (l<=count &&count<=r){
                    re++;
                }
            }
        }

        System.out.println(re);
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
