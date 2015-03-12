package uk.ac.ncl.csc2022.t14.bankingapp.tabs.banking;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.Utility;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.AccountActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.ProductActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;
import uk.ac.ncl.csc2022.t14.bankingapp.models.User;

/**
 * A Fragment to show the user their accounts
 */
public class BankingFragment extends Fragment {

    private static final String USER_KEY = "currentUser";


    public static BankingFragment newInstance() {

        Bundle args = new Bundle();
        // Pass vars to the fragment through this bundle

        // Create a new fragment
        BankingFragment frag = new BankingFragment();
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

        // The layout of the whole banking tab
        LinearLayout linearLayout = (LinearLayout)rootView.findViewById(R.id.bankingLayout);

        /* Accounts title */
        TextView accountsTitle = new TextView(this.getActivity());
        accountsTitle.setText("My Accounts");
        accountsTitle.setTextColor(Color.GRAY);
        accountsTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        accountsTitle.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(accountsTitle);

        // Divider
        linearLayout.addView(Utility.divider(getActivity()));


        /* Display a list of accounts */
        User user = DataStore.sharedInstance().getCurrentUser();
        for (final Account account : user.getAccounts()) {

            // Each account as a whole (image, acc name, and balance)
            LinearLayout accountLayout = new LinearLayout(this.getActivity());

            // Makes each account lead to their account page.
            accountLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Create a new account page
                    Intent i = new Intent(getActivity(), AccountActivity.class);

                    // pass through the relevant account
                    i.putExtra("accountId", account.getId());
                    startActivity(i);
                }
            });

            // scale to size of device.
            float scale = getResources().getDisplayMetrics().density;

            // make it look nicer for all device resolutions
            int paddingInDp = (int) (20 * scale + 0.5f);
            accountLayout.setPadding(0, paddingInDp, 0, paddingInDp);

            // layout that stores account name and balance
            LinearLayout accountTextLayout = new LinearLayout(this.getActivity());
            accountTextLayout.setOrientation(LinearLayout.VERTICAL);

            ImageView accountImage = new ImageView(this.getActivity());
            TextView accountName = new TextView(this.getActivity());
            TextView balance = new TextView(this.getActivity());

            // Setting image properties
            int imgLength = 64;
            int imgLengthAsDp = (int) (imgLength * scale + 0.5f);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imgLengthAsDp, imgLengthAsDp);
            int imgMargin = (int) (20 * scale + 0.5f);
            layoutParams.setMargins(imgMargin, 0, imgMargin, 0);
            accountImage.setLayoutParams(layoutParams);
            accountImage.setBackgroundResource(R.drawable.account_image);

            // Account name properties
            accountName.setText(account.getName());
            accountName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
            accountName.setTypeface(null, Typeface.BOLD);
            accountName.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            // Balance properties
            balance.setText("£" + Double.toString(account.getBalance()));
            balance.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);

            // Change the color depending on positive or negative balance.
            if (account.getBalance() < 0) {
                balance.setTextColor(Color.RED);
            } else {
                balance.setTextColor(Color.rgb(30, 143, 30));   // GREEN
            }
            balance.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));


            // Adding the images and texts to their layouts.
            accountLayout.addView(accountImage);
            accountTextLayout.addView(accountName);
            accountTextLayout.addView(balance);

            accountLayout.addView(accountTextLayout);
            linearLayout.addView(accountLayout);
        }



        /* TEST === List of all available products */
        List<Product> allProducts = DataStore.sharedInstance().getProducts();

        // Remove products that the user already has
        Iterator<Product> i = allProducts.iterator();
        while (i.hasNext()) {
            Product tempProduct = i.next();
            for (Account account : user.getAccounts()) {
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
        linearLayout.addView(Utility.divider(getActivity()));

        /* Display Products */
        for (final Product product : allProducts) {

            LinearLayout productLayout = new LinearLayout(this.getActivity());

            // Makes each account lead to their account page.
            productLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), ProductActivity.class);

                    // pass through the relevant product
                    i.putExtra("product", product);
                    startActivity(i);
                }
            });

            // Scale the padding to look better on different resolutions.
            float scale = getResources().getDisplayMetrics().density;
            int dpAsPixels = (int) (20 * scale + 0.5f);
            productLayout.setPadding(0, dpAsPixels, 0, dpAsPixels);     // set the padding

            // Text section of a product
            LinearLayout textSection = new LinearLayout(this.getActivity());
            textSection.setOrientation(LinearLayout.VERTICAL);


            ImageView productImage = new ImageView(this.getActivity());
            TextView productName = new TextView(this.getActivity());
            TextView productDesc = new TextView(this.getActivity());

            // Image properties
            int imgLength = 64;
            int imgLengthAsDp = (int) (imgLength * scale + 0.5f);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imgLengthAsDp, imgLengthAsDp);
            int imgMargin = (int) (20 * scale + 0.5f);
            layoutParams.setMargins(imgMargin, 0, imgMargin, 0);
            productImage.setLayoutParams(layoutParams);
            productImage.setBackgroundResource(R.drawable.account_image);


            // Product name properties
            productName.setText(product.getTitle());
            productName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
            productName.setTypeface(null, Typeface.BOLD);
            productName.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            // Cut off text after 2 lines with an ellipsis. Prevents filling the screen with text.
            productName.setEllipsize(TextUtils.TruncateAt.END);
            productName.setLines(2);

            // Product description properties
            productDesc.setText(product.getContent());
            productDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            productDesc.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            // cut off text after 2 lines with an ellipsis
            productDesc.setEllipsize(TextUtils.TruncateAt.END);
            productDesc.setLines(2);


            productLayout.addView(productImage);
            textSection.addView(productName);
            textSection.addView(productDesc);
            productLayout.addView(textSection);
            linearLayout.addView(productLayout);
        }


        return rootView;
    }

}
