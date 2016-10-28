package com.sam_chordas.android.stockhawk.ui;

import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.QuoteCursorAdapter;
import com.sam_chordas.android.stockhawk.rest.RecyclerViewItemClickListener;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.service.StockIntentService;
import com.sam_chordas.android.stockhawk.service.StockTaskService;
import com.sam_chordas.android.stockhawk.touch_helper.SimpleItemTouchHelperCallback;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MyStocksActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor> {
  private static final int STOCK_INVALID = 435;
  private static final int STOCK_SERVER_DOWN = 4235;
  private static final int STOCK_OFFLINE = 4123;
  /**
   * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
   */

  private static final int CURSOR_LOADER_ID = 0;
  boolean isConnected;
  /**
   * Used to store the last screen title. For use in {@link #restoreActionBar()}.
   */
  private CharSequence mTitle;
  private Intent mServiceIntent;
  private ItemTouchHelper mItemTouchHelper;
  private QuoteCursorAdapter mCursorAdapter;
  private Context mContext;
  private Cursor mCursor;

  private BroadcastReceiver receiver = new BroadcastReceiver() {
    @Override public void onReceive(Context context, Intent intent) {
      String message = intent.getStringExtra("message");
      Toast.makeText(MyStocksActivity.this, "" + message, Toast.LENGTH_SHORT).show();
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mContext = this;
    ConnectivityManager cm =
        (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    setContentView(R.layout.activity_my_stocks);
    // The intent service is for executing immediate pulls from the Yahoo API
    // GCMTaskService can only schedule tasks, they cannot execute immediately
    mServiceIntent = new Intent(this, StockIntentService.class);
    if (savedInstanceState == null) {
      // Run the initialize task service so that some stocks appear upon an empty database
      mServiceIntent.putExtra("tag", "init");
      if (isConnected) {
        startService(mServiceIntent);
      } else {
        networkToast();
      }
    }

    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    assert recyclerView != null;
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

    mCursorAdapter = new QuoteCursorAdapter(this, null);
    recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this,
        new RecyclerViewItemClickListener.OnItemClickListener() {
          @Override public void onItemClick(View v, int position) {
            mCursor.moveToPosition(position);
            String symbol = mCursor.getString(mCursor.getColumnIndex("symbol"));

            Intent i = DetailActivity.putIntent(getApplicationContext(), symbol);
            startActivity(i);
          }
        }));
    recyclerView.setAdapter(mCursorAdapter);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

    if (fab != null) {
      fab.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          if (isConnected) {
            new MaterialDialog.Builder(mContext)
                .inputType(InputType.TYPE_CLASS_TEXT)
                    .input(R.string.content_test, R.string.input_prefill,
                    new MaterialDialog.InputCallback() {
                      @Override public void onInput(MaterialDialog dialog, CharSequence input) {
                        // On FAB click, receive user input. Make sure the stock doesn't already exist
                        // in the DB and proceed accordingly
                        Cursor c = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                            new String[] { QuoteColumns.SYMBOL }, QuoteColumns.SYMBOL + "= ?",
                                new String[]{input.toString().toUpperCase()}, null);
                        if (c.getCount() != 0) {
                          Toast toast =
                                  Toast.makeText(MyStocksActivity.this, getString(R.string.stock_already_saved),
                                  Toast.LENGTH_LONG);
                          toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                          toast.show();
                          return;
                        } else {
                          // Add the stock to DB
                          mServiceIntent.putExtra("tag", "add");
                          mServiceIntent.putExtra("symbol", input.toString().toUpperCase());
                          startService(mServiceIntent);
                        }
                      }
                    })
                .show();
          } else {
            networkToast();
          }
        }
      });
    }

    ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mCursorAdapter);
    mItemTouchHelper = new ItemTouchHelper(callback);
    mItemTouchHelper.attachToRecyclerView(recyclerView);

    mTitle = getTitle();
    if (isConnected) {
      long period = 3600L;
      long flex = 10L;
      String periodicTag = "periodic";

      // create a periodic task to pull stocks once every hour after the app has been opened. This
      // is so Widget data stays up to date.
      PeriodicTask periodicTask = new PeriodicTask.Builder().setService(StockTaskService.class)
          .setPeriod(period)
          .setFlex(flex)
          .setTag(periodicTag)
          .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
          .setRequiresCharging(false)
          .build();
      // Schedule task with tag "periodic." This ensure that only the stocks present in the DB
      // are updated.
      GcmNetworkManager.getInstance(this).schedule(periodicTask);
    }
  }

  @Override public void onResume() {
    super.onResume();
    LocalBroadcastManager.getInstance(this)
        .registerReceiver(receiver, new IntentFilter("my-event"));
    getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
  }

  @Override protected void onPause() {
    super.onPause();
    LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
  }

  public void networkToast() {
    Toast.makeText(mContext, getString(R.string.network_toast), Toast.LENGTH_SHORT).show();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.my_stocks, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_change_units) {
      // this is for changing stock changes from percent value to dollar value
      Utils.showPercent = !Utils.showPercent;
      this.getContentResolver().notifyChange(QuoteProvider.Quotes.CONTENT_URI, null);
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    // This narrows the return to only the stocks that are most current.
    return new CursorLoader(this, QuoteProvider.Quotes.CONTENT_URI, new String[] {
        QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE, QuoteColumns.PERCENT_CHANGE,
        QuoteColumns.CHANGE, QuoteColumns.ISUP, QuoteColumns.NAME
    }, QuoteColumns.ISCURRENT + " = ?", new String[] { "1" }, null);
  }

  @Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    mCursorAdapter.swapCursor(data);
    mCursor = data;
  }

  @Override public void onLoaderReset(Loader<Cursor> loader) {
    mCursorAdapter.swapCursor(null);
  }

  @Retention(RetentionPolicy.SOURCE) @IntDef({ STOCK_INVALID, STOCK_SERVER_DOWN, STOCK_OFFLINE })
  public @interface Stock {
  }
}
