package me.droan.netsu.addData.addMedicine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.droan.netsu.R;
import me.droan.netsu.addData.AddDataActivity;
import me.droan.netsu.addData.AddDialogFragment;
import me.droan.netsu.addData.TimeDatePickerFragment;


/**
 * Created by Drone on 26/09/16.
 */

public class AddMedicineFragment extends AddDialogFragment implements AddMedicineContract.View {

    private static final String KEY_TRACKER_ID = "trackerId";
    private static final String KEY_CHILD_ID = "childId";

    @BindView(R.id.addMedicineTxt)
    AutoCompleteTextView addMedicineTxt;
    @BindView(R.id.currentTime)
    Button currentTime;
    ArrayAdapter<String> autoCompleteAdapter;

    AddMedicineContract.UserActionListener presenter;


    public static AddMedicineFragment newInstance(String trackerId, String childId) {
        Bundle args = new Bundle();
        args.putString(KEY_TRACKER_ID, trackerId);
        args.putString(KEY_CHILD_ID, childId);
        AddMedicineFragment fragment = new AddMedicineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_medicine, container, false);
        ButterKnife.bind(this, view);
        presenter = new AddMedicinePresenter(this, getTrackerId(), getChildId());
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
                presenter.onClickDone(addMedicineTxt.getText().toString());
                break;
        }
    }

    public void onClickTime() {
        TimeDatePickerFragment dialog = TimeDatePickerFragment.newInstance();
        dialog.setTargetFragment(AddMedicineFragment.this, TimeDatePickerFragment.REQUEST_TIME);
        dialog.show(getFragmentManager(), TimeDatePickerFragment.DIALOG_PICKER);
    }

    @Override
    protected void updateTime(long timeAt) {
        presenter.refreshTime(timeAt);
    }


    @Override
    public void initialize() {
        autoCompleteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
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

    @Override
    public void updateAutoCompleteAdapter(List<String> medList) {
        autoCompleteAdapter.clear();
        autoCompleteAdapter.addAll(medList);
        addMedicineTxt.setAdapter(autoCompleteAdapter);
    }
}
