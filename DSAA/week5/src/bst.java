import java.util.Scanner;
import java.util.Stack;

public class bst {
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        int n= in.nextInt();
        tree tree=new tree();
        for (int i = 0; i < n; i++) {
            int x=in.nextInt();
            if (x==1){
                tree.insert(in.nextInt());
            }else if (x==2){
                System.out.println(tree.kthSmallest(in.nextInt()));
            }else {
                tree.midOder();
                System.out.println();
            }
        }
        tree.preOder();
    }
    static class tree{
        public static node root;
        public tree(){
            this.root=null;
        }
        public void insert(int key){
        if (root==null){
            node news=new node(key);
            root=news;
        }else {
            node parent=null;
            node current=root;
            while (current!=null){
                if (key<current.key){
                    parent=current;
                    current=current.left;
                }else {
                    parent=current;
                    current=current.right;
                }
            }
            if (key<parent.key){
               parent.left=new node(key);
            }else {
                parent.right=new node(key);
            }
        }
        }
        public int kthSmallest(int k) {
            node helper=root;
            if (null == helper) {
                return 0;
            }
            int count = 0;
            Stack<node> stack = new Stack<>();
            while (null != helper || !stack.empty()) {
                if (null != helper) {
                    stack.push(helper);
                    helper = helper.left;
                } else {
                    helper = stack.pop();
                    count++;
                    if (count == k) {
                        return helper.key;
                    }
                    helper = helper.right;
                }
            }
            return 0;
        }

        public void preOder() {
            preOder(this.root);
        }

        private void preOder(node root) {
            if (root != null) {
                System.out.print(root.key + " ");
                preOder(root.left);
                preOder(root.right);
            }
        }
        public void midOder() {
            midOder(this.root);
        }

        private void midOder(node root) {//中序遍历实现接口
            if (root != null) {
                midOder(root.left);
                System.out.print(root.key + " ");
                midOder(root.right);
            }
        }

    }
    static class node{
       public int key;
       public node left;
      public  node right;

        public node(int key) {
            this.key = key;
            left=null;
            right=null;
        }
    }
}
