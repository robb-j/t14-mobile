package uk.ac.ncl.csc2022.t14.bankingapp.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import uk.ac.ncl.csc2022.t14.bankingapp.activities.AccountActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.LoginDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.TransactionDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.TransferDelegate;

/**
 * Created by Sam on 17/02/2015.
 */
public class DummyServerConnecter implements ServerInterface {


    @Override
    public void login(String username, char[] password, int[] indices, LoginDelegate delegate) {

        boolean passed = false;

        /* Correct credentials */
        if (username.equals("Jeff")) {
            if (password[0] == 's' && indices[0] == 2) {
                User user = new User(26, "Bobby99", "Jeff", "Ree", "23/08/1982");
                Product product1 = new Product(1, "Saving Accounts", "This is going to save you money.");
                Product product2 = new Product(1, "ISA", "Use this.");
                List<Product> products = new ArrayList<Product>();
                products.add(product1);
                products.add(product2);
                String token = "correctToken";
                delegate.loginPassed(user, products, token);
                passed = true;
            }
        }
        if (!passed) {
            delegate.loginFailed("Invalid login details");
        }
    }

    @Override
    public void loadTransactions(Account account, Month month, String token, TransactionDelegate delegate) {

        // Loading progress
        class TransactionTimerTask extends TimerTask {
            private String token;
            private Account account;
            private TransactionDelegate delegate;

            @Override
            public void run() {
                if (token.equals("correctToken")) {
                    List<Transaction> transactions = new ArrayList<Transaction>();
                    Calendar cal = Calendar.getInstance(), cal2 = Calendar.getInstance(), cal3 = Calendar.getInstance();
                    cal.set(Calendar.YEAR, 2015);
                    cal.set(Calendar.MONTH, Calendar.JANUARY);
                    cal.set(Calendar.DAY_OF_MONTH, 22);
                    cal2.set(Calendar.YEAR, 2015);
                    cal2.set(Calendar.MONTH, Calendar.JANUARY);
                    cal2.set(Calendar.DAY_OF_MONTH, 17);
                    cal3.set(Calendar.YEAR, 2015);
                    cal3.set(Calendar.MONTH, Calendar.JANUARY);
                    cal3.set(Calendar.DAY_OF_MONTH, 12);
                    transactions.add(new Transaction(20, 120.05, cal.getTime(), account, "payee1"));
                    transactions.add(new Transaction(22, 56.21, cal.getTime(), account, "payee3"));
                    transactions.add(new Transaction(22, 56.21, cal.getTime(), account, "payee6"));
                    transactions.add(new Transaction(21, 22.31, cal2.getTime(), account, "payee2"));
                    transactions.add(new Transaction(21, 22.31, cal2.getTime(), account, "payee7"));
                    transactions.add(new Transaction(23, 37.43, cal3.getTime(), account, "payee4"));
                    transactions.add(new Transaction(23, 37.43, cal3.getTime(), account, "payee5"));
                    transactions.add(new Transaction(23, 37.43, cal3.getTime(), account, "payee8"));
                    transactions.add(new Transaction(23, 37.43, cal3.getTime(), account, "payee9"));
                    transactions.add(new Transaction(23, 37.43, cal3.getTime(), account, "payee10"));
                    transactions.add(new Transaction(23, 37.43, cal3.getTime(), account, "payee11"));
                    transactions.add(new Transaction(23, 37.43, cal3.getTime(), account, "payee12"));
                    transactions.add(new Transaction(23, 37.43, cal3.getTime(), account, "payee13"));
                    transactions.add(new Transaction(23, 37.43, cal3.getTime(), account, "payee14"));
                    transactions.add(new Transaction(23, 37.43, cal3.getTime(), account, "payee15"));

                    delegate.transactionsLoaded(account, transactions);
                } else {
                    delegate.transactionsLoadFailed("Authentication error.");
                }
            }
        }

        Timer timer = new Timer();
        TransactionTimerTask task = new TransactionTimerTask();
        task.account = account;
        task.delegate = delegate;
        task.token = token;
        timer.schedule(task, 1000);


    }

    @Override
    public void makeTransfer(Account accFrom, Account accTo, double amount, String token, TransferDelegate delegate) {

        if (token.equals("correctToken")) {
            if (amount < accFrom.getBalance()) {
                delegate.transferPassed(accFrom, accTo, amount);
            } else {
                delegate.transferFailed("Not enough monies");
            }
        } else {
            delegate.transferFailed("Authentication error.");
        }

    }
}
