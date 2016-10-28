package com.sam_chordas.android.stockhawk;

import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViewsService;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import java.util.ArrayList;

/**
 * Created by drone on 17-05-2016.
 */
public class WidegtService extends RemoteViewsService {
  private static final String TAG = "WidegtService";
  @Override public RemoteViewsFactory onGetViewFactory(Intent intent) {
    //return remote view factory
    ArrayList list= new ArrayList();
    Cursor cursor = this.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI, new String[] {
        QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE, QuoteColumns.PERCENT_CHANGE,
        QuoteColumns.CHANGE, QuoteColumns.ISUP, QuoteColumns.NAME
    }, QuoteColumns.ISCURRENT + " = ?", new String[] { "1" }, null);
    return new WidgetDataProvider(this, intent,cursor);
  }
}
