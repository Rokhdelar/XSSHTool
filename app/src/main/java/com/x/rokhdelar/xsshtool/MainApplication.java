package com.x.rokhdelar.xsshtool;
import android.app.Application;

/**
 * Created by X on 15/6/14.
 */
public class MainApplication extends Application {
    private String serverUrl="http://61.128.177.2/xsshtool/";
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public String getServerUrl(){
        return serverUrl;
    }
}
