import java.lang.reflect.Array;
import java.util.*;

public class stable {
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        int n= in.nextInt();
        HashMap<String,Integer> men=new HashMap<>();
        HashMap<String,Integer> women=new HashMap<>();
        ArrayList<String> menno=new ArrayList<>();
        ArrayList<String> womenno=new ArrayList<>();
        Queue<String> que=new LinkedList<>();
        ArrayList<String> curpre=new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String s=in.next();
            men.put(s,i);
            menno.add(s);
            que.add(s);
        }
        for (int i = 0; i < n; i++) {
            String s=in.next();
            women.put(s, i);
            womenno.add(s);
        }
        int[][] menpre=new int[n][n];
        int[][] womenpre=new int[n][n];
        int[] menpar=new int[n];
        int[] wompar=new int[n];
        Arrays.fill(menpar,-1);
        Arrays.fill(wompar,-1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                String s=in.next();
                if (j==0){
                    curpre.add(s);
                }
                menpre[i][j]=women.get(s);
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                womenpre[i][j]=men.get(in.next());
            }
        }

        while (!que.isEmpty()){
            int t=men.get(que.poll());
            int w=women.get(curpre.get(t));
            if (wompar[w]==-1){
                wompar[w]=t;
                menpar[t]=w;
            }else {
                int m=wompar[w];
                for (int i = 0; i < n; i++) {
                    if (womenpre[w][i]==t){
                        wompar[w]=t;
                        menpar[t]=w;
                        menpar[m]=-1;
                        que.add(menno.get(m));
                        for (int j = 0; j < n; j++) {
                            if (menpre[m][j]==w){
                                String next=womenno.get(menpre[m][j+1]);
                                curpre.set(m,next);
                                break;
                            }
                        }
                        break;
                    }
                    if (womenpre[w][i]==m){
                        for (int j = 0; j < n; j++) {
                            if (menpre[t][j]==w){
                                String next=womenno.get(menpre[t][j+1]);
                                curpre.set(t,next);
                                que.add(menno.get(t));
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            String m=menno.get(i);
            String w=womenno.get(menpar[i]);
            System.out.println(m+" "+w);
        }
    }
}
