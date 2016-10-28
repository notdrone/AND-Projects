package me.droan.netsu.family;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.droan.netsu.R;
import me.droan.netsu.common.Utils;
import me.droan.netsu.model.Child;
import me.droan.netsu.model.LastTemperature;

import static android.content.ContentValues.TAG;

/**
 * Created by Drone on 16/09/16.
 */

public class FamilyAdapter extends FirebaseRecyclerAdapter<Child, RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 345;
    private static final int TYPE_FOOTER = 948;
    private static OnItemClick listener;
    private Context context;

    public FamilyAdapter(Class modelClass, int modelLayout, Class viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    public void setListener(OnItemClick listener) {
        FamilyAdapter.listener = listener;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (position == getItemCount() - 1) {

        } else {
            super.onBindViewHolder(viewHolder, position);
        }
    }

    @Override
    protected void populateViewHolder(RecyclerView.ViewHolder viewHolder, Child model, int position) {

        if (position != getItemCount() - 1) {
            FamilyHolder holder = (FamilyHolder) viewHolder;
            holder.childName.setText(model.getName());
            LastTemperature lastTemperature = model.getLastTemperature();
            if (lastTemperature != null) {
                holder.lastTemperature.setText(lastTemperature.getTemperature()
                        + context.getString(R.string.f_at) + Utils.convertToSimpleTime(lastTemperature.getTimeAt()));
            }

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == TYPE_ITEM) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_child, parent, false);
            return new FamilyHolder(view);
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_add_child, parent, false);
            return new AddChildHolder(view);
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + super.getItemCount() + 1);
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }

    }

    interface OnItemClick {
        void onClick(int position);
    }

    public static class FamilyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.childName)
        TextView childName;

        @BindView(R.id.lastTemperature)
        TextView lastTemperature;

        public FamilyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(getAdapterPosition());
                }
            });
        }
    }

    public static class AddChildHolder extends RecyclerView.ViewHolder {

        public AddChildHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(-1);//-1 depicts that the add new child is pressed
                }
            });

        }
    }

}
