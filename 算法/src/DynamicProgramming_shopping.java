import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class DynamicProgramming_shopping {




    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        int n= in.nextInt();
        int m= in.nextInt();

       int[] w=new int[n];
       int[] c=new int[n];
        for (int i = 0; i < n; i++) {
            w[i]= in.nextInt();
        }
        for (int i = 0; i < n; i++) {
            c[i]= in.nextInt();
        }

        int[][] dp = new int[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                dp[i][j] = dp[i - 1][j];

                if (j >= c[i - 1]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - c[i - 1]] + w[i - 1]);
                }
            }
        }
        System.out.println(dp[n][m]);


    }

}
