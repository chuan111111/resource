import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class fruit {
    static class fruits{
        int l;
        int r;
        int v;
        int start;

        public fruits(int l, int r, int v) {
            this.l = l;
            this.r = r;
            this.v = v;

        }
    }
    static List<fruits> list=new ArrayList<>();
    static fruits[] select;
    static List<Integer> time=new ArrayList<>();
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        int n= in.nextInt();
        for (int i = 0; i < n; i++) {
            list.add(new fruits(in.nextInt(), in.nextInt(), in.nextInt()));
        }

        list.sort(Comparator.comparingInt(o -> o.l));
        int x=0;
        for (int i = 0; i < n; i++) {
//            if (x+1>list.get(i).l){
//                time.add(x+1);
//                list.get(i).start=list.get(i-1).start;
//                x=x+1;
//            }else {
//                time.add(list.get(i).l);
//                list.get(i).start=time.size()-1;
//                x=list.get(i).l;
//            }
            x=Math.max(x+1,list.get(i).l);
            time.add(x);
        }
        for (int i = 0, j = 0; i < n;   i++) {

            while (time.get(j) < list.get(i).l && j < n) j++;
            list.get(i).start = j;
        }
        select=new fruits[time.size()+1];
        list.sort(new Comparator<fruits>() {
            @Override
            public int compare(fruits o1, fruits o2) {
                return o2.v- o1.v;
            }
        });
        for (fruits node:list) {
          lineamatch(node,node.start);
        }
        long ans=0;
        for (int i = 0; i < select.length; i++) {
            if (select[i]!=null){
                ans+=select[i].v;
            }

        }
        System.out.println(ans);
    }

    public static boolean lineamatch(fruits a,int t){
        if ( a.r<time.get(t)) {
            return false;}
        else if (select[t]==null){
            select[t] = a;
            return true;
        }else if (select[t].r<a.r){
            return lineamatch(a, t+ 1);
        }else if (lineamatch(select[t], t + 1)) {
            select[t] = a;
            return true;
        }
        return false;
    }
}
