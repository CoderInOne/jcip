package xunshan.jcip.ch10;

import xunshan.jcip.util.SimpleThreadUtils;

public class TransferDemo {
    public void transferMoney(Account fromAcc, Account toAcc, int amount) {
        synchronized (fromAcc) {
            synchronized (toAcc) {
                if (fromAcc.getBalance() < toAcc.getBalance()) {
                    throw new RuntimeException("no enough balance");
                }
                else {
                    fromAcc.debit(amount);
                    toAcc.credit(amount);
                }
            }
        }
    }

    public void transferMoneyWithOrdering(Account fromAcc, Account toAcc, int amount) {
        if (fromAcc.getId() < toAcc.getId()) {
            Account tmp = fromAcc;
            fromAcc = toAcc;
            toAcc = tmp;
        }

        transferMoney(fromAcc, toAcc, amount);
    }
}
