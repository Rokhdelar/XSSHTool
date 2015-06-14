package com.x.rokhdelar.xsshtool;

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

    private MainApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sw8900);

        getActionBar().setHomeButtonEnabled(true);
        application=(MainApplication)getApplication();

    }
    private void initView(){

    }

    private class getDeviceInfoTask extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... strings) {
            List<BasicNameValuePair> httpParams=new LinkedList<BasicNameValuePair>();
            httpParams.add(new BasicNameValuePair("action","getDeviceinfo"));
            httpParams.add(new BasicNameValuePair("deviceID","0"));
            String basicUrl=application.getServerUrl()+"APIDevice.php";

            HttpGet httpGet=new HttpGet(basicUrl+"?"+ URLEncodedUtils.format(httpParams,"UTF-8"));
            HttpClient httpClient=new DefaultHttpClient();
            try{
                HttpResponse httpResponse=httpClient.execute(httpGet);
                if(httpResponse.getStatusLine().getStatusCode()==200){
                    JSONObject jsonObject=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                    if(jsonObject.getInt("code")==200){
                        deviceName=jsonObject.getJSONObject("data").getString("deviceName");

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }


            return null;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sw8900, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
