package com.example.speedometer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Date display
        setDate();
        //added because new access fine location policies, imported class..
        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        //check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            //start the program if permission is granted
            doStuff();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        TextView txt = (TextView) this.findViewById(R.id.textView);
    //        Address
        TextView txtAddress = (TextView) this.findViewById(R.id.textLocation);
//        TextView txtWeather = (TextView) this.findViewById(R.id.textWeather);
        double latitude=location.getLatitude();
        double longitude=location.getLongitude();
        if (location == null) {
            //  Speed
            //  txt.setText("-.- km/h");
            txt.setText("");
            //  Address
            txtAddress.setText("");
            // Weather

        } else {
            // Speed Calculation
            float nCurrentSpeed = location.getSpeed() * 3.6f;
    //            txt.setText(String.format("%.2f", nCurrentSpeed)+ " km/h" );
            // Address Display
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String address =
                    addresses.get(0).getAddressLine(0);

//            //Weather Display - Logic
//            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//          String url = "https://api.openweathermap.org/data/2.5/weather?lat="+location.getLatitude()+"&lon="+location.getLongitude()+"&appid=76521916532457ce34d3a85912ee7774";
//
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
//
//
//                @Override
//                public void onResponse(JSONObject response) {
//
//                    try {
//                        JSONObject array= (JSONObject) response.get("main");
//                        //JSONArray array=  response.getJSONArray("main");
//                        Double t=Float.parseFloat(array.getString("temp"))-273.15;
//                        txtWeather.setText(String.format("%.0f", t));
//                        //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
//                    } catch (JSONException e) {
//                        txtWeather.setText(e.getMessage());
//                        e.printStackTrace();
//                    }
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                    txtWeather.setText(error.toString());
//                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
//                }
//            });
//            queue.add(request);

            txtAddress.setText(address);
            txt.setText(String.format("%.0f", nCurrentSpeed));
        }

//            Toast.makeText(getApplicationContext(),"Lat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //added according to suggestion
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doStuff();
            } else {
                finish();
            }
        }
    }

    //GPS location access permission
    @SuppressLint("MissingPermission")
    private void doStuff() {
     LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            // this.onLocationChanged(null);
        }
        Toast.makeText(this, "Waiting for GPS connection!", Toast.LENGTH_SHORT).show();
    }

    //Date Display function
    private void setDate(){
        TextView dateTimeDisplay = (TextView) this.findViewById(R.id.text_date_display);
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy '      ' hh:mm aaa");
        String date = dateFormat.format(today);
        dateTimeDisplay.setText(date);
    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}