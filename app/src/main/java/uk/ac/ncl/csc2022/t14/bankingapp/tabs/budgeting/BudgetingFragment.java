package uk.ac.ncl.csc2022.t14.bankingapp.tabs.budgeting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.Utility;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.CategorizeActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.TransferActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.AwardsAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.BudgetAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.TransactionAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Transaction;
import uk.ac.ncl.csc2022.t14.bankingapp.server.DummyServerConnector;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.BudgetUpdateDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.TransactionDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.tabs.Awards.AwardsFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BudgetingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BudgetingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetingFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    BudgetAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BudgetingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BudgetingFragment newInstance() {
        BudgetingFragment fragment = new BudgetingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BudgetingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_budgeting, container, false);

        checkForNewTransactions(rootView);

        refreshBudgets(rootView);

        // Setting up buttons
        TextView btnMakeTransfer = (TextView) rootView.findViewById(R.id.text_new_transactions);
        Button btnEditBudgets = (Button)rootView.findViewById(R.id.btn_edit_budgets);

        btnMakeTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Categorize activity.
                Intent i = new Intent(getActivity(), CategorizeActivity.class);
                startActivity(i);
            }
        });

        btnEditBudgets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open budget edit activity
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void refreshBudgets(View v) {

        /* Budget unspent */
        TextView textView = (TextView) v.findViewById(R.id.text_budget_unspent);
        textView.setText("Unspent: " + Utility.doubleToCurrency(getBudgetUnspent()));

        /* List of budget groups and categories. */
        ListView listBudgets = (ListView)v.findViewById(R.id.list_budgets);

        adapter = new BudgetAdapter(getActivity());

        for (BudgetGroup group : DataStore.sharedInstance().getCurrentUser().getAllGroups()) {
            adapter.addSectionHeaderItem(group);
            for (BudgetCategory category : group.getCategories()) {
                adapter.addItem(category);
            }
        }

        listBudgets.setAdapter(adapter);
    }

    public void checkForNewTransactions(View v) {

        LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout_new_transactions);

        if (numNewTransactions() > 0) {
            layout.setVisibility(View.VISIBLE);
            TextView textView = (TextView) v.findViewById(R.id.text_new_transactions);
            textView.setText(numNewTransactions() + " New Transactions");
        } else {
            layout.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Returns the number of new transactions that have not yet been categorised.
     * @return Number of new transactions.
     */
    public int numNewTransactions() {
        /* This needs implementing */
        return 2;
    }

    /**
     * Returns the total unspent of the user's budget.
     * @return The total unspent of the user's budget.
     */
    public double getBudgetUnspent() {
        double total = 0;
        for (BudgetGroup group : DataStore.sharedInstance().getCurrentUser().getAllGroups()) {
            for (BudgetCategory category : group.getCategories()) {
                total += (category.getBudgeted() - category.getSpent());
            }
        }
        return total;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
