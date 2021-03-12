package com.andflow.cekongkir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class BiayaActivity extends AppCompatActivity {

    TextView tvTest;
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biaya);
        Intent intent = getIntent();
        ArrayList<DataType> data = (ArrayList<DataType>) intent.getSerializableExtra("data");
        HashMap<String, String>  temp = new HashMap<>();
        ArrayList<HashMap<String, String>> costList = null;
        for(int i = 0; i < data.size(); i++){
            temp.put("service",data.get(i).service );
            temp.put("value",data.get(i).cost.get(0).value.toString() );
            temp.put("value",data.get(i).cost.get(0).etd );
            costList.add(temp);
            temp.clear();
        }
        ListView lv = findViewById(R.id.serviceListView);
        ListAdapter adapter = new SimpleAdapter(this,
                costList,
                R.layout.service_list,new String[]{"service","value", "etd"}, new int[]{R.id.service, R.id.value, R.id.etd});
        lv.setAdapter(adapter);


        String origin =intent.getStringExtra("origin");
        String destination =intent.getStringExtra("destination");

        tvTest = findViewById(R.id.test);
        tvTest.setText(origin);
    }
}