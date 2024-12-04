package lab7;

import java.util.concurrent.locks.ReentrantLock;

public class AccountLock extends Account{
    private double balance;
    private final ReentrantLock lock = new ReentrantLock();

    public void deposit(double money) {
        lock.lock();
        try {
            double newBalance = balance + money;
            Thread.sleep(10); // Simulating processing time
            balance = newBalance;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public double getBalance() {
        return balance;
    }
}