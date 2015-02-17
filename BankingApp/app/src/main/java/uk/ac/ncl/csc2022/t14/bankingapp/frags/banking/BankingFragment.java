package uk.ac.ncl.csc2022.t14.bankingapp.frags.banking;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;

/**
 * A Fragment to show the user their accounts
 */
public class BankingFragment extends Fragment {

    private User mUser;
    private static final String USER_KEY = "currentUser";

    public static BankingFragment newInstance(User user) {

        Bundle args = new Bundle();
        // Pass vars to the fragment through this bundle


        // Create a new fragment
        BankingFragment frag = new BankingFragment();
        frag.mUser = user;
        frag.setArguments(args);
        return frag;
    }

    public BankingFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Retrieve parameters from newInstance() ...
        Bundle args = getArguments();


        // Create the view from the xml layout
        View rootView = inflater.inflate(R.layout.fragment_banking, container, false);


        // Setup the view here
        // Listen for taps
        // ...

        return rootView;
    }

}