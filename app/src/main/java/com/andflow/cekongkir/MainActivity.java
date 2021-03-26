package com.andflow.cekongkir;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";

    Database mydb;
    costModels cM;
    private Button swapCity;
    private Button cekOngkir;
    private Spinner mSpinerOrigin;
    private Spinner mSpinerDestination;
    private Spinner mSpinerCourier;
    private EditText mWeight;
    private ListView listView;
    ArrayList<DataCost> dataCost = new ArrayList<DataCost>();
    ArrayList<DataType> dataCosts = new ArrayList<DataType>();
    ArrayList<DataCity> dataCities = new ArrayList<DataCity>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cM = new costModels();
        mydb = new Database(this);

        listView = findViewById(R.id.user_list);
        mSpinerOrigin = findViewById(R.id.origin);
        swapCity = findViewById(R.id.swapCity);
        cekOngkir = findViewById(R.id.cekPrice);
        mSpinerDestination = findViewById(R.id.destination);
        mSpinerCourier = findViewById(R.id.courier);
        mWeight = findViewById(R.id.weight);

        //Riwayat Pencarian
        ArrayList<HashMap<String, String>> costList = mydb.getAllRecord();
        ListView lv = (ListView) findViewById(R.id.user_list);
        ListAdapter adapter = new SimpleAdapter(MainActivity.this, costList, R.layout.list_row,new String[]{"origin-name","destination-name"}, new int[]{R.id.name, R.id.designation});
        lv.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View itemView, int itemPosition, long itemId) {
//                int weight = Integer.parseInt(mWeight.getText().toString());
                HashMap<String,String> map =(HashMap<String,String>)lv.getItemAtPosition(itemPosition);
                requestFromHistory(map);

            }
        });

        //Riwayat Pencarian

        final String courier[] = {"JNE", "POS", "TIKI"};
        try {

            OkHttpClient client = new OkHttpClient();
            String url = "https://api.rajaongkir.com/starter/province";

            Request request = new Request.Builder()
                    .url("https://api.rajaongkir.com/starter/city?province=10")
                    .get()
                    .addHeader("key", "ff66dfc01b6a9905f0bd919c0d56b95f")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if(response.isSuccessful()){
                        String myResponse = response.body().string();
                        ArrayList<JSONObject> arrays = new ArrayList<JSONObject>();
                        JSONObject[] jsons =null;
                        List<String> city_name = new ArrayList<String>();
                        try {
                                JSONObject responseJSON = new JSONObject(myResponse);
//                            Log.d("DATA", responseJSON.getJSONObject("rajaongkir").getJSONArray("results").toString());
                            JSONArray dataArray = responseJSON.getJSONObject("rajaongkir").getJSONArray("results");
                            for (int i = 0; i < dataArray.length();i++){
                                JSONObject otherJSONObject = dataArray.getJSONObject(i);
                                arrays.add(otherJSONObject);
                            }

                            jsons = new JSONObject[arrays.size()];
                            arrays.toArray(jsons);

                            for(int i = 0; i< dataArray.length();i++){
                                city_name.add(jsons[i].getString("city_name"));
                                dataCities.add(new DataCity(
                                        jsons[i].getString("city_id"),
                                        jsons[i].getString("province_id"),
                                        jsons[i].getString("province"),
                                        jsons[i].getString("type"),
                                        jsons[i].getString("city_name"),
                                        jsons[i].getString("postal_code")
                                ));
//                                Log.d("Arr", jsons[i].getString("city_name"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Log.d("CityName", city_name.get(0));
                                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                                        android.R.layout.simple_spinner_dropdown_item,city_name);
                                mSpinerOrigin.setAdapter(adapter);
                                mSpinerDestination.setAdapter(adapter);
//                                mTextViewResult.setText(arrays.size());
                            }
                        });
                    }
                }
            });
        }catch (Exception e){

        }

        ArrayAdapter<String> courierAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, courier);

        mSpinerCourier.setAdapter(courierAdapter);

        swapCity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int pos1 = mSpinerOrigin.getSelectedItemPosition();
                int pos2 = mSpinerDestination.getSelectedItemPosition();

                mSpinerOrigin.setSelection(pos2);
                mSpinerDestination.setSelection(pos1);
                Log.d("Cek Button", "Tertukar");
            }
        });


    }
    public void requestFromHistory(HashMap<String,String> map){
        String url = "https://api.rajaongkir.com/starter/cost";
        String originCek, destinationCek, couriercek;
        originCek = map.get("origin-id");
        destinationCek = map.get("destination-id");
        couriercek = map.get("courier");
        int weight = Integer.parseInt(map.get("weight"));
        String content = "origin=" + originCek + "&destination=" + destinationCek + "&weight=" + weight + "&courier=" + couriercek.toLowerCase();
        ArrayList<DataCost> DataCost = new ArrayList<DataCost>();

        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(content, mediaType);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("key", "ff66dfc01b6a9905f0bd919c0d56b95f")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.w("Error", "WOW "+e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    JSONObject[] jsons =null;
                    ArrayList<JSONObject> arrays = new ArrayList<JSONObject>();
                    try{
                        JSONObject responseJSON = new JSONObject(response.body().string());
                        Log.d("DATA", responseJSON.getJSONObject("rajaongkir")
                                .getJSONArray("results")
                                .getJSONObject(0)
                                .getJSONArray("costs")
                                .toString());
                        JSONArray dataArray = responseJSON.getJSONObject("rajaongkir")
                                .getJSONArray("results")
                                .getJSONObject(0)
                                .getJSONArray("costs");
                        for (int i = 0; i < dataArray.length();i++){
                            JSONObject otherJSONObject = dataArray.getJSONObject(i);
                            arrays.add(otherJSONObject);
                        }
                        jsons = new JSONObject[arrays.size()];
                        arrays.toArray(jsons);
                        List<DataCost> temp = new ArrayList<DataCost>();
                        for(int i = 0; i< dataArray.length();i++){
                            JSONObject tempCost = jsons[i]
                                    .getJSONArray("cost")
                                    .getJSONObject(0);
                            temp.add(new DataCost(
                                    Integer.parseInt(tempCost.getString("value")),
                                    tempCost.getString("etd"),
                                    tempCost.getString("note")
                            ));
                            dataCosts.add(new DataType(
                                    jsons[i].getString("service"),
                                    jsons[i].getString("description"),
                                    temp
                            ));

                            Log.d("IN DATATYPE", "Service : "+dataCosts.get(i).service );

                        }

                    }catch (Exception e){

                    }

                }
            });
        }catch (Exception e){
            Log.w("ERROR", "TEST : "+e.getMessage());
        }

        String originName = map.get("origin-name");
        String destinationName = map.get("destination-name");
        Log.d("UkuranData", !dataCosts.isEmpty() ? dataCosts.get(0).service + " " + originName + "|"+destinationName: "Tidak ada data");
        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        // memanggil method untuk menampilkan NOTIFICATION_ID
        // dengan mengirimkan data yang dikirim dari komponen EditText
        tampilNotifikasi("JUDUL"
                , "Pesan", in);

        Intent intent = new Intent(MainActivity.this, BiayaActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST",(Serializable)dataCosts);
        intent.putExtra("BUNDLE",args);
        intent.putExtra("weight", map.get("weight"));
        intent.putExtra("origin", originName);
        intent.putExtra("destination", destinationName);
        startActivity(intent);

    }
    public void cekPrice(View view) {
        String url = "https://api.rajaongkir.com/starter/cost";
        String originCek, destinationCek, couriercek;
        originCek = dataCities.get(mSpinerOrigin.getSelectedItemPosition()).cityId;
        destinationCek = dataCities.get(mSpinerDestination.getSelectedItemPosition()).cityId;
        couriercek = mSpinerCourier.getSelectedItem().toString();
        int weight = Integer.parseInt(mWeight.getText().toString());
        String content = "origin=" + originCek + "&destination=" + destinationCek + "&weight=" + weight + "&courier=" + couriercek.toLowerCase();
        ArrayList<DataCost> DataCost = new ArrayList<DataCost>();

        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(content, mediaType);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("key", "ff66dfc01b6a9905f0bd919c0d56b95f")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.w("Error", "WOW "+e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    JSONObject[] jsons =null;
                    ArrayList<JSONObject> arrays = new ArrayList<JSONObject>();
                    try{
                        JSONObject responseJSON = new JSONObject(response.body().string());
                        JSONArray dataArray = responseJSON.getJSONObject("rajaongkir")
                                .getJSONArray("results")
                                .getJSONObject(0)
                                .getJSONArray("costs");
                        for (int i = 0; i < dataArray.length();i++){
                            JSONObject otherJSONObject = dataArray.getJSONObject(i);
                            arrays.add(otherJSONObject);
                        }
                        jsons = new JSONObject[arrays.size()];
                        arrays.toArray(jsons);
                        List<DataCost> temp = new ArrayList<DataCost>();
                        for(int i = 0; i< dataArray.length();i++){
                            JSONObject tempCost = jsons[i]
                                    .getJSONArray("cost")
                                    .getJSONObject(0);
                            temp.add(new DataCost(
                                    Integer.parseInt(tempCost.getString("value")),
                                    tempCost.getString("etd"),
                                    tempCost.getString("note")
                            ));
                            dataCosts.add(new DataType(
                                    jsons[i].getString("service"),
                                    jsons[i].getString("description"),
                                    temp
                            ));

                            Log.d("IN DATATYPE", "Service : "+dataCosts.get(i).service );

                        }
                    }catch (Exception e){

                    }

                }
            });
        }catch (Exception e){
            Log.w("ERROR", "TEST : "+e.getMessage());
        }
        String origin = dataCities.get(mSpinerOrigin.getSelectedItemPosition()).cityName;
        String destination = dataCities.get(mSpinerDestination.getSelectedItemPosition()).cityName;
        mydb.addRecord(new costModels(originCek, destinationCek, weight, couriercek,
                dataCities.get(mSpinerOrigin.getSelectedItemPosition()).cityName,
                dataCities.get(mSpinerDestination.getSelectedItemPosition()).cityName));
        ArrayList<HashMap<String, String>> costList = mydb.getAllRecord();
        ListView lv = (ListView) findViewById(R.id.user_list);
        ListAdapter adapter = new SimpleAdapter(MainActivity.this, costList, R.layout.list_row,new String[]
                {"origin-name","destination-name"}, new int[]{R.id.name, R.id.designation});
        lv.setAdapter(adapter);

        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        // memanggil method untuk menampilkan NOTIFICATION_ID
        // dengan mengirimkan data yang dikirim dari komponen EditText
        tampilNotifikasi("JUDUL"
                , "Pesan", in);

        Intent intent = new Intent(this, BiayaActivity.class); //Replace Class
//        intent.putExtra("data", dataCosts); //Send This to Other Activity
        intent.putExtra("weight", mWeight.getText().toString());
        intent.putExtra("destination", destination);
        intent.putExtra("origin", origin);
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST",(Serializable)dataCosts);
        intent.putExtra("BUNDLE",args);
        startActivity(intent);


    }


    private void tampilNotifikasi(String s, String s1, Intent intent) {
        // membuat komponen pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this
                , NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) MainActivity.this
                .getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
            notificationChannel.enableVibration(true);
            notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // membuat komponen
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Notification notification;
        notification = builder
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVibrate(new long[]{100, 200, 100, 200})
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(s)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(MainActivity.this.getResources()
                        , R.mipmap.ic_launcher))
                .setContentText(s1)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

}