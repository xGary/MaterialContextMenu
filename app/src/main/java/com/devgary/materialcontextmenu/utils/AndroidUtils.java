package com.devgary.materialcontextmenu.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

import com.devgary.materialcontextmenu.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Gary on 3/10/2016.
 */
public class AndroidUtils {

    static long initialReceivedWifiBytes;
    static long initialTransferredWifiBytes;

    static long initialReceivedMobileBytes;
    static long initialTransferredMobileBytes;

    public static final int VIBRATION_DURATION_MINIMUM = 10;
    public static final int VIBRATION_DURATION_SHORT = 25;
    public static final int VIBRATION_DURATION_MEDIUM_SHORT = 50;
    public static final int VIBRATION_DURATION_MEDIUM = 75;
    public static final int VIBRATION_DURATION_MEDIUM_LONG = 100;
    public static final int VIBRATION_DURATION_LONG = 125;

    public static void initFragment(AppCompatActivity appCompatActivity, Fragment fragment, int viewId) {

        FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(viewId, fragment);
        fragmentTransaction.commit();
    }

    public static void replaceFragment(AppCompatActivity appCompatActivity, Fragment fragment, int viewId) {

        FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(viewId, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Checks to see if debugger is attached and enables waiting for debugger if it is
     */
    public static void waitForDebuggerIfAttached(){

        if (Debug.isDebuggerConnected()) Debug.waitForDebugger();
    }

    /**
     * Converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     *
     * @return float representing px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    /**
     * Converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     *
     * @return float representing dp equivalent to px value
     */
    public static float convertPixelToDp(float px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);
    }

    public static double convertPixelToSp(double px) {
        float scaledDensity = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }

    /**
     * Checks whether or not sdk level of device satisfies passed in requirement
     *
     * @param minimumSdk
     *
     * @return boolean indicating whether sdk requirement is satisfied
     */
    public static boolean greaterOrEqualSdk(int minimumSdk){

        return Build.VERSION.SDK_INT >= minimumSdk;
    }

    /**
     * Vibrates for {@value #VIBRATION_DURATION_SHORT} ms. Calls {@link #vibrate(Context, long)}
     *
     * @param context
     *
     * @see #vibrate(android.content.Context, long)
     */
    public static void vibrate(Context context){

        vibrate(context, VIBRATION_DURATION_SHORT);
    }

    /**
     * Vibrates for the passed in amount of time in ms.
     *
     * @param context
     * @param duration duration of vibration in milliseconds
     */
    public static void vibrate(Context context, long duration){

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(duration);
    }

    public static void displayNotification(Context context, String message){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(context.getApplicationContext().getResources().getString(R.string.app_name))
                .setContentText(message);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    public static void displayHeadsUpNotification(Context context, String message){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(context.getApplicationContext().getResources().getString(R.string.app_name))
                .setContentText(message)
                .setVibrate(new long[VIBRATION_DURATION_MINIMUM])
                .setPriority(Notification.PRIORITY_HIGH);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    public static boolean isWifiConnected(Context context){

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mWifi.isConnected();
    }

    /**
     * Logs (info) the amount of traffic used in KBs from when the method is called till
     * the end of the specified duration in ms.
     *
     * @param uid of the application to measure
     * @param durationInMsToMeasure
     */
    public static void logWifiTrafficUsed(final int uid, int durationInMsToMeasure){

        final long initialReceivedBytes = TrafficStats.getUidRxBytes(uid);
        final long initialTransferredBytes = TrafficStats.getUidTxBytes(uid);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                double kiloBytesReceived = (TrafficStats.getUidRxBytes(uid) - initialReceivedBytes) / 1000.0;
                double kiloBytesTransferred = (TrafficStats.getUidTxBytes(uid) - initialTransferredBytes) / 1000.0;

                Log.i("TrafficStats", "KBs Received: " + new DecimalFormat("0.#").format(kiloBytesReceived) +
                        ", KBs Transferred: " + new DecimalFormat("0.#").format(kiloBytesTransferred) +
                        ", Total = " + new DecimalFormat("0.#").format(kiloBytesReceived + kiloBytesTransferred));
            }
        }, durationInMsToMeasure);
    }

