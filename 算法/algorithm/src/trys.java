import java.util.Scanner;

public class trys {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt(); // Number of queries

            int n = scanner.nextInt(); // Length of arrays A and B
            int[] A = new int[n + 1];
            int[] B = new int[n + 1];

            for (int i = 1; i <= n; i++) {
                A[i] = scanner.nextInt();
            }

            for (int i = 1; i <= n; i++) {
                B[i] = scanner.nextInt();
            }

           // Number of queries for the current test case


            while (T-- > 0) {
                int l = scanner.nextInt();
                int r = scanner.nextInt();
                int k = scanner.nextInt();

                int result = findKthElement(A, B, l, r, k);
                System.out.println(result);
            }

    }

    private static int findKthElement(int[] A, int[] B, int l, int r, int k) {
        int low = Math.min(A[l], B[l]);
        int high = Math.max(A[r], B[r]);

        while (low < high) {
            int mid = low + (high - low) / 2;

            int countA = countLessEqual(A, mid, l, r);
            int countB = countLessEqual(B, mid, l, r);
            int totalCount = countA + countB;

            if (totalCount < k) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        return low;
    }

    private static int countLessEqual(int[] array, int value, int l, int r) {
        int count = 0;

        for (int i = l; i <= r; i++) {
            if (array[i] <= value) {
                count++;
            }
        }

        return count;
    }
}
