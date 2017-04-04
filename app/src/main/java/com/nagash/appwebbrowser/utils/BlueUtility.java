package com.nagash.appwebbrowser.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.nagash.appwebbrowser.R;


/**
 * Created by nagash on 27/12/16.
 */

public class BlueUtility {

    private static final String MSG_BLE_NOT_SUPPORTED = "accessRequester is null, BLE not supported";


    private BlueUtility(AccessRequester accessRequester){
        this.accessRequester = accessRequester;
    }

    private static void init(BluetoothAdapter adapter) {
        BlueUtility.accessRequester = new AccessRequester(adapter);
    }

    private static AccessRequester accessRequester = new AccessRequester(BluetoothAdapter.getDefaultAdapter());

    /**
     * Checks if Bluetooth Low Energy is enabled in the current Android version
     *
     * @return boolean
     */
    public static boolean isBleSupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    /**
     * Checks if Bluetooth is enabled
     *
     * @return boolean true if enabled
     */
    public static boolean isBluetoothEnabled() {
        checkNotNull(accessRequester, MSG_BLE_NOT_SUPPORTED);
        return accessRequester.isBluetoothEnabled();
    }

    /**
     * Checks if location provider is enabled
     *
     * @param context current Context
     * @return boolean true if enabled
     */
    public static boolean isLocationEnabled(Context context) {
        checkNotNull(accessRequester, MSG_BLE_NOT_SUPPORTED);
        return accessRequester.isLocationEnabled(context);
    }

    /**
     * Starts intent requesting Bluetooth connection, which can be enabled by user
     * if it's not enabled already
     *
     * @param activity current Activity
     */
    public static void requestBluetoothAccess(Activity activity) {
        checkNotNull(accessRequester, MSG_BLE_NOT_SUPPORTED);
        accessRequester.requestBluetoothAccess(activity);
    }

    /**
     * Checks is device has installed at least Lollipop Android version
     *
     * @return true if has installed at least Lollipop and false in opposite case
     */
    public static  boolean isAtLeastAndroidLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


    private static void checkNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }






    /**
     * Function to show settings alert dialog
     * TODO: use this method http://stackoverflow.com/questions/33251373/turn-on-location-services-without-navigating-to-settings-page
     * */
    public static void requestGps(final Context mContext){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("Location is disabled");

        // Setting Dialog Message
        alertDialog.setMessage("To use this application location must be enabled. " +
                               "Do you want to open location settings to enable Location service?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }






    private static String dywtols = "Do you want to open Location settings to enable location services? Without it beacons will not be detected.";

    public static class AccessRequester {
        private static final String EMPTY_STRING = "";
        private final BluetoothAdapter bluetoothAdapter;

        public AccessRequester(BluetoothAdapter bluetoothAdapter) {
            this.bluetoothAdapter = bluetoothAdapter;
        }

        public boolean isBluetoothEnabled() {
            return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
        }

        @SuppressWarnings("deprecation") public boolean isLocationEnabled(Context context) {
            String name = Settings.Secure.LOCATION_PROVIDERS_ALLOWED;
            ContentResolver contentResolver = context.getContentResolver();
            String providers = Settings.Secure.getString(contentResolver, name);
            return providers != null && !providers.equals(EMPTY_STRING);
        }

        public void requestBluetoothAccess(Activity activity) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(intent, Activity.RESULT_FIRST_USER);
        }

        public void requestLocationAccess(final Activity activity) {
            buildLocationAccessDialog(activity, new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivity(intent);
                }
            }).show();
        }

        private AlertDialog.Builder buildLocationAccessDialog(Activity activity,
                                                              DialogInterface.OnClickListener onOkClickListener) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(R.string.requesting_location_access);
//    builder.setMessage(R.string.do_you_want_to_open_location_settings);
            builder.setMessage(dywtols);
            builder.setPositiveButton(android.R.string.ok, onOkClickListener);
            builder.setNegativeButton(android.R.string.no, null);
            builder.setCancelable(true);
            return builder;
        }
    }
}
