package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.ExpandableListAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Categorisation;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;
import uk.ac.ncl.csc2022.t14.bankingapp.server.DummyServerConnector;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.CategoriseDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.NewPaymentsDelegate;

public class CategorizeActivity extends ActionBarActivity {
    User currentUser;


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
        List<category> categories = new ArrayList<category>();
        List<String> categoryNameList = new ArrayList<String>();
        View currentView;

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
                public boolean onGroupClick(ExpandableListView parent, final View Gv, int groupPosition, long id)
                {
                    currentView = Gv;
                    return false;
                }
            });

            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
            {

                //for each open group start a child listener for their children
                @Override
                public boolean onChildClick(ExpandableListView parent, View Cv, int groupPosition, int childPosition, long id)
                {

                    //The childPosition is the category which was selected, and the groupPosition is the transaction
                    //set the selected category as the transactions category
                    newTransactionList.get(groupPosition).setCategory(categories.get(childPosition));
                    newTransactionList.get(groupPosition).categorize();
                    Log.d("Saved: ", listDataHeader.get(groupPosition));
                    //change the text to notify the user that their category selection has been noted
                    //This bit can fuck up but the correct category is saved
                    //For some reason, the bottom two category captions switch with each other when you open the top group

                    TextView tV = (TextView)currentView.findViewById(R.id.transaction_category);
                    tV.setText(categoryNameList.get(childPosition));
                    expListView.collapseGroup(groupPosition);
                    return false;
                }
            }
            );

            //collapse all other groups when you open another
            expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition)
                {
                    for(int i = 0; i<newTransactionList.size();i++)
                    {
                        if(i!=groupPosition)
                        {
                            expListView.collapseGroup(i);
                        }
                    }
                }
            });
            Button confirmButton = (Button)this.getActivity().findViewById(R.id.confirm_categories);
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                   List<Categorisation> sortedTransactions = new ArrayList<Categorisation>();
                    Log.d("Working", "yay");
                    //Loop goes through all the transactions to check if they're all categorized and to attach a budget category to each one
                    for(int i=0; i<newTransactionList.size();i++)
                    {

                        //Is it categorized?
                        if(newTransactionList.get(i).isCategorized)
                        {
                            //What is the id?
                            Categorisation cat = new Categorisation(i);
                            //the set functions in categorisation were private, I've changed them
                            cat.setTransaction(newTransactionList.get(i).getTransaction());
                            //use the coordinates saved when the category object was created to find the correct budget category
                            cat.setBudgetCategory(currentUser.getAllGroups().get(newTransactionList.get(i).getCategory().getGroupCoordinate()).getCategories().get(newTransactionList.get(i).getCategory().getCategoryCoordinate()));
                            sortedTransactions.add(cat);

                        }
                        else
                        {
                            //if there are any uncategorized transactions, the user cannot confirm
                            return;
                            //throw an error message
                        }

                    }
                    CategoriseDelegate cD = new CategoriseDelegate() {
                        @Override
                        public void categorisationPassed(boolean hasNewSpin)
                        {
                            if(hasNewSpin)
                            {
                                //currentUser.setNumberOfSpins(currentUser.getNumberOfSpins()+1);
                                //:(
                            }

                            //All the transactions are categorized, go back to main activity
                            Intent i = new Intent(getActivity(), MainActivity.class);
                            startActivity(i);
                        }

                        @Override
                        public void categorisationFailed(String errMessage)
                        {
                            Log.d("Something went wrong...", "");

                        }
                    };
                    dSC.categorisePayments(sortedTransactions, cD);


                }
            });

            return rootView;
        }

        public class newTransaction
        {
            private Transaction transaction;
            private category category;
            private boolean isCategorized;
            public void setTransaction(Transaction t)
            {
                transaction = t;
            }

            public Transaction getTransaction()
            {
                return transaction;
            }
            public category getCategory()
            {
                return category;
            }
            public void setCategory(category cat)
            {
                category = cat;
            }
            public Boolean getiscategorized(){return isCategorized;}
            public void categorize(){isCategorized=true;}
        }

        public class category
        {
            private int groupCoordinate;
            private int categoryCoordinate;
            private String category;

            public void setGroupCoordinate(int coordinate){groupCoordinate=coordinate;}
            public void setCategoryCoordinate(int coordinate){categoryCoordinate=coordinate;}
            public void setCategory(String cat){category=cat;}

            public int getGroupCoordinate(){return groupCoordinate;}
            public int getCategoryCoordinate(){return categoryCoordinate;}
            public String getCategory(){return category;}
        }
        private void getListData()
        {
            newTransactionList = new ArrayList<PlaceholderFragment.newTransaction>();
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
            for(int i=0;i<currentUser.getAllGroups().size();i++)
            {
                for(int j=0;j<currentUser.getAllGroups().get(i).getCategories().size();j++)
                {
                    category cat = new category();
                    cat.setCategory(currentUser.getAllGroups().get(i).getCategories().get(j).getName());
                    cat.setCategoryCoordinate(j);
                    cat.setGroupCoordinate(i);
                    categories.add(cat);
                    categoryNameList.add(currentUser.getAllGroups().get(i).getCategories().get(j).getName());
                }
            }



            //Will use a for loop when I get proper transactions
            for(int i=0; i<newTransactionList.size(); i++)
            {
                listDataChild.put(newTransactionList.get(i).getTransaction().getPayee(), categoryNameList);
                listDataHeader.add(newTransactionList.get(i).getTransaction().getPayee());
            }


        }




    }


}
