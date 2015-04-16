package uk.ac.ncl.csc2022.t14.bankingapp.listadapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.Utility;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Reward;
import uk.ac.ncl.csc2022.t14.bankingapp.models.RewardTaken;

/**
 * Created by Robert Hamilton on 29/03/2015.
 */
public class AwardsAdapter extends BaseAdapter
{
    public Context mContext;
    protected LayoutInflater mInflater;

    private List<RewardTaken> mDataChild;

    public AwardsAdapter(Context aContext, List<RewardTaken> DataChild)
    {
        mContext = aContext;

        mDataChild = DataChild;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return mDataChild.size();
    }

    @Override
    public RewardTaken getItem(int position)
    {
        return mDataChild.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.item_award, null);
        }
        TextView awardDate = (TextView)convertView.findViewById(R.id.textview_award_date);
        TextView awardDetails = (TextView)convertView.findViewById(R.id.textview_award_details);
        //awardDate.setText(mDataChild.get(position).getDateTaken().toString());
        awardDetails.setText(mDataChild.get(position).getReward().getName());
        return convertView;
    }







}
