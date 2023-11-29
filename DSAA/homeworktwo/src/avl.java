import java.io.*;
import java.util.Random;
import java.util.StringTokenizer;

public class avl {

   static int maxn=1000019;
    static final int INF = 1000000000;

    static int[][] ch=new int[maxn][2];
    static int[] val=new int[maxn];
    static int[] dat=new int[maxn];
    static int[] cnt=new int[maxn];
    static int[] size=new int[maxn];
    static Random b=new Random();
   static int tot=0,root=0;
   static int id;

    static void pushup(int id) {
        size[id] = size[ch[id][0]] + size[ch[id][1]] + cnt[id];
    }

    static int New(int v) {
        val[++tot] = v;
        dat[tot] = b.nextInt();
        size[tot] = 1;
        cnt[tot] = 1;
        return tot;
    }
    static void build(){
            root = New(-INF);
            ch[root][1] = New(INF);
            pushup(root);

    }

  static void rotate(int u, int d) {
        int id=u;
        int temp = ch[id][d ^ 1];
        ch[id][d ^ 1] = ch[temp][d];
        ch[temp][d] = id;
        id = temp;
        pushup(ch[id][d]);
        pushup(id);

    }

   static void insert(int u, int v) {
        int id=u;
        if(id==0){
            id = New(v);//若节点为空，则新建一个节点
            return ;
        }
        if(v == val[id])cnt[id]++;//若节点已存在，则副本数++;
        else{//要满足BST性质，小于插到左边，大于插到右边
            int d = v < val[id] ? 0 : 1;//这个d是方向的意思，按照BST的性质，小于本节点则向左，大于向右
            insert(ch[id][d],v);//递归实现
            if(dat[id] < dat[ch[id][d]])rotate(id,d ^ 1);//(参考一下图)与左节点交换右旋，与右节点交换左旋
        }
        pushup(id);//现在更新一下本节点的信息
    }

   static void remove(int u, int v) {
        int id=u;
        if(id==0){return ;}
        if(v == val[id]){
            if(cnt[id] > 1){cnt[id]--;
                pushup(id);return ;}
            if(ch[id][0]!=0 || ch[id][1]!=0){//发现只有一个值，且有儿子节点,我们只能把值旋转到底部删除
                if(ch[id][1]==0 || dat[ch[id][0]] > dat[ch[id][1]]){//当前点被移走之后，会有一个新的点补上来(左儿子或右儿子)，按照优先级，优先级大的补上来
                    rotate(id,1);
                    remove(ch[id][1],v);//我们会发现，右旋是与左儿子交换，当前点变成右节点；左旋则是与右儿子交换，当前点变为左节点
                }
                else rotate(id,0);
                remove(ch[id][0],v);
                pushup(id);
            }
            else id = 0;//发现本节点是叶子节点，直接删除
            return ;//这个return对应的是检索到值de所有情况
        }
        if (v < val[id]){
            remove(ch[id][0],v);
        }else {
            remove(ch[id][1],v);
        }
        pushup(id);
    }

    static int getRank(int id, int v) {

        if(v == val[id])return size[ch[id][0]] + 1;//查询到该值，由BST性质可知：该点左边值都比该点的值(查询值)小，故rank为左儿子大小 + 1
        else if(v < val[id])return getRank(ch[id][0],v);//发现需查询的点在该点左边，往左边递归查询
        else return size[ch[id][0]] + cnt[id] + getRank(ch[id][1],v);
    }

    static int get_val(int id, int rank) {

        if(rank <= size[ch[id][0]])return get_val(ch[id][0],rank);//左边排名已经大于rank了，说明rank对应的值在左儿子那里
        else if(rank <= size[ch[id][0]] + cnt[id])return val[id];//上一步排除了在左区间的情况，若是rank在左与中(目前节点)中，则直接返回目前节点(中区间)的值
        else return get_val(ch[id][1],rank - size[ch[id][0]] - cnt[id]);
    }

    static int get_pre(int v) {
        int id = root,pre=0;//递归不好返回，以循环求解
        while(id!=0){//查到节点不存在为止
            if(val[id] < v){
                pre = val[id];
                id = ch[id][1];}//满足当前节点比目标小，往当前节点的右侧寻找最优值
            else id = ch[id][0];//无论是比目标节点大还是等于目标节点，都不满足前驱条件，应往更小处靠近
        }
        return pre;
    }

    static int get_next(int v) {
        int id = root,next=0;
        while(id!=0){
            if(val[id] > v){
                next = val[id];
                id = ch[id][0];}//同理，满足条件向左寻找更小解(也就是最优解)
            else id = ch[id][1];//与上方同理
        }
        return next;
    }


    public static void main(String[] args) {
        build();
        QReader in = new QReader();
        QWriter out = new QWriter();
        int n=in.nextInt();
        avl tree=new avl();
        for (int i = 0; i < n; i++) {
            int cmd = in.nextInt();
            if (cmd == 1) {
               tree.insert(root, in.nextInt());
            } else if (cmd == 2) {
                tree.remove(root, in.nextInt());
            } else if (cmd == 3) {
                out.println(getRank(root, in.nextInt()) - 1);
            } else if (cmd == 4) {
                out.println(get_val(root, in.nextInt() + 1));
            } else if (cmd == 5) {
                out.println(get_pre(in.nextInt()));
            } else if (cmd == 6) {
                out.println(get_next(in.nextInt()));
            }
        }
        out.close();
    }

    static   class QReader {
        private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        private StringTokenizer tokenizer = new StringTokenizer("");

        private String innerNextLine() {
            try {
                return reader.readLine();
            } catch (IOException e) {
                return null;
            }
        }

        public boolean hasNext() {
            while (!tokenizer.hasMoreTokens()) {
                String nextLine = innerNextLine();
                if (nextLine == null) {
                    return false;
                }
                tokenizer = new StringTokenizer(nextLine);
            }
            return true;
        }

        public String nextLine() {
            tokenizer = new StringTokenizer("");
            return innerNextLine();
        }

        public String next() {
            hasNext();
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    static class QWriter implements Closeable {
        private BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        public void print(Object object) {
            try {
                writer.write(object.toString());
            } catch (IOException e) {
                return;
            }
        }
        public void println(Object object) {
            try {
                writer.write(object.toString());
                writer.write("\n");
            } catch (IOException e) {
                return;
            }
        }
        @Override
        public void close() {
            try {
                writer.close();
            } catch (IOException e) {
                return;
            }
        }
    }
}