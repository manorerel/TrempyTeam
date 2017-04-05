package com.example.hadar.trempyteam;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.provider.MediaStore;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;


import com.example.hadar.trempyteam.Model.User;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.widget.EditText;
import android.widget.ImageView;


import com.example.hadar.trempyteam.Model.Model;
import com.example.hadar.trempyteam.Model.ModelFirebase;
import com.example.hadar.trempyteam.Model.ModelSql;
import com.example.hadar.trempyteam.Model.Tremp;

import java.text.SimpleDateFormat;
import java.util.Date;
public class CreateNewTrempActivity extends Activity {
    private static final int REQUEST_WRITE_STORAGE = 112;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static int count = 0;
    int YEAR = 1900;


    public static final int REQUEST_CODE_ASK_PERMISSIONS = 1;

    //check
    private GoogleMap googleMap;
    final int main = 1;

    ImageView imageView;
    String imageFileName = null;
    Bitmap imageBitmap = null;
    AlertDialog.Builder dlgAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_tremp);


        final ModelFirebase fbModel = new ModelFirebase();
        imageView = (ImageView) findViewById(R.id.Image);
        dlgAlert = new AlertDialog.Builder(CreateNewTrempActivity.this);

        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }


        Button saveBtn = (Button) findViewById(R.id.btnSave);
        Button cancleBtn = (Button) findViewById(R.id.btnCancel);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText phone = (EditText)findViewById(R.id.editTextPhone);
                EditText source = (EditText)findViewById(R.id.exitfrom);
                EditText dest = (EditText)findViewById(R.id.dest);
                EditText seetsText = (EditText)findViewById(R.id.avaliable_seats);
                DateEditText dateText = (DateEditText)findViewById(R.id.date);
                TimeEditText time = (TimeEditText)findViewById(R.id.time);
                EditText carModel = (EditText)findViewById(R.id.car_model);

                if(phone.getText().toString().isEmpty() || source.getText().toString().isEmpty() || dest.getText().toString().isEmpty()
                        || seetsText.getText().toString().isEmpty() || carModel.getText().toString().isEmpty()){

                    dlgAlert.setMessage("לא מילאת את כל הפרטים!");
                    dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });
                    dlgAlert.show();
                    return;
                }

          //      AccessToken.getCurrentAccessToken().getUserId();

                long seets = Long.parseLong(seetsText.getText().toString());
                Date date = new Date(dateText.getYear()-YEAR, dateText.getMonth(), dateText.getDay(), time.getHour(),time.getMinute(), time.getSecond());

                String createdUserId = User.GetAppUser().Id;
                String trempId = CreateID();
                List<String> TrempistsList = new LinkedList<String>();

                Tremp newTremp = new Tremp(trempId,seets, createdUserId, date, source.getText().toString(), dest.getText().toString(),phone.getText().toString(), carModel.getText().toString(),"imageUrl",TrempistsList);
                String imName = "";


                if(imageBitmap != null){
                    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    imName = "image_" + newTremp.getId() + "_" + timeStamp + ".jpg";
                    Model.getInstance().saveImage(imageBitmap, imName, new Model.SaveImageListener() {
                        @Override
                        public void complete(String url) {
                            saveAndClose();
                        }

                        @Override
                        public void fail() {
                            saveAndClose();
                        }
                    });
                }else{
                    saveAndClose();
                }

                newTremp.setImageName(imName);
                fbModel.addTremp(newTremp);

                ModelSql sqlLight = ModelSql.getInstance();
                sqlLight.addTremp(newTremp,true);
                saveAndClose();

                Log.d("TAG", "Create new tremp and save to db");


            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                takingPicture();
            }
        });


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

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }


    private String CreateID(){
        String id =User.GetAppUser().Id + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + count++;
        return id;
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
                            TextView exitFromAddr = (TextView) findViewById(R.id.exitfrom);
                            List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                            String _Location = listAddresses.get(0).getAddressLine(0) + " " + listAddresses.get(0).getLocality();;
                            String contry = listAddresses.get(0).getLocality();
                            String g = listAddresses.get(0).getCountryName();

                            exitFromAddr.setText(_Location);


                            Button btmMap = (Button) findViewById(R.id.btnMap);
                            btmMap.setOnClickListener(new View.OnClickListener() {
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

    private void saveAndClose(){

        dlgAlert.setMessage("הטרמפ נוצר בהצלחה!");
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener()  {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                finish();

            }
        });
        dlgAlert.show();
    }

    private void takingPicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }
}


