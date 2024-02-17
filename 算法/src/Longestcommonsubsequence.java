import java.util.Scanner;

public class Longestcommonsubsequence {
  static   String s1;
   static String s2;
   static int[][] mem;
   static int[][] b;
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        s1= in.next();
       s2= in.next();
        mem=new int[s1.length()][s2.length()];
        b=new int[s1.length()][s2.length()];

        lcs(s1.length()-1,s2.length()-1);
        for (int i = 0; i <s1.length() ; i++) {
            for (int j = 0; j <s2.length() ; j++) {
                System.out.print(mem[i][j]+" ");
            }
            System.out.println();
        }
        print(s1.length()-1,s2.length()-1);
    }

    public static int lcs(int n,int m){
            if (n==-1 || m==-1) return 0;
            if (s1.charAt(n)==s2.charAt(m)){
                b[n][m]=1;
               return mem[n][m]=lcs(n-1,m-1)+1;

            }else {
                if (lcs(n - 1, m) > lcs(n, m - 1)) {
                    b[n][m]=2;
                   return mem[n][m]=lcs(n-1,m);
                }else {
                    b[n][m]=3;
                    return mem[n][m]=lcs(n,m-1);
                }

            }
    }

    public static void print(int n,int m){
        if (n==-1 || m==-1) return ;

        if (b[n][m]==1){
            print(n-1,m-1);
            System.out.print(s1.charAt(n));
        }else if (b[n][m]==2){
            print(n-1,m);
        }else {
            print(n,m-1);
        }
    }
}
