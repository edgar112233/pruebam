// ToastModule.java

package com.pruebam;

import android.widget.Toast;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.Arguments;

import android.graphics.drawable.Drawable;

import java.io.File;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import androidx.annotation.RequiresApi;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Process;
import android.content.pm.PackageInfo;

import com.pruebam.Utility;
import android.app.ActivityManager;

import android.content.SharedPreferences;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.app.Service;
import android.provider.Settings;
import android.util.Log;
import android.os.Build;
import android.net.Uri;
import java.lang.Runnable;

import android.os.Handler;
import java.lang.Thread;
import android.os.Message;
import android.os.Looper;

public class ToastModule extends ReactContextBaseJavaModule {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    boolean mLock=false;
    List<String> lockedApp=new ArrayList<String>();
    Map<String, UsageStats> map;
    UsageStatsManager mUsageStatsManager;

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";
    private static final String E_LAYOUT_ERROR = "E_LAYOUT_ERROR";
    PackageManager pm;
    ActivityManager am;
    ReactApplicationContext reactContext;

    static List<TheApp> theApp=new ArrayList<>();  

    public ToastModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        pm = reactContext.getPackageManager();
        am = (ActivityManager) reactContext.getSystemService(reactContext.ACTIVITY_SERVICE);

        sharedpreferences = (SharedPreferences) reactContext.getSharedPreferences("Locked apps list", reactContext.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        //reactContext.stopService(new Intent(reactContext.getApplicationContext(), CurrentActivityService.class));//stop any earlier services
        mUsageStatsManager = (UsageStatsManager) reactContext.getSystemService(Service.USAGE_STATS_SERVICE);
        
        //showToast("HOLA");

        Thread thread = new Thread(){
            public void run(){
                Looper.prepare();//Call looper.prepare()
           
                Handler mHandler = new Handler() {
                    public void handleMessage(Message msg) {
                        Toast.makeText(reactContext, "Finco is Daddy", Toast.LENGTH_LONG);
                    }
                };
           
                Looper.loop();
            }
        };
        thread.start();

        //Toast.makeText(this.reactContext,"TOAST!!!!",Toast.LENGTH_LONG);
        mLock=sharedpreferences.getBoolean("master lock state",true);
        
        List<PackageInfo> pList = pm.getInstalledPackages(0);
        TheApp aApp=new TheApp();

        for (int i = 0; i < pList.size(); i++) {
            PackageInfo packageInfo = pList.get(i);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                aApp=new TheApp();
                aApp.setPackageName(packageInfo.packageName);
                aApp.setName(((String) packageInfo.applicationInfo.loadLabel(pm)).trim());
                theApp.add(aApp);
            }
        }

