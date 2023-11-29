import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class greed1 {
    public static void main(String[] args) {
        QReader in=new QReader();

        int apples= in.nextInt();
        int baskets= in.nextInt();
        long[] arr=new long[baskets];
        long total=0;
        for (int i = 0; i < baskets; i++) {
            arr[i]= in.nextLong();
            total+=arr[i];
        }
        Arrays.sort(arr);
        long[] position=new long[apples];
        for (int i = 0; i < apples; i++) {
            position[i]= in.nextLong();
        }
        long cost=0;
        long last=-1;
        if (total>apples){
            int i=0;
            for (; i <baskets ; i++) {
                total-=arr[i];
                if (total<apples){
                    break;
                }else if (total==apples){
                    i+=1;
                    break;
                }
            }


            for (; i < baskets; i++) {
                long num=arr[i];
                if (num+last<=apples-1){
                    last=last+num;
                    cost+=position[(int) last]* 2;
                }else {
                    cost+=position[apples-1]* 2;
                    break;
                }

            }

        }else {

            for (int i = 0; i < baskets; i++) {
                long num=arr[i];
                if (num+last<=apples-1){
                    last=last+num;
                    cost+=position[(int) last]* 2;
                }else {
                    cost+=position[apples-1]* 2;
                    break;
                }

            }


        }

        System.out.print(cost);
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
                if (nextLine == null ) {
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
