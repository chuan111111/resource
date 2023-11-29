import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class avlbina {
   static ArrayList<Integer> list=new ArrayList<>();
    public static void main(String[] args) {
        QReader in = new QReader();
        int n= in.nextInt();
        for (int i = 0; i < n; i++) {
            int x= in.nextInt();
            if (x==1){
                insert(in.nextInt());
            }else if (x==2){
                delete(in.nextInt());
            }else if (x==3){
                findrank(in.nextInt());
            }else if (x==4){
                System.out.println(list.get(in.nextInt()-1));
            }else if (x==5){
                finlagest(in.nextInt());
            }else if (x==6){
                findsmallest(in.nextInt());
            }
        }

    }
    public static void findsmallest(int x){
        if (x < list.get(0)) {
            System.out.println(list.get(0));
        } else  if (list.size() == 2) {
            System.out.println(list.get(1));
        }else {
            int left = 0;
            int right = list.size() - 1;
            while (left <= right) {
                int mid = (left + right) / 2;

                if (list.get(mid) <= x && list.get(mid + 1) > x) {
                    System.out.println(list.get(mid+1));
                    return;
                } else if (list.get(mid) > x && list.get(mid - 1) <= x) {
                    System.out.println(list.get(mid));
                    return;
                }
                if (list.get(mid) > x) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }
    }
    public static void finlagest(int x) {
        if (x > list.get(list.size() - 1)) {
            System.out.println(list.get(list.size() - 1));
        } else  if (list.size() == 2) {
            System.out.println(list.get(0));
        }else {
            int left = 0;
            int right = list.size() - 1;
            while (left <= right) {
                int mid = (left + right) / 2;

                if (list.get(mid) < x && list.get(mid + 1) >= x) {
                    System.out.println(list.get(mid));
                    return;
                } else if (list.get(mid) >= x && list.get(mid - 1) < x) {
                    System.out.println(list.get(mid-1));
                    return;
                }
                if (list.get(mid) >= x) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }}

        public static void findrank ( int x){
        if (list.get(0)==x){
            System.out.println(1);
        }else if (list.size()==2){
            System.out.println(2);
        }else {
            int left = 0;
            int right = list.size() - 1;
            while (left <= right) {
                int mid = (left + right) / 2;
                if (list.get(mid) == x && list.get(mid-1)<x) {
                    System.out.println(mid+1);
                    return;
                }
                if (list.get(mid) >= x) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }}
        public static void delete ( int x){
            int left = 0;
            int right = list.size() - 1;
            while (left <= right) {
                int mid = (left + right) / 2;
                if (list.get(mid) == x) {
                    list.remove(mid);
                    return;
                }
                if (list.get(mid) > x) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }
        public static void insert ( int x){

            if (list.size() == 0) {
                list.add(x);
            } else if (x <= list.get(0)) {
                list.add(0, x);
            } else if (x >= list.get(list.size() - 1)) {
                list.add(x);
            } else if (list.size() == 1) {
                if (x < list.get(0)) {
                    list.add(0, x);
                } else {
                    list.add(x);
                }
            } else if (list.size() == 2) {
                if (x < list.get(0)) {
                    list.add(0, x);
                } else if (x < list.get(1)) {
                    list.add(1, x);
                } else {
                    list.add(x);
                }
            } else {
                int left = 0;
                int right = list.size() - 1;
                while (left <= right) {
                    int mid = (left + right) / 2;
                    if (list.get(mid) <= x && list.get(mid + 1) >= x) {
                        list.add(mid + 1, x);
                        return;
                    } else if (list.get(mid) >= x && list.get(mid - 1) <= x) {
                        list.add(mid, x);
                        return;
                    }
                    if (list.get(mid) > x) {
                        right = mid - 1;
                    } else {
                        left = mid + 1;
                    }
                }
            }
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
}
