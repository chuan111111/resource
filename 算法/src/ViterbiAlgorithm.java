import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ViterbiAlgorithm {

    static int n;
    static int m;
    static int s;
    static int t;
    static int[][] mem;
    static int[][] E;

    public static void main(String[] args) {
        QReader scanner=new QReader();


        // Read input parameters
        n = scanner.nextInt();
        m = scanner.nextInt();
        s = scanner.nextInt();
        t = scanner.nextInt();

        // Initialize the transition matrix E
         E = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                E[i][j] = scanner.nextInt();
            }
        }
        mem=new int[n][m+2];
        int re=Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {

            re=Math.min( s*s-E[i][0]*E[i][0]+s*E[i][0]+calculateMinimumCost(i,1),re);
        }

        System.out.println(re);



    }

    public static int calculateMinimumCost(int a, int b) {
       if (mem[a][b]!=0){
           return mem[a][b];
       }
       int re=Integer.MAX_VALUE;
       if (b==m){
           return mem[a][b]=E[a][b-1]*E[a][b-1]-t*t+t*E[a][b-1];
       }
        for (int i = 0; i < n; i++) {

           re=Math.min(re,E[a][b-1]*E[a][b-1]-E[i][b]*E[i][b]+E[i][b]*E[a][b-1]+calculateMinimumCost(i,b+1)) ;
        }
        return mem[a][b]=re;

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
