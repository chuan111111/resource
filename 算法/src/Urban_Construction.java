import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class Urban_Construction {
    static QWriter out=new QWriter();
    public static void main(String[] args) {
        QReader in=new QReader();
        int n= in.nextInt();
        Integer[] arr = new Integer[n+1];
        Integer[] indices = new Integer[n+1];
        for (int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
            indices[i] = i;
        }

        //

        // Sort the indices based on the values in arr
        Arrays.sort(indices,1,n+1, Comparator.comparingInt(i -> arr[i]));

        // Create an array to store relative positions
        int[] relativeOrder = new int[n+1];
        for (int i = 1; i <= n; i++) {
            relativeOrder[indices[i]] = i ;
        }

        dandc(relativeOrder,1,n);

        out.println("-1 -1");
        out.close();
    }
    public static int divideandc(int[] arr,int low,int high,int boundary){
        if (low>=high) {
            if (arr[low] <= boundary) {
                return 1;
            } else {
                return 0;
            }
        }
        int mid=(low+high)/2;
        int lbd=divideandc(arr,low,mid,boundary);
        int rbd= divideandc(arr,mid+1,high,boundary);
        int l = low+lbd;
        int m=mid+rbd;
        if (l<m){
            out.println(l+" "+m);
            for (; l<=m-1 ; l++,m--) {
                int t=arr[l];
                arr[l]=arr[m];
                arr[m]=t;
            }
        }
        return lbd+rbd;
    }
    public static void dandc(int[] arr,int low,int high){
        if(low<high){
            int mid = (low+high)/2;
            divideandc(arr,low,high,mid);
            dandc(arr,low,mid); //对左边序列进行归并排序
            dandc(arr,mid+1,high);  //对右边序列进行归并排序
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