        requestUsageStatsPermission();

    }

    public void showToast(final String toast){
        //getActivity().runOnUiThread(() -> Toast.makeText(this.reactContext, toast, Toast.LENGTH_SHORT).show());
        Toast.makeText(this.reactContext, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getName() {
        return "ToastModule";
    }

    public void requestUsageStatsPermission(){
        Intent i = new Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS);
        //startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        //Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + this.reactContext.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        reactContext.startActivity(i);
    }
    @ReactMethod 
    public void BlockApplication(String pkgName, Promise promise){
        Log.e("Block app", "");
        int n = 0;
        for(TheApp app: theApp){
            if(app.getPackageName().equals(pkgName)){
                app.setLocked(true);
                n = 1;
            }
        }
        if(n == 1){
            promise.resolve(1);
        }
        else{
            promise.resolve(0);
        }
    }

    @ReactMethod 
    public void showBlockApplications(Promise promise){
        WritableArray list = Arguments.createArray();
        for(TheApp app: theApp){
            if(app.isLocked()==true){
                WritableMap appInfo = Arguments.createMap();
                appInfo.putString("AppName", app.getName());
                list.pushMap(appInfo);

            }
        }
        promise.resolve(list);
    }

    @ReactMethod 
    public void DesBlockApplication(String pkgName){
        WritableArray list = Arguments.createArray();
        for(TheApp app: theApp){
            if(app.getPackageName().equals(pkgName)){
                app.setLocked(false);
            }
        }
    }

    public int saveList(List<TheApp> list)
    {

        int q=1;
        for(TheApp app: list)
        {
            editor.putBoolean(app.getPackageName(),app.isLocked());
            if (app.isLocked())
            {
                editor.putString(""+q,app.getPackageName());
                        q++;
            }
        }
        editor.putInt("total locked",q-1);
        editor.putBoolean("master lock state", true);

        editor.commit();
        //Log.d("Saving..........","Saved");
        return q;
    }

    @ReactMethod
    public void runService(){
        this.reactContext.startService(new Intent(this.reactContext, MyService.class));  
    }

    @ReactMethod
    public void stopService(){
        this.reactContext.stopService(new Intent(this.reactContext, MyService.class));  
    }

    @ReactMethod
    public void RunBackground(Promise promise){
        //Log.d("Background",null);
        
        int a = saveList(theApp);
        promise.resolve(a);

        //Intent i=new Intent(this.reactContext,FullscreenActivity.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //this.reactContext.startActivity(i);


        this.reactContext.stopService(new Intent(this.reactContext,CurrentActivityService.class));
        this.reactContext.startService(new Intent(this.reactContext,CurrentActivityService.class));
    }
    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }

    /*@RequiresApi(api = Build.VERSION_CODES.M)
    public void checkDrawOverlayPermission() {

        // Checks if app already has permission to draw overlays
        if (!Settings.canDrawOverlays(this.reactContext)) {

            // If not, form up an Intent to launch the permission request
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + this.reactContext.getPackageName()));

            // Launch Intent, with the supplied request code
            this.reactContext.startActivity(intent, 5675);
        }
    }*/
    @ReactMethod
    public void muerte(Promise promise) {
        int puid;
        String packageName = "com.spotify.music";
        try {

            List<ActivityManager.RunningAppProcessInfo> activityes = am.getRunningAppProcesses();
            WritableArray list = Arguments.createArray();
            for (int iCnt = 0; iCnt < activityes.size(); iCnt++) {
                WritableMap appInfo = Arguments.createMap();
                // Log.d("APP: ", iCnt +" "+ activityes.get(iCnt).processName);
                appInfo.putString("name", activityes.get(iCnt).processName);
                appInfo.putInt("pid", activityes.get(iCnt).pid);
                /*
                 * if (activityes.get(iCnt).processName.contains(packageName)){
                 * android.os.Process.sendSignal(activityes.get(iCnt).pid,
                 * android.os.Process.SIGNAL_KILL);
                 * android.os.Process.killProcess(activityes.get(iCnt).pid);
                 * //manager.killBackgroundProcesses("com.android.email");
                 * 
                 * //manager.restartPackage("com.android.email");
                 * promise.resolve(1);
                 * //System.out.println("Inside if");
                 * }
                 */
                list.pushMap(appInfo);

            }
            promise.resolve(list);

        } catch (Error e) {
            promise.reject(e);
        }
        // killBackgroundProcesses (String packageName)
    }

    @ReactMethod
    public void show(String message, int duration) {

        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<ApplicationInfo> installedApps = new ArrayList<ApplicationInfo>();

        for (ApplicationInfo app : packages) {
            // checks for flags; if flagged, check if updated system app
            if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                installedApps.add(app); // Agregar Apps instaladas
                // it's a system app, not interested
            } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                // Discard this one
                // in this case, it should be a user-installed app
                installedApps.add(app);
            }

            // else if (app.flags & ApplicationInfo.)
            else {
                installedApps.add(app);
            }
        }

        /*
         * List<String> appList=new ArrayList<>();
         * 
         * /*for(ApplicationInfo i : installedApps){
         * appList.add( (String) i.loadLabel(pm));
         * }
         */

        // System.out.println(installedApps);
        Toast.makeText(getReactApplicationContext(), installedApps.toString(), duration).show();

    }

    @ReactMethod
    public void pcallback(
            String name,
            String location,
            Callback errorCallback,
            Callback successCallback) {

        try {
            int x = 15;
            int y = 13;
            String hola = "hola";
            String Name = "juan";
            successCallback.invoke(x, y, hola, name);
        } catch (Error e) {
            errorCallback.invoke(e.getMessage());
        }

    }

    @ReactMethod
    public void ppromise(
            String name,
            String location,
            Promise promise) {

        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<ApplicationInfo> installedApps = new ArrayList<ApplicationInfo>();

        for (ApplicationInfo app : packages) {
            // checks for flags; if flagged, check if updated system app
            if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                installedApps.add(app); // Agregar Apps instaladas
                // it's a system app, not interested
            } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                // Discard this one
                // in this case, it should be a user-installed app
                installedApps.add(app);
            }

            // else if (app.flags & ApplicationInfo.)
            else {
                installedApps.add(app);
            }
        }

        try {

            WritableMap map = Arguments.createMap();

            map.putDouble("x", 15);
            map.putInt("y", 13);
            map.putString("APPs", installedApps.toString());
            map.putString("Name", name);
            promise.resolve(map);

        } catch (Error e) {
            promise.reject(E_LAYOUT_ERROR, e);
        }

    }

    @ReactMethod
    public void getApps(Promise promise) {
        try {
            List<PackageInfo> pList = pm.getInstalledPackages(0);
            WritableArray list = Arguments.createArray();
            for (int i = 0; i < pList.size(); i++) {
                PackageInfo packageInfo = pList.get(i);
                WritableMap appInfo = Arguments.createMap();
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    appInfo.putString("packageName", packageInfo.packageName);
                    // appInfo.putString("versionName", packageInfo.versionName);
                    // appInfo.putDouble("versionCode", packageInfo.versionCode);
                    // appInfo.putDouble("firstInstallTime", (packageInfo.firstInstallTime));
                    // appInfo.putDouble("lastUpdateTime", (packageInfo.lastUpdateTime));
                    appInfo.putString("appName", ((String) packageInfo.applicationInfo.loadLabel(pm)).trim());

                    Drawable icon = pm.getApplicationIcon(packageInfo.applicationInfo);
                    appInfo.putString("icon", Utility.convert(icon));

                    // String apkDir = packageInfo.applicationInfo.publicSourceDir;
                    // appInfo.putString("apkDir", apkDir);

                    // File file = new File(apkDir);
                    // double size = file.length();
                    // appInfo.putDouble("size", size);
                    list.pushMap(appInfo);
                }
            }
            promise.resolve(list);
        } catch (Exception ex) {
            promise.reject(ex);
        }

    }

    /*
     * @ReactMethod
     * public void killProcesso(String pkgName, Promise promise){
     * ActivityManager manager = (ActivityManager)
     * getApplicationContext.getSystemService(getApplicationContext.ACTIVITY_SERVICE
     * );
     * List<RunningAppProcessInfo> activityes =
     * ((ActivityManager)manager).getRunningAppProcesses();
     * 
     * for (int iCnt = 0; iCnt < activityes.size(); iCnt++){
     * 
     * System.out.println("APP: "+iCnt +" "+ activityes.get(iCnt).processName);
     * 
     * if (activityes.get(iCnt).processName.contains(pkgName)){
     * android.os.Process.sendSignal(activityes.get(iCnt).pid,
     * android.os.Process.SIGNAL_KILL);
     * android.os.Process.killProcess(activityes.get(i).pid);
     * //manager.killBackgroundProcesses("com.android.email");
     * 
     * //manager.restartPackage("com.android.email");
     * 
     * System.out.println("Inside if");
     * }
     * 
     * }
     * }
     */

    @ReactMethod
    public void isPackageInstalled(String packageName, Promise cb) {
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            cb.resolve(true);
        } catch (Exception e) {
            cb.resolve(false);
        }

    }

    @ReactMethod
    public void killbypackage(String packageName, Promise cb) {
        try {
            am.killBackgroundProcesses(packageName);
            cb.resolve(true);
        } catch (Exception e) {
            cb.resolve(false);
        }

    }

}