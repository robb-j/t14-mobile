package uk.ac.ncl.csc2022.t14.bankingapp.listadapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.Utility;
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
    List<BudgetCategory> deletedCategories = new ArrayList<>();


    public EditCategoriesAdapter(Context context, BudgetGroup group) {
        this.context = context;
        inflater = LayoutInflater.from(context);

        this.group = group;
        categories = group.getCategories();
        removeDeleted();
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
        BudgetCategory category = new BudgetCategory(BudgetCategory.TYPE_NEW, "New Category", 0, 0);
        category.setMode(BudgetCategory.Mode.NEW);
        group.getCategories().add(category);
        notifyItemInserted(categories.size() - 1);
    }

    public void removeCategory(int position) {
        if (position > 0) {
            BudgetCategory current = categories.get(position);
            BudgetCategory deletedCategory = new BudgetCategory(current.getId(), current.getName(), current.getBudgeted(), current.getSpent());
            deletedCategory.setMode(BudgetCategory.Mode.REMOVED);
            deletedCategories.add(deletedCategory);
            categories.remove(position);
            notifyItemRemoved(position);
        } else {
            Toast.makeText(context, "Cannot delete last category", Toast.LENGTH_SHORT).show();
        }


    }

    public BudgetGroup getGroup() {
        return group;
    }

    public BudgetGroup addDeletedToGroup() {
        for (BudgetCategory category : deletedCategories) {
            group.getCategories().add(category);
        }
        return group;
    }

    public void removeDeleted() {

        for (Iterator<BudgetCategory> iterator = categories.iterator(); iterator.hasNext();) {
            BudgetCategory category = iterator.next();
            if (category.getMode() == BudgetCategory.Mode.REMOVED) {
                iterator.remove();
                notifyDataSetChanged();
            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        EditText name, budget;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (EditText) itemView.findViewById(R.id.edittext_category_name);
            budget = (EditText) itemView.findViewById(R.id.edittext_category_budget);

            name.setOnLongClickListener(this);
            budget.setOnLongClickListener(this);

            name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (!hasFocus) {                                   // run when focus is lost
                        EditText text = (EditText) v;
                        BudgetCategory current = group.getCategories().get(getPosition());
                        if (!current.getName().equals(text.getText().toString())) {
                            current.setName(text.getText().toString());
                            current.setMode(BudgetCategory.Mode.EDITED);

                            if (group.getMode() == BudgetGroup.Mode.UNCHANGED) {
                                group.setMode(BudgetGroup.Mode.EDITED);
                            }
                        }

                    }


                }
            });

            budget.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {                                   // run when focus is lost
                        EditText text = (EditText)v;
                        BudgetCategory current = group.getCategories().get(getPosition());
                        if (current.getBudgeted() != Utility.currencyToDouble(text.getText().toString())) {
                            current.setBudgeted(Utility.currencyToDouble(text.getText().toString()));
                            current.setMode(BudgetCategory.Mode.EDITED);

                            if (group.getMode() == BudgetGroup.Mode.UNCHANGED) {
                                group.setMode(BudgetGroup.Mode.EDITED);
                            }
                        }

                    }
                }
            });

        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to delete: " + categories.get(getPosition()).getName() + "?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    removeCategory(getPosition());
                }
            });
            builder.setNegativeButton("No", null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }
    }
}
