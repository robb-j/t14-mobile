package uk.ac.ncl.csc2022.t14.bankingapp.activities;

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

import java.util.ArrayList;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.TransactionAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;

public class AccountActivity extends ActionBarActivity {

    private static Account account;

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

            // Set the balance, available, and overdraft values
            TextView txtAccBalance = (TextView)rootView.findViewById(R.id.textview_money_details);
            String balanceColor = account.getBalance() < 0 ? "red" : "\"#1E8F1E\"";

            txtAccBalance.setText(Html.fromHtml("Balance: " + "<font color=" + balanceColor + ">" + "£" + account.getBalance() + "</font><br>" +
                    "Available: " + "<font color=\"#1E8F1E\">" + "£" + (account.getOverdraftLimit() + account.getBalance()) + "</font><br>" +
                    "Overdraft: " + "<font color=\"#1E8F1E\">" + "£" + account.getOverdraftLimit() + "</font>"));



            // Construct the data source
            ArrayList<Transaction> arrayOfTransactions = new ArrayList<Transaction>();
            // Create the adapter to convert the array to views
            TransactionAdapter adapter = new TransactionAdapter(getActivity(), arrayOfTransactions);
            // Attach the adapter to a ListView
            ListView listTransactions = (ListView)rootView.findViewById(R.id.list_transactions);
            listTransactions.setAdapter(adapter);

            Transaction transaction1 = new Transaction(1, 3.51, account, "Greggs");
            Transaction transaction2 = new Transaction(1, 8.65, account, "Burger King");
            Transaction transaction3 = new Transaction(1, 33.70, account, "KFC");
            Transaction transaction4 = new Transaction(1, 62.13, account, "Maccies");
            Transaction transaction5= new Transaction(1, 2.75, account, "Guitar");
            Transaction transaction6= new Transaction(1, 1.24, account, "Eat4Less");
            Transaction transaction7= new Transaction(1, 4.30, account, "Rubber");
            Transaction transaction8= new Transaction(1, 6.22, account, "Compass");
            Transaction transaction9= new Transaction(1, 7.11, account, "Protractor");
            Transaction transaction10 = new Transaction(1, 2.53, account, "Rulers");
            Transaction transaction11 = new Transaction(1, 2.66, account, "Pens");
            Transaction transaction12 = new Transaction(1, 0.74, account, "Pencils");
            Transaction transaction13 = new Transaction(1, 1.67, account, "Textbooks");
            Transaction transaction14 = new Transaction(1, 19.35, account, "Uni");
            Transaction transaction15 = new Transaction(1, 3.64, account, "Metro");
            Transaction transaction16 = new Transaction(1, 17.33, account, "Maplin");
            Transaction transaction17 = new Transaction(1, 22.10, account, "Primark");


            adapter.addAll(transaction1, transaction2, transaction3, transaction4, transaction5, transaction6, transaction7, transaction8, transaction9,
            transaction10, transaction11, transaction12, transaction13, transaction14, transaction15, transaction16,transaction17);



            // Setting up the "make a transfer" button. See btnMakeTransfer(View v)
                    Button btn = (Button) rootView.findViewById(R.id.btn_make_transfer);

            // Calls btnMakeTransfer(View v) when clicked.
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnMakeTransfer(v);
                }
            });

            return rootView;
        }

        public void btnMakeTransfer(View v) {

            // MAKE A TRANSFER HAS BEEN CLICKED
        }
    }
}
