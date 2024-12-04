package lab7;

public class AccountSync extends Account{
    private double balance;

    public synchronized void deposit(double money) {
        try {
            double newBalance = balance + money;
            Thread.sleep(10); // Simulating processing time
            balance = newBalance;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getBalance() {
        return balance;
    }
}