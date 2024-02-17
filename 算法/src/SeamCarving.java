import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.StringTokenizer;

public class SeamCarving {

    public static void main(String[] args) {
        QReader scanner=new QReader();


        // Read input dimensions
        int m = scanner.nextInt();
        int n = scanner.nextInt();

        // Initialize disruption measures array
        int[][] d = new int[m][n];

        // Read pixel values
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                d[i][j] = scanner.nextInt();
            }
        }

        // Initialize memoization array
        int[][] memo = new int[m][n];


        // Find the seam with the lowest disruption measure
        int result = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            result = Math.min(result, findLowestDisruptionMeasure(d, m - 1, i, memo));
        }

        // Output the result
        System.out.println(result);

    }

    private static int findLowestDisruptionMeasure(int[][] d, int i, int j, int[][] memo) {
        if (i == 0) {
            return d[i][j];
        }

        if (memo[i][j] != 0) {
            return memo[i][j];
        }

        int left = findLowestDisruptionMeasure(d, i - 1, Math.max(j - 1, 0), memo);
        int middle = findLowestDisruptionMeasure(d, i - 1, j, memo);
        int right = findLowestDisruptionMeasure(d, i - 1, Math.min(j + 1, d[0].length - 1), memo);

        int minDisruption = Math.min(left, Math.min(middle, right));
        memo[i][j] = d[i][j] + minDisruption;

        return memo[i][j];
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
