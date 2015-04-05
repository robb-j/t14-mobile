package uk.ac.ncl.csc2022.t14.bankingapp.server.live;

import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Categorisation;
import uk.ac.ncl.csc2022.t14.bankingapp.models.MonthBudget;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Reward;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ATMDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.BudgetUpdateDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.CategoriseDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ChooseRewardDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.HeatMapDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.LoginDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.NewPaymentsDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.PointSpinDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerBudgetingInterface;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.TransactionDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.TransferDelegate;

/**
 * An implementation of the server interfaces that connects to the live server
 * Created by Rob A on 30/03/15.
 */
public class LiveServerConnector implements ServerInterface, ServerBudgetingInterface {


    @Override
    public void login(String username, char[] password, int[] indices, LoginDelegate delegate) {

    }

    @Override
    public void loadTransactions(Account account, int month, int year, String token, TransactionDelegate delegate) {

    }

    @Override
    public void makeTransfer(int accFrom, int accTo, double amount, String token, TransferDelegate delegate) {

    }

    @Override
    public void loadATMS(ATMDelegate delegate) {

    }

    @Override
    public void loadHeatMap(HeatMapDelegate delegate) {

    }

    @Override
    public void loadNewPaymentsForUser(NewPaymentsDelegate delegate) {

    }

    @Override
    public void categorisePayments(List<Categorisation> categorizations, CategoriseDelegate delegate) {

    }

    @Override
    public void updateBudget(MonthBudget newBudget, BudgetUpdateDelegate delegate) {

    }

    @Override
    public void chooseReward(Reward reward, ChooseRewardDelegate delegate) {

    }

    @Override
    public void performSpin(PointSpinDelegate delegate) {

    }

}
