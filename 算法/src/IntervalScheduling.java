import java.util.*;

public class IntervalScheduling {
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        PriorityQueue<node> queue=new PriorityQueue<>(Comparator.comparingInt(o -> o.f));
        while (in.hasNextLine()){
            String s1=in.nextLine();
            if (s1.equals("")){break;}
           String[] s2=s1.split(" ");
            queue.add(new node(s2[0],Integer.parseInt(s2[1]),Integer.parseInt(s2[2])));

        }
        node pre=queue.poll();
        System.out.print(pre.name+" ");
        while (!queue.isEmpty()){
            node cur=queue.poll();
            if (cur.s>= pre.f){
                System.out.print(cur.name+" ");
                pre=cur;
            }
        }

    }
    static class node{
         String name;
         int s;
         int f;
        public node(String name,int s,int f){
            this.name=name;
            this.s=s;
            this.f=f;
        }
    }
}
