package me.droan.netsu.tracker;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.droan.netsu.R;
import me.droan.netsu.common.Utils;
import me.droan.netsu.model.Reading;
import me.droan.netsu.tracker.customFirebaseui.CustomFirebaseAdapter;

/**
 * Created by Drone on 17/09/16.
 */

public class TrackerAdapter extends CustomFirebaseAdapter<Reading, TrackerAdapter.TrackerHolder> {


    public TrackerAdapter(Class<Reading> modelClass, int modelLayout, Class<TrackerHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    public TrackerAdapter(Class<Reading> modelClass, int modelLayout, Class<TrackerHolder> viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(TrackerHolder viewHolder, Reading model, int position) {
        viewHolder.timeStampTxt.setText(Utils.convertToSimpleTime(model.getReverseTimeStamp()));
        int type = model.getType();
        if (type == 0) {
            viewHolder.valueTxt.setText(String.format("%s", model.getTemperature()));
        } else if (type == 1) {
            viewHolder.valueTxt.setText(model.getMemo());
        } else if (type == 2) {
            viewHolder.valueTxt.setText(model.getMedicine());
        }
        viewHolder.typeTxt.setText(type + "");
    }

    public static class TrackerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.timeStampTxt)
        TextView timeStampTxt;
        @BindView(R.id.typeTxt)
        TextView typeTxt;
        @BindView(R.id.valueTxt)
        TextView valueTxt;

        public TrackerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
