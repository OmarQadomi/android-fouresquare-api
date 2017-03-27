package com.example.myplacesapp.myplacesapp.SharedClasses;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myplacesapp.myplacesapp.R;

/**
 * Created by 1234485 on 3/25/2017.
 */

public class ControllerFunctions {
    public static String getPreferences(Context context,String keyName)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(keyName, null);
    }
    public static void persistPreferences(Context context,String key,String value)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static String getCurrentDate()
    {
        java.util.Calendar calendar= java.util.Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(calendar.getTime());
    }
    public void glideShowImageInternal(Context mContext, Uri urlImage, ImageView img) {
        Glide.with(mContext)
                .load(urlImage)
                .error(R.mipmap.ic_launcher)
                .dontTransform()
                .dontAnimate()
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img);
    }
}
