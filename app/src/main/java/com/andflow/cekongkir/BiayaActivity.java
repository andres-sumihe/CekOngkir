package com.andflow.cekongkir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class BiayaActivity extends AppCompatActivity {

    TextView tvCity;
    TextView tvWeight;
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biaya);
        tvCity = findViewById(R.id.city);
        tvWeight = findViewById(R.id.weight);


        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        ArrayList<DataType> data = (ArrayList<DataType>) args.getSerializable("ARRAYLIST");
        Log.d("DATA SIZE: ", data.size()+"");

        ArrayList<HashMap<String, String>> costList = new ArrayList<>();
        for(int i = 0; i < data.size(); i++){
            HashMap<String, String>  temp = new HashMap<>();
            temp.put("service",data.get(i).service );
            temp.put("value",data.get(i).cost.get(i).value.toString() );
            temp.put("etd",data.get(i).cost.get(i).etd );
            costList.add(temp);
//            temp.clear();
        }

        ListView lv = findViewById(R.id.serviceListView);
        ListAdapter adapter = new SimpleAdapter(this,
                costList,
                R.layout.service_list,new String[]{"service","value", "etd"}, new int[]{R.id.service, R.id.value, R.id.etd});
        lv.setAdapter(adapter);

        String origin = intent.getStringExtra("origin");
        String destination = intent.getStringExtra("destination");
        String wg = intent.getStringExtra("weight");
        int wgResult = (wg == null) ? 0:  Integer.parseInt(wg) / 1000;

        tvCity.setText(origin+"-"+destination);
        tvWeight.setText( wgResult +" KG");
    }
}