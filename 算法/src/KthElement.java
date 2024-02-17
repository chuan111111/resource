import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

public class KthElement {
    public static void main(String[] args) {
        QReader input = new QReader();
        int T = input.nextInt();
        int n = input.nextInt();
        int[] A = new int[n];
        int[] B = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = input.nextInt();
        }
        for (int i = 0; i < n; i++) {
            B[i] = input.nextInt();
        }
        for (int i = 0; i < T; i++) {
            int l = input.nextInt();
            int r = input.nextInt();
            int k = input.nextInt();
            System.out.println(findKthSmallest(A, B, k, l - 1, r - 1, l - 1, r - 1));
        }
    }

    public static int findKthSmallest(int[] arr1, int[] arr2, int k, int start1, int end1, int start2, int end2) {
        // 计算每个数组中要考虑的元素个数
        int len1 = end1 - start1 + 1;
        int len2 = end2 - start2 + 1;

        // 确保arr1是较短的数组，如果不是，交换数组和长度的值
        if (len1 > len2) {
            return findKthSmallest(arr2, arr1, k, start2, end2, start1, end1);
        }

        // 如果arr1为空，返回arr2中的第k小值
        if (len1 == 0) {
            return arr2[start2 + k - 1];
        }

        // 如果k等于1，返回两个数组中第一个元素的最小值
        if (k == 1) {
            return Math.min(arr1[start1], arr2[start2]);
        }

        // 在arr1和arr2中分别找到第k/2小的值
        int i = start1 + Math.min(len1, k / 2) - 1;
        int j = start2 + Math.min(len2, k / 2) - 1;

        // 如果arr1的值小于arr2的值，丢弃arr1中的前i+1个元素
        if (arr1[i] < arr2[j]) {
            return findKthSmallest(arr1, arr2, k - (i - start1 + 1), i + 1, end1, start2, end2);
        }
        // 如果arr1的值大于等于arr2的值，丢弃arr2中的前j+1个元素
        else {
            return findKthSmallest(arr1, arr2, k - (j - start2 + 1), start1, end1, j + 1, end2);
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

