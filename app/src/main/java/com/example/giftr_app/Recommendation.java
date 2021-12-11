package com.example.giftr_app;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

public class Recommendation extends AppCompatActivity implements View.OnClickListener{

    ListView listView;
  public String link[] ; //= {"www.amazon.com/squid_game","www.ali-express.com/toys","www.wish.com/bracelet"};
    public String price[];// = {"2.99", "4.99", "12.97"};
    public int icons[] = {R.drawable.amz, R.drawable.aliexpress, R.drawable.wish};
    public String []title;

    Friend friend;
    String userId;
    float min;
    float max;

    String[] interestArray;
    ArrayList<String> interestAList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.rec_listview_layout);
        findViewById(R.id.birthdays).setOnClickListener(this);
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
//        userId = (String) bundle.getSerializable("userId");

        try {

            friend = (Friend) bundle.getSerializable("friend");
            userId = (String) bundle.getSerializable("userId");
            min = friend.getMinPrice();
            max = friend.getMaxPrice();

            String s = friend.getStringInterests();

            //Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
            interestArray = s.split(",");
            //move array into arraylist
            interestAList.addAll(Arrays.asList(interestArray));


            if(interestAList.isEmpty()){
                Toast.makeText(this,"No recommendations yet, please set interests for your friends",Toast.LENGTH_SHORT).show();
            }
            else{
                for(String f : interestAList){
                    fetchAmzData(f);

                }
            }
          //  fetchAliData("sports");
          //  fetchAliData("music");

            //fetchAmzData("sports");

        } catch (IOException e) {
            e.printStackTrace();
        }
         // listViewAdapter();

    }
    public void fetchAliData(String interest) throws IOException {


        listView = findViewById(R.id.listView);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();


        //"https://magic-aliexpress1.p.rapidapi.com/api/products/search?minSalePrice=5&sort=ORDER_DESC&page=1&maxSalePrice=20&name="+interest

        Request request = new Request.Builder()
                .url("https://magic-aliexpress1.p.rapidapi.com/api/products/search?minSalePrice=" + min + "&sort=ORDER_DESC&page=1&maxSalePrice=" + max + "&name="+interest)
                .get()
                .addHeader("x-rapidapi-host", "magic-aliexpress1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "4618320329mshbc0568d0efbf48ap11ff75jsn6ce363a6a28a")
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                Toast.makeText(Recommendation.this, "Error +", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String resp = response.body().string();

                if (response.isSuccessful()) {

                    //https://stackoverflow.com/questions/24246783/okhttp-response-callbacks-on-the-main-thread
                    //call back runs on background thread, need to put it back on the main thread
                    Recommendation.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONObject jsonObject = new JSONObject(resp);
                                JSONArray jArray = jsonObject.getJSONArray("docs");
                                title = new String[jArray.length()];
                                price = new String[jArray.length()];
                                link = new String[jArray.length()];

                                for (int i = 0; i < jArray.length(); i++) {

                                    JSONObject obj = jArray.getJSONObject(i);

                                    String product_title = obj.getString("product_title");
                                    String product_price = obj.getString("app_sale_price");
                                    String product_url = obj.getString("product_detail_url");

                                    //converting to CAD
                                    double d = Double.valueOf(product_price) * 1.26;
                                    String s = Double.toString(d);

                                    title[i] = product_title;
                                    price[i] = s;
                                    link[i] = product_url;

                                }

                                MyAdapter myAdapter = new MyAdapter(getApplicationContext(), link,  R.drawable.aliexpress,price,title);

                                listView.setAdapter(myAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

            }

        });

    }

    public void fetchAmzData(String interest) throws IOException {


        listView = findViewById(R.id.listView);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();


        Request request = new Request.Builder()
                .url("https://amazon23.p.rapidapi.com/product-search?query="+interest)
                .get()
                .addHeader("x-rapidapi-host", "amazon23.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "4618320329mshbc0568d0efbf48ap11ff75jsn6ce363a6a28a")
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                Toast.makeText(Recommendation.this, "Error +", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String resp = response.body().string();

                if (response.isSuccessful()) {

                    //https://stackoverflow.com/questions/24246783/okhttp-response-callbacks-on-the-main-thread
                    //call back runs on background thread, need to put it back on the main thread
                    Recommendation.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                JSONObject jsonObject = new JSONObject(resp);
                                JSONArray jArray = jsonObject.getJSONArray("result");
                                title = new String[jArray.length()];
                                price = new String[jArray.length()];
                                link = new String[jArray.length()];

                                for (int i = 0; i < jArray.length(); i++) {

                                    JSONObject obj = jArray.getJSONObject(i);

                                    String product_title = obj.getString("title");
                                    String product_price = obj.getString("price");
                                    JSONObject jsonObjectPrice = new JSONObject(product_price);
                                    String actualPrice = jsonObjectPrice.getString("current_price");

                                    String product_url = obj.getString("url");


                                    title[i] = product_title;
                                    price[i] = actualPrice;
                                    link[i] = product_url;


                                }

                                MyAdapter myAdapter = new MyAdapter(getApplicationContext(), link,  R.drawable.amz,price,title);

                                listView.setAdapter(myAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

            }

        });

    }

    @Override
    public void onClick(View view) {

  if(view.getId() == R.id.birthdays){

            Intent intent = new Intent(Recommendation.this, ClosestBday.class);

            Bundle info = new Bundle();

            //putting email in bundle
            info.putSerializable("userId", userId);
            // info.putSerializable("friend", friend);
            intent.putExtras(info);
            startActivity(intent);


        }

        else if(view.getId() == R.id.settings){

            Intent intent = new Intent(Recommendation.this, ClosestBday.class);

            Bundle info = new Bundle();

            //putting email in bundle
            info.putSerializable("userId", userId);
            // info.putSerializable("friend", friend);
            intent.putExtras(info);
            startActivity(intent);


        }

    }


}




