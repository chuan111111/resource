import java.io.*;
import java.util.StringTokenizer;

public class game {
    static String t;
   static int[][] mem;
    public static void main(String[] args) {
        QReader in=new QReader();
        QWriter out=new QWriter();
        int n= in.nextInt();
        for (int i = 0; i < n; i++) {
            t= in.next();
            int m=t.length();
            mem=new int[m][m];
           int re= select(0,m-1);
            if (re==1){
                out.println("Alice");
            }else if (re==2){
               out.println("Draw");
            }else {
                out.println("Bob");
            }
        }
        out.close();
    }

    public static int select(int l,int r){
        if (mem[l][r]!=0) return mem[l][r];
        if (l==r-1){
            if (t.charAt(l)!=t.charAt(r)){
                mem[l][r]=1;
                return 1;
            }else {
                mem[l][r]=2;
                return 2;
            }
        }else {
                if (t.charAt(l)<t.charAt(r)){
                    if (t.charAt(l+1)<t.charAt(l)){
                        return mem[l][r]=3;
                    }else if (t.charAt(l+1)>t.charAt(l)){
                        return mem[l][r]=1;
                    }else {
                        return mem[l][r]=select(l+2,r);
                    }
                }else if (t.charAt(l)>t.charAt(r)){
                    if (t.charAt(r-1)<t.charAt(r)){
                        return mem[l][r]=3;
                    }else if (t.charAt(r-1)>t.charAt(r)){
                        return mem[l][r]=1;
                    }else {
                        return mem[l][r]=select(l,r-2);
                    }
                }else {
                    int m1;
                    int m2;
                    //选左边
                    if (t.charAt(l+1)<t.charAt(l)){
                        m1=3;
                    }else if (t.charAt(l+1)>t.charAt(l)){
                        m1=select(l+1,r-1);
                    }else{
                        int t=select(l+2,r);
                        if (t==3){
                            m1=3;
                        }else {
                            int t1=select(l+1,r-1);
                            if (t1==3){
                               m1=3;
                            }else {
                                m1=Math.max(t,t1);
                            }

                        }

                    }
                    //选右边
                    if (t.charAt(r-1)<t.charAt(l)){
                        m2=3;
                    }else if (t.charAt(r-1)>t.charAt(l)){
                        m2=select(l+1,r-1);
                    }else {
                        int t=select(l,r-2);
                        if (t==3){
                            m2=3;
                        }else {
                            int t1=select(l+1,r-1);
                            if (t1==3){
                                m2=3;
                            }else {
                                m2=Math.max(t,t1);
                            }

                        }

                    }
                    return mem[l][r]=Math.min(m2,m1);
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