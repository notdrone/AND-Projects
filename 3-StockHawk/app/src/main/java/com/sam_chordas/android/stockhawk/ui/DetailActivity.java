package com.sam_chordas.android.stockhawk.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteDetailColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "DetailActivity";
    private static final int DAY_TODAY = 469;
    private static final int DAY_5 = 916;
    private static final int MONTH_1 = 698;
    private static final int MONTH_3 = 237;
    private static final int YEAR_1 = 13;
    private static final int YEAR_5 = 473;
    private static final String EXTRA_SYMBOL = "419";
    private static final int CURSOR_LOADER_ID = 407;
    private static final int CURSOR_LOADER_DETAIL_ID = 409;
    Context context;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.chart)
    LineChart chart;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.symbol)
    TextView symbol;
    @BindView(R.id.bid_price)
    TextView bid_price;
    @BindView(R.id.change)
    TextView change;

    String dayToday, day5, month1, month3, year1, year5;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private OkHttpClient client = new OkHttpClient();

    public static Intent putIntent(Context c, String symbol) {
        Intent i = new Intent(c, DetailActivity.class);
        i.putExtra(EXTRA_SYMBOL, symbol);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        context = this;
        ButterKnife.bind(this);
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        getLoaderManager().initLoader(CURSOR_LOADER_DETAIL_ID, null, this);
        initToolbar();
        getDates();
        String sortOrder = QuoteColumns._ID + " ASC LIMIT 1";
        Cursor c = getContentResolver().query(QuoteProvider.QuotesHistory.CONTENT_URI, new String[]{QuoteDetailColumns.DATE}, QuoteDetailColumns.SYMBOL + " = \"" + getIntent().getStringExtra(EXTRA_SYMBOL) + "\"", null, sortOrder);
        String startDate = "";
        if (c.getCount() != 0) {
            c.moveToFirst();
            String dateStr = c.getString(c.getColumnIndex(QuoteDetailColumns.DATE));
            Log.d(TAG, "onCreate: " + dateStr);
            if (!dateStr.equals(dayToday)) {
                startDate = dateStr;
            } else {
                startDate = "";
            }

        } else {
            startDate = month3;
        }
        if (!startDate.equals("")) {
            String url = getUrl(getIntent().getStringExtra(EXTRA_SYMBOL), startDate);
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String jsonResponse = response.body().string();
                        Utils.saveHistoryToDB(context, jsonResponse);
                    } else {
                    }
                }
            });
        }

    }

    @SuppressWarnings("ConstantConditions")
    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private String getUrl(String stock, String startDate) {
        String prefix = "http://query.yahooapis.com/v1/public/yql?q=";
        String query = "select * from yahoo.finance.historicaldata where symbol = \""
                + stock
                + "\" and startDate = \""
                + startDate
                + "\" and endDate = \""
                + dayToday
                + "\"";
        String suffix = "&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&format=json&";
        String s = prefix + query.replaceAll(" ", "%20") + suffix;
        return s;
    }

    private void getDates() {
        dayToday = getSimpleDate(DAY_TODAY);
        day5 = getSimpleDate(DAY_5);
        month1 = getSimpleDate(MONTH_1);
        month3 = getSimpleDate(MONTH_3);
        year1 = getSimpleDate(YEAR_1);
        year5 = getSimpleDate(YEAR_5);
    }

    private String getSimpleDate(@DayType int dayType) {
        Calendar calendar = Calendar.getInstance();
        switch (dayType) {
            case DAY_TODAY:
                break;
            case DAY_5:
                calendar.add(Calendar.DATE, -5);
                break;
            case MONTH_1:
                calendar.add(Calendar.MONTH, -1);
                break;
            case MONTH_3:
                calendar.add(Calendar.MONTH, -3);
                break;
            case YEAR_1:
                calendar.add(Calendar.YEAR, -1);
                break;
            case YEAR_5:
                calendar.add(Calendar.YEAR, -5);
                break;
        }
        return format.format(calendar.getTime());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
        getLoaderManager().restartLoader(CURSOR_LOADER_DETAIL_ID, null, this);
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == CURSOR_LOADER_ID) {
            return new CursorLoader(context, QuoteProvider.Quotes.CONTENT_URI, new String[]{
                    QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.NAME, QuoteColumns.CHANGE, QuoteColumns.BIDPRICE
            }, QuoteColumns.SYMBOL + "= \"" + getIntent().getStringExtra(EXTRA_SYMBOL) + "\"", null, null);
        } else if (id == CURSOR_LOADER_DETAIL_ID) {
            return new CursorLoader(context, QuoteProvider.QuotesHistory.CONTENT_URI, new String[]{
                    QuoteDetailColumns.DATE, QuoteDetailColumns.BID},
                    QuoteDetailColumns.SYMBOL + " = \"" + getIntent().getStringExtra(EXTRA_SYMBOL) + "\"", null, null);

        }

        return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        if (loader.getId() == CURSOR_LOADER_ID && cursor != null) {
            updateDetailToolbar(cursor);
        } else if (CURSOR_LOADER_DETAIL_ID == loader.getId() & cursor != null) {
            Log.e(TAG, "onLoadFinished:" + cursor.getColumnCount());
            updateGraph(cursor);
        }
    }

    private void updateGraph(Cursor cursor) {
        List<String> xVals = new ArrayList<>();
        List<ILineDataSet> yVals = new ArrayList<>();
        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();
        while (cursor.moveToNext()) {
            float value = (float) (cursor.getFloat(cursor.getColumnIndex(QuoteDetailColumns.BID)));
            String date = cursor.getString(cursor.getColumnIndex(QuoteDetailColumns.DATE));
            Entry entry = new Entry(value, cursor.getPosition()); // 0 == quarter 1
            valsComp1.add(entry);
            xVals.add(date);

        }
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        LineDataSet setComp1 = new LineDataSet(valsComp1, "Company 1");
        dataSets.add(setComp1);
        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chart.invalidate();
            }
        });
    }

    private void updateDetailToolbar(Cursor cursor) {
        cursor.moveToLast();
        String nameStr = cursor.getString(cursor.getColumnIndex(QuoteColumns.NAME));
        String symbolStr = cursor.getString(cursor.getColumnIndex(QuoteColumns.SYMBOL));
        String bidStr = cursor.getString(cursor.getColumnIndex(QuoteColumns.BIDPRICE));
        String changeStr = cursor.getString(cursor.getColumnIndex(QuoteColumns.CHANGE));
        name.setText(nameStr);
        symbol.setText(symbolStr);
        bid_price.setText(bidStr);
        change.setText(changeStr);

    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DAY_TODAY, DAY_5, MONTH_1, MONTH_3, YEAR_1, YEAR_5})
    public @interface DayType {
    }
}
