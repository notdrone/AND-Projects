package me.droan.udaciousmovies;

import me.droan.udaciousmovies.Model.MovieListModel.MovieList;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Drone on 3/22/2016.
 */
public interface MovieServices {

    @GET("popular?api_key=" + Constants.API_KEY)
    Call<MovieList> getPopularMovies();

    @GET("top_rated?api_key=" + Constants.API_KEY)
    Call<MovieList> getTopRatedMovies();
}
