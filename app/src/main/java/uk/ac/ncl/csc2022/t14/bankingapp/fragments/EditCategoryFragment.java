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

public class EditCategoryFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    public List<BudgetCategory> tempCategories;

    private EditCategoriesAdapter adapter;

    private EditBudgetActivity activity;

    RecyclerView recyclerView;

    EditText groupName;

    private int groupPosition;

    public BudgetGroup group;

    private OnFragmentInteractionListener mListener;

    public static EditCategoryFragment newInstance(int position) {
        EditCategoryFragment fragment = new EditCategoryFragment();
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

        tempCategories = new ArrayList<BudgetCategory>();

        for (BudgetCategory category : group.getCategories()) {
            BudgetCategory tempCategory = new BudgetCategory(category.getId(), category.getName(), category.getBudgeted(), category.getSpent());
            tempCategories.add(tempCategory);
        }

        BudgetGroup tempGroup = new BudgetGroup(group.getId(), group.getName());
        tempGroup.getCategories().addAll(tempCategories);


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

        newCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addCategory();
            }
        });

        Button btnSaveChanges = (Button)rootView.findViewById(R.id.btn_save_group);

        groupName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EditText text = (EditText) v;
                    if (!group.getName().equals(text.getText().toString())) {
                        group.setName(text.getText().toString());
                        group.setMode(BudgetGroup.Mode.EDITED);
                    }


                }
            }
        });

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_save_group:

                        EditBudgetActivity activity = (EditBudgetActivity)getActivity();
                        activity.saveGroup();

                        break;
                }
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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
