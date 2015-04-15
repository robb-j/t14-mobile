package uk.ac.ncl.csc2022.t14.bankingapp.listadapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;

/**
 * Created by Sam on 15/04/2015.
 */
public class EditCategoriesAdapter extends RecyclerView.Adapter<EditCategoriesAdapter.MyViewHolder> {

    Context context;
    private LayoutInflater inflater;
    BudgetGroup group;
    List<BudgetCategory> categories = new ArrayList<>();

    public EditCategoriesAdapter(Context context, BudgetGroup group) {
        this.context = context;
        inflater = LayoutInflater.from(context);

        this.group = group;
        categories = group.getCategories();
    }

    @Override
    public EditCategoriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_edit_category, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(EditCategoriesAdapter.MyViewHolder holder, int position) {

        holder.name.setText(categories.get(position).getName());
        holder.budget.setText(String.format("%.2f", categories.get(position).getBudgeted()));

    }

    @Override
    public int getItemCount() {
        return group.getCategories().size();
    }

    public void addCategory() {
        group.getCategories().add(new BudgetCategory(BudgetCategory.TYPE_NEW, "New Category", 0, 0));
        notifyItemInserted(group.getCategories().size()-1);
    }

    public BudgetGroup getGroup() {
        return group;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        EditText name, budget;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (EditText) itemView.findViewById(R.id.edittext_category_name);
            budget = (EditText) itemView.findViewById(R.id.edittext_category_budget);

            name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {                                   // run when focus is lost
                        EditText text = (EditText)v;
                        group.getCategories().get(getPosition()).setName(text.getText().toString());
                    }
                }
            });

            budget.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {                                   // run when focus is lost
                        EditText text = (EditText)v;
                        group.getCategories().get(getPosition()).setName(text.getText().toString());
                    }
                }
            });

        }

        @Override
        public boolean onLongClick(View v) {

            return false;
        }
    }
}
