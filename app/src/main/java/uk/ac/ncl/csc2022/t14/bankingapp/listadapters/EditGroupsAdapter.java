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
 * Created by Sam on 14/04/2015.
 */
public class EditGroupsAdapter extends RecyclerView.Adapter<EditGroupsAdapter.MyViewHolder> {

    Context context;
    private LayoutInflater inflater;
    List<BudgetGroup> groups = new ArrayList<BudgetGroup>();

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
        groups.add(new BudgetGroup(BudgetGroup.TYPE_NEW, "New Group"));
        groups.get(groups.size()-1).getCategories().add(new BudgetCategory(BudgetCategory.TYPE_NEW, "New Category", 0,0));
        notifyItemInserted(groups.size() - 1);
    }

    public List<BudgetGroup> getGroups() {
        return groups;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        TextView name, budget;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.text_group_name);
            budget = (TextView) itemView.findViewById(R.id.text_group_budget);

        }

        @Override
        public boolean onLongClick(View v) {

            return false;
        }
    }
}
