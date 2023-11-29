
import java.io.*;
import java.util.*;


public class BalancedBinaryTree {

    private Node root ;

    public void push(int key){
        if (root == null){
            root = new Node(key,null);
        }else {
            push(root,key);
        }
    }

    private void push(Node node , int key){
        if (key > node.getKey()){
            if (node.getRight() == null){
                node.setRight(new Node(key,node));
               this.balanced();
            }else {
                push(node.getRight(),key);
            }
        }else if (key < node.getKey()){
            if (node.getLeft() == null){
                node.setLeft(new Node(key,node));
                this.balanced();
            }else {
                push(node.getLeft(),key);
            }
        }else {
            node.count++;

        }
    }

    public boolean delete(int key){
        if (root == null){
            //空树直接加到根节点
            return false;
        }
        return delete(root,key);
    }

    public boolean delete(Node node , int key){
        if (key > node.getKey()){
            //大于则去右子树
            if (node.getRight() == null){
                //没有这个节点
                return false;
            }else {
                //继续递归比较
                delete(node.getRight(),key);
            }
        }else if (key < node.getKey()){
            //小于则去左子树
            if (node.getLeft() == null){
                //没有这个节点
                return false;
            }else {
                //继续递归比较
                delete(node.getLeft(),key);
            }
        }
        // 等于则删除
        Node father = node.getFather();
        if (node.count > 1) {
            node.count--;
            return true;
        }else {
        if (node.isLeaf()){
            //没有子节点 ： 直接去除即可，不需要合并
            if (father == null){
                this.root = null;
            }else if (father.getKey() >  key){
                father.setLeft(null);
            }else {
                father.setRight(null);
            }
        }else if (node.getLeft() != null && node.getRight() == null){
            if (father == null){
                this.root = node.getLeft();
            }else if (father.getKey() >  key){
                father.setLeft(node.getLeft());
            }else {
                father.setRight(node.getLeft());
            }
            //维持父子关系
            node.getLeft().setFather(father);
        }else if (node.getRight() != null && node.getLeft() == null){
            //只有一个子节点 ：用待删除节点的子节点替代他
            if (father == null){
                this.root = node.getRight();
            }else if (father.getKey() >  key){
                father.setLeft(node.getRight());
            }else {
                father.setRight(node.getRight());
            }
            node.getRight().setFather(father);
        }else {
            Node max = this.findMax(node.getLeft());
            if (father == null){
                this.root = max;
            }else if (father.getKey() >  key){
                father.setLeft(max);
            }else {
                father.setRight(max);
            }
            max.setFather(father);
            max.setLeft(node.getLeft());
            max.setRight(node.getRight());
            node.getLeft().setFather(max);
            node.getRight().setFather(max);
        }
        //调整
        this.balanced();

        return true;
    }}

    private int size(Node node) {
        if (node == null) {
            return 0;
        }
        return size(node.getLeft()) + node.count + size(node.getRight());
    }

    public int findRank(int x) {
        if (root == null) {
            return -1; // 如果树为空，则返回 -1
        }

        Node current = root;
        int rank = 1;
        while (current != null) {
            if (current.getKey() == x) {
                return rank + size(current.getLeft()); // 找到 x，返回它的排名
            } else if (current.getKey() > x) {
                current = current.getLeft();
            } else {
                rank = rank + size(current.getLeft()) + current.count;
                current = current.getRight();
            }
        }

        return -1; // 如果没有找到 x，则返回 -1
    }
    public int findMaxLessThanX(int x) {
        Node node = root;
        int res = Integer.MIN_VALUE;

        while (node != null) {
            if (node.getKey() < x) {
                res = node.getKey();
                node = node.getRight();
            } else {
                node = node.getLeft();
            }
        }

        return res;
    }
    public int findMinGreaterThanX(int x) {
        Node node = root;
        int res = Integer.MAX_VALUE;

        while (node != null) {
            if (node.getKey() > x) {
                res = node.getKey();
                node = node.getLeft();
            } else {
                node = node.getRight();
            }
        }

        return res;
    }
    private Node findMax(Node node){
        if (node.getRight() == null){
            return node;
        }
        return this.findMax(node.getRight());
    }


    private boolean balanced(){
        if (root == null){
            return false;
        }
        //采用前序遍历是找的第一个，可能不是最小非平衡树，所以这里采用后续遍历
        Iterator<Node> iterator = backIterator();
        while (iterator.hasNext()){
            Node next = iterator.next();
            // 平衡因子的绝对值 > 1 ， 则需要调整
            int bf = next.getLeftLength() - next.getRightLength();
            if (bf < -1 || bf > 1){
                //调整 ， 传入最小非平衡树的根节点，与平衡因子，传入平衡因子是因为不想计算两次 getLeftLength()方法也是采用递归的、
                rotate(next, bf);
                return true;
            }
        }
        return false;
    }

