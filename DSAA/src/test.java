import java.util.Scanner;

public class test {

    public static void DecTest(int a[]) {
        System.out.println(mergeSort(a));
    }

    private static int mergeSort(int a[]) {
        int count = 0;
        int n = a.length;
        if (n <= 1) {
            return 0;
        }
        int b[] = new int[n / 2];
        System.arraycopy(a, 0, b, 0, n / 2);
        int c[];
        if (n % 2 == 0) {
            c = new int[n / 2];
            System.arraycopy(a, n / 2, c, 0, n / 2);
        } else {
            c = new int[n / 2 + 1];
            System.arraycopy(a, n / 2, c, 0, n / 2 + 1);
        }

        count += mergeSort(b);
        count += mergeSort(c);
        count += merge(b, c, a);
        return count;
    }

    private static int merge(int b[], int c[], int a[]) {
        int count = 0;
        int i = 0, j = 0, k = 0;
        int p = b.length, q = c.length;
        while (i < p && j < q) {
            if (b[i] <= c[j]) {
                a[k] = b[i];
                i++;
            } else {
                a[k] = c[j];
                j++;
                count += p - i;
            }
            k++;
        }
        if (i == p) {
            for (; j < q; j++, k++) {
                a[k] = c[j];
            }
        } else if (j == q) {
            for (; i < p; i++, k++) {
                a[k] = b[i];
            }
        }
        return count;
    }

    public static void main(String args[]) {
        Scanner in=new Scanner(System.in);
        int a[];
        int n= in.nextInt();
        a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i]=in.nextInt();
        }
        DecTest(a);

    }
}