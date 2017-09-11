package com.example.hadar.trempyteam;

import android.app.ActionBar;
import android.app.Activity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.provider.MediaStore;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.example.hadar.trempyteam.Model.ModelRest;
import com.example.hadar.trempyteam.Model.User;

import com.example.hadar.trempyteam.Model.Utils;
import com.facebook.AccessToken;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.widget.EditText;
import android.widget.ImageView;


import com.example.hadar.trempyteam.Model.Model;
import com.example.hadar.trempyteam.Model.ModelFirebase;
import com.example.hadar.trempyteam.Model.ModelSql;
import com.example.hadar.trempyteam.Model.Tremp;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
public class CreateNewTrempActivity extends Activity {
    private static final int REQUEST_WRITE_STORAGE = 112;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static int count = 0;
    int YEAR = 1900;
    PlacesTask placesTask;
    ParserTask parserTask;
    AutoCompleteTextView atvPlaces;
    AutoCompleteTextView atvPlaces_;

    final String user_connected_id = AccessToken.getCurrentAccessToken().getUserId();
    public static final int  REQUEST_CODE_ASK_PERMISSIONS = 1;
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

        ProgressDialog progressdialog = new ProgressDialog(CreateNewTrempActivity.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        Notification noti = new Notification.Builder(this.getApplicationContext())
                .setContentTitle("New mail from hadar test")
                .setContentText("hhhh")
                .setSmallIcon(R.drawable.com_facebook_send_button_icon)
                .build();

        ActionBar actionBar = this.getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#212121"));
        getActionBar().setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(colorDrawable);
        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        atvPlaces = (AutoCompleteTextView)findViewById(R.id.dest);
        atvPlaces_ = (AutoCompleteTextView)findViewById(R.id.exitfrom);
       /* atvPlaces.setThreshold(1);
        atvPlaces_.setThreshold(1);*/

        atvPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        atvPlaces_.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

//    final ModelFirebase fbModel = new ModelFirebase();
        final ModelRest modelRest = ModelRest.getInstance();
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
                EditText seetsText = (EditText)findViewById(R.id.avaliable_seats);
                DateEditText dateText = (DateEditText)findViewById(R.id.date);
                TimeEditText time = (TimeEditText)findViewById(R.id.time);
                EditText carModel = (EditText)findViewById(R.id.car_model);
                String dateString = dateText.getText() + " " + time.getText();

                if(phone.getText().toString().isEmpty() || atvPlaces.getText().toString().isEmpty() || atvPlaces.getText().toString().isEmpty()
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

                long seets = Long.parseLong(seetsText.getText().toString());

                String createdUserId = User.GetAppUser().Id;
                List<String> TrempistsList = new LinkedList<String>();
                LatLng destCoord = Utils.getLocationFromAddress(CreateNewTrempActivity.this, atvPlaces.getText().toString());
                LatLng sourceCoord = Utils.getLocationFromAddress(CreateNewTrempActivity.this, atvPlaces_.getText().toString());

                Tremp newTremp = new Tremp(seets, createdUserId, dateString, sourceCoord, destCoord, phone.getText().toString(), carModel.getText().toString(),"imageUrl",TrempistsList);

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
                modelRest.createTremp(newTremp);

//                ModelSql sqlLight = ModelSql.getInstance();
//                sqlLight.addTremp(newTremp,true);
                Log.d("TAG", "Create new tremp and save to db");

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

        progressdialog.dismiss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    private void saveAndClose(){

        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
       // finish();
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("E", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyBKOu5K4zgwFgUHunEMaWbDecdOVkTR7r8";

            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input+"&"+types+"&"+sensor+"&"+key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

            try{
                // Fetching the data from we service
                data = downloadUrl(url);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            atvPlaces.setAdapter(adapter);
            atvPlaces_.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_buttons, menu);

        View view = (View) LayoutInflater.from(getBaseContext() ).inflate(R.layout.check, null);
        com.example.hadar.trempyteam.ProfilePictureView editText =  (com.example.hadar.trempyteam.ProfilePictureView) view.findViewById(R.id.friendProfilePicture);
        editText.setProfileId(user_connected_id);

        MenuItem personalArea =  menu.findItem(R.id.personalArea);
        personalArea.setVisible(true);
        personalArea.setActionView(view);


        return true;
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

    private LatLng getLocationFromAddresss(Context context, String strAddress){
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

}


