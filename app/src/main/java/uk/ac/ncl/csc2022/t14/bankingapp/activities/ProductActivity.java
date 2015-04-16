package uk.ac.ncl.csc2022.t14.bankingapp.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.ac.ncl.csc2022.t14.bankingapp.LloydsActionBarActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;

public class ProductActivity extends LloydsActionBarActivity {

    private static Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        // Receive the correct product object from the previous activity
        product = getIntent().getExtras().getParcelable("product");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product, menu);
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                    Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_product, container, false);

            // Get the title and content from fragment_product
            TextView title = (TextView) rootView.findViewById(R.id.productTitle);
            TextView content = (TextView) rootView.findViewById(R.id.productContent);

            // Set the title and content of this product to appear on the fragment
            title.setText(product.getTitle());
            content.setText(android.text.Html.fromHtml("<p>"+product.getContent() + "</p>"));

            return rootView;
        }
    }
}
