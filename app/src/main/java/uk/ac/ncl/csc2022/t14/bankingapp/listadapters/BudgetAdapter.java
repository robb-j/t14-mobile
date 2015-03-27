package uk.ac.ncl.csc2022.t14.bankingapp.listadapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeSet;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.Utility;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;
import uk.ac.ncl.csc2022.t14.bankingapp.models.MonthBudget;

/**
 * Created by Sam on 26/03/2015.
 */
public class BudgetAdapter extends BaseAdapter{
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_SEPARATOR = 1;

    private ArrayList<Object> mData = new ArrayList<>();
    private TreeSet<Integer> sectionHeader = new TreeSet<>();

    public ViewHolder holder = null;

    private LayoutInflater mInflater;

    public BudgetAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final BudgetCategory item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final BudgetGroup item) {
        mData.add(item);
        sectionHeader.add(mData.size()-1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.item_budget, null);
                    holder.textViewBudgetName = (TextView) convertView.findViewById(R.id.textview_budget_details);
                    holder.textViewBudgetCost = (TextView) convertView.findViewById(R.id.textview_budget_cost);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.item_budget_header, null);
                    holder.textViewBudgetName = (TextView) convertView.findViewById(R.id.budget_separator);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItemViewType(position) == TYPE_SEPARATOR) {
            BudgetGroup current = (BudgetGroup)mData.get(position);
            holder.textViewBudgetName.setText(current.getName());
        } else {
            BudgetCategory current = (BudgetCategory)mData.get(position);
            // set text to budget name
            holder.textViewBudgetName.setText(current.getName());
            if (current.getBudgeted() > 0) {
                holder.textViewBudgetCost.setTextColor(Color.rgb(0,100,0));
            } else {
                holder.textViewBudgetCost.setTextColor(Color.rgb(100,0,0));
            }
            holder.textViewBudgetCost.setText(Utility.doubleToCurrency(current.getBudgeted() - current.getSpent()));
        }



        return convertView;
    }

    public static class ViewHolder {

        public TextView textViewBudgetName;
        public TextView textViewBudgetCost;
    }
}
