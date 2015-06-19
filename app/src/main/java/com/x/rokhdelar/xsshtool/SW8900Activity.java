package com.x.rokhdelar.xsshtool;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.x.rokhdelar.model.Device;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;


public class SW8900Activity extends Activity {
    private String deviceName;
    private String ip;
    private TextView textViewDeviceName,textViewDeviceVersion;

    private MainApplication application;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Device device=(Device)data.getSerializableExtra("device");
        textViewDeviceName.setText(device.getDeviceName() + "--" + device.getDeviceIP());

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sw8900);

        getActionBar().setHomeButtonEnabled(true);
        application=(MainApplication)getApplication();

        textViewDeviceName=(TextView)findViewById(R.id.textViewDeviceName);
        textViewDeviceVersion=(TextView)findViewById(R.id.textViewDeviceVersion);
        ImageButton imageButton=(ImageButton)findViewById(R.id.ImageButtonSelectDevice);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),SelectDeviceActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        //弹出选择设备对话框。
        Intent intent=new Intent(getApplicationContext(),SelectDeviceActivity.class);
        startActivityForResult(intent, 1);

        //获取设备运行时间。

        //获取设备端口列表，采用spinner方式。
    }

}
