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
//            if (b[fb]<=a[sa]){
//                if (length<num){
//                    out.println(a[sa+num-length-1]);
//                }else if (length>num){
//                    out.println(b[sb+num]);
//                }else {
//                    out.println(b[fb]);
//                }
//
//                continue;
//            }else if (b[sb]>=a[fa]){
//                if (length<num){
//                    out.println(b[sb+num-length-1]);
//                }else if (length>num){
//                out.println(a[sa+num]);}
//                else {
//                    out.println(a[fa]);
//                }
//                continue;
//            }
            int mida=sa+Math.min(fa-sa+1,num/2)-1;
            int midb=sb+Math.min(fb-sb+1,num/2)-1;
//            if (mida!=0){
//                midb-=1;
//                mida-=1;
//            }

            while (true){

                if (a[mida]<b[midb]){
                    num=num-Math.min(fa-sa+1,num/2);
                        sa=mida+1;
                }else if (a[mida]>b[midb]){
                    num=num-Math.min(fb-sb+1,num/2);
                    sb=midb+1;
                }else {
                    if (sa>=sb){
                        num=num-Math.min(fb-sb+1,num/2);
                        sb=midb+1;
                    }else {
                        num=num-Math.min(fa-sa+1,num/2);
                        sa=mida+1;

                    }
                }
                mida=sa+Math.min(fa-sa+1,num/2)-1;
                midb=sb+Math.min(fb-sb+1,num/2)-1;

                if (sa>fa){
                    out.println(b[sb+num-1]);
                    break;
                }else if (sb>fb){

                    out.println(a[sa+num-1]);
                    break;
                }
               if (num==1){
                   out.println(Math.min(a[sa],b[sb]));
                   break;
               }

            }


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
