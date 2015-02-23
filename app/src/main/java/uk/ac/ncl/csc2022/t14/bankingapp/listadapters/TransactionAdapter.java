package uk.ac.ncl.csc2022.t14.bankingapp.listadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;

/**
 * Created by Sam on 23/02/2015.
 */
public class TransactionAdapter extends ArrayAdapter<Transaction> {

    public TransactionAdapter(Context context, ArrayList<Transaction> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Transaction transaction = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_transaction, parent, false);
        }
        // Lookup view for data population
        TextView transactionDetails = (TextView) convertView.findViewById(R.id.textview_transaction_details);
        TextView transactionCost = (TextView) convertView.findViewById(R.id.textview_transaction_cost);
        // Populate the data into the template view using the data object
        transactionDetails.setText(transaction.getPayee());
        transactionCost.setText("£" + Double.toString(transaction.getAmount()));
        // Return the completed view to render on screen
        return convertView;
    }
}
