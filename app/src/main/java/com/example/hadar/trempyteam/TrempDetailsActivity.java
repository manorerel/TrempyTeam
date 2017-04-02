package com.example.hadar.trempyteam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hadar.trempyteam.Model.ModelFirebase;
import com.example.hadar.trempyteam.Model.Tremp;
import com.example.hadar.trempyteam.Model.Model;
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
         de = intent.getExtras().getString("dest");
         so = intent.getExtras().getString("source");
        seet = (Long.toString(intent.getExtras().getLong("seets")));

        Seets.setText(Long.toString(intent.getExtras().getLong("seets")));
        CarModel.setText(intent.getExtras().getString("car"));
       String imageName = intent.getExtras().getString("image");
        if ((imageName != null)&&(!imageName.equals(""))) {
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

        Button cancel = (Button) findViewById(R.id.detailsBtnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
            }
        });

        Button delete = (Button) findViewById(R.id.btnDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ModelFirebase fbModel = new ModelFirebase();
                Intent resultIntent = getIntent();
                fbModel.deleteTremp(resultIntent.getExtras().getString("id"), resultIntent.getExtras().getString("image"));

                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
            }
        });


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
                startActivity(intent);



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



}

