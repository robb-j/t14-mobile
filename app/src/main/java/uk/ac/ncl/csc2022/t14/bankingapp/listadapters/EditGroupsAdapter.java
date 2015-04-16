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

import java.util.ArrayList;
import java.util.List;

import uk.ac.ncl.csc2022.t14.bankingapp.R;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetCategory;
import uk.ac.ncl.csc2022.t14.bankingapp.models.BudgetGroup;

/**
 * Created by Sam on 14/04/2015.
 */
public class EditGroupsAdapter extends RecyclerView.Adapter<EditGroupsAdapter.MyViewHolder> {

    Context context;
    private LayoutInflater inflater;
    List<BudgetGroup> groups = new ArrayList<BudgetGroup>();
    List<BudgetGroup> deletedGroups = new ArrayList<>();

    public EditGroupsAdapter(Context context, List<BudgetGroup> groups) {
        this.context = context;
        inflater = LayoutInflater.from(context);

        this.groups = groups;
    }

    @Override
    public EditGroupsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_edit_group, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(EditGroupsAdapter.MyViewHolder holder, int position) {

        BudgetGroup current = groups.get(position);

        holder.name.setText(current.getName());
        holder.budget.setText(String.format("%.2f", current.getBudget()));

    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public void addGroup() {
        BudgetGroup group = new BudgetGroup(BudgetGroup.TYPE_NEW, "New Group");
        group.setMode(BudgetGroup.Mode.NEW);
        BudgetCategory category = new BudgetCategory(BudgetCategory.TYPE_NEW, "New Category", 0, 0);
        category.setMode(BudgetCategory.Mode.NEW);
        group.getCategories().add(category);
        groups.add(group);
        notifyItemInserted(groups.size() - 1);
    }

    public void removeGroup(int position) {
        BudgetGroup current = groups.get(position);
        BudgetGroup deletedGroup = new BudgetGroup(current.getId(), current.getName());
        deletedGroup.setMode(BudgetGroup.Mode.REMOVED);
        deletedGroups.add(deletedGroup);
        groups.remove(position);
        notifyItemRemoved(position);
    }

    public List<BudgetGroup> getGroups() {
        for (BudgetGroup group : deletedGroups) {
            groups.add(group);
        }
        return groups;
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        TextView name, budget;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.text_group_name);
            budget = (TextView) itemView.findViewById(R.id.text_group_budget);

            name.setOnLongClickListener(this);
            budget.setOnLongClickListener(this);

        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to delete: " + groups.get(getPosition()).getName() + "?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    removeGroup(getPosition());
                }
            });
            builder.setNegativeButton("No", null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }
    }
}
