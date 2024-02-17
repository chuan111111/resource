public class ITStaffFactory {

    public static ITStaff createDeveloper(){
        return new Developer();
    }
    public static ITStaff createManager(){return new ITManager();}
    public static ITStaff createTester(){return new Tester();}
}
