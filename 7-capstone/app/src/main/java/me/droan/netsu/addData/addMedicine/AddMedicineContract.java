package me.droan.netsu.addData.addMedicine;

import java.util.List;

/**
 * Created by Drone on 26/09/16.
 */

public interface AddMedicineContract {
    interface View {

        void initialize();

        void showTimeDate(String currentTime);

        void close();

        void updateAutoCompleteAdapter(List<String> medList);
    }

    interface UserActionListener {

        void showData();

        void onClickDone(String medicine);

        void refreshTime(long timeAt);
    }
}
