package uk.ac.ncl.csc2022.t14.bankingapp.tabs.rewards;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.RewardsListActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.SpinActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.MyLinearLayoutManager;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.MyRewardsAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.RecentPointsAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.server.live.LiveServerConnector;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RewardsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RewardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RewardsFragment extends Fragment implements View.OnClickListener {

    private MyRewardsAdapter rewardsAdapter;
    private RecentPointsAdapter pointsAdapter;
    RecyclerView rewardsRecyclerView, pointsRecyclerView;

    private OnFragmentInteractionListener mListener;

    public static RewardsFragment newInstance() {
        RewardsFragment fragment = new RewardsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public RewardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ((TextView)getView().findViewById(R.id.text_current_points)).setText(DataStore.sharedInstance().getCurrentUser().getPoints() + " points");

        // setting up the adapters
        rewardsAdapter = new MyRewardsAdapter(getActivity(), DataStore.sharedInstance().getCurrentUser().getRecentRewards());
        pointsAdapter = new RecentPointsAdapter(getActivity(), DataStore.sharedInstance().getCurrentUser().getRecentPoints());

        rewardsRecyclerView.setAdapter(rewardsAdapter);
        pointsRecyclerView.setAdapter(pointsAdapter);

        rewardsRecyclerView.setLayoutManager(new MyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        pointsRecyclerView.setLayoutManager(new MyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        final float scale = getActivity().getResources().getDisplayMetrics().density;

        // roughly scales the rewards and recent points to the correct size.
        rewardsRecyclerView.getLayoutParams().height = (int)((45 * scale + 0.5f) * rewardsAdapter.getItemCount());
        pointsRecyclerView.getLayoutParams().height = (int)((45 * scale + 0.5f) * pointsAdapter.getItemCount());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_rewards, container, false);

        rewardsRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_my_rewards);
        pointsRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_recent_points);

        Button btnClaim = (Button) rootView.findViewById(R.id.btn_claim_reward);
        Button btnSpin = (Button) rootView.findViewById(R.id.btn_spin);

        btnClaim.setOnClickListener(this);
        btnSpin.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_claim_reward:
                intent = new Intent(getActivity(), RewardsListActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_spin:
                intent = new Intent(getActivity(), SpinActivity.class);
                startActivity(intent);
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
