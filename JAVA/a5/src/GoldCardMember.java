public class GoldCardMember extends Member{
private double totalconsume=0;

    public GoldCardMember(String info) {
        super(info);

    }

    @Override
    public double consume(int amount) {
        if (amount<=2000){
            totalconsume+=amount;
            return amount;
        }else if (amount<=5000){
            totalconsume+=2000+(amount-2000)*0.95;
            return 2000+(amount-2000)*0.95;
        }else if (amount<=10000){
            totalconsume+=2000+3000*0.95+(amount-5000)*0.9;
            return 2000+3000*0.95+(amount-5000)*0.9;
        }else if (amount<=20000){
            totalconsume+=2000 + 3000 * 0.95 + 5000 * 0.9 + (amount-10000) * 0.85;
            return 2000 + 3000 * 0.95 + 5000 * 0.9 + (amount-10000) * 0.85;
        }else {
            totalconsume+=(2000 + 3000 * 0.95 + 5000 * 0.9 + 10000 * 0.85 + (amount-20000) * 0.8);
            return 2000 + 3000 * 0.95 + 5000 * 0.9 + 10000 * 0.85 + (amount-20000) * 0.8;
        }
    }
    @Override
    public double getTotalCost(){
        return this.totalconsume;
    }
    @Override
    public String getGenderAgeCost(){
       double get_double=(double)(Math.round(this.getTotalCost()*10)/10.0);
        return super.getMemberId() +" "+super.getGender()+" "+super.getAge()+" "+get_double;
    }
    @Override
    public String toString(){
        return "GoldCardMember: "+super.getMemberId()+" "+super.getGender()+" "+super.getAge();
    }

    public double getTotalconsume() {
        return totalconsume;
    }

    public void setTotalconsume(double totalconsume) {
        this.totalconsume = totalconsume;
    }

}
