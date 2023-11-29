import java.util.*;

public class ConcreteShoppingMall implements ShoppingMall{
    private List<Member> members=new ArrayList<>();
    private List<Double> SKINCARE=new ArrayList<>();
    private double skcost=0;
    private List<Double> DIGITAL_PRODUCT=new ArrayList<>();
    private double dicost=0;
    private List<Double> WATCH=new ArrayList<>();
    private double wacost=0;
    private List<Double> JEWELRY=new ArrayList<>();
    private double jecost=0;
    private List<Double> DRINKS=new ArrayList<>();
    private double drcost=0;
    private List<Double> LUGGAGE=new ArrayList<>();
    private double lucost=0;
    private List<Double> PERFUME=new ArrayList<>();
    private double pecost=0;



    @Override
    public void addMember(String info) {
        String[] arr1=info.split(" ",4);
       if (arr1[3].equals("G")){
           Member m1=new GoldCardMember(info);
           members.add(m1);
       }else {
           Member m1=new SilverCardMember(info);
           members.add(m1);
       }
    }

    @Override
    public void addMember(List<String> infos) {
        for (int i = 0; i < infos.size(); i++) {
            addMember(infos.get(i));
        }
    }

    @Override
    public Member getMember(String memberId) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getMemberId().equals(memberId)){
                return members.get(i);
            }
        }
        return null;
    }

    @Override
    public double placeOrder(String memberId, int cost, ProductCategory category) {
        Member m3=getMember(memberId);
        double a=m3.consume(cost);
        String record=String.format("%s "+category+" "+cost+" %.0f",memberId,a);
        m3.getMemberRecords().add(record);
        if (category.equals(ProductCategory.SKINCARE)){
            SKINCARE.add(a);
            skcost+=a;
        }else if (category.equals(ProductCategory.DIGITAL_PRODUCT)){
            DIGITAL_PRODUCT.add(a);
            dicost+=a;
        }else if (category.equals(ProductCategory.WATCH)){
            wacost+=a;
            WATCH.add(a);
        }else if (category.equals(ProductCategory.JEWELRY)){
            JEWELRY.add(a);
            jecost+=a;
        }else if (category.equals(ProductCategory.DRINKS)){
            DRINKS.add(a);
            drcost+=a;
        }else if (category.equals(ProductCategory.LUGGAGE)){
            LUGGAGE.add(a);
            lucost+=a;
        }else {
            PERFUME.add(a);
            pecost+=a;
        }
        return a;
    }

    @Override
    public List<String> getMemberRecords(String memberId) {
            Member m4=getMember(memberId);
        return m4.getMemberRecords();
    }

    @Override
    public List<String> getCostByCategory() {
        List<String> CostByCategory=new ArrayList<>();
        String sk=String.format("SKINCARE "+SKINCARE.size()+" %.0f",skcost);
        CostByCategory.add(sk);
        String di=String.format("DIGITAL_PRODUCT "+DIGITAL_PRODUCT.size()+" %.0f",dicost);
        CostByCategory.add(di);
        String wa=String.format("WATCH "+WATCH.size()+" %.0f",wacost);
        CostByCategory.add(wa);
        String je=String.format("JEWELRY "+JEWELRY.size()+" %.0f",jecost);
        CostByCategory.add(je);
        String dr=String.format("DRINKS "+DRINKS.size()+" %.0f",drcost);
        CostByCategory.add(dr);
        String lu=String.format("LUGGAGE "+LUGGAGE.size()+" %.0f",lucost);
        CostByCategory.add(lu);
        String pe=String.format("PERFUME "+PERFUME.size()+" %.0f",pecost);
        CostByCategory.add(pe);
        return CostByCategory;
    }

    @Override
    public List<String> getMemberRecordByGenderAndAge(char gender, int lowerAge, int upperAge) {
        List<Member> newmember=new ArrayList<>();
        for (int i = 0; i < this.members.size(); i++) {
            if (this.members.get(i).getAge()<=upperAge && this.members.get(i).getAge()>=lowerAge && this.members.get(i).getGender()==gender && this.members.get(i).getTotalCost()>0){
                newmember.add(this.members.get(i));
            }
        }
        Collections.sort(newmember, new Comparator<Member>() {
            @Override
            public int compare(Member o1, Member o2) {
                if (o1.getAge()==o2.getAge()){
                    return (int) Math.round(o2.getTotalCost()-o1.getTotalCost());
                }else {
                    return o1.getAge()-o2.getAge();
                }
            }
        });
        List<String> memberre=new ArrayList<>();
        for (int i = 0; i < newmember.size(); i++) {
            memberre.add(newmember.get(i).getGenderAgeCost());
        }
        return memberre;

    }

    @Override
    public double getTotalCost(String memberId) {
        Member m5=getMember(memberId);
        return m5.getTotalCost();
    }

    @Override
    public double getTotalIncome() {
        return skcost+drcost+dicost+wacost+jecost+lucost+pecost;
    }
}
