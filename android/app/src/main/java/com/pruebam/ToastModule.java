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

import android.content.pm.PackageManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Process;
import android.content.pm.PackageInfo;

import com.pruebam.Utility;

public class ToastModule extends ReactContextBaseJavaModule {

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";
    private static final String E_LAYOUT_ERROR = "E_LAYOUT_ERROR";
    PackageManager pm;

    public ToastModule(ReactApplicationContext reactContext) {
        super(reactContext);
        pm = reactContext.getPackageManager();
    }

    @Override
    public String getName() {
        return "ToastModule";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
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
    public void muerte() {
        android.os.Process.killProcess(android.os.Process.myPid());
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
                    //appInfo.putString("versionName", packageInfo.versionName);
                    //appInfo.putDouble("versionCode", packageInfo.versionCode);
                    //appInfo.putDouble("firstInstallTime", (packageInfo.firstInstallTime));
                    //appInfo.putDouble("lastUpdateTime", (packageInfo.lastUpdateTime));
                    appInfo.putString("appName", ((String) packageInfo.applicationInfo.loadLabel(pm)).trim());

                    //Drawable icon = pm.getApplicationIcon(packageInfo.applicationInfo);
                    //appInfo.putString("icon", Utility.convert(icon));

                    //String apkDir = packageInfo.applicationInfo.publicSourceDir;
                    //appInfo.putString("apkDir", apkDir);

                    //File file = new File(apkDir);
                    //double size = file.length();
                    //appInfo.putDouble("size", size);
                    list.pushMap(appInfo);
                }
            }
            promise.resolve(list);
        } catch (Exception ex) {
        promise.reject(ex);
        }

    }


    @ReactMethod
    public void isPackageInstalled(String packageName, Promise cb){
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            cb.resolve(true);
        } catch (Exception e) {
            cb.resolve(false);
        }
        
    }


}