package me.droan.udaciousmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.droan.udaciousmovies.Model.MovieListModel.Result;

/**
 * Created by Drone on 3/22/2016.
 */
public class MovieDetailFragment extends Fragment {
    private static final String KEY_MOVIEDETAIL_FRAG = "moviewdetailfragmnet.movie";
    @Bind(R.id.poster)
    ImageView poster;
    @Bind(R.id.releaseDate)
    TextView releaseDate;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.summary)
    TextView summary;
    @Bind(R.id.rating)
    TextView rating;
    @Bind(R.id.background)
    ImageView background;


    public static MovieDetailFragment newInsatnce(Result movie) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_MOVIEDETAIL_FRAG, movie);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, view);
        Result movie = (Result) getArguments().getSerializable(KEY_MOVIEDETAIL_FRAG);
        title.setText(movie.title);
        summary.setText(movie.overview);
        releaseDate.setText(movie.release_date);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185" + movie.backdrop_path).into(background);
        rating.setText("Rating: " + movie.vote_average + "/10");
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185" + movie.poster_path).into(poster);

        return view;

    }
}
