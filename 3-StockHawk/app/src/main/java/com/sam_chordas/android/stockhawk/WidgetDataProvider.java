package com.sam_chordas.android.stockhawk;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by drone on 17-05-2016.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
  List<String> collection = new ArrayList<>();
  private Context context;
  private Intent intent;
  private Cursor mCursor;
  private static final String TAG = "WidgetDataProvider";

  public WidgetDataProvider(Context context, Intent intent, Cursor cursor) {
    this.context = context;
    this.intent = intent;
    mCursor = cursor;
  }

  @Override public void onCreate() {
  }

  @Override public void onDataSetChanged() {
  }

  @Override public void onDestroy() {
    mCursor.close();
  }

  @Override public int getCount() {
    Log.e(TAG, "getCount: " + mCursor.getCount());
    return mCursor.getCount();
  }

  @Override public RemoteViews getViewAt(int position) {
    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
    mCursor.moveToPosition(position);
    remoteViews.setTextViewText(R.id.symbol,
        mCursor.getString(mCursor.getColumnIndex(QuoteColumns.SYMBOL)));
    remoteViews.setTextViewText(R.id.change,
        mCursor.getString(mCursor.getColumnIndex(QuoteColumns.CHANGE)));
    Log.e(TAG, "getViewAt: " + mCursor.getString(mCursor.getColumnIndex(QuoteColumns.SYMBOL)));
    return remoteViews;
  }

  @Override public RemoteViews getLoadingView() {
    return null;
  }

  @Override public int getViewTypeCount() {
    return 1;
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public boolean hasStableIds() {
    return true;
  }
}
