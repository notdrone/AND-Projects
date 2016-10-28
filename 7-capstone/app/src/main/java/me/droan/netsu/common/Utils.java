package me.droan.netsu.common;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.TypedValue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Drone on 15/09/16.
 */

public class Utils {


    public static String convertEmailToUid(String email) {
        return email.replace('.', ',');
    }

    public static String convertUidToEmail(String uid) {
        return uid.replace(',', '.');
    }

    public static int getPeekValue(Context context, int valueInDp) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, resources.getDisplayMetrics());
    }

    public static String getEid() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            return convertEmailToUid(email);
        } else {
            return null;
        }
    }

    public static HashMap<String, Object> convertToMap(Object object) {
        return (HashMap<String, Object>) new ObjectMapper().convertValue(object, Map.class);
    }


    public static String generatePushId(DatabaseReference reference) {
        return reference.push().getKey();
    }

    public static String convertToSimpleTime(@NonNull long reverseTimeStamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        Date date = new Date(reverseTimeStamp * -1);
        return simpleDateFormat.format(date);
    }

    public static String convertToSimpleTimeDate(@NonNull long timeStamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a, dd MMM");
        Date date = new Date(timeStamp);
        return simpleDateFormat.format(date);
    }

    public static double convertC2F(double c) {
        return c * 9.0 / 5 + 32;
    }

    public static double convertF2C(double f) {
        return f * 5.0 / 9 - 32;
    }

    public static String getMedicineKey(String medicine) {
        return medicine.replace('.', ',').toLowerCase().trim();
    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString().trim();
    }

    public static long getCurrentTimePlusHours(int hours) {
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date()); // sets calendar time/date
        cal.add(Calendar.MINUTE, hours); // adds one hour
        Date time = cal.getTime();
        return time.getTime();
    }

    public static long getTimeNow() {
        long date = new Date().getTime();
        return date;
    }
}
