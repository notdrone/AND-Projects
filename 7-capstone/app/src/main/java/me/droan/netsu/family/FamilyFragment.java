package me.droan.netsu.family;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.droan.netsu.R;
import me.droan.netsu.del.ShareActivity;
import me.droan.netsu.firstChild.AddChildFragment;
import me.droan.netsu.tracker.TrackerActivity;

/**
 * Created by Drone on 15/09/16.
 */

public class FamilyFragment extends Fragment implements FamilyContract.View {

    FamilyContract.UserActionListener presenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_family, container, false);
        ButterKnife.bind(this, rootView);
        presenter = new FamilyPresenter(this);
        presenter.createAdapter();
        return rootView;
    }


    @Override
    public void start() {
        initToolbar();
    }

    private void initToolbar() {
        setHasOptionsMenu(true);
    }

    @Override
    public void updateAdapter(FamilyAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void openTracker(String trackerId) {
        if (getView().findViewById(R.id.fragment_container) == null) {
            openTrackerActivity(trackerId);
        }
    }

    @Override
    public void showAddChild() {
        AddChildFragment dialog = new AddChildFragment();
        dialog.show(getFragmentManager(), "yo");
    }

    private void openTrackerActivity(String trackerId) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity());
        ActivityCompat.startActivity(getActivity(), TrackerActivity.createIntent(getActivity(), trackerId), optionsCompat.toBundle());
    }

    //DELETE AFTER SUBMISSION


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_share, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            startActivity(new Intent(getActivity(), ShareActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
