import java.util.LinkedList;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        String inorder=in.next();
        String poorder=in.next();
        TreeNode tree=buildtree(inorder,poorder);
        TreeNode current;

        LinkedList<TreeNode> s = new LinkedList<TreeNode>();
        s.addFirst(tree);
        while (!s.isEmpty()) {
            current = s.removeFirst();
            System.out.print(current.val);
            if (current.right != null)
                s.addFirst(current.right);
            if (current.left != null)
                s.addFirst(current.left);
        }


    }
    /**
     * 二叉树结点
     */
   static class TreeNode {
        public char val;
        public TreeNode left;
        public TreeNode right;

        private TreeNode root;
        public TreeNode(char val) {
            this.val = val;
        }

        public TreeNode getRoot() {
            return root;
        }

        public void setRoot(TreeNode root) {
            this.root = root;
        }
    }

        public static int findRootIndex(String inorder, char val){
            for(int i = 0; i < inorder.length(); i++){
                if(inorder.charAt(i)==val){
                    return i;
                }
            }
            return -1;
        }
        public static TreeNode buildtree(String inorder,String postorder){
       if (inorder.length()==0 || postorder.length()==0){
           return null;
       }
            TreeNode root=new TreeNode(postorder.charAt(postorder.length()-1));
            int root1=findRootIndex(inorder,root.val);
            String leftpost=postorder.substring(0,root1);
            String leftin=inorder.substring(0,root1);
            String rightpost=postorder.substring(root1,postorder.length()-1);
            String rightin=inorder.substring(root1+1);
            TreeNode left=buildtree(leftin,leftpost);
            TreeNode right=buildtree(rightin,rightpost);
            root.left=left;
            root.right=right;
            return root;
        }



}
