import java.util.*;

public class greed2 {
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        PriorityQueue<node> queue=new PriorityQueue<>(Comparator.comparingInt(o -> o.s));
        List<List<node>> classroom=new ArrayList<>();
        int t= in.nextInt();
        for (int i = 0; i < t; i++) {
            queue.add(new node(i,in.nextInt(),in.nextInt()));
        }

        classroom.add(new ArrayList<>());
        int num=0;
        node one=queue.poll();
        classroom.get(0).add(one);
        while (!queue.isEmpty()){
            node cur=queue.poll();
            boolean re=false;
            for (List<node> nodes : classroom) {
                node com = nodes.get(0);
                if (cur.s >= com.f) {
                    nodes.add(cur);
                    re = true;
                    break;
                }
            }
            if (!re){
                classroom.add(new ArrayList<>());
                num++;
                classroom.get(num).add(cur);
            }
        }
        System.out.println(classroom.size());

    }
    static class node{
        int num;
        int s;
        int f;
        public node(int num,int s,int f){
            this.num=num;
            this.s=s;
            this.f=f;
        }
    }
}
