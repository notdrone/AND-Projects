package me.droan.netsu.tracker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import me.droan.netsu.R;
import me.droan.netsu.common.Constants;
import me.droan.netsu.model.About;
import me.droan.netsu.model.Reading;
import me.droan.netsu.tracker.customFirebaseui.CustomFirebaseAdapter;

/**
 * Created by Drone on 16/09/16.
 */

public class TrackerPresenter implements TrackerContract.UserActionListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    private TrackerContract.View view;
    private String childId;

    public TrackerPresenter(TrackerContract.View view) {
        this.view = view;
        view.start();
    }


    @Override
    public void createAdapter(final String trackerId) {
        ref.child(Constants.PATH_TRACKER).child(trackerId).child(Constants.PATH_TRACKER_ABOUT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                About about = dataSnapshot.getValue(About.class);
                childId = about.getChildId();
                Query readingsQuery = ref.child(Constants.PATH_TRACKER).child(trackerId).child(Constants.PATH_TRACKER_DATA)
                        .orderByChild(Constants.PATH_TRACKER_DATA_REVERSE_TIME);
                TrackerAdapter adapter = new TrackerAdapter(Reading.class, R.layout.item_reading, TrackerAdapter.TrackerHolder.class, readingsQuery);
                CustomFirebaseAdapter<Reading, TrackerAdapter.TrackerHolder>.GraphAdapter graphAdapter = adapter.getGraphAdapter();
                view.updateAdapter(adapter, graphAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void fetchName(String trackerId) {
    }

    @Override
    public void createGraphAdapter(TrackerAdapter adapter) {

    }

    @Override
    public void onClickAddTemperature() {
        view.temperature(childId);
    }

    @Override
    public void onClickAddMemo() {
        view.memo(childId);
    }

    @Override
    public void onClickAddMedicine() {
        view.medicine(childId);
    }
}
