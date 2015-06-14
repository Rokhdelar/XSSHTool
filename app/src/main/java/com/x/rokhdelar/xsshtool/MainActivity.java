package com.x.rokhdelar.xsshtool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listViewMain=(ListView)findViewById(R.id.mainListView);
        final ArrayList<HashMap<String,Object>> arrayListMainItem=new ArrayList<HashMap<String, Object>>();
        HashMap<String,Object> map=new HashMap<String,Object>();

        map.put("itemImage",R.mipmap.ic_sw8905);
        map.put("itemCaption", "交换机管理");
        map.put("itemDescription", "交换机相关操作，端口、mac、vlan等...");
        arrayListMainItem.add(map);

        map=new HashMap<String, Object>();
        map.put("itemImage",R.mipmap.ic_hwolt);
        map.put("itemCaption", "华为OLT管理");
        map.put("itemDescription", "华为OLT相关操作，端口、mac、vlan等...");
        arrayListMainItem.add(map);

        map=new HashMap<String, Object>();
        map.put("itemImage",R.mipmap.ic_zteolt);
        map.put("itemCaption", "中兴OLT管理");
        map.put("itemDescription", "中兴OLT相关操作，端口、mac、vlan等...");
        arrayListMainItem.add(map);

        map=new HashMap<String, Object>();
        map.put("itemImage",R.mipmap.ic_setting);
        map.put("itemCaption","App设置");
        map.put("itemDescription","应用设置...");
        arrayListMainItem.add(map);

        SimpleAdapter simpleAdapterMainItem=new SimpleAdapter(this,
                arrayListMainItem,
                R.layout.mainitem,
                new String[]{"itemImage","itemCaption","itemDescription"},
                new int[]{R.id.itemImage,R.id.itemCaption,R.id.itemDescription});

        listViewMain.setAdapter(simpleAdapterMainItem);

        listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intent=new Intent(getApplicationContext(),SW8900Activity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;


                }
                Toast.makeText(getApplicationContext(),"您选择了"+position,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
