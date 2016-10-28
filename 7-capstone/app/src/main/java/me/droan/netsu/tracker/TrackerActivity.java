package me.droan.netsu.tracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import butterknife.ButterKnife;
import me.droan.netsu.BaseActivity;
import me.droan.netsu.R;

public class TrackerActivity extends BaseActivity {
    private static final String EXTRA_TRACKER_ID = "me.droan.home.tracker.trackerFragment.intent.trackerId";

    public static Intent createIntent(Context context, String trackerId) {
        Intent i = new Intent();
        i.setClass(context, TrackerActivity.class);
        i.putExtra(EXTRA_TRACKER_ID, trackerId);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        ButterKnife.bind(this);

        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.fragment_container) == null) {
            TrackerFragment fragment = TrackerFragment.newInstance(getTrackerId());
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private String getTrackerId() {
        return getIntent().getStringExtra(EXTRA_TRACKER_ID);
    }
}
