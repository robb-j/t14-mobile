package uk.ac.ncl.csc2022.t14.bankingapp.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        if (token.equals("correctToken")) {
            List<Transaction> transactions = new ArrayList<Transaction>();
            transactions.add(new Transaction(20, 120.00, account, "payee1"));
            transactions.add(new Transaction(21, 22.31, account, "payee2"));

            delegate.transactionsLoaded(account, transactions);
        } else {
            delegate.transactionsLoadFailed("Authentication error.");
        }
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
