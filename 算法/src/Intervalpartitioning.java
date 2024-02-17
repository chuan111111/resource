import java.util.*;

public class Intervalpartitioning {
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        PriorityQueue<node> queue=new PriorityQueue<>(Comparator.comparingInt(o -> o.s));
        List<List<node>> classroom=new ArrayList<>();
        while (in.hasNextLine()){
            String s1=in.nextLine();
            if (s1.equals("")){break;}
            String[] s2=s1.split(" ");
            queue.add(new node(s2[0],Integer.parseInt(s2[1]),Integer.parseInt(s2[2])));

        }
        classroom.add(new ArrayList<>());
        int num=0;
        node one=queue.poll();
        classroom.get(0).add(one);
        while (!queue.isEmpty()){
            node cur=queue.poll();
            boolean re=false;
            for (List<node> nodes : classroom) {
                node com = nodes.get(nodes.size() - 1);
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
        for (List<node> nodes : classroom) {
            for (int j = 0; j < nodes.size(); j++) {
                System.out.print(nodes.get(j).name + " ");
            }
            System.out.println();
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
