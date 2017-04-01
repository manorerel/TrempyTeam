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
    }


}

