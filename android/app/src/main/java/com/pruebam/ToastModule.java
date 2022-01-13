// ToastModule.java

package com.pruebam;

import android.widget.Toast;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import android.content.pm.PackageManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;

public class ToastModule extends ReactContextBaseJavaModule {

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";
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

        List<ApplicationInfo> packages= pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<ApplicationInfo> installedApps = new ArrayList<ApplicationInfo>();

        for(ApplicationInfo app : packages) {
                //checks for flags; if flagged, check if updated system app
                if((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                    installedApps.add(app); //Agregar Apps instaladas
                    //it's a system app, not interested
                } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    //Discard this one
                    //in this case, it should be a user-installed app
                    installedApps.add(app);
                }

                //else if (app.flags & ApplicationInfo.)
                else {
                    installedApps.add(app);
                }
        }

        /*List<String> appList=new ArrayList<>();

        /*for(ApplicationInfo i : installedApps){
            appList.add( (String) i.loadLabel(pm));
        }*/

        //System.out.println(installedApps);
        Toast.makeText(getReactApplicationContext(), installedApps.toString(), duration).show();

    }

}