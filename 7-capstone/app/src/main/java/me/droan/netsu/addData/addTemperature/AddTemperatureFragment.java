package me.droan.netsu.addData.addTemperature;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.droan.netsu.R;
import me.droan.netsu.addData.AddDataActivity;
import me.droan.netsu.addData.AddDialogFragment;
import me.droan.netsu.addData.TimeDatePickerFragment;
import me.droan.netsu.notification.NotificationReceiver;
import me.droan.netsu.notification.NotificationUtil;

/**
 * Created by Drone on 16/09/16.
 */

public class AddTemperatureFragment extends AddDialogFragment implements AddTemperatureContract.View {

    private static final String KEY_TRACKER_ID = "trackerId";
    private static final String KEY_CHILD_ID = "childId";

    @BindView(R.id.addMemoEditText)
    EditText addTemperatureEditTxt;
    @BindView(R.id.currentTime)
    Button currentTime;

    AddTemperatureContract.UserActionListener presenter;
    @BindView(R.id.notificationTime)
    Button notificationTime;

    public static AddTemperatureFragment newInstance(String trackerId, String childId) {
        Bundle args = new Bundle();
        args.putString(KEY_TRACKER_ID, trackerId);
        args.putString(KEY_CHILD_ID, childId);
        AddTemperatureFragment fragment = new AddTemperatureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_temperature, container, false);
        ButterKnife.bind(this, view);
        presenter = new AddTemperaturePresenter(this, getTrackerId(), getChildId());
        presenter.showData();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private String getTrackerId() {
        return getArguments().getString(KEY_TRACKER_ID);
    }

    private String getChildId() {
        return getArguments().getString(KEY_CHILD_ID);
    }


    @OnClick({R.id.currentTime, R.id.done})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.currentTime:
                onClickTime();
                break;
            case R.id.done:
                presenter.onClickDone(addTemperatureEditTxt.getText().toString());
                break;
        }
    }

    public void onClickTime() {
        TimeDatePickerFragment dialog = TimeDatePickerFragment.newInstance();
        dialog.setTargetFragment(AddTemperatureFragment.this, TimeDatePickerFragment.REQUEST_TIME);
        dialog.show(getFragmentManager(), TimeDatePickerFragment.DIALOG_PICKER);
    }

    @Override
    protected void updateTime(long timeAt) {
        presenter.refreshTime(timeAt);
    }


    @Override
    public void initialize() {

    }

    @Override
    public void showCurrentTime(String _currentTime) {
        currentTime.setText(_currentTime);
    }

    @Override
    public void close() {
        if (getActivity() instanceof AddDataActivity) {
            getActivity().finish();
        } else {
            dismiss();
        }
    }

    @Override
    public void showNotificationTime(String notificationTime) {
        this.notificationTime.setText(notificationTime);
    }

    @Override
    public void createNotification(long timeNext) {
        NotificationUtil.createBroadcast(getActivity(), NotificationReceiver.TYPE_TEMPERATURE, getTrackerId(), timeNext);
    }
}
