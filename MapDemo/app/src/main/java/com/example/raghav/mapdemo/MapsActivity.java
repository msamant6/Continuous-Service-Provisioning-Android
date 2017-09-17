package com.example.raghav.mapdemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;
import android.net.Uri;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, ConnectionCallbacks,
            OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 60 * 1000; // 30 seconds
    private long FASTEST_INTERVAL = 2 * 1000; // 2 seconds
    private double latitude;
    private double longitude;
    JSONObject[] final_results = new JSONObject[3];
    private List<String> restNames = new ArrayList<String>();
    private List<String> cuisine = new ArrayList<String>();
    private String favCuisines = "";
    Bundle instanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instanceState = savedInstanceState;
        setContentView(R.layout.result);
    }

    public void sendMessage(View view) {
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    public String getCuisine(int i) {
        String str = "";
        switch (i){
            case 0:
                str = "American";
                break;
            case 1:
                str = "Mexican";
                break;
            case 2:
                str = "Italian";
                break;
            case 3:
                str = "Mediterranean";
                break;
            case 4:
                str = "Asian";
                break;
            case 5:
                str = "Buffet";
                break;
            case 6:
                str = "FastFood";
                break;
            case 7:
                str = "Cafes";
                break;
            case 8:
                str = "Spirits";
                break;
            case 9:
                str = "Middle Eastern";
                break;
            case 10:
                str = "Indian";
                break;
            case 11:
                str = "Barbeque";
                break;
            case 12:
                str = "Caribbean";
                break;
        }
       return str;
    }
    public void RestbyPreference(final View view) {
        Log.d("Debug","Inside RestbyPreference");
        final boolean[] checkedItems = new boolean[13];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Cuisine)
                .setMultiChoiceItems(R.array.cuisine_array, checkedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                String str = "";
                                str = getCuisine(i);
                                if(b){
                                    Log.d("Adding into cuisines : ", str);
                                    cuisine.add(str);
                                }
                                else if(cuisine.contains(str)){
                                    Log.d("Removing from cuisines : ", str);
                                    cuisine.remove(str);
                                    checkedItems[i] =false;
                                    ((AlertDialog) dialogInterface).getListView().setItemChecked(i, false);
                                }
                            }
                        })
                .setPositiveButton(R.string.Submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int itr = 0;
                        String str = "";
                        for(itr = 0; itr < cuisine.size(); itr++){
                            if(cuisine.get(itr) == "Asian"){
                                str += "Chinese," + "Japanese," + "Thai," + "Taiwanese," + "sushi" + "Korean," + "Vietnamese,";
                            }
                            else if (cuisine.get(itr) == "Buffet"){
                                str += "Buffet," + "Diner," + "Seafood,";
                            }
                            else if (cuisine.get(itr) == "FastFood"){
                                str += "Fast Food," + "Burgers," + "Pizza," + "Hotdogs," + "Sandwiches," + "Chicken Wings,";
                            }
                            else if (cuisine.get(itr) == "Cafes"){
                                str += "Cafes," + "Bakery," + "Ice Cream," + "gourmet,";
                            }
                            else if (cuisine.get(itr) == "Spirits"){
                                str += "Spirits," + "Bar," + "Beer," + "Wine,";
                            }
                            else if (cuisine.get(itr) == "Italian"){
                                str += "Italian," + "French," + "Modern European,";
                            }
                            else if(cuisine.get(itr) == "Barbeque"){
                                str += "Barbeque," + "Chicken Wings,";
                            }
                            else{
                                str += cuisine.get(itr) + ",";
                            }
                        }
                        favCuisines = str;
                        Log.d("FavCuisines of user : ",favCuisines);
                        Log.d("Debug : ", "Before calling sendMessage");
                        sendMessage(view);
                    }
                })
                .setNegativeButton(R.string.Reset, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cuisine.clear();
                        for(int itr = 0; itr < checkedItems.length; itr++) {
                            checkedItems[itr] = false;
                            ((AlertDialog) dialogInterface).getListView().setItemChecked(itr, false);
                        }
                    }
                });
                builder.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Get last known location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Connection failed", "Bad Permission");
            return;
        }
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // This is NULL if Last Location isn't known
        if (mCurrentLocation != null) {
            Log.d("DEBUG", "current location:" + mCurrentLocation.toString());
            String msg = "Last Location: " +
                    Double.toString(mCurrentLocation.getLatitude()) + "," +
                    Double.toString(mCurrentLocation.getLongitude());
        }
        // Start polling for location updates
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        // Set the parameters for the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL)
                .setSmallestDisplacement(250);
        // Request Location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Connection failed", "Bad Update Permission");
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        LatLng currLoc = new LatLng(latitude, longitude);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(currLoc).title("You are here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLoc,14));

        // When location changes, request new nearest neighbours from the server
        String urlString = "http://10.0.0.97:8000/nearby/" + String.valueOf(latitude) + "/" + String.valueOf(longitude) + "/" + favCuisines + "/";
        Log.d("Req to server : ", urlString);
        new JsonTask(getApplicationContext()).execute(urlString);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class NotificationReceiverActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.result);
        }
    }

    private class JsonTask extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        private Context context;

        public JsonTask(Context context){
            this.context = context;
        }

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MapsActivity.this);
            pd.setMessage("Fetching places nearby...");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {

            /* Test */
            long timeReq = 0;
            long timeResp = 0;
            /* Test */

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                timeReq = System.currentTimeMillis();
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);
                }
                timeResp = System.currentTimeMillis();
                long timeTaken = timeResp-timeReq;
                Log.d("Time taken for REST API",String.valueOf(timeTaken));
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            int restCount = 0;

            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }

            try {
                JSONArray jsonArr = new JSONArray(result);
                JSONObject obj = null;
                double dist = 0, min_dist_1 = 999, min_dist_2 = 999, min_dist_3 = 999;
                if(jsonArr.length() > 3) {
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        double jsonLat = jsonObj.getDouble("latitude");
                        double jsonLon = jsonObj.getDouble("longitude");
                        dist = Math.sqrt(Math.pow((jsonLat - latitude), 2) + Math.pow((jsonLon - longitude), 2));

                        if (dist < min_dist_3) {
                            if (dist < min_dist_2) {
                                if (dist < min_dist_1) {
                                    min_dist_1 = dist;
                                    final_results[0] = jsonObj;
                                    restCount++;
                                } else {
                                    min_dist_2 = dist;
                                    final_results[1] = jsonObj;
                                    restCount++;
                                }
                            } else {
                                min_dist_3 = dist;
                                final_results[2] = jsonObj;
                                restCount++;
                            }
                        }
                    }
                }
                else {
                    for (int i = 0; i < jsonArr.length(); i++) {
                        final_results[i] =  jsonArr.getJSONObject(i);
                        restCount++;
                    }
                }

                Log.d("restCount - ", Integer.toString(restCount));
                if(restCount > final_results.length){
                    restCount = final_results.length;
                }
                for (int itr = 0; itr < restCount; itr++) {
                    double jsonLat = final_results[itr].getDouble("latitude");
                    double jsonLon = final_results[itr].getDouble("longitude");

                    LatLng loc1 = new LatLng(jsonLat, jsonLon);
                    mMap.addMarker(new MarkerOptions().position(loc1).title(final_results[itr].getString("name")));
                    Log.d("Adding Marker for -", final_results[itr].getString("name"));
                }

                showNotification(restCount);
            } catch (Exception e) {
                Log.d("Unable to", "create JSONObject");
                String msg = "No restaurants matching your preferences nearby";
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }
        public void showNotification(int restCount){

            Log.d("showNotification","Inside");
            Intent intent = new Intent(this.context, NotificationReceiverActivity.class);
            //PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
            PendingIntent pIntent = PendingIntent.getActivity(this.context, (int) System.currentTimeMillis(), intent, 0);
            long[] pattern = {300,300,300};
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // Build notification
            try {
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                for(int itr = 0; itr < restCount; itr++) {
                    if(false == restNames.contains(final_results[itr].getString("name"))) {
                        Notification noti = new Notification.Builder(this.context)
                                .setContentTitle(final_results[itr].getString("name"))
                                .setContentText(final_results[itr].getString("phone")).setSmallIcon(R.drawable.yelp)
                                .setContentIntent(pIntent)
                                .setVibrate(pattern)
                                .setSound(sound)
                                .addAction(R.drawable.yelp, final_results[itr].getString("url"), pIntent)
                                .build();

                        // hide the notification after its selected
                        noti.flags |= Notification.FLAG_AUTO_CANCEL;

                        notificationManager.notify(itr, noti);
                        Log.d("restNames size - ", Integer.toString(restNames.size()));
                        if(true != restNames.contains(final_results[itr].getString("name"))){
                            restNames.add(final_results[itr].getString("name"));
                            Log.d("Added into restNames - ", restNames.get(itr));
                        }
                    }
                    final_results[itr] = null;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
