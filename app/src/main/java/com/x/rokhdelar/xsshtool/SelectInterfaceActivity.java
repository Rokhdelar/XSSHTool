package com.x.rokhdelar.xsshtool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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
import java.util.LinkedList;
import java.util.List;


public class SelectInterfaceActivity extends Activity {
    private Spinner spinnerInterface;
    private ArrayList<String> listInterfaceWithDesc=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_interface);

        spinnerInterface=(Spinner)findViewById(R.id.spinnerInterface);
        listInterfaceWithDesc=getIntent().getStringArrayListExtra("interfaceWithDesc");

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                listInterfaceWithDesc);
        spinnerInterface.setAdapter(arrayAdapter);
        spinnerInterface.setPrompt("请选择端口");

        Button buttonInterfaceChose=(Button)findViewById(R.id.buttonInterfaceChose);
        buttonInterfaceChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SelectInterfaceActivity.this,SW8900Activity.class);
                String selectedItem[]=((String)spinnerInterface.getSelectedItem()).split("--");
                intent.putExtra("interfaceName",selectedItem[0]);
                intent.putExtra("interfaceDesc",selectedItem[1]);
                setResult(2,intent);
                finish();
            }
        });
        ((Button)findViewById(R.id.buttonInterfaceCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
