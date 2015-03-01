package uk.ac.ncl.csc2022.t14.bankingapp.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import uk.ac.ncl.csc2022.t14.bankingapp.activities.AccountActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.MainActivity;
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
    public void loadTransactions(Account account, int month, String token, TransactionDelegate delegate) {

        final int fMonth = month;

        // Loading progress
        class TransactionTimerTask extends TimerTask {
            private String token;
            private Account account;
            private TransactionDelegate delegate;

            @Override
            public void run() {
                if (token.equals("correctToken")) {

                    /* February test data --- will be deleted when pulling data from the server */
                    List<Transaction> transactions = new ArrayList<Transaction>();
                    Calendar cal = Calendar.getInstance(), cal2 = Calendar.getInstance(), cal3 = Calendar.getInstance();
                    cal.set(Calendar.YEAR, 2015);
                    cal.set(Calendar.MONTH, Calendar.FEBRUARY);
                    cal.set(Calendar.DAY_OF_MONTH, 22);
                    cal2.set(Calendar.YEAR, 2015);
                    cal2.set(Calendar.MONTH, Calendar.FEBRUARY);
                    cal2.set(Calendar.DAY_OF_MONTH, 17);
                    cal3.set(Calendar.YEAR, 2015);
                    cal3.set(Calendar.MONTH, Calendar.FEBRUARY);
                    cal3.set(Calendar.DAY_OF_MONTH, 12);
                    transactions.add(new Transaction(20, 120.05, cal.getTime(), account, "payee1"));
                    transactions.add(new Transaction(21, -1.21, cal.getTime(), account, "payee3"));
                    transactions.add(new Transaction(22, -55.21, cal.getTime(), account, "payee6"));
                    transactions.add(new Transaction(23, -6.31, cal2.getTime(), account, "payee2"));
                    transactions.add(new Transaction(24, -29.31, cal2.getTime(), account, "payee7"));
                    transactions.add(new Transaction(25, -33.11, cal3.getTime(), account, "payee4"));
                    transactions.add(new Transaction(26, -5.99, cal3.getTime(), account, "payee5"));
                    transactions.add(new Transaction(27, 21.34, cal3.getTime(), account, "payee8"));
                    transactions.add(new Transaction(28, -23.73, cal3.getTime(), account, "payee9"));
                    transactions.add(new Transaction(29, -2.43, cal3.getTime(), account, "payee10"));
                    transactions.add(new Transaction(30, -7.49, cal3.getTime(), account, "payee11"));
                    transactions.add(new Transaction(31, -35.91, cal3.getTime(), account, "payee12"));
                    transactions.add(new Transaction(32, -50.99, cal3.getTime(), account, "payee13"));
                    transactions.add(new Transaction(33, 74.13, cal3.getTime(), account, "payee14"));
                    transactions.add(new Transaction(34, -3.42, cal3.getTime(), account, "payee15"));

                    /* March test data --- will be deleted when pulling data from the server */
                    List<Transaction> transactionsMar = new ArrayList<Transaction>();
                    Calendar cal4 = Calendar.getInstance(), cal5 = Calendar.getInstance(), cal6 = Calendar.getInstance();
                    cal4.set(Calendar.YEAR, 2015);
                    cal4.set(Calendar.MONTH, Calendar.MARCH);
                    cal4.set(Calendar.DAY_OF_MONTH, 22);
                    cal5.set(Calendar.YEAR, 2015);
                    cal5.set(Calendar.MONTH, Calendar.MARCH);
                    cal5.set(Calendar.DAY_OF_MONTH, 17);
                    cal6.set(Calendar.YEAR, 2015);
                    cal6.set(Calendar.MONTH, Calendar.MARCH);
                    cal6.set(Calendar.DAY_OF_MONTH, 12);
                    transactionsMar.add(new Transaction(35, 25.20, cal4.getTime(), account, "payee1"));
                    transactionsMar.add(new Transaction(36, -10.13, cal4.getTime(), account, "payee3"));
                    transactionsMar.add(new Transaction(37, -3.14, cal5.getTime(), account, "payee6"));
                    transactionsMar.add(new Transaction(38, -4.98, cal5.getTime(), account, "payee2"));
                    transactionsMar.add(new Transaction(39, -8.99, cal5.getTime(), account, "payee7"));
                    transactionsMar.add(new Transaction(40, -2.99, cal6.getTime(), account, "payee4"));
                    transactionsMar.add(new Transaction(41, -1.99, cal6.getTime(), account, "payee5"));
                    transactionsMar.add(new Transaction(42, -0.99, cal6.getTime(), account, "payee8"));
                    transactionsMar.add(new Transaction(43, 66.21, cal6.getTime(), account, "payee9"));
                    transactionsMar.add(new Transaction(44, -33.1, cal6.getTime(), account, "payee10"));

                    switch (fMonth) {
                        case Calendar.FEBRUARY:
                            delegate.transactionsLoaded(account, transactions);
                            break;
                        case Calendar.MARCH:
                            delegate.transactionsLoaded(account, transactionsMar);
                            break;
                    }

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
            if (amount <= accFrom.getBalance()) {

                // Subtract the value of the transfer fom the old account...
                double accFromNewBalance = accFrom.getBalance() - amount;
                accFrom.setBalance(accFromNewBalance);
                MainActivity.testUser.getAccounts();

                // ...and add it to the new one.
                double accToNewBalance = accTo.getBalance() + amount;
                accTo.setBalance(accToNewBalance);

                delegate.transferPassed(accFrom, accTo, amount);
            } else {
                delegate.transferFailed("Insufficient funds.");
            }
        } else {
            delegate.transferFailed("Authentication error.");
        }

    }
}
