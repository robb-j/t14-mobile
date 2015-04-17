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
import uk.ac.ncl.csc2022.t14.bankingapp.models.PointGain;
import uk.ac.ncl.csc2022.t14.bankingapp.models.RewardTaken;

/**
 * Created by Sam on 17/04/2015.
 */
public class RecentPointsAdapter extends RecyclerView.Adapter<RecentPointsAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<PointGain> pointGains = Collections.emptyList();
    Context context;

    public RecentPointsAdapter(Context context, List<PointGain> pointGains) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.pointGains = pointGains;
    }

    @Override
    public RecentPointsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_recent_points, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecentPointsAdapter.MyViewHolder holder, int position) {

        PointGain current = pointGains.get(position);

        holder.text.setText(current.getName());
        holder.points.setText("" + current.getPoints());

    }

    @Override
    public int getItemCount() {
        return pointGains.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text, points;

        public MyViewHolder(View itemView) {
            super(itemView);

            text = (TextView) itemView.findViewById(R.id.text_points_text);
            points = (TextView) itemView.findViewById(R.id.text_points);

        }

    }
}