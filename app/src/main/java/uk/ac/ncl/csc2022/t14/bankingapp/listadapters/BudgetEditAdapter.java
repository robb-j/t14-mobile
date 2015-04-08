package uk.ac.ncl.csc2022.t14.bankingapp.listadapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.DataStore;
import uk.ac.ncl.csc2022.t14.bankingapp.Utilities.Utility;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;

/**
 * Created by Sam on 07/04/2015.
 */
public class BudgetEditAdapter extends RecyclerView.Adapter<BudgetEditAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<BudgetGroup> groups = Collections.emptyList();
    Context context;
    private boolean categoriesBeenAdded = false;


    public BudgetEditAdapter(Context context, List<BudgetGroup> groups) {
        inflater = LayoutInflater.from(context);

        this.groups = groups;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_budget_edit, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.layoutCategories.removeAllViews();

        final BudgetGroup current = groups.get(position);

        for (BudgetCategory category : current.getCategories()) {



            LinearLayout layoutCategory = new LinearLayout(context);
            layoutCategory.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.weight = 1;



            EditText categoryName = new EditText(context);
            categoryName.setText(category.getName());
            categoryName.setLayoutParams(lp);
            categoryName.setBackgroundDrawable(null);


            EditText categoryBudget = new EditText(context);
            categoryBudget.setText(String.format("%.2f", category.getBudgeted()));
            categoryBudget.setLayoutParams(lp);
            categoryBudget.setGravity(Gravity.RIGHT);
            categoryBudget.setBackgroundDrawable(null);
            categoryBudget.setInputType(InputType.TYPE_CLASS_NUMBER);



            layoutCategory.addView(categoryName);
            layoutCategory.addView(categoryBudget);
            holder.layoutCategories.addView(layoutCategory);
        }


        holder.editGroup.setText(current.getName());
        holder.textNewCat.setText("+ New Category");


    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public void deleteItem(int position) {
        groups.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(BudgetGroup group) {
        groups.add(group);
        notifyItemInserted(getItemCount()-1);
    }

    public ArrayList<BudgetGroup> getNewGroups() {
        ArrayList<BudgetGroup> newGroups = new ArrayList<>();
        for (BudgetGroup group : groups) {
            if (group.getId() == BudgetGroup.TYPE_NEW) {
                newGroups.add(group);
            }
        }

        return newGroups;
    }

    public ArrayList<BudgetGroup> getDeletedGroups() {
        ArrayList<BudgetGroup> deletedGroups = (ArrayList)DataStore.sharedInstance().getCurrentUser().getAllGroups();
        for (BudgetGroup group : groups) {
            for (BudgetGroup newGroup : deletedGroups) {
                if (newGroup.getId() == group.getId()) {
                    deletedGroups.remove(newGroup);
                }
            }
        }

        return deletedGroups;
    }


    public HashMap<Integer, BudgetGroup> getUpdatedGroups() {
        HashMap<Integer, BudgetGroup> groups = new HashMap<>();
        for (BudgetGroup group : DataStore.sharedInstance().getCurrentUser().getAllGroups()) {
            for (int i = 0; i < getItemCount(); i++) {
                    BudgetGroup current = groups.get(i);

            }
        }
        return groups;
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        EditText editGroup;
        LinearLayout layoutCategories;
        TextView textNewCat;

        public MyViewHolder(View itemView) {
            super(itemView);

            editGroup = (EditText)itemView.findViewById(R.id.edit_budget_group);
            layoutCategories = (LinearLayout)itemView.findViewById(R.id.layout_budget_categories);








            textNewCat = (TextView)itemView.findViewById(R.id.text_new_category);
            editGroup.setOnLongClickListener(this);
            textNewCat.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.edit_budget_group:

                    break;
                case R.id.layout_budget_categories:

                    break;
                case R.id.text_new_category:
                    groups.get(getPosition()).getCategories().add(new BudgetCategory(BudgetCategory.TYPE_NEW, "New Category", 0, 0));
                    notifyDataSetChanged();
                    break;
            }

        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.edit_budget_group:
                    AlertDialog.Builder builderGroup = new AlertDialog.Builder(context);
                    builderGroup.setTitle("Warning");
                    builderGroup.setMessage("Are you sure you want to delete " + ((EditText) v.findViewById(R.id.edit_budget_group)).getText() + "?");
                    builderGroup.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteItem(getPosition());
                        }
                    });
                    builderGroup.setNegativeButton("No", null);
                    AlertDialog dialogGroup = builderGroup.create();
                    dialogGroup.show();
                    break;
                case R.id.layout_budget_categories:
                    break;
                case R.id.text_new_category:

                    break;
            }

            return false;
        }
    }

}
