package me.droan.netsu.addData.addTemperature;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import me.droan.netsu.common.MappingHelper;
import me.droan.netsu.common.Utils;
import me.droan.netsu.model.Reading;

/**
 * Created by Drone on 22/09/16.
 */

class AddTemperaturePresenter implements AddTemperatureContract.UserActionListener {
    private final String trackerId;
    private final String childId;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    private AddTemperatureContract.View view;
    private long timeAt;
    private long timeNext;

    AddTemperaturePresenter(AddTemperatureContract.View view, String trackerId, String childId) {
        this.view = view;
        this.trackerId = trackerId;
        this.childId = childId;
        view.initialize();

    }

    @Override
    public void showData() {
        timeAt = Utils.getTimeNow();
        timeNext = Utils.getCurrentTimePlusHours(1);
        String currentTime = Utils.convertToSimpleTimeDate(timeAt);
        String notificationTime = Utils.convertToSimpleTimeDate(timeNext);
        view.showCurrentTime(currentTime);
        view.showNotificationTime(notificationTime);
    }

    @Override
    public void onClickDone(String _temperature) {

        if (!_temperature.equals("")) {
            double temperature = Double.parseDouble(_temperature);
            Reading reading = new Reading(temperature, timeAt);
            HashMap<String, Object> newTemperatureMap = MappingHelper.addNewTemperature(trackerId, childId, reading, ref);
            ref.updateChildren(newTemperatureMap);
            view.createNotification(timeNext);
            view.close();
        }
    }

    @Override
    public void refreshTime(long timeAt) {
        this.timeAt = timeAt;
        view.showCurrentTime(Utils.convertToSimpleTimeDate(timeAt));
    }
}
