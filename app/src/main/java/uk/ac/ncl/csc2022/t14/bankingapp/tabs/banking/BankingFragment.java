package uk.ac.ncl.csc2022.t14.bankingapp.tabs.banking;

import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.BankingAccountsAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.BankingProductsAdapter;
import uk.ac.ncl.csc2022.t14.bankingapp.listadapters.MyLinearLayoutManager;
import uk.ac.ncl.csc2022.t14.bankingapp.server.live.LiveServerConnector;

/**
 * A Fragment to show the user their accounts
 */
public class BankingFragment extends Fragment {

    private BankingAccountsAdapter accountsAdapter = null;
    private BankingProductsAdapter productsAdapter = null;
    RecyclerView accountRecyclerView, productRecyclerView;



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
    public void onResume() {
        super.onResume();

        accountsAdapter = new BankingAccountsAdapter(getActivity(), DataStore.sharedInstance().getCurrentUser().getAccounts());
        if (DataStore.sharedInstance().getConnector() instanceof LiveServerConnector) {
            productsAdapter = new BankingProductsAdapter(getActivity(), DataStore.sharedInstance().getNewProducts());
        } else {
            productsAdapter = new BankingProductsAdapter(getActivity(), DataStore.sharedInstance().getProducts());
        }


        accountRecyclerView.setAdapter(accountsAdapter);
        productRecyclerView.setAdapter(productsAdapter);

        accountRecyclerView.setLayoutManager(new MyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        productRecyclerView.setLayoutManager(new MyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        final float scale = getActivity().getResources().getDisplayMetrics().density;

        // roughly scales the accounts and products to the correct size.
        accountRecyclerView.getLayoutParams().height = (int)((110 * scale + 0.5f) * accountsAdapter.getItemCount());
        productRecyclerView.getLayoutParams().height = (int)((140 * scale + 0.5f) * productsAdapter.getItemCount());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Retrieve parameters from newInstance() ...
        Bundle args = getArguments();


        // Create the view from the xml layout
        View rootView = inflater.inflate(R.layout.fragment_banking, container, false);


        accountRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_banking_accounts);
        productRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_banking_products);



        TextView textAccounts = (TextView)rootView.findViewById(R.id.text_banking_accounts);
        textAccounts.setPaintFlags(textAccounts.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        TextView textProducts = (TextView)rootView.findViewById(R.id.text_banking_products);
        textProducts.setPaintFlags(textProducts.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        return rootView;
    }

}
