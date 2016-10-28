package me.droan.udaciousmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.droan.udaciousmovies.Model.MovieListModel.MovieList;
import me.droan.udaciousmovies.Model.MovieListModel.Result;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Drone on 3/22/2016.
 */
public class MoviesListFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "MoviesListFragment";
    private static final int popularSort = 0;
    private static final int topSort = 1;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.spinner_nav)
    Spinner spinner;
    ArrayList<Result> list = new ArrayList<>();
    RecyclerView.Adapter adapter = new Adapter(list);
    int currentPos = popularSort;
    View v;
    private MovieServices movieServices;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        movieServices = retrofit.create((MovieServices.class));
        serviceHandler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        v = view;
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sort_order, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Snackbar.make(v, "Refreshing...", Toast.LENGTH_SHORT).show();
        if (currentPos != position) {
            currentPos = position;
            serviceHandler();
        }
    }

    private void serviceHandler() {
        Call<MovieList> call;
        if (currentPos == popularSort) {
            call = movieServices.getPopularMovies();
        } else {
            call = movieServices.getTopRatedMovies();
        }
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                Log.d(TAG, "onResponse() called with: " + "call = [" + call + "], response = [" + response + "]");
                MovieList movieList = response.body();
                list = (ArrayList<Result>) movieList.results;
                recyclerView.setAdapter(new Adapter(list));

            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.d(TAG, "onFailure() called with: " + "call = [" + call + "], t = [" + t + "]");
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class Holder extends RecyclerView.ViewHolder {
        CustomMoviePoster itemView;
        Result movie;

        public Holder(CustomMoviePoster itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public void bindTo(Result _movie) {
            this.movie = _movie;
            itemView.bindTo("http://image.tmdb.org/t/p/w185" + movie.poster_path);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = MovieDetailActivity.putIntent(getContext(), movie);
                    startActivity(intent);
                }
            });
        }
    }

    class Adapter extends RecyclerView.Adapter<Holder> {
        ArrayList<Result> list;

        public Adapter(ArrayList<Result> list) {
            this.list = list;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            CustomMoviePoster view = (CustomMoviePoster) inflater.inflate(R.layout.item_movie, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Result movie = list.get(position);
            holder.bindTo(movie);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
