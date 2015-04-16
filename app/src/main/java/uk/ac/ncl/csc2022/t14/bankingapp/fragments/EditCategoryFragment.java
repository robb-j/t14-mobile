package uk.ac.ncl.csc2022.t14.bankingapp.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.EditBudgetActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.EditCategoriesAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.BudgetUpdateDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;

/**
 * Used to edit a budget group by the user
 */
public class EditCategoryFragment extends Fragment {

    private static final String ARG_POSITION = "position";  // the group's index in the user's budget groups
    public List<BudgetCategory> tempCategories;     // copy of the categories in the group
    private EditCategoriesAdapter adapter;      // adapter for the recycler view
    private EditBudgetActivity activity;        // the current activity
    RecyclerView recyclerView;      // used to display the categories
    EditText groupName;     // name of the group
    private int groupPosition;
    public BudgetGroup group;       // the group to edit

    private OnFragmentInteractionListener mListener;

    public static EditCategoryFragment newInstance(int position) {
        EditCategoryFragment fragment = new EditCategoryFragment();
        // retrieve variables passed through
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public EditCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupPosition = getArguments().getInt(ARG_POSITION);
        }
        activity = (EditBudgetActivity)getActivity();
        group = activity.getGroups().get(groupPosition);
    }

    @Override
    public void onResume() {
        super.onResume();


        groupName.setText(group.getName());

        // set up the adapter
        adapter = new EditCategoriesAdapter(getActivity(), group);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_category, container, false);

        activity = (EditBudgetActivity)getActivity();
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_edit_categories);

        groupName = (EditText) rootView.findViewById(R.id.edittext_group_name);
        TextView newCategory = (TextView) rootView.findViewById(R.id.text_new_category);
        Button btnSaveChanges = (Button)rootView.findViewById(R.id.btn_save_group);

        newCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addCategory();
            }
        });

        groupName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                /* error checking what the user types */
                EditText text = (EditText)v;
                if (text.getText().length() > 0) {
                    group.setName(text.getText().toString());
                    groupName.setText(text.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "Please enter some text", Toast.LENGTH_SHORT).show();
                    groupName.setText(group.getName());
                }
            }
        });


        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_save_group:

                        // add the deleted categories back to the group ready to be returned to the server if saved.
                        adapter.addDeletedToGroup();

                        // return to the view groups fragment
                        EditBudgetActivity activity = (EditBudgetActivity)getActivity();
                        activity.saveGroup();

                        break;
                }
            }
        });

        return rootView;
    }

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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
