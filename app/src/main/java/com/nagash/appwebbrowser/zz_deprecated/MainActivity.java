package com.nagash.appwebbrowser.zz_deprecated;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nagash.appwebbrowser.model.connection.AppListDownloadHandler;
import com.nagash.appwebbrowser.R;
import com.nagash.appwebbrowser.model.connection.CentralConnection;
import com.nagash.appwebbrowser.model.webapp.WebApp;

import java.util.ArrayList;
import java.util.List;


//public class MainActivity extends Activity {
public class MainActivity extends Activity implements AppListDownloadHandler {

    private static Context context;
    public static Context getContext(){return context;};

    public static final String LOCAL_USB_DEBUG_IP = "10.0.0.1";

    public static final String IP = "172.19.133.47";
    //public static final String IP = LOCAL_USB_DEBUG_IP;


    public static final String CENTRAL_LINK_SERVER = "http://" + IP + ":5000/list";
    public static final String CENTRAL_REPO_SERVER = "http://" + IP + ":80/";




    ProgressBar progressBarLinkServer;
    Button btnConnect;
    TextView tvAppList;

    private void initViews()
    {
        progressBarLinkServer = (ProgressBar) findViewById(R.id.progressBarLinkServer);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        tvAppList = (TextView) findViewById(R.id.tvAppList);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zz_deprecated_activity_main);
        initViews();
        context = this;

        connectToLinkServer();
    }

    private void connectToLinkServer() {
        tvAppList.setText(R.string.server_list_connecting);
        btnConnect.setVisibility(View.GONE);
        progressBarLinkServer.setVisibility(View.VISIBLE);
        try {
            CentralConnection.instance().startAppListDownload(this);
        } catch (CentralConnection.DoubleConnectionException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onAppListDownloaded(List<WebApp> webAppList) {


        progressBarLinkServer.setVisibility(View.GONE);
        if(webAppList != null && webAppList.isEmpty() == false)
        {

            tvAppList.setText(R.string.server_list_connected);
            // Construct the data source
            ArrayList<WebApp> arrayOfUsers = new ArrayList<WebApp>(webAppList);

            // Create the adapter to convert the array to views
            UsersAdapter adapter = new UsersAdapter(getBaseContext(), arrayOfUsers);

            // Attach the adapter to a ListView
            ListView listView = (ListView) findViewById(R.id.app_listView);
            listView.setAdapter(adapter);
        }
        else
        {
            tvAppList.setText(R.string.server_list_not_available);
            btnConnect.setVisibility(View.VISIBLE);
        }
    }




    public void retryConnection(View view)    {
        connectToLinkServer();
    }

    public static void startAppActivity(Context context, String JS_APP_ID, String JS_BUNDLE_REMOTE_URL)
    {
        Intent intent = new Intent(context, WebAppLauncherActivity.class);
        intent.putExtra(WebAppLauncherActivity.KEY_JS_APP_NAME, JS_APP_ID);
        intent.putExtra(WebAppLauncherActivity.KEY_JS_BUNDLE_REMOTE_URL, JS_BUNDLE_REMOTE_URL);
        intent.putExtra(WebAppLauncherActivity.KEY_JS_BUNDLE_REMOTE_VERSION_CODE, 1); //mocking version code
        context.startActivity(intent);
    }


    public void openScrollingActivity(View view)
    {
//        Intent intent = new Intent(context, ScrollingActivity.class);
//        context.startActivity(intent);
    }

    public void openTabbedActivity(View view)
    {
        Intent intent = new Intent(context, com.nagash.appwebbrowser.controller.MainActivity.class);
        context.startActivity(intent);
    }


}




class UsersAdapter extends ArrayAdapter<WebApp> {

    public UsersAdapter(Context context, ArrayList<WebApp> users) {
        super(context, 0, users);
    }



    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position]
        final WebApp app = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.app_list_item, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.app_name);
        Button btnApp = (Button) convertView.findViewById(R.id.app_button);

        // Populate the data into the template view using the data object
        tvName.setText(app.getName());
        btnApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.nagash.appwebbrowser.zz_deprecated.MainActivity.startAppActivity(com.nagash.appwebbrowser.zz_deprecated.MainActivity.getContext(), app.getId(), com.nagash.appwebbrowser.zz_deprecated.MainActivity.CENTRAL_REPO_SERVER );
            }
        });

        // Return the completed view to render on screen
        return convertView;

    }

}







//
//public class MainActivity extends Activity implements DefaultHardwareBackBtnHandler {
//    private ReactRootView mReactRootView;
//    private ReactInstanceManager mReactInstanceManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mReactRootView = new ReactRootView(this);
//
//        ReactInstanceManager.Builder builder = ReactInstanceManager.builder();
//
//
//        builder.setApplication(getApplication());
//        //builder.setBundleAssetName("index.android.bundle");
//        //builder.setJSBundleFile("/sdcard/AndroidTestBundle.js");
//       // builder.setJSBundleFile("https://raw.githubusercontent.com/fengjundev/React-Native-Remote-Update/master/remote/index.android.bundle");
//        builder.setJSBundleFile(Environment.getExternalStorageDirectory().toString() + File.separator + "index.android.bundle");
//        //builder.setJSBundleFile("http://192.168.0.226:8081/index.android.bundle?platform=android&dev=true&hot=true&minify=false");
//        //http://192.168.0.226:8081/index.android.bundle?platform=android&dev=true&hot=true&minify=false
//        //http://localhost:8081/index.android.bundle?platform=android
//        builder.setJSMainModuleName("index.android");
//        builder.addPackage(new MainReactPackage());
//        //builder.setUseDeveloperSupport(BuildConfig.DEBUG);
//        builder.setUseDeveloperSupport(false);
//        builder.setInitialLifecycleState(LifecycleState.RESUMED);
//
//        mReactInstanceManager = builder.build();
//
////        try {
////            Field field = mReactInstanceManager.getClass().getDeclaredField("mSourceUrl");
////            field.setAccessible(true);
////            String str1 = "miourl1";
////            String str2 = "miourl2";
////
////            field.set(str1,str2);
////            //http://192.168.0.228:8081/index.android.bundle?platform=android&dev=true&hot=true&minify=false
////
////        } catch (NoSuchFieldException e) {
////            e.printStackTrace();
////        } catch (IllegalAccessException e) {
////            e.printStackTrace();
////        }
//
//        mReactRootView.startReactApplication(mReactInstanceManager, "HelloWorld", null);
//
//        setContentView(mReactRootView);
//    }
//
//    @Override public void invokeDefaultOnBackPressed() {
//        super.onBackPressed();
//    }
//
//
//
//
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mReactInstanceManager != null)
//        {
//            mReactInstanceManager.onHostPause();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mReactInstanceManager != null)
//        {
//            mReactInstanceManager.onHostResume(this, this);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mReactInstanceManager != null)
//        {
//            mReactInstanceManager.onHostDestroy();
//        }
//    }
//
//
//    @Override
//    public void onBackPressed()
//    {
//        if (mReactInstanceManager != null)
//        {
//            mReactInstanceManager.onBackPressed();
//        }
//        else
//        {
//            super.onBackPressed();
//        }
//    }
//
//
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_MENU && mReactInstanceManager != null)
//        {
//            mReactInstanceManager.showDevOptionsDialog();
//            return true;
//        }
//        return super.onKeyUp(keyCode, event);
//    }
//}