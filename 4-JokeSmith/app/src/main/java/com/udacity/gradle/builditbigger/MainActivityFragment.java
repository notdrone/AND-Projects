package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import me.droan.jokeactivitylib.JokeAcivity;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements EndpointsAsyncTask.Listener {
    private static final String TAG = "MainActivityFragment";
    private ProgressBar spinner;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EndpointsAsyncTask.setListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        Button jokeButton = (Button) root.findViewById(R.id.jokeButton);
        spinner = (ProgressBar) root.findViewById(R.id.spinner);
        jokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EndpointsAsyncTask().execute();
                spinner.setVisibility(View.VISIBLE);
            }
        });
        if (BuildConfig.IS_FREE) {
            showAds(root);
        }

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void showAds(View root) {
        Utility.showAds(getActivity(), root);
    }


    @Override
    public void onPostCalled(String result) {
        spinner.setVisibility(View.GONE);
        Intent i = JokeAcivity.putIntent(getActivity(), result);
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EndpointsAsyncTask.setListener(null);
    }
}
