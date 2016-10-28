package me.droan.netsu.addData.addMemo;

/**
 * Created by Drone on 23/09/16.
 */

public interface AddMemoContract {

    interface View {

        void initialize();

        void showTimeDate(String currentTime);

        void close();
    }

    interface UserActionListener {

        void showData();

        void onClickDone(String memo);

        void refreshTime(long timeAt);
    }
}
