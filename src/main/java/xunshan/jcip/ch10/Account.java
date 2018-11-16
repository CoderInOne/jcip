package xunshan.jcip.ch10;

public class Account {
    private int id;
    private int balance;

    public int getId() {
        return this.id;
    }

    public Account(int id, int amount) {
        this.id = id;
        credit(amount);
    }

    public void credit(int amount) {
        this.balance += amount;
    }

    public void debit(int amount) {
        this.balance -= amount;
    }

    public int getBalance() {
        return this.balance;
    }
}
