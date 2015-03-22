package uk.ac.ncl.csc2022.t14.bankingapp.models;

import java.util.Date;

/**
 * Created by Jack on 22/03/2015.
 */
public class RewardTaken extends ModelObject {

    private Reward reward;
    private Date dateTaken;

    public RewardTaken(int id, Reward reward) {
        super(id);
        setReward(reward);
        setDateTaken(new Date());
    }

    public Reward getReward() {
        return reward;
    }
    private void setReward(Reward reward) {
        this.reward = reward;
    }

    public Date getDateTaken() {
        return dateTaken;
    }
    private void setDateTaken(Date dateTaken) {
        this.dateTaken = dateTaken;
    }
}
