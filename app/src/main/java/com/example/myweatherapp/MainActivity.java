package com.example.myweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static final String API_KEY = "74297b755872182c3cd39c022f90da1a";
    EditText edtProvince;
    Button btnOK;
    TextView txtProvince;
    TextView txtNational;
    ImageView imgWeatherIcon;
    TextView txtTemper;
    TextView txtTemperState;
    TextView txtCloud;
    TextView txtHumidity;
    TextView txtWind;
    TextView txtCurentTemper;
    TextView txtSunrise;
    TextView txtSunset;

    String city = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        btnOK.setOnClickListener(this);
        if(city == "") {
            getJsonWeather("saigon");
        }
        else {
            getJsonWeather(city);
        }

    }

    public void getJsonWeather(String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+API_KEY+"&units=metric";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject weatherObj = weatherArray.getJSONObject(0);
                            String icon = weatherObj.getString("icon");
                            String urlIcon = "https://openweathermap.org/img/wn/"+icon+".png";
                            //đổ dữ liệu ra màn hình
                            Picasso.get().load(urlIcon).into(imgWeatherIcon);

                            //trạng thái hiện tại
                            String temperState = weatherObj.getString("main");
                            //đổ dữ liệu ra màn hình
                            txtTemperState.setText(temperState);

                            JSONObject main = response.getJSONObject("main");
                            //nhiệt độ
                            String temp = main.getString("temp");
                            txtTemper.setText(temp + "°C");

                            //độ ẩm
                            String humidity = main.getString("humidity");
                            txtHumidity.setText(humidity + "%");

                            JSONObject wind = response.getJSONObject("wind");
                            String speed = wind.getString("speed");
                            txtWind.setText(speed + "m/s");

                            JSONObject clouds = response.getJSONObject("clouds");
                            //phần trăm mây
                            String all = clouds.getString("all");
                            txtCloud.setText(all + "%");

                            String sNgay = response.getString("dt");
                            long lNgay = Long.parseLong(sNgay);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, yyyy-MM-dd HH:mm:ss");
                            Date date = new Date(lNgay * 1000);
                            //ngày giờ hiện tại
                            String currentTime = dateFormat.format(date);
                            txtCurentTemper.setText(currentTime);

                            //lấy tên thành phố
                            String name = response.getString("name");
                            txtProvince.setText("City/Province: " + name);

                            //lấy tên quốc gia
                            JSONObject sys = response.getJSONObject("sys");
                            String country = sys.getString("country");
                            txtNational.setText("Country: " + country);

                            //lấy giờ bình minh
                            String hSunrise = sys.getString("sunrise");
                            long lSunrise = Long.parseLong(hSunrise);
                            SimpleDateFormat sunriseFormat = new SimpleDateFormat("HH:mm:ss");
                            Date hForSunrise = new Date(lSunrise * 1000);
                            String sunrise = sunriseFormat.format(hForSunrise);
                            txtSunrise.setText("Sunrise: " + sunrise);

                            //lấy giờ hoàng hôn
                            String hSunset = sys.getString("sunset");
                            long lSunset = Long.parseLong(hSunset);
                            SimpleDateFormat sunsetFormat = new SimpleDateFormat("HH:mm:ss");
                            Date hForSunset = new Date(lSunset * 1000);
                            String sunset = sunsetFormat.format(hForSunset);
                            txtSunset.setText("Sunset: " + sunset);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Không có dữ liệu cho thành phố " + city,Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void mapping() {
        edtProvince = findViewById(R.id.edtProvince);
        btnOK = findViewById(R.id.btnOK);
        txtProvince = findViewById(R.id.txtProvince);
        txtNational = findViewById(R.id.txtNational);
        imgWeatherIcon = findViewById(R.id.imgWeatherIcon);
        txtTemper = findViewById(R.id.txtTemper);
        txtTemperState = findViewById(R.id.txtTemperState);
        txtCloud = findViewById(R.id.txtCloud);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtWind = findViewById(R.id.txtWind);
        txtCurentTemper = findViewById(R.id.txtCurentTemper);
        txtSunrise = findViewById(R.id.txtSunrise);
        txtSunset = findViewById(R.id.txtSunset);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOK:
                city = edtProvince.getText().toString().trim();
                if (city.equals(""))
                    city = "saigon";
                getJsonWeather(city);
                break;
        }
    }
}