import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class TeamsSelection {
    public static team initial=new team();
    public static List<student> arr=new ArrayList<>();
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        int n=in.nextInt();
        int k=in.nextInt();
        List<student> first=new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            student news=new student(i,in.nextInt());
            arr.add(news);
            first.add(news);
        }

        initial.creating(arr);
        int teams=1;
        while (arr.size()>0){
          arr=initial.select(teams,k,findmax(arr),arr);
            if (teams==1){
                teams=2;
            }else {
                teams=1;
            }
        }
        for (int i = 0; i < first.size(); i++) {
            System.out.print(first.get(i).getTeam());
        }
    }
    public static int findmax(List<student> arr){
        int max=0;
        int m=0;
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getSkill()>max){
                max=arr.get(i).getSkill();
                m=i;
            }
        }
        return m;
    }
}
class team{
    private student first=null;
    public void creating(List<student> arr){
        student helper=first;
        for (int i = 0; i <arr.size() ; i++) {
            student news=arr.get(i);
            if (news.getNumber()==1){
                first=news;
                helper=first;
            }else {
                helper.setNext(news);
                news.setPre(helper);
                helper=news;
            }
        }
    }
    public List<student> select(int teams,int k,int m,List<student> arr){
            student helper=arr.get(m);
            student helper1=helper.getPre();
            helper.setTeam(teams);
            helper=helper.getNext();
            int count=0;
            if (helper!=null){
            while (helper.getNext()!=null && count<k){
                helper.setTeam(teams);
                helper=helper.getNext();
                count++;
            }
            if (count<k){
                helper.setTeam(teams);
                helper=helper.getNext();
            }
            }

            count=0;
            if (helper1!=null){
            while (helper1.getPre()!=null && count<k){
                helper1.setTeam(teams);
                helper1=helper1.getPre();
                count++;
            }
            if (count<k){
                helper1.setTeam(teams);
                helper1=helper1.getPre();
            }
            }
            if (helper!=null && helper1!=null){
            helper1.setNext(helper);
            helper.setPre(helper1);
            arr.removeIf(student -> student.getTeam()!=0);
            }
            else if (helper1!=null){
                helper1.setNext(helper);
                arr.removeIf(student -> student.getTeam()!=0);
            }else if (helper!=null){
                helper.setPre(helper1);
                arr.removeIf(student -> student.getTeam()!=0);
            }else {
                arr.clear();
            }
        return arr;
    }
}
class student{
    private int number;
    private int skill;
    private student next;
    private student pre;
    private int team;

    public student(int number,int skill){
        this.number=number;
        this.skill=skill;
    }
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSkill() {
        return skill;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public student getNext() {
        return next;
    }

    public void setNext(student next) {
        this.next = next;
    }

    public student getPre() {
        return pre;
    }

    public void setPre(student pre) {
        this.pre = pre;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }
}