import java.io.*;
import java.util.*;

public class que {
    public static void main(String[] args) {
        QReader in = new QReader();
        QWriter out = new QWriter();
        String s=in.next();
        String t=in.next();

           int k=get_index(s,t);
           while (k!=-1){
            if (k==0){
                s=s.substring(t.length());
            }else if (k==s.length()-t.length()){
                s=s.substring(0,s.length()-t.length());
            }else {
                String s1=s.substring(0,k);
                String s2=s.substring(k+t.length());
                s=s1+s2;
                k=get_index(s,t);
            }}
        out.print(s);
        out.close();
    }
    public static int get_index(String a,String b)   //用于在主串中搜索子串
    {
        int a1=0;
        int[] arr=kmpnext(b);
        int i=0;
        int j=0;
        while(i<a.length()&&j<b.length())
        {
            if(j==0||a.charAt(i)==b.charAt(j))    //用来对比使用
            {
                if(a.charAt(i)!=b.charAt(j))
                {
                    i++;
                    continue;
                }
                i++;
                j++;
            }
            else
            {
                j=arr[j];
            }
        }
        if(j>=b.length())
        {
            return i-b.length();
        }
        else
        {
            return -1;
        }
    }

    public static int[] kmpnext(String a){
        int[] nextval=new int[a.length()];
        nextval[0] = -1;
        int i = 0;
        int j = -1;
        while(i < a.length()) {
            if (j == -1 || a.charAt(i) == a.charAt(j)) {
                i++;
                j++;
                if(i < a.length()&&a.charAt(i)!=a.charAt(j))
                    nextval[i]=j;
                else if(i < a.length()&&a.charAt(i)==a.charAt(j))
                    nextval[i]=nextval[j];
            } else {
                j = nextval[j];
            }
        }
return nextval;
    }

    static   class QReader {
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

