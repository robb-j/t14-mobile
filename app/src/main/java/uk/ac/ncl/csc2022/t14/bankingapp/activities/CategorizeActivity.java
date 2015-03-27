package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.ExpandableListAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;
import uk.ac.ncl.csc2022.t14.bankingapp.server.DummyServerConnector;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.NewPaymentsDelegate;

public class CategorizeActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorize);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }



        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categorize, menu);
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
        ExpandableListAdapter eListAdapter;
        ExpandableListView expListView;
        List<String> listDataHeader;
        List<newTransaction> newTransactionList;
        NewPaymentsDelegate nPD;
        HashMap<String, List<String>> listDataChild;
        List<String> categories = new ArrayList<String>();

        User currentUser = DataStore.sharedInstance().getCurrentUser();
        DummyServerConnector dSC = new DummyServerConnector();


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_categorize, container, false);
            //get the list view


            getListData();

            eListAdapter = new ExpandableListAdapter(this.getActivity(), listDataHeader, listDataChild);
            getActivity().setContentView(R.layout.fragment_categorize);
            expListView = (ExpandableListView) getActivity().findViewById(R.id.list);


            expListView.setAdapter(eListAdapter);
            //I really don't know how this worked but it did
            //Set a listener for each group opening
            expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, final View Gv, int groupPosition, long id) {
                    expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
                    {
                        //for each open group start a child listener for their children
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View Cv, int groupPosition, int childPosition, long id)
                        {

                            //The childPosition is the category which was selected, and the groupPosition is the transaction
                            //set the selected category as the transactions category
                            newTransactionList.get(groupPosition).setCategory(categories.get(childPosition));
                            //change the text to notify the user that their category selection has been noted
                            TextView tV = (TextView)Gv.findViewById(R.id.transaction_category);
                            tV.setText(categories.get(childPosition));

                            return false;
                        }
                    }
                    );
                    return false;
                }
            });







            return rootView;
        }
        public class newTransaction
        {
            private Transaction transaction;
            private String category;
            public void setTransaction(Transaction t)
            {
                transaction = t;
            }

            public Transaction getTransaction()
            {
                return transaction;
            }
            public String getCategory()
            {
                return category;
            }
            public void setCategory(String cat)
            {
                category = cat;
            }
        }
        private void getListData()
        {
            newTransactionList = new ArrayList<newTransaction>();
            listDataChild = new HashMap<String, List<String>>();
            listDataHeader = new ArrayList<String>();

            //Sorting the payments when they come from the DummyServerConnector
            NewPaymentsDelegate nPD = new NewPaymentsDelegate() {
                @Override
                public void newPaymentsLoaded(List<Transaction> transactions)
                {
                    for(int i = 0; i<transactions.size();i++)
                    {
                        newTransaction nT = new newTransaction();
                        nT.setTransaction(transactions.get(i));
                        newTransactionList.add(nT);
                    }
                }

                @Override
                public void newPaymentsLoadFailed(String errMessage)
                {
                    //I guess just go back to the budget view?
                }
            };

            dSC.loadNewPaymentsForUser(nPD);

            //adding the possible categories, might add an option to add your own

            categories.add("Bills");
            categories.add("Food & Drink");
            categories.add("Going out");

            //Will use a for loop when I get proper transactions
            for(int i=0; i<newTransactionList.size(); i++)
            {
                listDataChild.put(newTransactionList.get(i).getTransaction().getPayee(), categories);
                listDataHeader.add(newTransactionList.get(i).getTransaction().getPayee());
            }


        }


    }


}
