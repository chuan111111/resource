import java.util.*;

class trys {
    static int n, m, s, t, u, v;
    static long w, ans;
    static int tot = 1;
    static int[] vis = new int[520010];
    static int[] pre = new int[520010];
    static int[] head = new int[520010];
    static int[][] flag = new int[2510][2510];

    static class Node {
        int to, net;
        long val;

        Node(int to, int net, long val) {
            this.to = to;
            this.net = net;
            this.val = val;
        }
    }

    static Node[] e = new Node[520010];
    static long[] dis;  // Move dis array here

    static void add(int u, int v, long w) {
        e[++tot] = new Node(v, head[u], w);
        head[u] = tot;
        e[++tot] = new Node(u, head[v], 0);
        head[v] = tot;
    }

    static boolean bfs() {
        Arrays.fill(vis, 0);
        Queue<Integer> q = new LinkedList<>();
        q.add(s);
        vis[s] = 1;
        dis = new long[520010];  // Move dis initialization here
        dis[s] = 2005020600;
        while (!q.isEmpty()) {
            int x = q.poll();
            for (int i = head[x]; i != 0; i = e[i].net) {
                if (e[i].val == 0) continue;
                int vert = e[i].to;
                if (vis[vert] == 1) continue;
                dis[vert] = Math.min(dis[x], e[i].val);
                pre[vert] = i;
                q.add(vert);
                vis[vert] = 1;
                if (vert == t) return true;
            }
        }
        return false;
    }

    static void update() {
        int x = t;
        while (x != s) {
            int vert = pre[x];
            e[vert].val -= dis[t];
            e[vert ^ 1].val += dis[t];
            x = e[vert ^ 1].to;
        }
        ans += dis[t];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        m = scanner.nextInt();
        s = scanner.nextInt();
        t = scanner.nextInt();
        for (int i = 1; i <= m; i++) {
            u = scanner.nextInt();
            v = scanner.nextInt();
            w = scanner.nextLong();
            if (flag[u][v] == 0) {
                add(u, v, w);
                flag[u][v] = tot;
            } else {
                e[flag[u][v] - 1].val += w;
            }
        }
        while (bfs()) {
            update();
        }
        System.out.println(ans);
    }
}
