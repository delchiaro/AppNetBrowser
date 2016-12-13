package com.nagash.appwebbrowser.model.connection;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;

/**
 * Created by nagash on 31/08/16.
 */

// TODO: download cancel no working?
public class AppBundleDownloader {

    private final String TAG = "AppBundleDownloader";
    private final AppBundleDownloadHandler _handler;
    private final Activity _activity;


    private CompleteReceiver mDownloadCompleteReceiver;



    public AppBundleDownloader(Activity activity, AppBundleDownloadHandler handler) {
        this._handler = handler;
        this._activity = activity;
    }


    enum Status { IDLE, DOWNLOADING, CANCELLED, COMPLETED, NOT_HANDLED_ERROR }

    Status status = Status.IDLE;

    private Long mDownloadId = null;
    private String appID = null;
    private DownloadManager dm = null;




    public boolean cancelDownload() {
        if(isDownloading()) {
            status = Status.CANCELLED;
            dm.remove(mDownloadId);
            return true;
        }
        else return false;
    }

    public boolean isDownloading() {
        if(status == Status.DOWNLOADING)
            return true;
        else return false;
    }



    public boolean start(@NonNull String app_id )
    {
        if(status != Status.IDLE)
            return false;
        this.appID = app_id;
        dm = (DownloadManager) _activity.getSystemService(Context.DOWNLOAD_SERVICE);
        startDownload();
        return true;
    }


    private void startDownload() {

        status = Status.DOWNLOADING;

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(CentralConnection.getRemoteBundleURL(appID)));

        request.setDestinationUri(Uri.parse("file://" + CentralConnection.getCachedBundlePath(appID)));
        request.setDescription("WebApp Download");
        request.setTitle("WebApp Download");
        request.setVisibleInDownloadsUi(false);
        request.setNotificationVisibility(2);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);

        mDownloadCompleteReceiver = new CompleteReceiver();
        _activity.registerReceiver(mDownloadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        mDownloadId = dm.enqueue(request);
        manageDownloadProcess( );

        Log.i(TAG, "start download remote bundle");
    }




    /*check for timeout / errors and retry logic*/

    private static int RETRIES_MAX_NUMBER = 3; //nr of retries
    private static int alreadyRetried;
    private static boolean isEntered = false;

    private void manageDownloadProcess() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(DownloadManager.STATUS_PENDING | DownloadManager.STATUS_SUCCESSFUL | DownloadManager.STATUS_PAUSED | DownloadManager.STATUS_RUNNING | DownloadManager.STATUS_FAILED);

        final Cursor cursor = dm.query(query.setFilterById(this.mDownloadId));
        final Handler handler = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {

//                if(status != Status.DOWNLOADING)
//                    return;

                if (cursor.moveToFirst()) {
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    switch (status) {

                        /*I introdused 'isEntered' param to eliminate first response from this method
                          * I don't know why but I get STATUS_PENDING always on first run, so this is an ugly workaround*/
                        case DownloadManager.STATUS_PENDING: {
                            Log.d("status", "STATUS_PENDING - timeout");
                            if (isEntered) {
                                if (alreadyRetried < RETRIES_MAX_NUMBER) {
                                    alreadyRetried++;
                                    dm.remove(mDownloadId);
                                    startDownload();
                                    manageDownloadProcess();

                                }
                            } else {
                                isEntered = true;
                                manageDownloadProcess();
                            }
                            break;
                        }

                        case DownloadManager.STATUS_PAUSED: {
                            Log.d("status", "STATUS_PAUSED - error");
                            if (alreadyRetried < RETRIES_MAX_NUMBER) {
                                alreadyRetried++;
                                dm.remove(mDownloadId);
                                startDownload();
                            }
                            break;
                        }

                        case DownloadManager.STATUS_RUNNING: {
                            Log.d("status", "STATUS_RUNNING - good");
                            manageDownloadProcess();
                            break;
                        }

                        case DownloadManager.STATUS_SUCCESSFUL: {
                            Log.d("status", "STATUS_SUCCESSFUL - done");
                            break;
                        }

                        case DownloadManager.STATUS_FAILED: {
                            Log.d("status", "STATUS_FAILED - error");
                            if (alreadyRetried < RETRIES_MAX_NUMBER) {
                                alreadyRetried++;
                                dm.remove(mDownloadId);
                                startDownload();
                            }
                            break;
                        }
                    }
                }
            }
        };

        handler.postDelayed(r, 5000);//do this after 5 sec
    }



    private class CompleteReceiver extends BroadcastReceiver {
        @Override public void onReceive(Context context, Intent intent)
        {

            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if(completeDownloadId == mDownloadId && intent.getAction() == "android.intent.action.DOWNLOAD_COMPLETE")
            {
                String app_id = appID;
//                appID = null;
//                mDownloadId = null;
//                dm = null;

                if(status == Status.CANCELLED){
                    File file = new File(CentralConnection.getCachedBundlePath(app_id));
                    if (file != null && file.exists())
                        file.delete(); // delete a possible partial/corrupted download file
                    Log.i(TAG, "download canceled by the user");
                    _handler.onAppBundleDownloadCanceled(app_id);
                }

                else if(status == Status.DOWNLOADING)
                {
                    status = Status.COMPLETED;
                    File file = new File(CentralConnection.getCachedBundlePath(app_id));

                    if (file == null || !file.exists()) {
                        Log.i(TAG, "download error, check URL or network state");
                        _handler.onAppBundleDownloadFailed(app_id);
                    } else {
                        Log.i(TAG, "download success!");
                        _handler.onAppBundleDownloaded(app_id);
                    }
                }

                else status = Status.NOT_HANDLED_ERROR;

            }
        }
    }


}



