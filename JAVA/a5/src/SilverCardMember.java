public class SilverCardMember extends Member{
    private double toltalconsume=0;
    private double point=0;



    public SilverCardMember(String info) {
        super(info);
        String[] arr2=info.split(" ",4);

    }
    @Override
    public double consume(int amount) {
        if (toltalconsume==0){
            point=amount/30;
            toltalconsume+=amount;
            return amount;
        }else if (toltalconsume<=10000){
           if (point<=amount){
               double actual=amount-point;
               toltalconsume+=actual;
               point=amount/30;
               return actual;
           }else {
               point=point-amount+amount/30;
               return 0;
           }
        }else {
            if (point<=amount){
                double actual=amount-point;
                toltalconsume+=actual;
                point=(amount/30)*1.5;
                return actual;
            }else {
                point=point-amount+(amount/30)*1.5;
                return 0;
            }
        }
    }
    @Override
    public double getTotalCost(){
        return toltalconsume;
    }
    @Override
    public String getGenderAgeCost(){
        double get_double=(Math.round(this.getTotalCost()*10)/10.0);
        return super.getMemberId() +" "+super.getGender()+" "+super.getAge()+" "+get_double;
    }
    public String toString(){
        double get_double2=(Math.round(this.point*10)/10.0);
        return "SilverCardMember: "+super.getMemberId()+" "+super.getGender()+" "+super.getAge()+" points="+get_double2;
    }

    public double getToltalconsume() {
        return toltalconsume;
    }

    public void setToltalconsume(double toltalconsume) {
        this.toltalconsume = toltalconsume;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

}
