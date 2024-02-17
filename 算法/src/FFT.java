import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class FFT {

    // Recursive FFT function
    private static void fft(double[] aReal, double[] aImag, int type) {
        int n = aReal.length;

        if (n <= 1) {
            return;
        }

        double[] a0Real = new double[n / 2];
        double[] a0Imag = new double[n / 2];
        double[] a1Real = new double[n / 2];
        double[] a1Imag = new double[n / 2];

        // Split array into even and odd parts
        for (int i = 0; i < n / 2; i++) {
            a0Real[i] = aReal[2 * i];
            a0Imag[i] = aImag[2 * i];
            a1Real[i] = aReal[2 * i + 1];
            a1Imag[i] = aImag[2 * i + 1];
        }

        // Recursive FFT
        fft(a0Real, a0Imag, type);
        fft(a1Real, a1Imag, type);

        // Combine results
        for (int k = 0; k < n / 2; k++) {
            double angle = type * 2.0 * Math.PI * k / n;
            double wReal = Math.cos(angle);
            double wImag = Math.sin(angle);

            double tReal = wReal * a1Real[k] - wImag * a1Imag[k];
            double tImag = wReal * a1Imag[k] + wImag * a1Real[k];

            aReal[k] = a0Real[k] + tReal;
            aImag[k] = a0Imag[k] + tImag;
            aReal[k + n / 2] = a0Real[k] - tReal;
            aImag[k + n / 2] = a0Imag[k] - tImag;
        }
    }

    // Polynomial convolution using FFT


    public static void main(String[] args) {
        QReader in=new QReader();
        QWriter out=new QWriter();
        int n= in.nextInt();
        int m= in.nextInt();

        int[] coefficientsA = new int[n+1]; // 4 + 3x + 2x^2
        int[] coefficientsB = new int[m+1]; // 1 + 2x + 3x^2
        for (int i = 0; i < n+1; i++) {
            coefficientsA[i]= in.nextInt();
        }
        for (int i = 0; i < m+1; i++) {
            coefficientsB[i]= in.nextInt();
        }
        int size = 1;
        while (size < coefficientsA.length + coefficientsB.length - 1) {
            size <<= 1;
        }

        double[] aReal = new double[size];
        double[] aImag = new double[size];
        double[] bReal = new double[size];
        double[] bImag = new double[size];

        // Initialize input arrays with real coefficients
        for (int i = 0; i < coefficientsA.length; i++) {
            aReal[i] = coefficientsA[i];
        }

        for (int i = 0; i < coefficientsB.length; i++) {
            bReal[i] = coefficientsB[i];
        }

        // FFT for polynomial A
        fft(aReal, aImag, 1);
        // FFT for polynomial B
        fft(bReal, bImag, 1);

        // Pointwise multiplication
        for (int i = 0; i < size; i++) {
            double tReal = aReal[i] * bReal[i] - aImag[i] * bImag[i];
            double tImag = aReal[i] * bImag[i] + aImag[i] * bReal[i];
            aReal[i] = tReal;
            aImag[i] = tImag;
        }

        // Inverse FFT for the convolution result
        fft(aReal, aImag, -1);

        for (int i = 0; i < n+m+1; i++) {
           out.print((int) (aReal[i] / size + 0.5)+" ");
        }
out.close();
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
}
