import java.util.*;
public class prime {
    static int[] prime = new int[9000];
    static int[] visited = new int[9000];
    static int[] step = new int[9000];


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        for (int i = 1000; i <10000 ; i++) {
            if (isPrime(i)){
                prime[i-1000]=1;
            }
        }
        int n = sc.nextInt();
        for (int k = 0; k < n; k++) {
            int initial = sc.nextInt();
            int destine = sc.nextInt();
            Arrays.fill(visited, 0);
            Arrays.fill(step, 0);
            ArrayList<Integer> list = new ArrayList<>();
            list.add(initial);

            visited[initial -1000] = 1;
            while (list.size()!=0) {
                int u = list.get(0);
                list.remove(0);
                if (u == destine) {break;}
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 10; j++) {
                        String s=Integer.toString(u);
                        int[] digit=new int[4];
                        for (int m = 0; m < 4; m++) {
                            digit[m]=Character.getNumericValue(s.charAt(m));
                        }
                        digit[i] = j;
                        if (digit[0]==0){
                            continue;
                        }
                        int v = digit[0]*1000+digit[1]*100+digit[2]*10+digit[3];
                        if (prime[v-1000]==1 && visited[v-1000]==0) {
                            visited[v-1000] = 1;
                            step[v-1000] = step[u-1000] + 1;
                            list.add(v);
                        }
                    }
                }
            }
            if (visited[destine -1000]==1){
                System.out.println(step[destine -1000]);
            }else {
                System.out.println("Impossible");
            }

        }


    }
    public static boolean isPrime(int number) {
        for( int i=2;i<number;i++) {
            if(number%i==0) {
               return false;
            }
        }
        return true;
    }
}