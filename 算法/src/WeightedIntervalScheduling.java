import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class WeightedIntervalScheduling {
    public static void main(String[] args) {
        QReader in=new QReader();
        int n= in.nextInt();
        node[] nodes=new node[n+1];

        for (int i = 1; i <= n; i++) {
            nodes[i]=new node(in.nextInt(),in.nextInt(),in.nextInt());
        }
        Arrays.sort(nodes,1,n+1,Comparator.comparingInt(o -> o.f));
        nodes[0]=new node(0,0,0);
        int[] p=new int[n+1];
        for (int i = 2; i <=n; i++) {

            int high=i-1;
            int low=0;
            while (high>low+1){
                int mid=(high+low)/2;
                if (nodes[i].s>=nodes[mid].f){
                    low=mid;
                }else {
                    high=mid-1;
                }
            }

            if (nodes[i].s>=nodes[high].f){
                p[i]=high;
            }else {
                p[i]=low;
            }


        }
        int[] m=new int[n+1];
        for (int i = 1; i <=n ; i++) {
            m[i]=Math.max(nodes[i].weight+m[p[i]],m[i-1]);
        }
        System.out.print(m[n]);

    }

    static class node{

        int s;
        int f;
        int weight;
        public node(int s,int f,int weight){
            this.s=s;
            this.f=f;
            this.weight=weight;
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
