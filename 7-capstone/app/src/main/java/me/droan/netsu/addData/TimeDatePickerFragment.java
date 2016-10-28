package me.droan.netsu.addData;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

import me.droan.netsu.R;

/**
 * Created by Drone on 19/09/16.
 */

public class TimeDatePickerFragment extends DialogFragment {

    public static final String DIALOG_PICKER = "datetimePicler";
    public static final String EXTRA_TIME = "time_extra";
    public static final int REQUEST_TIME = 6;
    private static final String TAG = "TimeDatePickerFragment";

    public static TimeDatePickerFragment newInstance() {
        return new TimeDatePickerFragment();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_date_time_picker, null);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        final Button button = (Button) view.findViewById(R.id.button);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getText().toString().equals(getActivity().getResources().getString(R.string.next))) {
                    button.setText(getActivity().getResources().getString(R.string.ok));
                    timePicker.setVisibility(View.GONE);
                    datePicker.setVisibility(View.VISIBLE);
                } else {
                    Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                            datePicker.getMonth(),
                            datePicker.getDayOfMonth(),
                            timePicker.getCurrentHour(),
                            timePicker.getCurrentMinute());

                    long time = calendar.getTimeInMillis();
                    sendResult(Activity.RESULT_OK, time);
                }
            }


        });
        return dialog;
    }

    private void sendResult(int resultCode, long time) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dismiss();
        }


        if (getTargetFragment() == null) {
            return;
        } else {
            Intent i = new Intent();
            i.putExtra(EXTRA_TIME, time);
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
        }

    }
}
