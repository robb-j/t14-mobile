package uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces;

import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.models.Categorisation;
import uk.ac.ncl.csc2022.t14.bankingapp.models.MonthBudget;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Reward;

/**
 * Created by Jack on 12/03/2015.
 */
public interface ServerBudgetingInterface {

    void loadNewPaymentsForUser(NewPaymentsDelegate delegate);

    void categorisePayments(List<Categorisation> categorizations, CategoriseDelegate delegate);

    void updateBudget(MonthBudget newBudget, BudgetUpdateDelegate delegate);

    void chooseReward(Reward reward, ChooseRewardDelegate delegate);

    void performSpin(PointSpinDelegate delegate);
}
