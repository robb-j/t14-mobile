package uk.ac.ncl.csc2022.t14.bankingapp.listadapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.CategorizeActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.MainActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.CategorizeLocationDelegate;

/**
 * Created by Robert Hamilton on 20/03/2015.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter
{
    private Context _context;
    //The transactions
    private List<String> _listDataHeader;
    //which transactions have been given a location
    private List<Boolean> listTransactionLocated;
    //The categories
    private HashMap<String, List<String>> _listDataChild;
    private CategorizeLocationDelegate cLD;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData, CategorizeLocationDelegate _cLD, List<Boolean> lTD)
    {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.cLD = _cLD;
        this.listTransactionLocated = lTD;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    //Returns the view for the child item
    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        final String childText = (String) getChild(groupPosition, childPosition);


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.transaction_categories, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.transaction_category_item);
        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {

        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    //Returns the view for the list group header
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,View convertView, ViewGroup parent)
    {

        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.transaction_categorize_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.transaction_payee);

            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);
        final View header = convertView;
        ImageView globe = (ImageView)convertView.findViewById(R.id.location_categorize_button);
        globe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Tell the activity to open the map, giving it the groupnumber as the relevant transaction
                cLD.openMap(groupPosition);


            }
        });
        if(listTransactionLocated.get(groupPosition))
        {
            //If the transaction has been given a location, set the globe icon to white
            Log.d("The list is: ", listTransactionLocated.toString());
            globe.setImageResource(R.drawable.tglobe_icon_white);
        }
        else
        {
            //if it hasn't, make sure the icon is still black
            globe.setImageResource(R.drawable.tglobe_icon);
        }





        return convertView;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }


}
