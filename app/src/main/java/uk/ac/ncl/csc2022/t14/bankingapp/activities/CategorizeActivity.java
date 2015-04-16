package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.LloydsActionBarActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.ExpandableListAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Categorisation;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;
import uk.ac.ncl.csc2022.t14.bankingapp.server.DummyServerConnector;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.CategoriseDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.CategorizeLocationDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.NewPaymentsDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;

public class CategorizeActivity extends LloydsActionBarActivity {
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
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categorize, menu);
        return true;
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

        List<Boolean> listTransactionLocated = new ArrayList<Boolean>();

        User currentUser = DataStore.sharedInstance().getCurrentUser();
        ServerInterface dSC = DataStore.sharedInstance().getConnector();



        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_categorize, container, false);
            //get the list view


            getListData();
            //Communication between the adapter and the fragment through this delegate
            CategorizeLocationDelegate cLD = new CategorizeLocationDelegate() {
                @Override
                public void openMap(int groupNumber) {
                    //go to the map screen, making a note of the transaction selected(groupNumber)

                    Intent i = new Intent(getActivity(), AddTransactionLocationActivity.class);
                    startActivityForResult(i, groupNumber);

                }

                @Override
                public void locationConfirmed(int groupNumber, View view) {
                }
            };


            eListAdapter = new ExpandableListAdapter(this.getActivity(), listDataHeader, listDataChild, cLD, listTransactionLocated);
            getActivity().setContentView(R.layout.fragment_categorize);
            expListView = (ExpandableListView) getActivity().findViewById(R.id.list);


            expListView.setAdapter(eListAdapter);
            //Set a listener for each group opening
            expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, final View Gv, int groupPosition, long id)
                {
                    //make a note of the view which has been opened
                    currentView = Gv;
                    //Clear the category added to that transaction
                    listDataHeader.set(groupPosition, newTransactionList.get(groupPosition).getTransaction().getPayee());
                    newTransactionList.get(groupPosition).uncategorize();

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

                    //change the text to notify the user that their category selection has been noted
                    listDataHeader.set(groupPosition, newTransactionList.get(groupPosition).getTransaction().getPayee() + " - " + categoryNameList.get(childPosition));
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
                            //collapse all other groups if you open one
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


                    //Loop goes through all the transactions to check if they're all categorized and to attach a budget category to each one
                    for(int i=0; i<newTransactionList.size();i++)
                    {

                        //Is it categorized?
                        if(newTransactionList.get(i).isCategorized)
                        {
                            //add all the categorized transactions into a list to be sent to the server
                            //if the user has chosen not to categorize
                            if(newTransactionList.get(i).getCategory().getDontCat())
                            {
                                Categorisation cat = new Categorisation(i);
                                cat.setTransaction(newTransactionList.get(i).getTransaction());
                                //pass a null budget category to indicate that the user does not want this transaction categorized
                                BudgetCategory bC = null;
                                cat.setBudgetCategory(bC);
                                sortedTransactions.add(cat);

                            }
                            //if the user did not set a location
                            else if(newTransactionList.get(i).getLatitude() == 0.0 && newTransactionList.get(i).getLongitude() == 0)
                            {
                                Categorisation cat = new Categorisation(i);
                                //the set functions in categorisation were private, I've changed them
                                cat.setTransaction(newTransactionList.get(i).getTransaction());
                                //use the coordinates saved when the category object was created to find the correct budget category
                                cat.setBudgetCategory(currentUser.getAllGroups().get(newTransactionList.get(i).getCategory().getGroupCoordinate()).getCategories().get(newTransactionList.get(i).getCategory().getCategoryCoordinate()));

                                sortedTransactions.add(cat);

                            }
                            else {
                                //if a location has been set, use the alternatative constructer
                                Categorisation cat = new Categorisation(i, newTransactionList.get(i).getLatitude(), newTransactionList.get(i).getLongitude());
                                //the set functions in categorisation were private, I've changed them
                                cat.setTransaction(newTransactionList.get(i).getTransaction());
                                //use the coordinates saved when the category object was created to find the correct budget category
                                cat.setBudgetCategory(currentUser.getAllGroups().get(newTransactionList.get(i).getCategory().getGroupCoordinate()).getCategories().get(newTransactionList.get(i).getCategory().getCategoryCoordinate()));

                                sortedTransactions.add(cat);

                            }

                        }
                        else
                        {
                            //if there are any uncategorized transactions, the user cannot confirm
                            return;

                        }

                    }
                    CategoriseDelegate cD = new CategoriseDelegate() {
                        @Override
                        public void categorisationPassed(boolean hasNewSpin)
                        {
                            if(hasNewSpin)
                            {

                            }

                            //All the transactions are categorized, go back to main activity

                            getActivity().finish();

                        }

                        @Override
                        public void categorisationFailed(String errMessage)
                        {
                            Log.d("Failing", errMessage);
                            Toast.makeText(getActivity(), errMessage, Toast.LENGTH_SHORT).show();
                        }
                    };
                    //send the payments to the server
                    dSC.categorisePayments(sortedTransactions, cD);
                }
            });

            return rootView;
        }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data)
        {
            try {
                //record the position the user selected for their transaction
                newTransactionList.get(requestCode).setLongitude(data.getExtras().getDouble("Lng"));
                newTransactionList.get(requestCode).setLatitude(data.getExtras().getDouble("Lat"));
                listTransactionLocated.set(requestCode, true);
                //update the adapter to show the globe as white
                eListAdapter.notifyDataSetChanged();
            }
            catch(NullPointerException e)
            {

            }
        }

        public class newTransaction
        {
            //Transaction class which holds the transaction, the category, whether or not it has been categorized and the location if the user chooses to set one.
            private Transaction transaction;
            private category category;
            private boolean isCategorized;
            private double latitude;
            private double longitude;



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
            public void uncategorize(){isCategorized=false;}
            public void setLatitude(double lat){latitude=lat;}
            public double getLatitude(){return latitude;}
            public void setLongitude(double lng){longitude=lng;}
            public double getLongitude(){return longitude;}


        }

        public class category
        {
            //Class which holds the category name for the list, and the coordinates of said category so the budgetcategory object can be easily found
            private int groupCoordinate;
            private int categoryCoordinate;
            private String category;
            private boolean dontCat=false;


            public void setGroupCoordinate(int coordinate){groupCoordinate=coordinate;}
            public void setCategoryCoordinate(int coordinate){categoryCoordinate=coordinate;}
            public void setCategory(String cat){category=cat;}
            public void setDontCat(){dontCat=true;}

            public int getGroupCoordinate(){return groupCoordinate;}
            public int getCategoryCoordinate(){return categoryCoordinate;}
            public String getCategory(){return category;}
            public boolean getDontCat(){return dontCat;}


        }
        private void getListData()
        {
            //The list of uncategorized transactions
            newTransactionList = new ArrayList<PlaceholderFragment.newTransaction>();
            //The budget categories for the expandable list
            listDataChild = new HashMap<String, List<String>>();
            //The uncategorized transaction payees for the expandable list
            listDataHeader = new ArrayList<String>();

            //Sorting the payments when they come from the DummyServerConnector
            NewPaymentsDelegate nPD = new NewPaymentsDelegate() {
                @Override
                public void newPaymentsLoaded(List<Transaction> transactions)
                {
                    for(int k = 0; k<transactions.size();k++)
                    {
                        newTransaction nT = new newTransaction();
                        nT.setTransaction(transactions.get(k));
                        //add all the transsactions from the server into a list
                        newTransactionList.add(nT);
                    }
                    //adding the possible categories
                    for(int i=0;i<currentUser.getAllGroups().size();i++)
                    {
                        for(int j=0;j<currentUser.getAllGroups().get(i).getCategories().size();j++)
                        {
                            //Make a note of the coordinates within the datastore of each category found
                            //makes it easier when it comes to saving the categorizations
                            category cat = new category();
                            cat.setCategory(currentUser.getAllGroups().get(i).getCategories().get(j).getName());
                            cat.setCategoryCoordinate(j);
                            cat.setGroupCoordinate(i);
                            categories.add(cat);
                            categoryNameList.add(currentUser.getAllGroups().get(i).getCategories().get(j).getName());
                        }
                    }
                    category c = new category();
                    c.setCategory("Don't categorize");
                    c.setDontCat();
                    categories.add(c);
                    categoryNameList.add("Don't categorize");
                    //Will use a for loop when I get proper transactions
                    for(int i=0; i<newTransactionList.size(); i++)
                    {
                        //fill out the expandable list view
                        listDataChild.put(newTransactionList.get(i).getTransaction().getPayee(), categoryNameList);
                        listDataHeader.add(newTransactionList.get(i).getTransaction().getPayee());
                        Log.d("Hello", listDataHeader.toString());
                        listTransactionLocated.add(false);
                    }

                    try {
                        //notify the list that there is new data
                        eListAdapter.notifyDataSetChanged();
                    }
                    catch(NullPointerException e)
                    {

                    }

                }

                @Override
                public void newPaymentsLoadFailed(String errMessage)
                {

                }
            };
            //load the new transactions
            dSC.loadNewPaymentsForUser(nPD);




        }




    }


}
