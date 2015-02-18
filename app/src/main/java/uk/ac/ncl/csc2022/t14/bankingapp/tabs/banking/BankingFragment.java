package uk.ac.ncl.csc2022.t14.bankingapp.tabs.banking;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

        TextView accountsTitle = new TextView(this.getActivity());
        accountsTitle.setId(R.id.title_my_accounts);
        accountsTitle.setText("My Accounts");
        accountsTitle.setTextColor(Color.GRAY);
        accountsTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        accountsTitle.setPaintFlags(accountsTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        accountsTitle.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(accountsTitle);



        for (Account account : mUser.getAccounts()) {
            LinearLayout horizontal = new LinearLayout(this.getActivity());
            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (30*scale + 0.5f);
            horizontal.setPadding(0, dpAsPixels, 0, 0);
            LinearLayout vertical = new LinearLayout(this.getActivity());
            vertical.setOrientation(LinearLayout.VERTICAL);

            ImageView accountImage = new ImageView(this.getActivity());
            TextView accountName = new TextView(this.getActivity());
            TextView balance = new TextView(this.getActivity());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
            accountImage.setLayoutParams(layoutParams);
            accountImage.setBackgroundResource(R.drawable.account_image);

            accountName.setText(account.getName());
            balance.setText("£" + Double.toString(account.getBalance()));

            accountName.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            balance.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));


            horizontal.addView(accountImage);
            vertical.addView(accountName);
            vertical.addView(balance);
            horizontal.addView(vertical);
            linearLayout.addView(horizontal);
        }

    }
}
