package me.droan.netsu.family;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import me.droan.netsu.R;
import me.droan.netsu.common.Constants;
import me.droan.netsu.common.Utils;
import me.droan.netsu.family.FamilyContract.View;
import me.droan.netsu.model.Child;


/**
 * Created by Drone on 15/09/16.
 */

class FamilyPresenter implements FamilyContract.UserActionListener {
    private static final String TAG = "FamilyPresenter";
    View view;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();


    FamilyPresenter(FamilyContract.View view) {
        this.view = view;
        this.view.start();
    }

    @Override
    public void createAdapter() {
        String eid = Utils.getEid();
        if (eid != null) {
            final FamilyAdapter adapter = new FamilyAdapter(Child.class, R.layout.item_child, FamilyAdapter.FamilyHolder.class, reference.child(Constants.PATH_FAMILIES).child(eid));
            adapter.setListener(new FamilyAdapter.OnItemClick() {
                @Override
                public void onClick(int position) {
                    if (position == -1) {
                        view.showAddChild();
                    } else {
                        String trackerId = adapter.getItem(position).getTrackerId();
                        view.openTracker(trackerId);
                    }

                }
            });
            view.updateAdapter(adapter);
        }

    }

}
