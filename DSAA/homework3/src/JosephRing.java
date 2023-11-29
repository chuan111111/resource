import java.util.Scanner;

public class JosephRing {
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        int totalnumber= in.nextInt();
        int m= in.nextInt();
        CircleSingleLinkedList c1=new CircleSingleLinkedList();
        c1.creatring(totalnumber);
        c1.removepeople(m);
    }
}
    class CircleSingleLinkedList{
    private people first=null;

        public void creatring(int totalnumber){
            people curpeo=null;
        for (int i = 1; i <= totalnumber; i++) {
            people newpeople=new people(i);
            if (i==1){
                first=newpeople;
                first.setNext(first);
                curpeo=first;
            }else {
                curpeo.setNext(newpeople);
                newpeople.setNext(first);
                curpeo=newpeople;
            }
        }
    }
    public void removepeople(int m){
        people temp=first;
        while (temp.getNext() != first) {
            temp = temp.getNext();
        }
       while (temp!=first){
           for (int i = 1; i < m; i++) {
            first=first.getNext();
            temp=temp.getNext();
        }
           System.out.print(first.getNumber()+" ");
           first = first.getNext();
           temp.setNext(first);
       }
        System.out.print(first.getNumber());
    }
}
    class people{
        private  int number;
        private people next;
         public people(int number){
                this.number=number;
         }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public people getNext() {
            return next;
        }

        public void setNext(people next) {
            this.next = next;
        }
    }

