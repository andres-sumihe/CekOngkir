package com.andflow.cekongkir;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    }
}