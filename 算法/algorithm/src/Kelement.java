import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Kelement {


    public static void main(String[] args) {
        QReader in=new QReader();
        QWriter out=new QWriter();
        int t= in.nextInt();
        int n= in.nextInt();
        int[] a=new int[n+1];
        int[] b=new int[n+1];
        for (int i = 1; i <= n; i++) {
            a[i]= in.nextInt();
        }
        for (int i = 1; i <= n; i++) {
            b[i]= in.nextInt();
        }
     for (int u = 0; u < t; u++) {

            int sa= in.nextInt(); int fa= in.nextInt(); int num= in.nextInt();

            int length=fa-sa+1;
            fa=Math.min(fa,sa+num);
            int sb=sa;
            int fb=fa;
            if (b[fb]<=a[sa]){
                if (length<num){
                    out.println(a[sa+num-length-1]);
                }else if (length>num){
                    out.println(b[sb+num]);
                }else {
                    out.println(b[fb]);
                }

                continue;
            }else if (b[sb]>=a[fa]){
                if (length<num){
                    out.println(b[sb+num-length-1]);
                }else if (length>num){
                out.println(a[sa+num]);}
                else {
                    out.println(a[fa]);
                }
                continue;
            }
            int mida=sa+Math.min(fa,num/2);
            int midb=sb+Math.min(fb,num/2);
            if (mida!=0){
                midb-=1;
                mida-=1;
            }
            boolean re=false;
            while (sa<=fa || sb<=fb){

                if (a[mida]<b[midb]){
                        sa=mida+1;
                }else if (a[mida]>b[midb]){
                    sb=midb+1;
                }else {
                    out.println(a[mida]);
                    re=true;
                    break;

                }

                while (sa+mida>fa || sb+midb>fb){
                    midb/=2;
                    mida/=2;
                }

            }
            if (!re){
                out.println(Math.min(a[sa],b[sb]));
            }



        }
        out.close();
    }
    public static int findKthSmallest(int[] arr1, int[] arr2, int k, int start1, int end1, int start2, int end2) {
        while (true) {
            int len1 = end1 - start1 + 1;
            int len2 = end2 - start2 + 1;

            if (len1 > len2) {
                // Swap arrays and lengths
                int[] tempArr = arr1;
                arr1 = arr2;
                arr2 = tempArr;

                int tempLen = len1;
                len1 = len2;
                len2 = tempLen;

                // Swap start and end indices
                int tempStart = start1;
                start1 = start2;
                start2 = tempStart;

                int tempEnd = end1;
                end1 = end2;
                end2 = tempEnd;
            }

            if (len1 == 0) {
                return arr2[start2 + k - 1];
            }

            if (k == 1) {
                return Math.min(arr1[start1], arr2[start2]);
            }

            int i = start1 + Math.min(len1, k / 2) - 1;
            int j = start2 + Math.min(len2, k / 2) - 1;

            if (arr1[i] < arr2[j]) {
                k -= (i - start1 + 1);
                start1 = i + 1;
            } else {
                k -= (j - start2 + 1);
                start2 = j + 1;
            }

            // Adjust end indices
            if (k <= 0) {
                end1 = i;
                end2 = j;
            } else {
                end1 = i - 1;
                end2 = j - 1;
            }
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
