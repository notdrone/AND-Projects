package me.droan.udaciousmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.io.Serializable;

import me.droan.udaciousmovies.Model.MovieListModel.Result;

/**
 * Created by Drone on 3/22/2016.
 */
public class MovieDetailActivity extends SingleFragmentActivity {

    private static final String KEY_MOVIE_DETAIL = "moviedetailActivity.moviedetailobject";

    public static Intent putIntent(Context context, Result movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(KEY_MOVIE_DETAIL, (Serializable) movie);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        return MovieDetailFragment.newInsatnce((Result) getIntent().getSerializableExtra(KEY_MOVIE_DETAIL));
    }
}
