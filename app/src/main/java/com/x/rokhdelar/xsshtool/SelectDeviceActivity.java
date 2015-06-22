package com.x.rokhdelar.xsshtool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Entity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.x.rokhdelar.model.CommRoom;
import com.x.rokhdelar.model.Device;
import com.x.rokhdelar.model.SubStation;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class SelectDeviceActivity extends Activity {
    private MainApplication mainApplication;
    private ArrayList<SubStation> listSubStation=new ArrayList<SubStation>();
    private ArrayList<CommRoom> listCommRoom=new ArrayList<CommRoom>();
    private ArrayList<Device> listDevice=new ArrayList<Device>();
    private Spinner spinnerSubstation,spinnerCommRoom,spinnerDevice;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);

        mainApplication=(MainApplication)getApplication();
        spinnerSubstation=(Spinner)findViewById(R.id.spinnerSubstation);
        spinnerCommRoom=(Spinner)findViewById(R.id.spnnerCommRoom);
        spinnerDevice=(Spinner)findViewById(R.id.spnnerDevice);
        Button buttonChose = (Button) findViewById(R.id.buttonChose);
        Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在读取数据，请等待...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);

        progressDialog.show();
        GetSubStationTask getSubStationTask=new GetSubStationTask();
        getSubStationTask.execute();
        spinnerSubstation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SubStation subStation = listSubStation.get(i);
                progressDialog.show();
                GetCommRoomTask getCommRoomTask = new GetCommRoomTask();
                getCommRoomTask.execute(String.valueOf(subStation.getSubID()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerCommRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CommRoom commRoom=listCommRoom.get(i);
                progressDialog.show();
                GetDeviceTask getDeviceTask=new GetDeviceTask();
                getDeviceTask.execute(String.valueOf(commRoom.getCommRoomID()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Device device = (Device) spinnerDevice.getSelectedItem();
                Intent intent = new Intent(SelectDeviceActivity.this, SW8900Activity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("device", device);
                intent.putExtras(bundle);
                setResult(1, intent);
                finish();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public class GetSubStationTask extends AsyncTask<String,Integer,String>{

        @Override
        protected void onPostExecute(String s) {
            ArrayAdapter<SubStation> arrayAdapter=new ArrayAdapter<SubStation>(getApplicationContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    listSubStation);
            spinnerSubstation.setAdapter(arrayAdapter);
            spinnerSubstation.setPrompt("请选择设备所在支局");
            progressDialog.dismiss();

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            List<BasicNameValuePair> httpParams=new LinkedList<BasicNameValuePair>();
            httpParams.add(new BasicNameValuePair("action","getAllSubStations"));

            String basicUrl=mainApplication.getServerUrl()+"DeviceInfoAPI.php";
            HttpGet httpGet=new HttpGet(basicUrl+"?"+ URLEncodedUtils.format(httpParams,"UTF-8"));
            HttpClient httpClient=new DefaultHttpClient();
            try{
                HttpResponse httpResponse=httpClient.execute(httpGet);
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    JSONObject jsonObject=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                    if(jsonObject.getInt("code")==200){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        listSubStation.clear();
                        for(int i=0;i<jsonArray.length();i++){
                            SubStation subStation=new SubStation();
                            subStation.setSubID(jsonArray.getJSONObject(i).getInt("subID"));
                            subStation.setSubName(jsonArray.getJSONObject(i).getString("subName"));
                            subStation.setSubLeader(jsonArray.getJSONObject(i).getString("subLeader"));
                            subStation.setSubPhone(jsonArray.getJSONObject(i).getString("subPhone"));
                            subStation.setSubMemo(jsonArray.getJSONObject(i).getString("subMemo"));
                            listSubStation.add(subStation);
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;

        }
    }

    public class GetCommRoomTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPostExecute(String s) {
            ArrayAdapter<CommRoom> arrayAdapter=new ArrayAdapter<CommRoom>(getApplicationContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    listCommRoom);
            spinnerCommRoom.setAdapter(arrayAdapter);
            spinnerCommRoom.setPrompt("请选择设备所在机房");
            progressDialog.dismiss();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            List<BasicNameValuePair> httpParams=new LinkedList<BasicNameValuePair>();
            httpParams.add(new BasicNameValuePair("action","getCommRoomsBySubID"));
            httpParams.add(new BasicNameValuePair("subID",strings[0]));

            String basicUrl=mainApplication.getServerUrl()+"DeviceInfoAPI.php";
            HttpGet httpGet=new HttpGet(basicUrl+"?"+ URLEncodedUtils.format(httpParams,"UTF-8"));
            HttpClient httpClient=new DefaultHttpClient();
            try{
                HttpResponse httpResponse=httpClient.execute(httpGet);
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    JSONObject jsonObject=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                    if(jsonObject.getInt("code")==200){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        listCommRoom.clear();
                        for(int i=0;i<jsonArray.length();i++){
                            CommRoom commRoom=new CommRoom();
                            commRoom.setCommRoomID(jsonArray.getJSONObject(i).getInt("commRoomID"));
                            commRoom.setCommRoomName(jsonArray.getJSONObject(i).getString("commRoomName"));
                            commRoom.setCommRoomAddress(jsonArray.getJSONObject(i).getString("commRoomAddress"));
                            commRoom.setCommRoomContact(jsonArray.getJSONObject(i).getString("commRoomContact"));
                            commRoom.setCommRoomPhone(jsonArray.getJSONObject(i).getString("commRoomPhone"));
                            commRoom.setSubID(jsonArray.getJSONObject(i).getInt("subID"));
                            commRoom.setCommRoomMemo(jsonArray.getJSONObject(i).getString("commRoomMemo"));
                            listCommRoom.add(commRoom);
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public class GetDeviceTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPostExecute(String s) {
            ArrayAdapter<Device> arrayAdapter=new ArrayAdapter<Device>(getApplicationContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    listDevice);
            spinnerDevice.setAdapter(arrayAdapter);
            spinnerDevice.setPrompt("请选择设备");
            progressDialog.dismiss();

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            List<BasicNameValuePair> httpParams=new LinkedList<BasicNameValuePair>();
            httpParams.add(new BasicNameValuePair("action","getDevicesByCommRoomID"));
            httpParams.add(new BasicNameValuePair("commRoomID",strings[0]));

            String basicUrl=mainApplication.getServerUrl()+"DeviceInfoAPI.php";
            HttpGet httpGet=new HttpGet(basicUrl+"?"+ URLEncodedUtils.format(httpParams,"UTF-8"));
            HttpClient httpClient=new DefaultHttpClient();
            try{
                HttpResponse httpResponse=httpClient.execute(httpGet);
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    JSONObject jsonObject=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                    if(jsonObject.getInt("code")==200){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        listDevice.clear();
                        for(int i=0;i<jsonArray.length();i++){
                            Device device=new Device();
                            device.setDeviceID(jsonArray.getJSONObject(i).getInt("deviceID"));
                            device.setDeviceName(jsonArray.getJSONObject(i).getString("deviceName"));
                            device.setDeviceIP(jsonArray.getJSONObject(i).getString("deviceIP"));
                            device.setCommRoomID(jsonArray.getJSONObject(i).getInt("commRoomID"));
                            device.setDeviceSNMPRO(jsonArray.getJSONObject(i).getString("deviceSNMPRO"));
                            device.setDeviceSNMPRW(jsonArray.getJSONObject(i).getString("deviceSNMPRW"));
                            device.setDeviceType(jsonArray.getJSONObject(i).getString("deviceType"));
                            device.setDeviceModel(jsonArray.getJSONObject(i).getString("deviceModel"));
                            device.setDeviceBRAS(jsonArray.getJSONObject(i).getString("deviceBRAS"));
                            device.setDeviceBRASPort(jsonArray.getJSONObject(i).getString("deviceBRASPORT"));
                            device.setDeviceInternetVLAN(jsonArray.getJSONObject(i).getString("deviceInternetVLAN"));
                            device.setDeviceMemo(jsonArray.getJSONObject(i).getString("deviceMemo"));
                            listDevice.add(device);
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
