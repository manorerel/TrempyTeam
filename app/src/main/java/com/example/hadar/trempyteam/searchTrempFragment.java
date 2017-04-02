package com.example.hadar.trempyteam;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hadar.trempyteam.Model.Model;
import com.example.hadar.trempyteam.Model.ModelFirebase;
import com.example.hadar.trempyteam.Model.Tremp;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class searchTrempFragment extends Fragment {

    //h
    FragmentManager fragmentManager;
    public static final int  REQUEST_CODE_ASK_PERMISSIONS = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the fragment layout file
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.search_tremp, container, false);
        try {
            ActionBar actionBar = getActivity().getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            setHasOptionsMenu(true);
        }
        catch (Exception e){
            Log.d("exception:" , "exception while trying to set action bar " + e.getMessage());
        }


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {


                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {


                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {


            // create class object
            GPSTracker gps = new GPSTracker(getActivity());

            // check if GPS enabled
            if (gps.canGetLocation()) {

                final double latitude = gps.getLatitude();
                final double longitude = gps.getLongitude();

                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                EditText source = (EditText) rootView.findViewById(R.id.from);
                try {
                    List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (null != listAddresses && listAddresses.size() > 0) {

                        String _Location = listAddresses.get(0).getAddressLine(0) + " " + listAddresses.get(0).getLocality();
                        String contry = listAddresses.get(0).getLocality();
                        String g = listAddresses.get(0).getCountryName();

                        source.setText(_Location);


                        Button btnSearch = (Button) rootView.findViewById(R.id.btnSearch);
                        EditText dest = (EditText) rootView.findViewById(R.id.destination);

                        btnSearch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                EditText dest = (EditText) rootView.findViewById(R.id.destination);
                                EditText from = (EditText) rootView.findViewById(R.id.from);
                                Intent intent = new Intent(getActivity(), ListTrempActivity.class);
                                String d = dest.getText().toString();
                                String f = from.getText().toString();
                                intent.putExtra("dest", d);
                                intent.putExtra("from", f);
                                startActivity(intent);

                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        Log.d("TAG", "handle action bar");
        inflater.inflate(R.menu.menu_buttons, menu);

        MenuItem personalArea =  menu.findItem(R.id.personalArea);
        personalArea.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.personalArea:{
//                getActivity().getFragmentManager().beginTransaction().replace(R.id.main_container, new NewStudentFragment()).addToBackStack(null).commit();
                Intent intent = new Intent(getActivity(), PersonalAreaActivity.class);
                startActivity(intent);
                return true;}
            default:
                return super.onOptionsItemSelected(item);
        }
    }



            @Override
                public void onResume () {
                    super.onResume();

                }

                @Override
                public void onStart () {
                    super.onStart();
                }

                @Override
                public void onStop () {
                    super.onStop();
                }

        }