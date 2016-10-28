package com.sam_chordas.android.stockhawk.rest;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteDetailColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sam_chordas on 10/8/15.
 */
public class Utils {
  private static final String TAG = "Utils";
  public static boolean showPercent = true;
  private static String LOG_TAG = Utils.class.getSimpleName();

  public static ArrayList quoteJsonToContentVals(Context context, String JSON) {
    ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();
    JSONObject jsonObject = null;
    JSONArray resultsArray = null;
    try {
      Log.e(TAG, "quoteJsonToContentVals: " + JSON);
      jsonObject = new JSONObject(JSON);
      if (jsonObject != null && jsonObject.length() != 0) {
        jsonObject = jsonObject.getJSONObject("query");
        int count = Integer.parseInt(jsonObject.getString("count"));
        if (count == 1) {
          jsonObject = jsonObject.getJSONObject("results").getJSONObject("quote");
          //String nullChecker=jsonObject.getz
          if (!jsonObject.getString("Bid").equals("null")) {
            Log.e(TAG, "quoteJsonToContentVals:" + jsonObject.get("Bid"));
            batchOperations.add(buildBatchOperation(jsonObject));
          } else {
            sendMessage("invaid symbol", context);
          }
        } else {
          resultsArray = jsonObject.getJSONObject("results").getJSONArray("quote");

          if (resultsArray != null && resultsArray.length() != 0) {
            for (int i = 0; i < resultsArray.length(); i++) {
              jsonObject = resultsArray.getJSONObject(i);
              batchOperations.add(buildBatchOperation(jsonObject));
            }
          }
        }
      }
    } catch (JSONException e) {
      Log.e(LOG_TAG, "String to JSON failed: " + e);
    }
    return batchOperations;
  }

  private static void sendMessage(String message, Context context) {
    Intent i = new Intent("my-event");
    i.putExtra("message", message);
    LocalBroadcastManager.getInstance(context).sendBroadcast(i);
  }

  public static String truncateBidPrice(String bidPrice) {
    bidPrice = String.format("%.2f", Float.parseFloat(bidPrice));
    return bidPrice;
  }

  public static String truncateChange(String change, boolean isPercentChange) {
    String weight = change.substring(0, 1);
    String ampersand = "";
    if (isPercentChange) {
      ampersand = change.substring(change.length() - 1, change.length());
      change = change.substring(0, change.length() - 1);
    }
    change = change.substring(1, change.length());
    double round = (double) Math.round(Double.parseDouble(change) * 100) / 100;
    change = String.format("%.2f", round);
    StringBuffer changeBuffer = new StringBuffer(change);
    changeBuffer.insert(0, weight);
    changeBuffer.append(ampersand);
    change = changeBuffer.toString();
    return change;
  }

  public static ContentProviderOperation buildBatchOperation(JSONObject jsonObject) {
    ContentProviderOperation.Builder builder =
        ContentProviderOperation.newInsert(QuoteProvider.Quotes.CONTENT_URI);
    try {
      String change = jsonObject.getString("Change");
      builder.withValue(QuoteColumns.SYMBOL, jsonObject.getString("symbol"));
      builder.withValue(QuoteColumns.BIDPRICE, truncateBidPrice(jsonObject.getString("Bid")));
      builder.withValue(QuoteColumns.PERCENT_CHANGE,
          truncateChange(jsonObject.getString("ChangeinPercent"), true));
      builder.withValue(QuoteColumns.CHANGE, truncateChange(change, false));
      builder.withValue(QuoteColumns.ISCURRENT, 1);
      builder.withValue(QuoteColumns.NAME, jsonObject.getString("Name"));
      Log.e(TAG, "buildBatchOperation: " + jsonObject.getString("Name"));
      if (change.charAt(0) == '-') {
        builder.withValue(QuoteColumns.ISUP, 0);
      } else {
        builder.withValue(QuoteColumns.ISUP, 1);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return builder.build();
  }

  public static void saveHistoryToDB(Context context, String jsonResponse) {
    try {
      ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();
      JSONArray quotes = getResults(jsonResponse);
      for (int i = 0; i < quotes.length(); i++) {
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                QuoteProvider.QuotesHistory.CONTENT_URI
        );
        float value = (float) (quotes.getJSONObject(i).getDouble("Close"));
        String date = quotes.getJSONObject(i).getString("Date");
        String symbol = quotes.getJSONObject(i).getString("Symbol");

        builder.withValue(QuoteDetailColumns.SYMBOL, symbol);
        builder.withValue(QuoteDetailColumns.DATE, date);
        builder.withValue(QuoteDetailColumns.BID, value);
        batchOperations.add(builder.build());
      }
      context.getContentResolver().applyBatch(QuoteProvider.AUTHORITY, batchOperations);

    } catch (Exception e) {
      Log.d(TAG, "saveHistoryToDB: " + e.getMessage());
    }

  }

  private static JSONArray getResults(String jsonResponse) throws JSONException {
    JSONObject jsonObject = new JSONObject(jsonResponse);
    ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
    JSONObject query = jsonObject.getJSONObject("query");
    JSONObject result = query.getJSONObject("results");
    JSONArray quote = result.getJSONArray("quote");
    return quote;
  }
}
