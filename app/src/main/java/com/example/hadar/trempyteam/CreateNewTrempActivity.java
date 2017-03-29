package com.example.hadar.trempyteam;

import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CreateNewTrempActivity extends Activity {

    public static final int  REQUEST_CODE_ASK_PERMISSIONS = 1;

    private GoogleMap googleMap;
    final int main = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_tremp);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {


                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {


                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {

            // create class object
            GPSTracker gps = new GPSTracker(CreateNewTrempActivity.this);

            // check if GPS enabled
            if (gps.canGetLocation()) {

                final double latitude = gps.getLatitude();
               final double  longitude = gps.getLongitude();

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                TextView exitFromAddr = (TextView) findViewById(R.id.exitfrom);

                try {
                    List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (null != listAddresses && listAddresses.size() > 0) {

                        String _Location = listAddresses.get(0).getAddressLine(0);
                        String contry = listAddresses.get(0).getLocality();
                        String g = listAddresses.get(0).getCountryName();

                        exitFromAddr.setText(_Location);

                        // Click on the "+" button to add a new student
                        Button addBtn = (Button) findViewById(R.id.btnSave);
                        addBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                TextView toDest = (TextView) findViewById(R.id.dest);
                                toDest.getText().toString();
                                TextView source = (TextView) findViewById(R.id.exitfrom);
                                source.getText().toString();

                                LatLng c =  getLocationFromAddress(CreateNewTrempActivity.this, toDest.getText().toString());
                                LatLng s =  getLocationFromAddress(CreateNewTrempActivity.this, source.getText().toString());
                             //   LatLng s = new LatLng(latitude, longitude);

                                Intent intent = new Intent(CreateNewTrempActivity.this, MapsActivity.class);
                                intent.putExtra("DestLocation", c);
                                intent.putExtra("SourceLocation", s);
                                startActivityForResult(intent, main);



                            }
                        });

                        // \n is for new line=
                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude + "\nAddress: " + _Location + "\nCon: " + g + "\ngggg: " + contry, Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }





        public LatLng getLocationFromAddress(Context context, String strAddress)
        {
            Geocoder coder= new Geocoder(context);
            List<Address> address;
            LatLng p1 = null;

            try
            {
                address = coder.getFromLocationName(strAddress, 5);
                if(address==null)
                {
                    return null;
                }
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();

                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return p1;

        }





    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // create class object
                    GPSTracker gps = new GPSTracker(CreateNewTrempActivity.this);

                    // check if GPS enabled
                    if (gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try {
                            List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                            if(null!=listAddresses&&listAddresses.size()>0){
                                String _Location = listAddresses.get(0).getAddressLine(0);
                                // \n is for new line
                                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude + "\nAddress: " + _Location, Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }


    }
}


