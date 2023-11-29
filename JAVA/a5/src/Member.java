import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public abstract class Member {
    private String memberId;
    private char gender;
    private int age;
    private List<String> MemberRecords=new ArrayList<>();
    private String type;
    public Member() {
    }

    public Member(String memberId, char gender, int age) {
        this.memberId = memberId;
        this.gender = gender;
        this.age = age;
    }

    public Member(String info){
        String[] arr2=info.split(" ",4);
        this.memberId=arr2[0];
        this.gender=arr2[1].charAt (0);
        this.age=parseInt (arr2[2]);
        this.type=arr2[3];
    }
    public abstract double consume(int amount);

    public double getTotalCost() {
        return 0;
    }

    public String getGenderAgeCost() {
        return null;
    }
    @Override
    public String toString() {
        return String.format("%s %c %d", this.memberId, this.gender, this.age);
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getMemberRecords() {
        return MemberRecords;
    }

    public void setMemberRecords(List<String> memberRecords) {
        MemberRecords = memberRecords;
    }
}
