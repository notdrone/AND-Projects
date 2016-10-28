package me.droan.netsu.addData.addTemperature;

/**
 * Created by Drone on 22/09/16.
 */

public interface AddTemperatureContract {

    interface View {

        void initialize();

        void showCurrentTime(String currentTime);

        void close();

        void showNotificationTime(String notificationTime);

        void createNotification(long timeNext);
    }

    interface UserActionListener {

        void showData();

        void onClickDone(String temperature);

        void refreshTime(long timeAt);


    }
}
