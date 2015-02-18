package uk.ac.ncl.csc2022.t14.bankingapp.tabs.banking;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
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

    @Override
    public void onStart() {
        super.onStart();

        LinearLayout linearLayout = (LinearLayout)getActivity().findViewById(R.id.bankingLayout);

        TextView textView = new TextView(this.getActivity());
        textView.setId(R.id.testTextView);
        textView.setText("oioi m80");
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(textView);

        for (Account account : mUser.getAccounts()) {

        }

    }
}
