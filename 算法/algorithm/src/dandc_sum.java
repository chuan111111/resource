import java.io.*;
import java.util.StringTokenizer;

public class dandc_sum {
    public static void main(String[] args) {
        QReader in=new QReader();
        int n= in.nextInt();
        int l= in.nextInt();
        int r= in.nextInt();
        int[] arr=new int[n];
        for (int i = 0; i < n; i++) {
            arr[i]= in.nextInt();
        }

        long[] preSums = new long[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            preSums[i + 1] = preSums[i] + arr[i];
        }
        long[] tmp=new long[n+1];

        System.out.println(mergeSort(preSums, 0, preSums.length - 1, l, r,tmp));

    }

    /**
     * 归并排序
     */
    public  static long mergeSort(long[] preSums, int left, int right, int lower, int upper,long[] tmp) {
        if (left < right) {
            int mid = (left + right) / 2;
            long retL = mergeSort(preSums, left, mid, lower, upper,tmp);
            long retR = mergeSort(preSums, mid + 1, right, lower, upper,tmp);

            long ret = retL + retR;
            int i = left;
            int l = mid + 1;
            int r = mid + 1;
            while (i <= mid) {

                while (l <= right && preSums[l] - preSums[i] < lower) {
                    l++;
                }

                while (r <= right && preSums[r] - preSums[i] <= upper) {
                    r++;
                }
                ret += r - l;
                i++;
            }

            merge(preSums, left, mid, right,tmp);

            return ret;
        }else {
            return 0;
        }

    }

    public static void merge(long[] arr, int left, int mid, int right,long[] tmp) {


        int i = 0;

        int j = left;
        int k = mid + 1;

        while(j <= mid && k <= right){
            if(arr[j] < arr[k]){
                tmp[i++] = arr[j++];
            }else{
                tmp[i++] = arr[k++];
            }
        }

        while(j <= mid){
            tmp[i++] = arr[j++];
        }

        while(k <= right){
            tmp[i++] = arr[k++];
        }

        if (i >= 0) System.arraycopy(tmp, 0, arr, left, i);

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
