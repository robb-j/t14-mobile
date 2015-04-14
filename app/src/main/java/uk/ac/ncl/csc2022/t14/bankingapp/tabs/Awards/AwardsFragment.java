package uk.ac.ncl.csc2022.t14.bankingapp.tabs.Awards;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.MainActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.RewardsActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.SpinActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.AwardsAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.Utility;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.PointsListAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.models.PointGain;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Reward;
import uk.ac.ncl.csc2022.t14.bankingapp.models.RewardTaken;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;
import uk.ac.ncl.csc2022.t14.bankingapp.tabs.budgeting.BudgetingFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AwardsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AwardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AwardsFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    AwardsAdapter adapter;




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment AwardsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AwardsFragment newInstance() {
        AwardsFragment fragment = new AwardsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AwardsFragment() {
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
    User currentUser = DataStore.sharedInstance().getCurrentUser();
    List<Reward> recentRewards;
    List<PointGain> recentPoints;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_awards, container, false);
        //inform the user of how many points they have
        TextView tV = (TextView)getActivity().findViewById(R.id.text_current_points);
        try {
            tV.setText(Integer.toString(currentUser.getPoints()) + " points");
        }
        catch(java.lang.NullPointerException e)
        {

        }
        //get all recent rewards and points from the user
        recentRewards = DataStore.sharedInstance().getRewards();
        recentPoints = currentUser.getRecentPoints();
        refreshawards(rootView);
        refreshpoints(rootView);
        //button which takes the user to the rewards page
        Button claimRewards = (Button)rootView.findViewById(R.id.btn_goto_rewards);
        claimRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getActivity(), RewardsActivity.class);
                startActivity(i);
            }
        });

        // Set up the spin button to take the user to the spinner view
        Button btnGoToSpin = (Button) rootView.findViewById(R.id.btn_goto_spin);

        btnGoToSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSpin(v);
            }
        });
        return rootView;

    }

    public void goToSpin(View v) {
        //Start a spin activity
        Intent i = new Intent(getActivity(), SpinActivity.class);
        startActivity(i);
    }

    public void refreshawards(View v)
    {
        //set up the awards list
        ListView listAwards = (ListView)v.findViewById(R.id.awards_list);
        AwardsAdapter aA = new AwardsAdapter(this.getActivity(), recentRewards);
        Log.d("Hello", Boolean.toString((listAwards != null)));
        listAwards.setAdapter(aA);


    }
    public void refreshpoints(View v)
    {
        //set up the recent points list
        ListView listPoints = (ListView)v.findViewById(R.id.points_list);
        PointsListAdapter pLA = new PointsListAdapter(this.getActivity(), recentPoints);
        listPoints.setAdapter(pLA);
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
