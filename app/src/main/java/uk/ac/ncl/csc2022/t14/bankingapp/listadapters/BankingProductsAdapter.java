package uk.ac.ncl.csc2022.t14.bankingapp.listadapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.Utility;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.AccountActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.MainActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.ProductActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Product;

/**
 * Created by Sam on 12/04/2015.
 */
public class BankingProductsAdapter extends RecyclerView.Adapter<BankingProductsAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<Product> products = Collections.emptyList();
    final Context context;

    public BankingProductsAdapter(Context context, List<Product> products) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.products = products;

        for (Product product : DataStore.sharedInstance().getProducts()) {
            Log.d("PRODUCTS", "getProducts() id: " + product.getId() + ". name: " + product.getTitle());
        }
        for (Product product : products) {
            Log.d("PRODUCTS", "products id: " + product.getId() + ". name: " + product.getTitle());
        }
    }

    @Override
    public BankingProductsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_banking_product, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(BankingProductsAdapter.MyViewHolder holder, int position) {

        Product current = products.get(position);

        holder.name.setText(current.getTitle());
        holder.description.setText(Html.fromHtml(current.getContent()));

        for (Account account : DataStore.sharedInstance().getCurrentUser().getAccounts()) {
            if (account.getProduct() != null) {
                if (account.getProduct().getId() == current.getId()) {
                    removeItem(position);
                }
            }
        }

        int studentID = 0, savingsID = 0, isaID = 0, freezeID = 0;

        for (Product product : DataStore.sharedInstance().getProducts()) {
            switch (product.getTitle()) {
                case "Savings Account":
                    savingsID = product.getId();
                    break;
                case "ISA Account":
                    isaID = product.getId();
                    break;
                case "Student Account":
                    studentID = product.getId();
                    break;
                case "Freeze Account":
                    freezeID = product.getId();
                    break;
            }
        }

        if (current.getId() == studentID) {
            holder.image.setImageResource(R.drawable.student);
        } else if (current.getId() == savingsID) {
            holder.image.setImageResource(R.drawable.piggy_bank);
        } else if (current.getId() == isaID) {
            holder.image.setImageResource(R.drawable.isa);
        } else if (current.getId() == freezeID) {
            holder.image.setImageResource(R.drawable.snowflake);
        } else {
            holder.image.setImageResource(R.drawable.account_image);
        }

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    private void removeItem(int position) {
        products.remove(position);
        notifyItemRemoved(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout layout;

        ImageView image;
        TextView name;
        TextView description;

        public MyViewHolder(View itemView) {
            super(itemView);

            image = (ImageView)itemView.findViewById(R.id.image_banking_product);
            name = (TextView)itemView.findViewById(R.id.text_banking_name);
            description = (TextView)itemView.findViewById(R.id.text_banking_description);
            layout = (RelativeLayout)itemView.findViewById(R.id.layout_banking_products);

            layout.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_banking_products:
                    Intent i = new Intent(context, ProductActivity.class);
                    i.putExtra("product", products.get(getPosition()));
                    context.startActivity(i);
                    break;
            }
        }
    }
}
