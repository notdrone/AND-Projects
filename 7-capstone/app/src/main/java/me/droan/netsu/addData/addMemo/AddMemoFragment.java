package me.droan.netsu.addData.addMemo;

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

/**
 * Created by Drone on 16/09/16.
 */

public class AddMemoFragment extends AddDialogFragment implements AddMemoContract.View {

    private static final String KEY_TRACKER_ID = "trackerId";
    private static final String KEY_CHILD_ID = "childId";


    @BindView(R.id.addMemoEditText)
    EditText addMemoEditText;
    @BindView(R.id.currentTime)
    Button currentTime;

    AddMemoContract.UserActionListener presenter;

    public static AddMemoFragment newInstance(String trackerId, String childId) {
        Bundle args = new Bundle();
        args.putString(KEY_TRACKER_ID, trackerId);
        args.putString(KEY_CHILD_ID, childId);
        AddMemoFragment fragment = new AddMemoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_memo, container, false);
        ButterKnife.bind(this, view);
        presenter = new AddMemoPresenter(this, getTrackerId(), getChildId());
        presenter.showData();
        return view;
    }

    private String getTrackerId() {
        return getArguments().getString(KEY_TRACKER_ID);
    }

    private String getChildId() {
        return getArguments().getString(KEY_CHILD_ID);
    }

    public void onClickTime() {
        TimeDatePickerFragment dialog = TimeDatePickerFragment.newInstance();
        dialog.setTargetFragment(AddMemoFragment.this, TimeDatePickerFragment.REQUEST_TIME);
        dialog.show(getFragmentManager(), TimeDatePickerFragment.DIALOG_PICKER);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void showTimeDate(String _currentTime) {
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

    @OnClick({R.id.currentTime, R.id.done})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.currentTime:
                onClickTime();
                break;
            case R.id.done:
                presenter.onClickDone(addMemoEditText.getText().toString());
                break;
        }
    }

    @Override
    protected void updateTime(long longExtra) {
        presenter.refreshTime(longExtra);
    }
}
