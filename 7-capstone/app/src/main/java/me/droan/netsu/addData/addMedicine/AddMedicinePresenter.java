package me.droan.netsu.addData.addMedicine;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import me.droan.netsu.common.Constants;
import me.droan.netsu.common.MappingHelper;
import me.droan.netsu.common.Utils;
import me.droan.netsu.model.Reading;

/**
 * Created by Drone on 26/09/16.
 */

public class AddMedicinePresenter implements AddMedicineContract.UserActionListener {
    private static final String TAG = "AddMedicinePresenter";
    private final String trackerId;
    private final String childId;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference();
    private AddMedicineContract.View view;
    private long timeAt;
    private List<String> medList = new ArrayList<>();

    AddMedicinePresenter(AddMedicineContract.View view, String trackerId, String childId) {
        this.view = view;
        this.trackerId = trackerId;
        this.childId = childId;
        view.initialize();
    }

    @Override
    public void showData() {
        timeAt = new Date().getTime();
        String currentTime = Utils.convertToSimpleTimeDate(timeAt);
        view.showTimeDate(currentTime);
        final DatabaseReference medicineRef = ref.child(Constants.PATH_MEDICINES).child(Utils.getEid());
        medList.clear();
        medicineRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> children = dataSnapshot.getChildren().iterator();
                while (children.hasNext()) {
                    DataSnapshot next = children.next();
                    String med = next.getValue(String.class);
                    medList.add(med);
                }
                view.updateAutoCompleteAdapter(medList);
            }

            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClickDone(String _medicine) {

        if (!_medicine.equals("")) {
            String medicine = Utils.toTitleCase(_medicine);
            Reading reading = new Reading(2, medicine, new Date().getTime());
            HashMap<String, Object> hashMap = MappingHelper.addNewMedicine(trackerId, childId, reading, ref);
            ref.updateChildren(hashMap);
            view.close();
        }
    }

    @Override
    public void refreshTime(long timeAt) {
        this.timeAt = timeAt;
        view.showTimeDate(Utils.convertToSimpleTimeDate(timeAt));
    }
}
