package com.example.hadar.trempyteam;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hadar.trempyteam.Model.Tremp;
import com.example.hadar.trempyteam.Model.Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by aviac on 3/31/2017.
 */

public class TrempDetailsActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState, Tremp trempDetails) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.tremp_details);
        final TextView  PhoneNumber = (TextView) findViewById(R.id.detailsPhone);
        final TextView SourceAddress = (TextView) findViewById(R.id.detailsExitfrom);
        final TextView  DestAddress = (TextView) findViewById(R.id.detailsDest);
        final TextView Seets = (TextView) findViewById(R.id.detailsAvaliable_seats);
        DateEditText TrempDate = (DateEditText)findViewById(R.id.detailsDate);
        TimeEditText TrempTime = (TimeEditText)findViewById(R.id.detailsTime);
        final TextView CarModel = (TextView) findViewById(R.id.detailsCar_model);
        final ImageView image = (ImageView) findViewById(R.id.DetailsImage);

        Intent intent = getIntent();
        String id = intent.getExtras().getString("id");
        Date date = trempDetails.getTrempDate();

        TrempDate.setText("" + trempDetails.getTrempDate().getDay() + "/" + (trempDetails.getTrempDate().getMonth()+1) + "/" + trempDetails.getTrempDate().getYear());
        TrempTime.setText("" +trempDetails.getTrempDate().getHours() + ":" + trempDetails.getTrempDate().getMinutes() + ":" + trempDetails.getTrempDate().getSeconds());
        PhoneNumber.setText(trempDetails.getPhoneNumber());
        SourceAddress.setText(trempDetails.getSourceAddress());
        DestAddress.setText(trempDetails.getDestAddress());
        Seets.setText(Long.toString(trempDetails.getSeets()));
        CarModel.setText(trempDetails.getCarModel());
        if(trempDetails.getImageName() != null){
            Model.getInstance().loadImage(trempDetails.getImageName(), new Model.GetImageListener() {
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
    }

}
