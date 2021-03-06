package uk.ac.ncl.csc2022.t14.bankingapp.tabs.budgeting;

import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.Utility;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.CategorizeActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.EditBudgetActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.BudgetAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BudgetingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BudgetingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetingFragment extends Fragment{

    BudgetAdapter adapter;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BudgetingFragment.
     */
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
            // get arguments
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        refreshBudgets(getView());
        checkForNewTransactions(getView());
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
                Intent i = new Intent(getActivity(), EditBudgetActivity.class);
                startActivity(i);
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

    /**
     * Refresh the budget.
     * @param v
     */
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

    /**
     * Display the amount of new transactions to be categorised.
     * @param v
     */
    public void checkForNewTransactions(View v) {

        LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout_new_transactions);
        TextView textUnspent = (TextView) v.findViewById(R.id.text_budget_unspent);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)textUnspent.getLayoutParams();

        if (DataStore.sharedInstance().getCurrentUser().getNumNewPayments() > 0) {
            layout.setVisibility(View.VISIBLE);
            TextView textView = (TextView) v.findViewById(R.id.text_new_transactions);
            textView.setText(DataStore.sharedInstance().getCurrentUser().getNumNewPayments() + " New Transactions");


            layoutParams.addRule(RelativeLayout.BELOW, layout.getId());

        } else {
            layout.setVisibility(View.INVISIBLE);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.title_budgeting);
        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
