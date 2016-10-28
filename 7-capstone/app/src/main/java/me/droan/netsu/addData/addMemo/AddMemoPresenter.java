package me.droan.netsu.addData.addMemo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import me.droan.netsu.common.Constants;
import me.droan.netsu.common.Utils;
import me.droan.netsu.model.Reading;

import static me.droan.netsu.common.Constants.PATH_TRACKER;

/**
 * Created by Drone on 23/09/16.
 */

public class AddMemoPresenter implements AddMemoContract.UserActionListener {

    private final String trackerId;
    private final String childId;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    private AddMemoContract.View view;
    private long timeAt;

    AddMemoPresenter(AddMemoContract.View view, String trackerId, String childId) {
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


    }

    @Override
    public void onClickDone(String memo) {

        if (!memo.equals("")) {
            Reading reading = new Reading(1, memo, new Date().getTime());
            ref.child(PATH_TRACKER).child(trackerId).child(Constants.PATH_TRACKER_DATA).push().setValue(reading);
            view.close();
        }
    }

    @Override
    public void refreshTime(long timeAt) {
        this.timeAt = timeAt;
        view.showTimeDate(Utils.convertToSimpleTimeDate(timeAt));
    }
}
