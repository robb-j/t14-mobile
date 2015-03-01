package uk.ac.ncl.csc2022.t14.bankingapp.listadapters;

import android.content.Context;
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
import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;

/**
 * Created by Sam on 23/02/2015.
 */
public class TransactionAdapter extends BaseAdapter {

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_SEPARATOR = 1;

    // private ArrayList<String> mData = new ArrayList<>();
    private ArrayList<Transaction> mData = new ArrayList<>();
    private TreeSet<Integer> sectionHeader = new TreeSet<>();

    public ViewHolder holder = null;

    private LayoutInflater mInflater;

    public TransactionAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final Transaction item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final Transaction item) {
        mData.add(item);
        sectionHeader.add(mData.size()-1);
        notifyDataSetChanged();
    }

    public void hideView(int position) {
        holder.textView1.setVisibility(View.GONE);
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
                    convertView = mInflater.inflate(R.layout.item_transaction, null);
                    holder.textView1 = (TextView) convertView.findViewById(R.id.textview_transaction_details);
                    holder.textView2 = (TextView) convertView.findViewById(R.id.textview_transaction_cost);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.item_transaction_header, null);
                    holder.textView1 = (TextView) convertView.findViewById(R.id.separator);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItemViewType(position) == TYPE_SEPARATOR) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(mData.get(position).getDate());
            holder.textView1.setText(cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR));
        } else {
            holder.textView1.setText(mData.get(position).getPayee());
            holder.textView2.setText(Utility.doubleToCurrency(mData.get(position).getAmount()));
        }



        return convertView;
    }

    public static class ViewHolder {
        public TextView textView1, textView2;
    }
}