    /**
     * Logs (info) the amount of traffic used in KBs from when the method is called till
     * the end of the specified duration in ms.
     *
     * @param packageManager to determine application Uid
     * @param packageName to determine application Uid
     * @param durationInMsToMeasure
     *
     * @see #logWifiTrafficUsed(int, int)
     */
    public static void logWifiTrafficUsed(PackageManager packageManager, String packageName, int durationInMsToMeasure){

        try {
            int uid = packageManager.getApplicationInfo(packageName, 0).uid;

            logWifiTrafficUsed(uid, durationInMsToMeasure);
        }
        catch (Exception e){

        }
    }

    /**
     * Logs (info) the amount of traffic used in KBs from when the method is called till
     * the end of the specified duration of 10s
     *
     * @param packageManager to determine application Uid
     * @param packageName to determine application Uid
     *
     * @see #logWifiTrafficUsed(int, int)
     */
    public static void logWifiTrafficUsed(PackageManager packageManager, String packageName){

        try {
            int uid = packageManager.getApplicationInfo(packageName, 0).uid;

            logWifiTrafficUsed(uid);
        }
        catch (Exception e){

        }
    }

    /**
     * Logs (info) the amount of traffic used in KBs from when the method is called till
     * the end of the specified duration of 10s
     *
     * @param uid of the application to measure
     *
     * @see #logWifiTrafficUsed(int, int)
     */
    public static void logWifiTrafficUsed(int uid){

        logWifiTrafficUsed(uid, 10000);
    }


    public static void logWifiTrafficUsedBegin(final int uid){

        initialReceivedWifiBytes = TrafficStats.getUidRxBytes(uid);
        initialTransferredWifiBytes = TrafficStats.getUidTxBytes(uid);

        Log.i("TrafficStats", "Wifi network activity logging begin");
    }

    public static void logWifiTrafficUsedBegin(PackageManager packageManager, String packageName){

        try {
            int uid = packageManager.getApplicationInfo(packageName, 0).uid;

            logWifiTrafficUsedBegin(uid);
        }
        catch (Exception e){

        }
    }

    public static void logWifiTrafficUsedEnd(final int uid){

        double kiloBytesReceived = (TrafficStats.getUidRxBytes(uid) - initialReceivedWifiBytes) / 1000.0;
        double kiloBytesTransferred = (TrafficStats.getUidTxBytes(uid) - initialTransferredWifiBytes) / 1000.0;

        Log.i("TrafficStats", "Wifi KBs Received: " + new DecimalFormat("0.#").format(kiloBytesReceived) +
                ", KBs Transferred: " + new DecimalFormat("0.#").format(kiloBytesTransferred) +
                ", Total = " + new DecimalFormat("0.#").format(kiloBytesReceived + kiloBytesTransferred));
    }


    public static void logWifiTrafficUsedEnd(PackageManager packageManager, String packageName){

        try {
            int uid = packageManager.getApplicationInfo(packageName, 0).uid;

            logWifiTrafficUsedEnd(uid);
        }
        catch (Exception e){

        }
    }

    public static void logDeviceMobileTrafficUsedBegin(){

        initialReceivedMobileBytes = TrafficStats.getMobileRxBytes();
        initialTransferredMobileBytes = TrafficStats.getMobileTxBytes();

        Log.i("TrafficStats", "Device Mobile network activity logging begin");
    }

    public static void logDeviceMobileTrafficUsedEnd(){

        double kiloBytesReceived = (TrafficStats.getMobileRxBytes() - initialReceivedMobileBytes) / 1000.0;
        double kiloBytesTransferred = (TrafficStats.getMobileTxBytes() - initialTransferredMobileBytes) / 1000.0;

        Log.i("TrafficStats", "Device Mobile KBs Received: " + new DecimalFormat("0.#").format(kiloBytesReceived) +
                ", KBs Transferred: " + new DecimalFormat("0.#").format(kiloBytesTransferred) +
                ", Total = " + new DecimalFormat("0.#").format(kiloBytesReceived + kiloBytesTransferred));
    }

