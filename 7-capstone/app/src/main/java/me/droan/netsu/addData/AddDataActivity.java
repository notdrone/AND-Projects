package me.droan.netsu.addData;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import me.droan.netsu.BaseActivity;
import me.droan.netsu.R;
import me.droan.netsu.addData.addMedicine.AddMedicineFragment;
import me.droan.netsu.addData.addMemo.AddMemoFragment;
import me.droan.netsu.addData.addTemperature.AddTemperatureFragment;


public class AddDataActivity extends BaseActivity {


    private static final String EXTRA_TRACKER_ID = "me.droan.netsu.addData.trackerId";
    private static final String EXTRA_CHILD_ID = "me.droan.netsu.addData.childId";
    private static final String EXTRA_TYPE = "me.droan.netsu.addData.type";

    public static Intent createIntent(Context context, String trackerId, String childId, int type) {
        Intent i = new Intent();
        i.setClass(context, AddDataActivity.class);
        i.putExtra(EXTRA_TRACKER_ID, trackerId);
        i.putExtra(EXTRA_CHILD_ID, childId);
        i.putExtra(EXTRA_TYPE, type);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        checkType();

    }

    private void checkType() {
        if (getType() == 0) {
            openFragment(AddTemperatureFragment.newInstance(getTrackerId(), getChildId()));
        } else if (getType() == 1) {
            openFragment(AddMemoFragment.newInstance(getTrackerId(), getChildId()));
        } else if (getType() == 2) {
            openFragment(AddMedicineFragment.newInstance(getTrackerId(), getChildId()));
        }
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }

    private String getTrackerId() {
        return getIntent().getStringExtra(EXTRA_TRACKER_ID);
    }

    private String getChildId() {
        return getIntent().getStringExtra(EXTRA_CHILD_ID);
    }

    private int getType() {
        return getIntent().getIntExtra(EXTRA_TYPE, 0);

    }

}
