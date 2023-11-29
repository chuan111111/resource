import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Shuffle {
    public static deck initial=new deck();
    public static void main(String[] args) {
        QReader in=new QReader();
        QWriter out=new QWriter();
        int n= in.nextInt();
        int m= in.nextInt();
        List<card> hel=new ArrayList<>();
        for (int i = 1; i <=n ; i++) {
            hel.add(new card(i));
        }
        initial.creatring(hel);
        for (int i = 0; i < m; i++) {
            String s=in.next();
            if (s.equals("right")){
                initial.right(in.nextInt(),in.nextInt(),hel);
            }else {
                initial.left(in.nextInt(),in.nextInt(),hel);
            }
        }
        card helper=initial.getFirst();
        while (helper.getNext()!=null){
            out.print(helper.getNumber()+" ");
            helper=helper.getNext();
        }
        out.print(helper.getNumber());
        out.close();
    }
    static class QReader {
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
        public String next() {
            hasNext();
            return tokenizer.nextToken();
        }
        public int nextInt() {
            return Integer.parseInt(next());
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

class deck{
    private card first=null;
    public card getFirst() {
        return first;
    }
    public card tail=null;
    public void creatring(List<card> hel){
        for (int i = 0; i <hel.size() ; i++) {
            card news=hel.get(i);
            if (news.getNumber()==1){
                first=news;
                tail=first;
            }else {
                tail.setNext(news);
                news.setPre(tail);
                tail=news;
            }
        }
    }
    public void right(int x,int y,List<card> hel){
        if (hel.get(x-1).getNext()!=null){
        card helper=hel.get(x-1);
        card helper1=hel.get(y-1);
        if (first.getNumber()==y){
            tail.setNext(first);
            first.setPre(tail);
            tail=helper;
            first=helper.getNext();
            tail.setNext(null);
            first.setPre(null);
        }else {
            tail.setNext(helper1);
            helper1.getPre().setNext(helper.getNext());
            helper.getNext().setPre(helper1.getPre());
            helper1.setPre(tail);
            tail=helper;
            tail.setNext(null);
        }
        }
    }
    public void left(int x,int y,List<card> hel){
        if (hel.get(x-1).getPre()!=null){
       card helper1=hel.get(x-1);
       card helper2=hel.get(y-1);
       if (helper2!=tail){
           first.setPre(helper2);
           helper2.getNext().setPre(helper1.getPre());
           helper1.getPre().setNext(helper2.getNext());
           helper2.setNext(first);
           first=helper1;
           first.setPre(null);
    }else {
           first.setPre(tail);
           tail.setNext(first);
           first=helper1;
           tail=helper1.getPre();
           first.setPre(null);
           tail.setNext(null);

       }
        }
}}
class card{
    private int number;
    private card next;

    private card pre;

    public card getPre() {
        return pre;
    }

    public void setPre(card pre) {
        this.pre = pre;
    }

    public card(int number){
        this.number=number;
    }
    public int getNumber() {
        return number;
    }

    public card getNext() {
        return next;
    }

    public void setNext(card next) {
        this.next = next;
    }
}
