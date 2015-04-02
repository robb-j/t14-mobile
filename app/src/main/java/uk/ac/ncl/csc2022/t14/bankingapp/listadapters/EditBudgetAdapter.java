package uk.ac.ncl.csc2022.t14.bankingapp.listadapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeSet;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.Utility;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;

/**
 * Created by Sam on 01/04/2015.
 */
public class EditBudgetAdapter extends BaseAdapter {

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_SEPARATOR = 1;
    public static final int TYPE_NEW = 2;

    private ArrayList<Object> mData = new ArrayList<>();
    private TreeSet<Integer> sectionBudget = new TreeSet<>();
    private TreeSet<Integer> sectionNew = new TreeSet<>();
    private TreeSet<Integer> sectionHeader = new TreeSet<>();

    public ViewHolder holder = null;

    private LayoutInflater mInflater;

    public EditBudgetAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final BudgetCategory item) {
        mData.add(item);
        sectionBudget.add(mData.size()-1);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final BudgetGroup item) {
        mData.add(item);
        sectionHeader.add(mData.size()-1);
        notifyDataSetChanged();
    }

    // item to be clicked which will add a new budget or category
    public void addNewItem(final String item) {
        mData.add(item);
        sectionNew.add(mData.size()-1);
        //notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {


        if (sectionHeader.contains(position)) {
            return TYPE_SEPARATOR;
        } else if (sectionBudget.contains(position)) {
            return TYPE_ITEM;
        } else {
            return TYPE_NEW;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
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

            // reset the holder
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.item_edit_budget, null);
                    holder.textHolder1 = (TextView) convertView.findViewById(R.id.edit_budget_details);
                    holder.textHolder2 = (TextView) convertView.findViewById(R.id.edit_budget_cost);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.item_budget_header, null);
                    holder.textHolder1 = (TextView) convertView.findViewById(R.id.budget_separator);
                    break;
                case TYPE_NEW:
                    convertView = mInflater.inflate(R.layout.item_add_new_budget, null);
                    holder.textHolder1 = (TextView) convertView.findViewById(R.id.text_new_item_plus);
                    holder.textHolder2 = holder.textHolder1;
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItemViewType(position) == TYPE_SEPARATOR) {
            BudgetGroup current = (BudgetGroup)mData.get(position);
            holder.textHolder1.setText(current.getName());
        } else if (getItemViewType(position) == TYPE_NEW) {
            String current = (String)mData.get(position);
            holder.textHolder1.setText(current);
            if (position == getCount()-1) {
                holder.textHolder1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                holder.textHolder1.setTypeface(null, Typeface.BOLD);
            }

        } else {
            BudgetCategory current = (BudgetCategory)mData.get(position);
            // set text to budget name
            holder.textHolder1.setText(current.getName());
            if (current.getBudgeted() > 0) {
                holder.textHolder2.setTextColor(Color.rgb(0, 100, 0));
            } else {
                holder.textHolder2.setTextColor(Color.rgb(100, 0, 0));
            }
            holder.textHolder2.setText(Utility.doubleToCurrency(current.getBudgeted() - current.getSpent()));
        }



        return convertView;
    }

    public static class ViewHolder {

        public TextView textHolder1;
        public TextView textHolder2;
    }

}
