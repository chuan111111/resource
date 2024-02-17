import java.util.*;



public class TVbreak {
    static String str ;
    static String dest ;

    static List<int[]> kmp1=new ArrayList<>();

    static  PriorityQueue<match> queue=new PriorityQueue<>(new Comparator<match>() {
        @Override
        public int compare(match o1, match o2) {
            if (o1.f==o2.f){
                return   o1.s-o2.s;
            }else {
                return o1.f-o2.f;
            }
        }
    });


    public static void KMP(int id) {
        int la = str.length() ;
        int lb = dest.length();
        int[] kp=new int[lb];
        kmp1.add(kp);
        for (int i = 1, j = 0; i < lb; i++) {
            while (j > 0 && dest.charAt(j) != dest.charAt(i)){
                j=kmp1.get(id)[j-1];}
            if (dest.charAt(i) == dest.charAt(j)){
                j++;
                kmp1.get(id)[i]=j;
            }

        }

        for (int i = 0, j = 0; i < la; i++) {
            while (j > 0 && str.charAt(i) != dest.charAt(j))
                j=kmp1.get(id)[j-1];

            if (str.charAt(i) == dest.charAt(j)){
                j++;
            }
            if (j == lb) {

                queue.add(new match(i-j+1,i));
                j=kmp1.get(id)[j-1];

            }
        }
    }

    static class match{

        int s;
        int f;
        public match(int s,int f){

            this.s=s;
            this.f=f;
        }
    }



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        str = scanner.next();
        int n= scanner.nextInt();

        for (int i = 0; i < n; i++) {
            dest = scanner.next();
            KMP(i);
        }
        if (queue.isEmpty()){
            System.out.println(0);
        }else {
            List<List<match>> classroom=new ArrayList<>();
            classroom.add(new ArrayList<>());
            int num=0;
            match one=queue.poll();
            classroom.get(0).add(one);
            while (!queue.isEmpty()){
                match cur=queue.poll();
                boolean re=false;
                for (List<match> nodes : classroom) {
                    match com = nodes.get(0);
                    if (cur.s <= com.f) {
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

    }
}
