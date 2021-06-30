package com.shakshi.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.VoiceInteractor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    ImageButton imageView;
    TextView coun_try,ci_ty,temp_yt,date_time,latiTude,longiTude,humidity,sunrise,sunset,pressure,wind_speed;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.cityName);
        button = findViewById(R.id.searchButton);
        imageView = findViewById(R.id.imageButton);
        coun_try = findViewById(R.id.country);
        ci_ty = findViewById(R.id.city);
        temp_yt = findViewById(R.id.tempErature);
        date_time = findViewById(R.id.dateTime);
        latiTude = findViewById(R.id.lati);
        longiTude = findViewById(R.id.longi);
        humidity = findViewById(R.id.humi);
        sunrise = findViewById(R.id.sunr);
        sunset = findViewById(R.id.suns);
        pressure = findViewById(R.id.press);
        wind_speed = findViewById(R.id.winds);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                findWeather();

            }
        });



    }

    public void findWeather(){

        final String city = editText.getText().toString();
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=2c9428c059979e62796ed864e1690a0b";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //calling api
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    //find country
                    JSONObject object1 = jsonObject.getJSONObject("sys");
                    String findCountry = object1.getString("country");
                    coun_try.setText(findCountry);

                    //find city
                    String findCity = jsonObject.getString("name");
                    ci_ty.setText(findCity);

                    //find temperature
                    JSONObject object2 = jsonObject.getJSONObject("main");
                    double findTemperature = object2.getDouble("temp");
                    double d = findTemperature-273.15;
                    String temp = String.format("%.2f", d);
                    temp_yt.setText(temp+"°C");

                    //find icon
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    JSONObject obj = jsonArray.getJSONObject(0);
                    String img = obj.getString("icon");
                    Picasso.get().load("http://openweathermap.org/img"+img+"@2x.png").into(imageView);

                    //add date and time
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd \nHH:mm:ss");
                    String date = simpleDateFormat.format(calendar.getTime());
                    date_time.setText(date);

                    //find latitude
                    JSONObject object3 = jsonObject.getJSONObject("coord");
                    double findLatitude = object3.getDouble("lat");
                    latiTude.setText(findLatitude+" °N");

                    //find longitude
                    JSONObject object4 = jsonObject.getJSONObject("coord");
                    double findLongitude = object4.getDouble("lon");
                    longiTude.setText(findLongitude+" °S");

                    //find humidity
                    JSONObject object5 = jsonObject.getJSONObject("main");
                    int findHumidity = object5.getInt("humidity");
                    humidity.setText(findHumidity+" %");

                    //find Sunrise
                    JSONObject object6 = jsonObject.getJSONObject("sys");
                    String findSunrise = object6.getString("sunrise");
                    sunrise.setText(findSunrise);

                    //find Sunset
                    JSONObject object7 = jsonObject.getJSONObject("sys");
                    String findSunset = object7.getString("sunset");
                    sunset.setText(findSunset);

                    //find pressure
                    JSONObject object8 = jsonObject.getJSONObject("main");
                    String findPressure = object8.getString("pressure");
                    pressure.setText(findPressure+" hPa");

                    //find windspeed
                    JSONObject object9 = jsonObject.getJSONObject("wind");
                    double findWindspeed = object9.getDouble("speed");
                    double ws = findWindspeed*3.6;
                    String windspe = String.format("%.2f", ws);
                    wind_speed.setText(windspe+ "km/h");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }
}