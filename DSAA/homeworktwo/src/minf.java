import java.util.*;

public class minf {
    static PriorityQueue<Long> q1 = new PriorityQueue<>(Collections.reverseOrder());
    static PriorityQueue<Long> q2 = new PriorityQueue<>();
    public static long val;

    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        int n= in.nextInt();
        long b=0;
        int tem=0;
        long num1=0;
        long num2=0;
        for (int i = 0; i < n; i++) {
            int x=in.nextInt();
            if (x==1){
                long y=in.nextLong();
                if (tem==0){
                    q1.add(y);
                    tem=1;
                    num1+=y;
                }else {
                    q2.add(y);
                    tem=0;
                    num2+=y;
                }
                b+=in.nextLong();
                if (!q1.isEmpty() && !q2.isEmpty()){
                while (q1.peek()> q2.peek()){
                    long a= q1.poll();
                    long c=q2.poll();
                    q2.add(a);
                    q1.add(c);
                    num1+=c-a;
                    num2+=a-c;
                }}

            }else if (x==2){
                long midval= q1.peek();
                    val = midval * q1.size() - num1 + num2 - midval * q2.size() + b;
                System.out.println(midval+ " " + val);
                val=0;
            }
        }

    }

}
