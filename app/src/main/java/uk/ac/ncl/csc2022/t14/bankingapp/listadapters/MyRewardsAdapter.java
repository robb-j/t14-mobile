package uk.ac.ncl.csc2022.t14.bankingapp.listadapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.models.RewardTaken;

/**
 * Created by Sam on 17/04/2015.
 */
public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<RewardTaken> rewards = Collections.emptyList();
    Context context;

    public MyRewardsAdapter(Context context, List<RewardTaken> rewards) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.rewards = rewards;
    }

    @Override
    public MyRewardsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_my_rewards, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyRewardsAdapter.MyViewHolder holder, int position) {

        RewardTaken current = rewards.get(position);

        holder.text.setText(current.getReward().getName());

    }

    @Override
    public int getItemCount() {
        return rewards.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text;

        public MyViewHolder(View itemView) {
            super(itemView);

            text = (TextView) itemView.findViewById(R.id.text_my_rewards);
        }

    }
}