    public static String getVersionName(Context context){

        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        }
        catch (Exception e){
        }

        return "Unavailable";
    }

    public static int getDrawableIdByName(Context context, String name){

        Resources resources = context.getResources();
        return resources.getIdentifier(name, "drawable", context.getPackageName());
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void postDelayedRunnable(int delayMillis, Runnable runnable){

        Handler handler = new Handler();
        handler.postDelayed(runnable, delayMillis);
    }

    public static Currency getCurrency() {

        Currency currency;

        // TODO: [!!] Fix currency
        try {
            currency = Currency.getInstance(Locale.getDefault());
        }
        catch (IllegalArgumentException e){

            currency = Currency.getInstance(Locale.US);
        }
        return currency;
    }

    public static ArrayList<Currency> getAllCurrencies() {

        Set<Currency> currencies = new HashSet<>();
        Locale[] locales = Locale.getAvailableLocales();

        for(Locale loc : locales) {
            try {
                currencies.add(Currency.getInstance(loc));
            }
            catch(Exception e){

            }
        }

        return new ArrayList<>(currencies);
    }

    public static void goToPlayStorePage(Context context){

        String packageName = context.getPackageName();

        packageName = packageName.replace("_dev", "");

        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    public static void sendHtmlTextIntent(Context context, Spanned subject, Spanned text){

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/html");
        context.startActivity(Intent.createChooser(sendIntent, "Send Using ..."));
    }

    public static void sendSimpleTextIntent(Context context, String subject, String text){

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "Send Using ..."));
    }

    public static void sendEmailIntent(Context context, String subject, String body){

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(emailIntent, "Send Email ..."));
    }

    public static int resolveColorFromAttribute(Context context, int attribute){

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attribute, typedValue, true);

        return typedValue.data;
    }

    public static Drawable resolveDrawableFromAttribute(Context context, int attribute){

        int[] attrs = new int[]{attribute};

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs);

        Drawable drawableFromTheme = ta.getDrawable(0);

        ta.recycle();

        return drawableFromTheme;
    }

    public static boolean isPermissionGranted(Activity activity, String permission) {

        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Brings up request permission dialog. Use when context is an Activity.
     *
     * @see #requestPermissionsFromFragment(Fragment, String, int)
     *
     * @param activity
     * @param permission
     * @param requestCode
     */
    public static void requestPermissionFromActivity(Activity activity, String permission, int requestCode) {

        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
    }

    /**
     * Brings up request permission dialog. Use when context is an Fragment.
     *
     * @see #requestPermissionFromActivity(Activity, String, int)
     *
     * @param fragment
     * @param permission
     * @param requestCode
     */
    public static void requestPermissionsFromFragment(Fragment fragment, String permission, int requestCode) {

        fragment.requestPermissions(new String[]{permission}, requestCode);
    }

    public static Typeface createTypeFaceFromAsset(Context context, String fontFileName) {

        try{
            return Typeface.createFromAsset(context.getAssets(), "fonts/" + fontFileName);
        }
        catch (RuntimeException e){

            try{
                return Typeface.createFromAsset(context.getAssets(), "fonts/" + fontFileName + ".ttf");
            }
            catch (RuntimeException e2){

                try {
                    return Typeface.createFromAsset(context.getAssets(), fontFileName);
                }
                catch (RuntimeException e3){
                    return Typeface.createFromAsset(context.getAssets(), fontFileName + ".ttf");
                }
            }
        }
    }

    public static void setStatusBarColor(Window window, int color){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(color);
        }
    }

    public static void hideStatusBar(Window window){

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void hideStatusBar(Activity activity){

        hideStatusBar(activity.getWindow());
    }
}
