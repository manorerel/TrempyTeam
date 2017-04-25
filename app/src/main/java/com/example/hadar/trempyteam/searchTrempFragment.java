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
import com.facebook.AccessToken;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class searchTrempFragment extends Fragment {

    FragmentManager fragmentManager;
    final String destii = "";
    final String user_connected_id = AccessToken.getCurrentAccessToken().getUserId();

    public static final int  REQUEST_CODE_ASK_PERMISSIONS = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the fragment layout file
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.search_tremp, container, false);

            ActionBar actionBar = getActivity().getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            setHasOptionsMenu(true);

       final EditText source = (EditText)rootView.findViewById(R.id.from);



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

            }
        } else {

            // create class object
            GPSTracker gps = new GPSTracker(getActivity());

            // check if GPS enabled
            if (gps.canGetLocation()) {

                final double latitude = gps.getLatitude();
                final double  longitude = gps.getLongitude();

                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                TextView exitFromAddr = (TextView) rootView.getRootView().findViewById(R.id.from);

                try {
                    List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (null != listAddresses && listAddresses.size() > 0) {

                        String _Location = listAddresses.get(0).getAddressLine(0) + " " + listAddresses.get(0).getLocality();
                        String contry = listAddresses.get(0).getLocality();
                        String g = listAddresses.get(0).getCountryName();

                        exitFromAddr.setText(_Location);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

            Button btnSearch = (Button) rootView.findViewById(R.id.btnSearch);
            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DateEditText dateText = (DateEditText)rootView.findViewById(R.id.date_);
                    TimeEditText time = (TimeEditText)rootView.findViewById(R.id.time_);
                    EditText dest = (EditText) rootView.findViewById(R.id.destination);
                    EditText from = (EditText) rootView.findViewById(R.id.from);

                    Intent intent = new Intent(getActivity(), ListTrempActivity.class);

                    String d = dest.getText().toString();
                    String f = from.getText().toString();
                    String text = dateText.getText().toString();
                    String time_ = time.getText().toString();

                    intent.putExtra("dest", d);
                    intent.putExtra("from", f);
                    intent.putExtra("date", text);
                    intent.putExtra("time", time_);
                    startActivity(intent);

                }
            });

        return rootView;
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
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        Log.d("TAG", "handle action bar");
        inflater.inflate(R.menu.menu_buttons, menu);

        View view = (View) LayoutInflater.from(getActivity().getBaseContext() ).inflate(R.layout.check, null);
        ProfilePictureView editText =  (ProfilePictureView) view.findViewById(R.id.friendProfilePicture);
        editText.setProfileId(user_connected_id);

        MenuItem personalArea =  menu.findItem(R.id.personalArea);
        personalArea.setVisible(true);
        personalArea.setActionView(view);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonalAreaActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.personalArea:{
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