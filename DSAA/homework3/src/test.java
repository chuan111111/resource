
import java.io.*;
import java.util.StringTokenizer;

public class test {
    public static void main(String[] args) {
        QReader in = new QReader();
        QWriter out = new QWriter();
        int T = in.nextInt();
        for (int i = 0; i < T; i++) {
            int n = in.nextInt();
            node head = new node(0, -1);
            node tail = new node(0, Integer.MAX_VALUE);
            head.next = tail;
            node pointer;
            for (int j = 0; j < n; j++) {
                pointer = head;
                node node = new node(in.nextInt(), in.nextInt());
                while (true) {
                    if (node.exponent < pointer.next.exponent) {
                        break;
                    }
                    pointer = pointer.next;
                }
                if (node.exponent == pointer.exponent) {
                    pointer.coefficient += node.coefficient;
                } else {
                    node.next = pointer.next;
                    pointer.next = node;
                }
            }
//            pointer=head.next;
////            while (pointer!=tail){
////                out.print(pointer.exponent+" ");
////                pointer=pointer.next;
////            }
            int m = in.nextInt();
            for (int j = 0; j < m; j++) {
                pointer = head;
                int coefficient = in.nextInt();
                int exponent = in.nextInt();
                while (true) {
                    if (exponent < pointer.next.exponent) {
                        break;
                    }
                    pointer = pointer.next;
                }
                if (exponent == pointer.exponent) {
                    pointer.coefficient += coefficient;
                } else {
                    node node = new node(coefficient, exponent);
                    node.coefficient = coefficient;
                    node.exponent = exponent;
                    node.next = pointer.next;
                    pointer.next = node;
                }
            }
            pointer = head.next;
            StringBuilder str = new StringBuilder();
            if (head.next.coefficient != 0) {
                if (head.next.coefficient == 1) {
                    if(head.next.exponent == 0){
                        str.append(1);
                    }else {
                    str.append("");
                    }
                } else if(head.next.coefficient == -1){
                    if(head.next.exponent == 0){
                        str.append(-1);
                    }else {
                        str.append("-");
                    }
                }else {str.append(head.next.coefficient);}
                if (head.next.exponent == 0) {
                    str.append("");
                } else if (head.next.exponent == 1) {
                    str.append("x");
                } else {
                    str.append("x^" + head.next.exponent);
                }
            }
            while (pointer != tail) {
                if (pointer.next.coefficient != 0) {
                    if (pointer.next.coefficient > 0) {
                        if (pointer.next.coefficient == 1) {
                            str.append("+");
                        } else {
                            str.append("+" + pointer.next.coefficient);
                        }
                    }else if(pointer.next.coefficient < 0){
                        if (pointer.next.coefficient == -1) {
                            str.append("-");
                        } else {
                            str.append(pointer.next.coefficient);
                        }
                    }
                    if (pointer.next.exponent == 0) {
                        str.append("");
                    } else if (pointer.next.exponent == 1) {
                        str.append("x");
                    } else {
                        str.append("x^" + pointer.next.exponent);
                    }
                }
                pointer = pointer.next;
            }
            if(str.length()==0){
                out.println(0);
            }else {
                if(str.charAt(0)=='+'){
                str.deleteCharAt(0);
            }
                out.println(str);
            }
        }
        out.close();
    }
}

class node {
    node next;
    int coefficient;
    int exponent;

    public node(int coefficient, int exponent) {
        this.coefficient = coefficient;
        this.exponent = exponent;
    }
}

class QReader {
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

class QWriter implements Closeable {
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