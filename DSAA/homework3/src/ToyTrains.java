import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ToyTrains {

    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        int n= in.nextInt();
        int q= in.nextInt();
        List<car> carlist =new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            carlist.add(new car(i));
        }
        for (int i = 0; i < q; i++) {
            int x=in.nextInt();
            if (x==1){
                query1(in.nextInt(), in.nextInt(),carlist);
            }else if (x==2){
                query2(in.nextInt(), in.nextInt(),carlist);
            }else {
                query3(carlist,in.nextInt());
            }
        }
    }
    public static void query1(int x,int y,List<car> carList){
        carList.get(x-1).setNext(carList.get(y-1));
        carList.get(y-1).setPre(carList.get(x-1));
    }
    public static void query2(int x,int y,List<car> carList){
        carList.get(x-1).setNext(null);
        carList.get(y-1).setPre(null);
    }
    public static void query3(List<car> carlist,int x){
        car helper;
        helper=carlist.get(x-1);
        List<Integer> list=new ArrayList<>();
        while (helper.getPre()!=null){
            helper=helper.getPre();
        }
        int count=1;
        while (helper.getNext()!=null){
            list.add(helper.getNumber());
            helper=helper.getNext();
            count++;
        }
        System.out.print(count+" ");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i)+" ");
        }
        System.out.println(helper.getNumber());
    }
}
class car{
    private int number;
    private car next;
    private car pre;
    public car(int number){
        this.number=number;
    }
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public car getNext() {
        return next;
    }

    public void setNext(car next) {
        this.next = next;
    }

    public car getPre() {
        return pre;
    }

    public void setPre(car pre) {
        this.pre = pre;
    }
}