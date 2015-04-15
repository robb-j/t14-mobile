package uk.ac.ncl.csc2022.t14.bankingapp.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import javax.xml.transform.Templates;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.EditBudgetActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.EditGroupsAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.RecyclerItemClickListener;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;
import uk.ac.ncl.csc2022.t14.bankingapp.server.DummyServerConnector;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.BudgetUpdateDelegate;
import uk.ac.ncl.csc2022.t14.bankingapp.server.interfaces.ServerInterface;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditGroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class EditGroupFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    EditGroupsAdapter adapter;

    EditBudgetActivity activity;

    ArrayList<BudgetGroup> tempGroups = new ArrayList<>();


    public EditGroupFragment() {
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
    public void onResume() {
        super.onResume();

        for (BudgetGroup group : DataStore.sharedInstance().getCurrentUser().getAllGroups()) {
            BudgetGroup tempGroup = new BudgetGroup(group.getId(), group.getName());
            for (BudgetCategory category : group.getCategories()) {
                tempGroup.getCategories().add(category);
            }
            tempGroups.add(tempGroup);
        }


        adapter = new EditGroupsAdapter(getActivity(), activity.getGroups());

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_edit_group, container, false);

        activity = (EditBudgetActivity)getActivity();

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_edit_groups);


        Button btnSaveGroups = (Button) rootView.findViewById(R.id.btn_save_groups);
        TextView newGroup = (TextView) rootView.findViewById(R.id.text_new_group);

        btnSaveGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBudget();
                getActivity().finish();
            }
        });

        newGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addGroup();
            }
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        loadEditCategories(position);
                    }
                })
        );

        return rootView;
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

    public void saveBudget() {

        ServerInterface budgetUpdater = DataStore.sharedInstance().getConnector();
        budgetUpdater.updateBudget(EditBudgetActivity.groups, (BudgetUpdateDelegate)getActivity());
    }

    public void loadEditCategories(int position) {
        EditBudgetActivity activity = (EditBudgetActivity)getActivity();
        activity.loadEditGroup(position);
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
