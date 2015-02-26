package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import java.util.Calendar;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.TransactionAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;
import uk.ac.ncl.csc2022.t14.bankingapp.server.DummyServerConnecter;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.TransactionDelegate;

public class AccountActivity extends ActionBarActivity implements TransactionDelegate {

    private static Account account;
    private static ProgressDialog progressLoadTransactions;
    private TransactionAdapter adapter;
    private static int month;

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
        account = getIntent().getExtras().getParcelable("account");

        month = Calendar.getInstance().get(Calendar.MONTH);




        // Hide the action bar. -----DON'T TOUCH THIS-----
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void transactionsLoaded(Account account, List<Transaction> transactions) {

        progressLoadTransactions.dismiss();

        final ListView listTransactions = (ListView)findViewById(R.id.list_transactions);

        adapter = new TransactionAdapter(this);

        Calendar cal = Calendar.getInstance();

        int lastDayAdded = 32;

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



        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                listTransactions.setAdapter(adapter);

            }
        });

    }

    @Override
    public void transactionsLoadFailed(String errMessage) {

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

            // Set the account name to the name of the account passed through.
            TextView txtAccName = (TextView)rootView.findViewById(R.id.textview_account_name);
            txtAccName.setText(account.getName());

            String balanceColor = account.getBalance() < 0 ? "red" : "\"#1E8F1E\"";

            TextView txtBalanceText = (TextView)rootView.findViewById(R.id.textview_balances_text);
            txtBalanceText.setText("Balance: \nAvailable: \nOverdraft: ");

            TextView txtBalanceCost = (TextView)rootView.findViewById(R.id.textview_balances_cost);
            txtBalanceCost.setText("£" + account.getBalance() + "\n£" + (account.getOverdraftLimit() + account.getBalance()) + "\n£" + account.getOverdraftLimit());





            progressLoadTransactions = new ProgressDialog(getActivity());
            progressLoadTransactions.setTitle("Loading Transactions");
            progressLoadTransactions.setMessage("Please wait...");
            progressLoadTransactions.show();



            refreshMonths();



            /* Set up the buttons */
            Button btnMakeTransfer = (Button) rootView.findViewById(R.id.btn_make_transfer);
            Button btnSelectMonth = (Button)rootView.findViewById(R.id.btn_select_month);

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


            return rootView;
        }

        public void btnMakeTransfer(View v) {

            // MAKE A TRANSFER HAS BEEN CLICKED
        }

        public void btnSelectMonth(View v) {

            String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select a month")
                    .setItems(months, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                            month = which;
                            refreshMonths();
                        }
                    });
            Dialog dialog = builder.create();
            dialog.show();

        }

        public void refreshMonths() {
            ServerInterface transactionLoader = new DummyServerConnecter();
            transactionLoader.loadTransactions(account, month, "correctToken", (TransactionDelegate)getActivity());

        }

    }
}
