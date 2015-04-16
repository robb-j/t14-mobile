package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;


//import com.google.android.gms.internal.cl;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.LloydsActionBarActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.Utility;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.TransactionAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;
import uk.ac.ncl.csc2022.t14.bankingapp.server.DummyServerConnector;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.TransactionDelegate;

public class AccountActivity extends LloydsActionBarActivity implements TransactionDelegate {

    private static int accountId;
    private TransactionAdapter adapter;
    private static int month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        /* retrieves the account that was passed through to this activity */
        accountId = getIntent().getExtras().getInt("accountId");

        month = Calendar.getInstance().get(Calendar.MONTH);
        year = Calendar.getInstance().get(Calendar.YEAR);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }



    @Override
    public void onResume() {
        super.onResume();

        //Get this account's updated values from a transaction
        Account account = DataStore.sharedInstance().getCurrentUser().getAccountForId(accountId);

        //Display them accordingly
        TextView txtBalanceCost = (TextView)findViewById(R.id.textview_balances_cost);
        txtBalanceCost.setText(Utility.doubleToCurrency(account.getBalance()) + "\n" + Utility.doubleToCurrency(account.getOverdraftLimit() + account.getBalance()) +
                "\n" + Utility.doubleToCurrency(account.getOverdraftLimit()));

    }



    @Override
    public void transactionsLoaded(Account account, List<Transaction> transactions) {

        final ListView listTransactions = (ListView)findViewById(R.id.list_transactions);
        final TextView errorNoTransactions = (TextView)findViewById(R.id.text_error_no_transactions);

        adapter = new TransactionAdapter(this);

        Calendar cal = Calendar.getInstance();

        int lastDayAdded = 32;

        if (transactions != null) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    errorNoTransactions.setVisibility(View.INVISIBLE);
                }
            });



            // Assume transactions are ordered in descending order and all from one month
            for (Transaction transaction : transactions) {

                cal.setTime(transaction.getDate());
                int currentDay = cal.get(Calendar.DAY_OF_MONTH);

                if (lastDayAdded != currentDay) {
                    adapter.addSectionHeaderItem(transaction);
                    lastDayAdded = currentDay;
                }

                adapter.addItem(transaction);
            }


        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    errorNoTransactions.setText("Sorry, no transactions found for " + getMonth(month) + " " + year);
                    errorNoTransactions.setVisibility(View.VISIBLE);
                }
            });


        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                listTransactions.setAdapter(adapter);

            }
        });



    }

    @Override
    public void transactionsLoadFailed(String errMessage) {

        TextView errorNoTransactions = (TextView)findViewById(R.id.text_error_no_transactions);

        errorNoTransactions.setText(errMessage);
        errorNoTransactions.setVisibility(View.VISIBLE);
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_account, container, false);

            Account account = DataStore.sharedInstance().getCurrentUser().getAccountForId(accountId);



            // Set the account name to the name of the account passed through.
            TextView txtAccName = (TextView)rootView.findViewById(R.id.textview_account_name);
            txtAccName.setText(account.getName());

            TextView txtBalanceText = (TextView)rootView.findViewById(R.id.textview_balances_text);
            txtBalanceText.setText("Balance: \nAvailable: \nOverdraft: ");

            TextView txtBalanceCost = (TextView)rootView.findViewById(R.id.textview_balances_cost);
            txtBalanceCost.setText(Utility.doubleToCurrency(account.getBalance()) + "\n" + Utility.doubleToCurrency(account.getOverdraftLimit() + account.getBalance()) +
                    "\n" + Utility.doubleToCurrency(account.getOverdraftLimit()));



            /* Set up the buttons */
            Button btnMakeTransfer = (Button) rootView.findViewById(R.id.btn_make_transfer);
            Button btnSelectMonth = (Button)rootView.findViewById(R.id.btn_select_month);
            Button btnSelectYear = (Button)rootView.findViewById(R.id.btn_select_year);

            btnMakeTransfer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnMakeTransfer(v);
                }
            });

            btnSelectMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnSelectMonth(v);
                }
            });

            btnSelectYear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnSelectYear(v);
                }
            });


            return rootView;
        }

        @Override
        public void onStart() {
            super.onStart();

            refreshMonth();
        }

        /**
         * Opens the transfer page and passes through the relevant account.
         */
        public void btnMakeTransfer(View v) {

            Intent i = new Intent(getActivity(), TransferActivity.class);

            // pass through the relevant account
            Account account = DataStore.sharedInstance().getCurrentUser().getAccountForId(accountId);
            i.putExtra("accountId", account.getId());
            startActivity(i);
        }

        /**
         * Allows the user to view the selected month of transactions.
         */
        public void btnSelectMonth(View v) {

            String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select a month")
                    .setItems(months, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                            month = which;
                            refreshMonth();
                        }
                    });
            Dialog dialog = builder.create();
            dialog.show();


        }

        /**
         * Allows the user to view the selected year of transactions.
         */
        public void btnSelectYear(View v) {

            final Dialog d = new Dialog(getActivity());
            d.setTitle("Select a year");
            d.setContentView(R.layout.year_number_picker);
            Button btnOK = (Button) d.findViewById(R.id.btn_year_number_picker);
            final NumberPicker np = (NumberPicker) d.findViewById(R.id.number_picker_year);
            np.setMaxValue(Calendar.getInstance().get(Calendar.YEAR)); // max value current year
            np.setMinValue(Calendar.getInstance().get(Calendar.YEAR) - 50); // 50 years before the current year
            np.setValue(year);  // set the default value to the year selected
            np.setWrapSelectorWheel(false);
            btnOK.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    year = np.getValue();
                    refreshMonth();
                    d.dismiss();
                }
            });
            d.show();



        }

        /**
         * Reload the transactions for the selected month.
         */
        public void refreshMonth() {

            ServerInterface transactionLoader = DataStore.sharedInstance().getConnector();
            Account account = DataStore.sharedInstance().getCurrentUser().getAccountForId(accountId);
            transactionLoader.loadTransactions(account, month+1, year, (TransactionDelegate)getActivity());

        }

    }

    /**
     * Return an integer between 0-11 as its related month.
     * @param month Integer to be converted
     * @return
     */
    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }
}