    private Node rotate(Node node , int bf){
        Node one , two ;
        // bf（平衡因子 ） 大于0 说明根节点深度大的子树在左边 ， 即 左旋
        if (bf>0){
            // 左旋
            one = node.getLeft();
            if (one.getRight() != null && !one.getRight().isLeaf()){
                one = rotate(one, one.getBf());
            }
            // 截取two , 左旋取右节点  反之
            two = one.getRight();
            node.setLeft(two);
            // node 成为 one 的右节点
            one.setRight(node);
        }else {
            //右旋
            one = node.getRight();
            if (one.getLeft() != null && !one.getLeft().isLeaf()){
                one = rotate(one, one.getBf());
            }
            two = one.getLeft();
            node.setRight(two);
            // node 成为 one 的左节点
            one.setLeft(node);
        }
        if (two !=null){
            two.setFather(node);
        }
        if (node.getFather()==null){
            // 如果传入的最小非平衡子树的根节点 是整棵树的根节点 那么需要修改 树对象 记录的根节点
            one.setFather(null);
            root = one;
        }else {
            // 不是上面这个条件时 ， 传入的最小非平衡子树的根节点的父节点的 左 或者 右 节点需要修改成one 。
            one.setFather(node.getFather());
            if (bf > 0){
                node.getFather().setLeft(one);
            }else {
                node.getFather().setRight(one);
            }
        }
        node.setFather(one);
        //返回one
        return one;
    }
    public Iterator<Node> backIterator(){
        ArrayList<Node> nodes = new ArrayList<>();
        if (this.root != null){
            addToBackNodeList(nodes,this.root);
        }
        return nodes.iterator();
    }

    private void addToBackNodeList(List<Node> nodes,Node node){
        if (node.getLeft()!=null){
            addToBackNodeList(nodes,node.getLeft());
        }
        if (node.getRight()!=null){
            addToBackNodeList(nodes,node.getRight());
        }
        nodes.add(node);
    }
    public int kthSmallest( int k) {
        Stack<Node> stk = new Stack<>();
        Node helper=root;
        while(helper != null || !stk.isEmpty()){
            while(helper != null){
                stk.push(helper);
                helper = helper.getLeft();
            }
            helper = stk.pop();
            if(--k == 0){
                return helper.getKey();
            }
            helper = helper.getRight();
        }
        return 0;
    }
    public static void main(String[] args) {
        BalancedBinaryTree avlTree = new BalancedBinaryTree();
        QReader input = new QReader();
        QWriter out = new QWriter();
        int n = input.nextInt();

        for (int i = 0; i < n; i++) {
            int x = input.nextInt();
            int y = input.nextInt();
            if (x == 1) {
                avlTree.push(y);
            }
            if (x == 2) {
                avlTree.delete(y);
            }
            if (x == 3) {
                out.println(avlTree.findRank(y));
            }
            if (x == 4) {
                out.println(avlTree.kthSmallest(y));
            }
            if (x == 5) {
                out.println(avlTree.findMaxLessThanX(y));
            }
            if (x == 6) {
                out.println(avlTree.findMinGreaterThanX(y));
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



class Node{

    Node(int key , Node father){
        this.key = key;
        this.father = father;
    }

    /**
     * 键值
     */
    private int key;
    /**
     * 左节点
     */
    private Node left ;
    /**
     * 右节点
     */
    private Node right;
    /**
     * 父节点
     */
    private Node father;
    public int count=1;



    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getFather() {
        return father;
    }

    public void setFather(Node father) {
        this.father = father;
    }


    private int findMaxLength(Node node){
        int left = node.left == null ? 0 : findMaxLength(node.left);
        int right = node.right == null ? 0 : findMaxLength(node.right);
        return Math.max(left,right) + 1;
    }

    public int getLeftLength(){
        return this.left == null ? 0 : this.findMaxLength(this.left);
    }

    public int getRightLength(){
        return this.right == null ? 0 : this.findMaxLength(this.right);
    }

    public int getBf(){
        return this.getLeftLength() - this.getRightLength();
    }

    public boolean isLeaf(){
        return left == null && right == null;
    }

    @Override
    public String toString(){
        return String.valueOf(key);
    }

}