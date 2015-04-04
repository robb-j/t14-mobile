package uk.ac.ncl.csc2022.t14.bankingapp.listadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.models.PointGain;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Reward;

/**
 * Created by Robert Hamilton on 02/04/2015.
 */
public class PointsListAdapter extends BaseAdapter {
    public Context mContext;
    protected LayoutInflater mInflater;

    private List<PointGain> mDataChild;

    public PointsListAdapter(Context aContext, List<PointGain> DataChild)
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
    public PointGain getItem(int position)
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
            convertView = mInflater.inflate(R.layout.item_recentpoints, null);
        }
        TextView pointValue = (TextView)convertView.findViewById(R.id.textview_point_value);
        TextView pointDescription = (TextView)convertView.findViewById(R.id.textview_point_description);
        pointValue.setText(Integer.toString(mDataChild.get(position).getPoints()));
        pointDescription.setText(mDataChild.get(position).getDescription());
        return convertView;
    }
}
