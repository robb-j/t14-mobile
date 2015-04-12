package uk.ac.ncl.csc2022.t14.bankingapp.listadapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.Utility;
import uk.ac.ncl.csc2022.t14.bankingapp.activities.AccountActivity;
import uk.ac.ncl.csc2022.t14.bankingapp.models.Account;

/**
 * Created by Sam on 12/04/2015.
 */
public class BankingAccountsAdapter extends RecyclerView.Adapter<BankingAccountsAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<Account> accounts = Collections.emptyList();
    Context context;

    public BankingAccountsAdapter(Context context, List<Account> accounts) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.accounts = accounts;
    }

    @Override
    public BankingAccountsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_banking_account, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(BankingAccountsAdapter.MyViewHolder holder, int position) {

        Account current = accounts.get(position);

        holder.name.setText(current.getName());
        holder.balance.setText(Utility.doubleToCurrency(current.getBalance()));
        if (current.getBalance() < 0) {
            holder.balance.setTextColor(Color.rgb(120,0,0));
        } else {
            holder.balance.setTextColor(Color.rgb(0,120,0));
        }
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView name;
        TextView balance;
        RelativeLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);

            image = (ImageView)itemView.findViewById(R.id.image_banking_account);
            name = (TextView)itemView.findViewById(R.id.text_banking_name);
            balance = (TextView)itemView.findViewById(R.id.text_banking_balance);
            layout = (RelativeLayout)itemView.findViewById(R.id.layout_banking_accounts);

            layout.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_banking_accounts:
                    Intent i = new Intent(context, AccountActivity.class);
                    i.putExtra("accountId", accounts.get(getPosition()).getId());
                    context.startActivity(i);
                    break;
            }
        }

    }
}
