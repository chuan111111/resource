import java.util.Arrays;
import java.util.Scanner;

public class FFT {
    // 进行FFT的主函数
    public static int[] fft(int[] a, int[] b) {
        int n = 1;
        while (n < a.length + b.length - 1) {
            n <<= 1;
        }

        // 将多项式系数补齐到2的幂
        int[] fa = Arrays.copyOf(fft(a, n, 1), n);
        int[] fb = Arrays.copyOf(fft(b, n, 1), n);

        // 点值相乘
        for (int i = 0; i < n; i++) {
            fa[i] *= fb[i];
        }

        // 进行逆FFT
        fft(fa, n, -1);

        // 四舍五入取整
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = Math.round(fa[i] / (float) n);
        }

        return result;
    }

    // FFT核心算法
    private static int[] fft(int[] a, int n, int sign) {
        if (n == 1) {
            return a;
        }

        int[] a0 = new int[n / 2];
        int[] a1 = new int[n / 2];

        for (int i = 0, j = 0; i < n; i += 2, j++) {
            if (i < a.length) {
                a0[j] = a[i];
            }
            if (i + 1 < a.length) {
                a1[j] = a[i + 1];
            }
        }

        int[] y0 = fft(Arrays.copyOf(a0, n / 2), n / 2, sign);
        int[] y1 = fft(Arrays.copyOf(a1, n / 2), n / 2, sign);

        double angle = sign * 2 * Math.PI / n;
        int wReal = 1;
        int wImaginary = 0;

        for (int k = 0; k < n / 2; k++) {
            int even = y0[k];
            int oddReal = wReal * y1[k];
            int oddImaginary = wImaginary * y1[k];

            a[k] = even + oddReal;
            a[k + n / 2] = even - oddReal;

            int nextWReal = (int) (wReal * Math.cos(angle) - wImaginary * Math.sin(angle));
            int nextWImaginary = (int) (wReal * Math.sin(angle) + wImaginary * Math.cos(angle));
            wReal = nextWReal;
            wImaginary = nextWImaginary;
        }

        return a;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 读取第一行的两个数字
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        // 读取第二行的多项式系数
        int[] a = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            a[i] = scanner.nextInt();
        }

        // 读取第三行的多项式系数
        int[] b = new int[m + 1];
        for (int i = 0; i <= m; i++) {
            b[i] = scanner.nextInt();
        }

        // 计算结果
        int[] result = fft(a, b);

        // 输出结果
        System.out.println(Arrays.toString(result));
    }
}
