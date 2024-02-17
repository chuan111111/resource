import java.util.Scanner;

public class Rod_cutting {
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        int n= in.nextInt();
        int[] arr=new int[n+1];
        for (int i = 1; i <n+1 ; i++) {
            arr[i]= in.nextInt();
        }

        System.out.println(findoptimal(arr,n));

    }

    public static int findoptimal(int[] prices,int n){
        int[] val = new int[n + 1];
        val[0] = 0;
        val[1]=prices[1];
        // Build the table val[] in a bottom-up manner
        for (int i = 2; i <= n; i++) {
            int maxVal = prices[i];
            for (int j = 1; j <= i/2; j++) {
                maxVal = Math.max(maxVal, prices[j] + val[i - j]);
            }
            val[i] = maxVal;
        }


        return val[n];

    }
}
