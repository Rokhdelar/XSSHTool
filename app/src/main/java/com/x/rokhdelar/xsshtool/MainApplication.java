package com.x.rokhdelar.xsshtool;
import android.app.Application;

import java.util.ArrayList;

/**
 * Created by X on 15/6/14.
 */
public class MainApplication extends Application {
    private String serverUrl="http://61.128.177.14/xssh/";
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public String getServerUrl(){
        return serverUrl;
    }
}
