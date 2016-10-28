package me.droan.netsu.addData;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
/**
 * Created by Drone on 20/09/16.
 */

public abstract class AddDialogFragment extends DialogFragment {

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == TimeDatePickerFragment.REQUEST_TIME) {
            updateTime(data.getLongExtra(TimeDatePickerFragment.EXTRA_TIME, 0));
        }

    }


    protected abstract void updateTime(long longExtra);
}
