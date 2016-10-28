package me.droan.netsu.tracker;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.robinhood.spark.SparkView;
import com.robinhood.ticker.TickerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.droan.netsu.R;
import me.droan.netsu.addData.AddDataActivity;
import me.droan.netsu.common.Constants;
import me.droan.netsu.common.PrefUtil;
import me.droan.netsu.tracker.customFirebaseui.CustomFirebaseAdapter;


public class TrackerFragment extends Fragment implements TrackerContract.View {
    private static final String KEY_TRACKER_ID = "me.droan.home.tracker.trackerFragment.trackerId";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tickerView)
    TickerView tickerView;

    TrackerContract.UserActionListener presenter;
    @BindView(R.id.addTemperatureView)
    LinearLayout addTemperatureView;
    @BindView(R.id.addMemoView)
    LinearLayout addMemoView;
    @BindView(R.id.addMedicineView)
    LinearLayout addMedicineView;
    @BindView(R.id.bottomSheet)
    FrameLayout bottomSheet;
    FragmentManager fm;
    @BindView(R.id.sparkView)
    SparkView sparkView;

    private BottomSheetBehavior bottomSheetBehavior;

    public static TrackerFragment newInstance(String trackerId) {

        Bundle args = new Bundle();
        args.putString(KEY_TRACKER_ID, trackerId);
        TrackerFragment fragment = new TrackerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracker, container, false);
        ButterKnife.bind(this, view);
        presenter = new TrackerPresenter(this);
        fm = getFragmentManager();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.createAdapter(getTrackerId());
        presenter.fetchName(getTrackerId());
    }

    @Override
    public void start() {
        createBottomSheet();
        initToolbar();
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //TODO add options menu for play store release
        setHasOptionsMenu(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_child_settings, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        String metric = PrefUtil.getPref(getActivity(), Constants.PREF_METRIC, Constants.CELSIUS);
        MenuItem metricItem = menu.findItem(R.id.metric);
        if (metric.equals(Constants.CELSIUS)) {
            metricItem.setTitle("");
        } else {
            metricItem.setTitle("");
        }
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.editName) {

        } else if (item.getItemId() == R.id.metric) {
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateAdapter(TrackerAdapter adapter, CustomFirebaseAdapter.GraphAdapter graphAdapter) {
        recyclerView.setLayoutManager(new CustomLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        sparkView.setAdapter(graphAdapter);
    }

    @Override
    public void updateGraphAdapter(ArrayList<Long> list) {

    }

    @Override
    public void temperature(String childId) {
        startActivity(AddDataActivity.createIntent(getActivity(), getTrackerId(), childId, 0));
    }

    @Override
    public void memo(String childId) {
        startActivity(AddDataActivity.createIntent(getActivity(), getTrackerId(), childId, 1));
    }

    @Override
    public void medicine(String childId) {
        startActivity(AddDataActivity.createIntent(getActivity(), getTrackerId(), childId, 2));
    }

    private void createBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(getPeekValue(54));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    private int getPeekValue(int valueInDp) {
        Resources resources = getContext().getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, resources.getDisplayMetrics());
    }

    @OnClick({R.id.addTemperatureView, R.id.addMemoView, R.id.addMedicineView, R.id.addReadingTxtView})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addTemperatureView:
                presenter.onClickAddTemperature();
                break;
            case R.id.addMemoView:
                presenter.onClickAddMemo();
                break;
            case R.id.addMedicineView:
                presenter.onClickAddMedicine();
                break;
            case R.id.addReadingTxtView:
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
        }
    }

    private String getTrackerId() {
        return getArguments().getString(KEY_TRACKER_ID);
    }

    class CustomLayoutManager extends LinearLayoutManager {

        public CustomLayoutManager(Context context) {
            super(context);
        }

        @Override
        public boolean canScrollVertically() {
            return false;
        }

        @Override
        public boolean canScrollHorizontally() {
            return false;
        }
    }
}


