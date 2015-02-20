package uk.ac.ncl.csc2022.t14.bankingapp.tabs.banking;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.Divider;
import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.AccountActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.MainActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.ProductActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
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
        //test

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

        // main linear layout
        LinearLayout linearLayout = (LinearLayout)rootView.findViewById(R.id.bankingLayout);

        /* Accounts title */
        TextView accountsTitle = new TextView(this.getActivity());
        accountsTitle.setId(R.id.title_my_accounts);
        accountsTitle.setText("My Accounts");
        accountsTitle.setTextColor(Color.GRAY);
        accountsTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        accountsTitle.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(accountsTitle);

        // Divider
        linearLayout.addView(new Divider(getActivity()));


        /* Display Accounts */
        for (final Account account : mUser.getAccounts()) {

            LinearLayout accountLayout = new LinearLayout(this.getActivity());

            // Makes each account lead to their account page.
            accountLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), AccountActivity.class);

                    // pass through the relevant account
                    i.putExtra("account", (Parcelable) account);
                    startActivity(i);
                }
            });

            // scale to size of device.
            float scale = getResources().getDisplayMetrics().density;
            int paddingInDp = (int) (20 * scale + 0.5f);
            accountLayout.setPadding(0, paddingInDp, 0, paddingInDp);

            // layout that stores account name and balance
            LinearLayout accountTextLayout = new LinearLayout(this.getActivity());
            accountTextLayout.setOrientation(LinearLayout.VERTICAL);

            ImageView accountImage = new ImageView(this.getActivity());
            TextView accountName = new TextView(this.getActivity());
            TextView balance = new TextView(this.getActivity());

            int imgLength = 64;
            int imgLengthAsDp = (int) (imgLength * scale + 0.5f);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imgLengthAsDp, imgLengthAsDp);
            int imgMargin = (int) (20 * scale + 0.5f);
            layoutParams.setMargins(imgMargin, 0, imgMargin, 0);
            accountImage.setLayoutParams(layoutParams);
            accountImage.setBackgroundResource(R.drawable.account_image);

            accountName.setText(account.getName());
            accountName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
            accountName.setTypeface(null, Typeface.BOLD);
            balance.setText("£" + Double.toString(account.getBalance()));
            balance.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            if (account.getBalance() < 0) {
                balance.setTextColor(Color.RED);
            } else {
                balance.setTextColor(Color.rgb(30, 143, 30));
            }

            accountName.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            balance.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));


            accountLayout.addView(accountImage);
            accountTextLayout.addView(accountName);
            accountTextLayout.addView(balance);
            accountLayout.addView(accountTextLayout);
            linearLayout.addView(accountLayout);
        }



        /* TEST === List of all available products */
        List<Product> allProducts = new ArrayList<Product>();
        allProducts.add(new Product(1, "New ISA offer", "lorem ipsum blah blah blah"));
        allProducts.add(new Product(2, "Amazing new offer", "This is a test offer that is going to be displayed on the banking page." +
                "This is a test offer that is going to be displayed on the banking page." +
                "This is a test offer that is going to be displayed on the banking page." +
                "This is a test offer that is going to be displayed on the banking page." +
                "This is a test offer that is going to be displayed on the banking page." +
                "This is a test offer that is going to be displayed on the banking page."));
        allProducts.add(new Product(3, "Student bank offer", "lorem ipsum blah blah blah"));
        allProducts.add(new Product(4, "Savings Increased Interest Offer", "If you have over £3000 in your bank account then you may be " +
                "eligible to increase your interest rate. Click here to find out more."));


        // Remove products that the user already has
        Iterator<Product> i = allProducts.iterator();
        while (i.hasNext()) {
            Product tempProduct = i.next();
            for (Account account : mUser.getAccounts()) {
                if (account.getProduct() != null) {
                    if (tempProduct.getId() == account.getProduct().getId()) {
                        i.remove();
                    }
                }

            }
        }

        /* Products title */
        TextView productsTitle = new TextView(this.getActivity());
        productsTitle.setText("Related Offers");
        productsTitle.setTextColor(Color.GRAY);
        productsTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        productsTitle.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(productsTitle);

        // Divider
        linearLayout.addView(new Divider(getActivity()));

        /* Display Products */
        for (final Product product : allProducts) {

            LinearLayout productLayout = new LinearLayout(this.getActivity());

            // Makes each account lead to their account page.
            productLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), ProductActivity.class);

                    // pass through the relevant product
                    i.putExtra("product", (Parcelable) product);
                    startActivity(i);
                }
            });

            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (20 * scale + 0.5f);
            productLayout.setPadding(0, dpAsPixels, 0, dpAsPixels);
            LinearLayout vertical = new LinearLayout(this.getActivity());
            vertical.setOrientation(LinearLayout.VERTICAL);

            ImageView productImage = new ImageView(this.getActivity());
            TextView productName = new TextView(this.getActivity());
            TextView productDesc = new TextView(this.getActivity());

            int imgLength = 64;
            int imgLengthAsDp = (int) (imgLength * scale + 0.5f);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imgLengthAsDp, imgLengthAsDp);
            int imgMargin = (int) (20 * scale + 0.5f);
            layoutParams.setMargins(imgMargin, 0, imgMargin, 0);
            productImage.setLayoutParams(layoutParams);
            productImage.setBackgroundResource(R.drawable.account_image);

            productName.setText(product.getTitle());
            productName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
            productName.setTypeface(null, Typeface.BOLD);
            productDesc.setText(product.getContent());

            // cut off text after 2 lines with an ellipsis
            productDesc.setEllipsize(TextUtils.TruncateAt.END);
            productDesc.setLines(2);


            productDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            productName.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            productDesc.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));


            productLayout.addView(productImage);
            vertical.addView(productName);
            vertical.addView(productDesc);
            productLayout.addView(vertical);
            linearLayout.addView(productLayout);
        }


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();



    }
}
