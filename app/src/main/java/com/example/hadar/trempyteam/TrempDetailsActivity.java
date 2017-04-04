package com.example.hadar.trempyteam;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.AlertDialog;

import com.example.hadar.trempyteam.Model.ModelFirebase;
import com.example.hadar.trempyteam.Model.ModelSql;
import com.example.hadar.trempyteam.Model.Tremp;
import com.example.hadar.trempyteam.Model.Model;
import com.example.hadar.trempyteam.Model.User;
import com.facebook.AccessToken;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by aviac on 3/31/2017.
 */

public class TrempDetailsActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tremp_details);
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        final TextView PhoneNumber = (TextView) findViewById(R.id.detailsPhone);
        final TextView SourceAddress = (TextView) findViewById(R.id.detailsExitfrom);
        final TextView DestAddress = (TextView) findViewById(R.id.detailsDest);
        final TextView Seets = (TextView) findViewById(R.id.detailsAvaliable_seats);
        DateEditText TrempDate = (DateEditText) findViewById(R.id.detailsDate);
        TimeEditText TrempTime = (TimeEditText) findViewById(R.id.detailsTime);
        final TextView CarModel = (TextView) findViewById(R.id.detailsCar_model);
        final ImageView image = (ImageView) findViewById(R.id.DetailsImage);
        final String de;
       final  String so;
        final String seet;
        final String id;
        Intent intent = getIntent();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        try {
            date = format.parse(intent.getExtras().getString("date"));
        }
        catch (Exception e)
        {

        }

        TrempDate.setText("" + date.getDay() + "/" + (date.getMonth() + 1) + "/" + date.getYear());
        TrempTime.setText("" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());
        PhoneNumber.setText(intent.getExtras().getString("phone"));
        SourceAddress.setText(intent.getExtras().getString("source"));
        DestAddress.setText(intent.getExtras().getString("dest"));
        final String tremp_id = intent.getExtras().getString("id");
         de = intent.getExtras().getString("dest");
         so = intent.getExtras().getString("source");
         id =  intent.getExtras().getString("id");
        seet = (Long.toString(intent.getExtras().getLong("seets")));

        Seets.setText(Long.toString(intent.getExtras().getLong("seets")));
        CarModel.setText(intent.getExtras().getString("car"));
       String imageName = intent.getExtras().getString("image");
        if ((imageName != null)&&(!imageName.equals(""))) {
            try {
                Model.getInstance().loadImage(imageName, new Model.GetImageListener() {
                    @Override
                    public void onSccess(Bitmap imageBmp) {
                        if (imageBmp != null) {
                            image.setImageBitmap(imageBmp);
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });
            }
            catch(Exception e){
                Log.d("EX", e.getMessage());
            }
        }


        Button cancel = (Button) findViewById(R.id.detailsBtnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
            }
        });

//        Button delete = (Button) findViewById(R.id.btnDelete);
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final ModelFirebase fbModel = new ModelFirebase();
//                Intent resultIntent = getIntent();
//                fbModel.deleteTremp(resultIntent.getExtras().getString("id"), resultIntent.getExtras().getString("image"));
//
//                setResult(Activity.RESULT_CANCELED, resultIntent);
//                finish();
//            }
//        });


        Button btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LatLng c =  getLocationFromAddress(TrempDetailsActivity.this, de);
                LatLng s =  getLocationFromAddress(TrempDetailsActivity.this, so);
                //   LatLng s = new LatLng(latitude, longitude);

                Intent intent = new Intent(TrempDetailsActivity.this, MapsActivity.class);
                intent.putExtra("DestLocation", c);
                intent.putExtra("SourceLocation", s);
                intent.putExtra("trempId", tremp_id);
                startActivity(intent);



            }
        });

        // Click on the "+" button to add a new student
        Button btnJoin = (Button) findViewById(R.id.btnJoin);
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(TrempDetailsActivity.this);
                dlgAlert.setMessage("You Are In !!");
                dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(TrempDetailsActivity.this, MainAactivity.class);
                        startActivity(intent);

                        dialog.dismiss();
                    }
                });
                dlgAlert.show();


                String user_id = AccessToken.getCurrentAccessToken().getUserId();

                ModelFirebase fbModel = new ModelFirebase();



                fbModel.UpdateSeatsTremp(tremp_id, user_id , new Model.UpdateSeatsTrempListener() {
                    @Override
                    public void onComplete() {
                    }
                });


            }
        });




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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_buttons, menu);
        Intent intent = getIntent();
        String driverId = (String)intent.getExtras().getString("driverId");
        if(driverId.equals(User.GetAppUser().getId())) {

            MenuItem edit = menu.findItem(R.id.editTremp);
            edit.setVisible(true);

            MenuItem delete = menu.findItem(R.id.deleteTremp);
            delete.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.deleteTremp:{
                final ModelFirebase fbModel = new ModelFirebase();
                Intent resultIntent = getIntent();
                ModelSql.getInstance().deleteTremp(resultIntent.getExtras().getString("id"));
                fbModel.deleteTremp(resultIntent.getExtras().getString("id"), resultIntent.getExtras().getString("image"));

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
                return true;}
            case R.id.editTremp:{
                startEdit();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startEdit()
    {
        Intent currentIntent = getIntent();
        Intent intent = new Intent(TrempDetailsActivity.this, EditTrempActivity.class);
        intent.putExtra("id",  currentIntent.getExtras().getString("id"));
        intent.putExtra("phone",  currentIntent.getExtras().getString("phone"));
        intent.putExtra("source",  currentIntent.getExtras().getString("source"));
        intent.putExtra("dest",  currentIntent.getExtras().getString("dest"));
        intent.putExtra("seets",  currentIntent.getExtras().getString("seets"));
        intent.putExtra("car",  currentIntent.getExtras().getString("car"));
        intent.putExtra("image",  currentIntent.getExtras().getString("image"));
        intent.putExtra("driverId",  currentIntent.getExtras().getString("driverId"));
        intent.putExtra("date", currentIntent.getExtras().getString("date"));
        try {
            startActivity(intent);
        }
        catch (Exception e){
            Log.d("Exception:" , e.getMessage());
        }

    }



}

