package me.droan.jokeactivitylib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class JokeAcivity extends AppCompatActivity {
    public static final String EXTRA_JOKE = "me.droan.jokeActivityLib.JokeActivity.extra_joke";

    public static Intent putIntent(Context context, String joke) {
        Intent i = new Intent(context, JokeAcivity.class);
        i.putExtra(EXTRA_JOKE, joke);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_acivity);
        TextView t = (TextView) findViewById(R.id.text);
        t.setText(getIntent().getStringExtra(EXTRA_JOKE));
    }
}
