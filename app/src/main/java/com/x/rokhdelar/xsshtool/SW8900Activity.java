package com.x.rokhdelar.xsshtool;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.x.rokhdelar.model.Device;
import com.x.rokhdelar.model.SubStation;

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
import org.w3c.dom.Text;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class SW8900Activity extends Activity {
    private TextView textViewDeviceName,textViewDeviceUpTime,textViewInterfaceName,textViewInterfaceDesc;
    private TextView textViewOutput;
    private MainApplication mainApplication;
    private Device device;//当前选择的device。
    private String interfaceName;//当前选择带端口。
    private ProgressDialog progressDialog;
    private ArrayList<String> listInterfaceWithDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sw8900);

        getActionBar().setHomeButtonEnabled(true);
        mainApplication=(MainApplication)getApplication();
        listInterfaceWithDesc=new ArrayList<String>();

        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在读取数据，请等待...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);

        textViewDeviceName=(TextView)findViewById(R.id.textViewDeviceName);
        textViewDeviceUpTime=(TextView)findViewById(R.id.textViewDeviceUpTime);
        textViewInterfaceName=(TextView)findViewById(R.id.textViewInterfaceName);
        textViewInterfaceDesc=(TextView)findViewById(R.id.textViewInterfaceDescription);
        textViewOutput=(TextView)findViewById(R.id.textViewOutput);
        textViewOutput.setMovementMethod(ScrollingMovementMethod.getInstance());

        ImageButton imageButtonSelectDevice=(ImageButton)findViewById(R.id.ImageButtonSelectDevice);
        imageButtonSelectDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectDeviceActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        ImageButton imageButtonSelectInterface=(ImageButton)findViewById(R.id.imageButtonSelectInterface);
        imageButtonSelectInterface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectInterfaceActivity.class);
                intent.putStringArrayListExtra("interfaceWithDesc", listInterfaceWithDesc);
                startActivityForResult(intent, 2);
            }
        });
        //弹出选择设备对话框。
        Intent intent=new Intent(getApplicationContext(),SelectDeviceActivity.class);
        startActivityForResult(intent, 1);

        ImageButton imageButtonShowConfig=(ImageButton)findViewById(R.id.imageButtonShowConfig);
        imageButtonShowConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkDeviceAndInterface()){
                    GetInterfaceConfigTask getInterfaceConfigTask=new GetInterfaceConfigTask();
                    progressDialog.show();
                    getInterfaceConfigTask.execute(String.valueOf(device.getDeviceID()),interfaceName);
                    }
                }
        });

        ImageButton imageButtonInterfaceInfo=(ImageButton)findViewById(R.id.imageButtonInterfaceInfo);
        imageButtonInterfaceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkDeviceAndInterface()){
                    GetInterfaceInfoTask getInterfaceInfoTask=new GetInterfaceInfoTask();
                    progressDialog.show();
                    getInterfaceInfoTask.execute(String.valueOf(device.getDeviceID()), interfaceName);
                }
            }
        });

        ImageButton imageButtonInterfaceOptic=(ImageButton)findViewById(R.id.imageButtonInterfaceOptic);
        imageButtonInterfaceOptic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkDeviceAndInterface()){
                    GetInterfaceOptionInfoTask getInterfaceOptionInfoTask=new GetInterfaceOptionInfoTask();
                    progressDialog.show();
                    getInterfaceOptionInfoTask.execute(String.valueOf(device.getDeviceID()));
                }
            }
        });

        ImageButton imageButtonSmartGroupInfo=(ImageButton)findViewById(R.id.imageButtonShowSmartgroup);
        imageButtonSmartGroupInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!interfaceName.contains("smartgroup")){
                    new AlertDialog.Builder(SW8900Activity.this)
                            .setMessage("您当前选择的端口不是smartgroup，请选择需要的端口。")
                            .setPositiveButton("确定",null)
                            .show();
                    return;
                }
                GetSmartGroupInfoTask getSmartGroupInfoTask=new GetSmartGroupInfoTask();
                progressDialog.show();
                getSmartGroupInfoTask.execute(String.valueOf(device.getDeviceID()),interfaceName);
            }
        });

        ImageButton imageButtonOpenInterface=(ImageButton)findViewById(R.id.imageButtonOpenInterface);
        imageButtonOpenInterface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenInterfaceTask openInterfaceTask=new OpenInterfaceTask();
                progressDialog.show();
                openInterfaceTask.execute(String.valueOf(device.getDeviceID()), interfaceName);
            }
        });

        ImageButton imageButtonShutdownInterface=(ImageButton)findViewById(R.id.imageButtonShutdownInterface);
        imageButtonShutdownInterface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(SW8900Activity.this)
                        .setMessage("关闭端口的动作会导致业务中断，您确定要关闭这个端口？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ShuDownInterfaceTask shuDownInterfaceTask=new ShuDownInterfaceTask();
                                progressDialog.show();
                                shuDownInterfaceTask.execute(String.valueOf(device.getDeviceID()),interfaceName);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }
        });
    }

    //检查是否已经选择了设备和端口。
    private boolean checkDeviceAndInterface(){
        if(device==null || interfaceName==null){
            new AlertDialog.Builder(this).setMessage("请先选择设备和端口再进行操作。")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1://选择设备对话框
                if(resultCode==1){
                    device=(Device)data.getSerializableExtra("device");
                    textViewDeviceName.setText("设备："+device.getDeviceName() + "@" + device.getDeviceIP());
                    //启动进程获取设备运行时间
                    progressDialog.show();
                    GetDeviceUpTimeTask getDeviceUpTimeTask=new GetDeviceUpTimeTask();
                    getDeviceUpTimeTask.execute(String.valueOf(device.getDeviceID()));

                    GetAllInterfaceWithDescTask getAllInterfaceWithDescTask=new GetAllInterfaceWithDescTask();
                    getAllInterfaceWithDescTask.execute(String.valueOf(device.getDeviceID()));
                }

                break;
            case 2://选择端口对话框
                if(resultCode==2){
                    interfaceName=data.getStringExtra("interfaceName");
                    textViewInterfaceName.setText("端口名称："+data.getStringExtra("interfaceName"));
                    textViewInterfaceDesc.setText("端口描述：" + data.getStringExtra("interfaceDesc"));
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public class GetDeviceUpTimeTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPostExecute(String s) {
            textViewDeviceUpTime.setText("设备运行时间："+s);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String basicUrl=mainApplication.getServerUrl()+"SW8900API.php";
            List<BasicNameValuePair> httpParams=new LinkedList<BasicNameValuePair>();
            httpParams.add(new BasicNameValuePair("action","getSystemUpTime"));
            httpParams.add(new BasicNameValuePair("deviceID",strings[0]));

            HttpGet httpGet=new HttpGet(basicUrl+"?"+ URLEncodedUtils.format(httpParams,"UTF-8"));
            HttpClient httpClient=new DefaultHttpClient();
            try{
                HttpResponse httpResponse=httpClient.execute(httpGet);
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    JSONObject jsonObject=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                    if(jsonObject.getInt("code")==200){
                        return jsonObject.getString("data");
                        }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public class GetInterfaceConfigTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPostExecute(String s) {
            textViewOutput.setText(s);
            progressDialog.dismiss();

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String basicUrl=mainApplication.getServerUrl()+"SW8900API.php";
            List<BasicNameValuePair> httpParams=new LinkedList<BasicNameValuePair>();
            httpParams.add(new BasicNameValuePair("action","getInterfaceConfig"));
            httpParams.add(new BasicNameValuePair("deviceID",strings[0]));
            httpParams.add(new BasicNameValuePair("interfaceName",interfaceName));

            HttpGet httpGet=new HttpGet(basicUrl+"?"+ URLEncodedUtils.format(httpParams,"UTF-8"));
            HttpClient httpClient=new DefaultHttpClient();
            try{
                HttpResponse httpResponse=httpClient.execute(httpGet);
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    JSONObject jsonObject=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                    if(jsonObject.getInt("code")==200){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        String s="";
                        for(int i=0;i<jsonArray.length();i++){
                            s=s+jsonArray.getString(i)+"\n";
                        }
                        return s;
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class GetInterfaceInfoTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPostExecute(String s) {
            textViewOutput.setText(s);
            progressDialog.dismiss();

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String basicUrl=mainApplication.getServerUrl()+"SW8900API.php";
            List<BasicNameValuePair> httpParams=new LinkedList<BasicNameValuePair>();
            httpParams.add(new BasicNameValuePair("action","getInterfaceInfo"));
            httpParams.add(new BasicNameValuePair("deviceID",strings[0]));
            httpParams.add(new BasicNameValuePair("interfaceName",interfaceName));

            HttpGet httpGet=new HttpGet(basicUrl+"?"+ URLEncodedUtils.format(httpParams,"UTF-8"));
            HttpClient httpClient=new DefaultHttpClient();
            try{
                HttpResponse httpResponse=httpClient.execute(httpGet);
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    JSONObject jsonObject=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                    if(jsonObject.getInt("code")==200){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        String s="";
                        for(int i=0;i<jsonArray.length();i++){
                            s=s+jsonArray.getString(i)+"\n";
                        }
                        return s;
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public class GetAllInterfaceWithDescTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String basicUrl = mainApplication.getServerUrl() + "SW8900API.php";
            List<BasicNameValuePair> httpParams = new LinkedList<BasicNameValuePair>();
            httpParams.add(new BasicNameValuePair("action", "getAllInterfaceWithDesc"));
            httpParams.add(new BasicNameValuePair("deviceID", strings[0]));

            HttpGet httpGet = new HttpGet(basicUrl + "?" + URLEncodedUtils.format(httpParams, "UTF-8"));
            HttpClient httpClient = new DefaultHttpClient();
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                    if (jsonObject.getInt("code") == 200) {
                        listInterfaceWithDesc.clear();
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++){
                            listInterfaceWithDesc.add(jsonArray.getString(i));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class GetInterfaceOptionInfoTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPostExecute(String s) {
            textViewOutput.setText(s);
            progressDialog.dismiss();

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String basicUrl=mainApplication.getServerUrl()+"SW8900API.php";
            List<BasicNameValuePair> httpParams=new LinkedList<BasicNameValuePair>();
            httpParams.add(new BasicNameValuePair("action","getInterfaceOpticInfo"));
            httpParams.add(new BasicNameValuePair("deviceID",strings[0]));
            httpParams.add(new BasicNameValuePair("interfaceName",interfaceName));

            HttpGet httpGet=new HttpGet(basicUrl+"?"+ URLEncodedUtils.format(httpParams,"UTF-8"));
            HttpClient httpClient=new DefaultHttpClient();
            try{
                HttpResponse httpResponse=httpClient.execute(httpGet);
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    JSONObject jsonObject=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                    if(jsonObject.getInt("code")==200){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        String s="";
                        for(int i=0;i<jsonArray.length();i++){
                            s=s+jsonArray.getString(i)+"\n";
                        }
                        return s;
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class GetSmartGroupInfoTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPostExecute(String s) {
            textViewOutput.setText(s);
            progressDialog.dismiss();

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String basicUrl=mainApplication.getServerUrl()+"SW8900API.php";
            List<BasicNameValuePair> httpParams=new LinkedList<BasicNameValuePair>();
            httpParams.add(new BasicNameValuePair("action","getSmartGroupInfo"));
            httpParams.add(new BasicNameValuePair("deviceID",strings[0]));
            httpParams.add(new BasicNameValuePair("interfaceName",interfaceName));

            HttpGet httpGet=new HttpGet(basicUrl+"?"+ URLEncodedUtils.format(httpParams,"UTF-8"));
            HttpClient httpClient=new DefaultHttpClient();
            try{
                HttpResponse httpResponse=httpClient.execute(httpGet);
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    JSONObject jsonObject=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                    if(jsonObject.getInt("code")==200){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        String s="";
                        for(int i=0;i<jsonArray.length();i++){
                            s=s+jsonArray.getString(i)+"\n";
                        }
                        return s;
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class OpenInterfaceTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPostExecute(String s) {
            textViewOutput.setText(s);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String basicUrl=mainApplication.getServerUrl()+"SW8900API.php";
            List<BasicNameValuePair> httpParams=new LinkedList<BasicNameValuePair>();
            httpParams.add(new BasicNameValuePair("action","openInterface"));
            httpParams.add(new BasicNameValuePair("deviceID",strings[0]));
            httpParams.add(new BasicNameValuePair("interfaceName",interfaceName));

            HttpGet httpGet=new HttpGet(basicUrl+"?"+ URLEncodedUtils.format(httpParams,"UTF-8"));
            HttpClient httpClient=new DefaultHttpClient();
            try{
                HttpResponse httpResponse=httpClient.execute(httpGet);
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    JSONObject jsonObject=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                    if(jsonObject.getInt("code")==200){

                        return "打开端口："+interfaceName+"成功。";
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class ShuDownInterfaceTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPostExecute(String s) {
            textViewOutput.setText(s);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String basicUrl=mainApplication.getServerUrl()+"SW8900API.php";
            List<BasicNameValuePair> httpParams=new LinkedList<BasicNameValuePair>();
            httpParams.add(new BasicNameValuePair("action","shutdownInterface"));
            httpParams.add(new BasicNameValuePair("deviceID",strings[0]));
            httpParams.add(new BasicNameValuePair("interfaceName",interfaceName));

            HttpGet httpGet=new HttpGet(basicUrl+"?"+ URLEncodedUtils.format(httpParams,"UTF-8"));
            HttpClient httpClient=new DefaultHttpClient();
            try{
                HttpResponse httpResponse=httpClient.execute(httpGet);
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    JSONObject jsonObject=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                    if(jsonObject.getInt("code")==200){
                        return "关闭端口："+interfaceName+"成功。";
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
