
import java.util.*;
class h4 {
    static class node implements Comparable<node> {
        int y, num;
        public int compareTo(node v) {
            return num - v.num;
        }
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int N = input.nextInt();
        int[] a = new int[N + 1];
        int[] b = new int[N + 1];
        for(int i = 1; i <= N; i++) {
            a[i] = input.nextInt();
        }
        for(int i = 1; i <= N; i++) {
            b[i] = input.nextInt();
        }
        PriorityQueue<node> q = new PriorityQueue<node>();
        for(int i = 1; i <= N; i++) {
            node k = new node();
            k.y = 1;
            k.num = a[i] + b[1];
            q.add(k);
        }
        int s = 1;
        while(s <= N) {
            node now = q.poll();
            if(now.y + 1 <= N) {
                node k = new node();
                k.y = now.y + 1;
                k.num = now.num - b[now.y] + b[now.y + 1];
                q.add(k);
            }
            s++;
            System.out.print(now.num + " ");
        }
        input.close();
    }
}

