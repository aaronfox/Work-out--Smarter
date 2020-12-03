package edu.cse570afox.workoutsmarter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class GymMap extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "GymMap";
    final int PERMISSION_REQUEST_LOCATION = 101;

    GoogleMap gMap;
    FusedLocationProviderClient fusedLocationClient;
//    LocationManager locationManager;
//    LocationListener gpsListener;
    LocationCallback locationCallback;
    LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_map);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        createLocationRequest();
        createLocationCallback();

        // Init buttons
        initHistoryButton();
        initMainButton();
        initGetLocationButton();

//        int PLACE_PICKER_REQUEST = 1;
//        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PLACE_PICKER_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlacePicker.getPlace(data, this);
//                String toastMsg = String.format("Place: %s", place.getName());
//                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
//            }
//        }
//    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "On Map Ready is called");
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//        RadioButton rbNormal = findViewById(R.id.radioButtonNormal);
//        rbNormal.setChecked(true);

//        putPointsOnTheMap();
//        findGymsOnMap();

        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(GymMap.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(GymMap.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Snackbar.make(findViewById(R.id.activity_gym_map),
                                "MyContactList requires this permission to locate " + "your contacts",
                                Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                ActivityCompat.requestPermissions(GymMap.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                            }
                        }).show();
                    } else {
                        ActivityCompat.requestPermissions(GymMap.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                    }
                } else {
                    startLocationUpdates();
                }
            } else {
                startLocationUpdates();
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error requesting permission", Toast.LENGTH_LONG).show();
        }
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    TextView textLongitude = findViewById(R.id.textLongitude);
                    TextView textLatitude = findViewById(R.id.textLatitude);
                    TextView textAccuracy = findViewById(R.id.textAccuracy);
                    textLongitude.setText("Lat: " + location.getLatitude());
                    textLatitude.setText("Long: " + location.getLongitude());
                    textAccuracy.setText("Acc: " + location.getAccuracy());

//                    URL url = null;
//                    try {
//                        url = new URL("http://www.google.com/");
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    }
//                    HttpURLConnection urlConnection = null;
//                    try {
//                        urlConnection = (HttpURLConnection) url.openConnection();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        InputStream in = null;
//                        try {
//                            in = new BufferedInputStream(urlConnection.getInputStream());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        Log.v(TAG, "!!!! " + in);
//                    } finally {
//                        urlConnection.disconnect();
//                    }
//                    Thread thread = new Thread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            try  {
//                                //Your code goes here
//                                URL url;
//                                HttpURLConnection urlConnection = null;
//                                try {
//                                    url = new URL("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=gym&inputtype=textquery&fields=photos,formatted_address,name,opening_hours,rating&locationbias=circle:2000@47.6918452,-122.2226413&key=AIzaSyAqiKVvDYBdWeDvCapJS3OfsyXMvSvA7Mk");
//
//                                    urlConnection = (HttpURLConnection) url
//                                            .openConnection();
//
//                                    InputStream in = urlConnection.getInputStream();
//
//                                    InputStreamReader isw = new InputStreamReader(in);
//
//                                    int data = isw.read();
//                                    StringBuilder s = new StringBuilder();
//                                    while (data != -1) {
//                                        char current = (char) data;
//                                        data = isw.read();
//                                        s.append(data);
//                                        Log.w(TAG, "!!!! " + current);
//                                    }
//                                    Log.w(TAG, "!!!!" + s.toString());
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                } finally {
//                                    if (urlConnection != null) {
//                                        urlConnection.disconnect();
//                                    }
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                    thread.start();


//                    Toast.makeText(getBaseContext(), "Lat: " + location.getLatitude() +
//                                    " Long: " + location.getLongitude() +
//                                    " Accuracy:  " + location.getAccuracy(),
//                            Toast.LENGTH_SHORT).show();
                }
            };
        };
    }

    private void startLocationUpdates() {

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        gMap.setMyLocationEnabled(true);
    }

    private void stopLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void initHistoryButton() {
        ImageButton ibHistory = findViewById(R.id.historyButton);
        ibHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(GymMap.this, WorkoutHistory.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initMainButton() {
        ImageButton ibMain = findViewById(R.id.mainButton);
        ibMain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(GymMap.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initGetLocationButton() {
        Button locationButton = (Button) findViewById(R.id.buttonGetLocation);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String urlString = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=Museum%20of%20Contemporary%20Art%20Australia&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry&key=AIzaSyAqiKVvDYBdWeDvCapJS3OfsyXMvSvA7Mk"
//                try {
//                    URL url = new URL(urlString);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
                try {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(GymMap.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            if (ActivityCompat.shouldShowRequestPermissionRationale(GymMap.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                                Snackbar.make(findViewById(R.id.activity_gym_map),
                                        "Work(out) Smarter requires this permission to locate " + "gyms near you",
                                        Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        ActivityCompat.requestPermissions(GymMap.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                                    }
                                }).show();
                            } else {
                                ActivityCompat.requestPermissions(GymMap.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                            }
                        } else {
                            startLocationUpdates();
                        }
                    } else {
                        startLocationUpdates();
                    }
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Error requesting permission", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // startLocationUpdates();
                } else {
                    Toast.makeText(GymMap.this, "Work(out) Smarter will not locate your contacts.",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        try {
            // locationManager.removeUpdates(gpsListener);
        }
        catch (Exception e){
            e.printStackTrace();
        }
//        stopLocationUpdates();
    }

//    private void putPointsOnTheMap() {
//        Point size = new Point();
//        WindowManager w = getWindowManager();
//        w.getDefaultDisplay().getSize(size);
//        int measuredWidth = size.x;
//        int measuredHeight = size.y;
//
//        if (gyms.size()>0) {
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            for (int i=0; i<contacts.size(); i++) {
//                currentContact = contacts.get(i);
//
//                Geocoder geo = new Geocoder(this);
//                List<Address> addresses = null;
//
//                String address = currentContact.getStreetAddress() + ", " +
//                        currentContact.getCity() + ", " +
//                        currentContact.getState() + " " +
//                        currentContact.getZipCode();
//
//                try {
//                    addresses = geo.getFromLocationName(address, 1);
//                }
//                catch (IOException e) {
//                    e.printStackTrace();
//                }
//                LatLng point = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
//                builder.include(point);
//
//                gMap.addMarker(new MarkerOptions().position(point).title(currentContact.getContactName()).snippet(address));
//            }
//            gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), measuredWidth, measuredHeight, 450));
//        }
//        else {
//            if (currentContact != null) {
//                Geocoder geo = new Geocoder(this);
//                List<Address> addresses = null;
//
//                String address = currentContact.getStreetAddress() + ", " +
//                        currentContact.getCity() + ", " +
//                        currentContact.getState() + " " +
//                        currentContact.getZipCode();
//
//                try {
//                    addresses = geo.getFromLocationName(address, 1);
//                }
//                catch (IOException e) {
//                    e.printStackTrace();
//                }
//                LatLng point = new LatLng(addresses.get(0).getLatitude(),addresses.get(0).getLongitude());
//
//                gMap.addMarker(new MarkerOptions().position(point).title(currentContact.getContactName()).snippet(address));
//                gMap.animateCamera(CameraUpdateFactory. newLatLngZoom(point, 16));
//            }
//            else {
//                AlertDialog alertDialog = new AlertDialog.Builder(ContactMapActivity.this).create();
//                alertDialog.setTitle("No Data");
//                alertDialog.setMessage("No data is available for the mapping function.");
//                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    } });
//                alertDialog.show();
//            }
//        }
//    }


}