package me.droan.udaciousmovies;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Drone on 3/22/2016.
 */
public class CustomMoviePoster extends FrameLayout {
    private static final String TAG = "CustomMoviePoster";
    @Bind(R.id.poster)
    ImageView poster;

    public CustomMoviePoster(Context context) {
        super(context);
    }

    public CustomMoviePoster(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomMoviePoster(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void bindTo(String url) {
        Log.d(TAG, "bindTo() called with: " + "url = [" + url + "]");
        Picasso.with(getContext()).load(url).into(poster);
    }
}
