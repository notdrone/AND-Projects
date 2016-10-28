package me.droan.udaciousmovies;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MoviesListActivity extends SingleFragmentActivity {


    @Override
    public Fragment createFragment() {
        return new MoviesListFragment();
    }
}
