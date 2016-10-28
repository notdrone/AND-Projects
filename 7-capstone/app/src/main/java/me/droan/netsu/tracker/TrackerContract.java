package me.droan.netsu.tracker;

import java.util.ArrayList;

import me.droan.netsu.tracker.customFirebaseui.CustomFirebaseAdapter;

/**
 * Created by Drone on 16/09/16.
 */

public interface TrackerContract {
    interface View {
        void start();

        void updateAdapter(TrackerAdapter adapter, CustomFirebaseAdapter.GraphAdapter graphAdapter);

        void updateGraphAdapter(ArrayList<Long> list);

        void temperature(String childId);

        void memo(String childId);

        void medicine(String childId);
    }

    interface UserActionListener {

        void createAdapter(String trackerId);

        void fetchName(String trackerId);

        void createGraphAdapter(TrackerAdapter recyclerAdapter);

        void onClickAddTemperature();


        void onClickAddMemo();

        void onClickAddMedicine();

    }
}
