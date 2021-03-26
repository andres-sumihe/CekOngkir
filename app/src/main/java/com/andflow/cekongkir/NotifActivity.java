package com.andflow.cekongkir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class NotifActivity extends AppCompatActivity {

    ListView lv;
    Database mydb;
    notifModels nM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        nM = new notifModels();
        mydb = new Database(this);

        ArrayList<HashMap<String, String>> notifList = mydb.getAllRecordNotif();
        ListView lv = (ListView) findViewById(R.id.serviceListView2);
        ListAdapter adapter = new SimpleAdapter(NotifActivity.this, notifList, R.layout.list_notif,new String[]
                {"judul","isi"}, new int[]{R.id.name1, R.id.designation1});
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idNotif = notifList.get(position).get("notif_no");
                mydb.deleteNotif(Integer.parseInt(idNotif));
                ArrayList<HashMap<String, String>> notifList = mydb.getAllRecordNotif();
                ListView lv = (ListView) findViewById(R.id.serviceListView2);
                Log.d("Pos", idNotif+"");
                ListAdapter adapter = new SimpleAdapter(NotifActivity.this, notifList, R.layout.list_notif,new String[]
                        {"judul","isi"}, new int[]{R.id.name1, R.id.designation1});
                lv.setAdapter(adapter);

                Intent intent = new Intent(getApplicationContext(), NotifActivity.class);
                startActivity(intent);
            }
        });

    }

    public void clearAllNotif(View v){
        mydb.deleteAllNotif();
        ListView lv = findViewById(R.id.serviceListView2);
        lv.setAdapter(null);
    }

}