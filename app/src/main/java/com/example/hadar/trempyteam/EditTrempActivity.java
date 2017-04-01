package com.example.hadar.trempyteam;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hadar.trempyteam.Model.Model;
import com.example.hadar.trempyteam.Model.Tremp;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aviac on 4/1/2017.
 */

public class EditTrempActivity extends Activity {

    boolean dateEdited=false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_tremp);

        final TextView PhoneNumber = (TextView) findViewById(R.id.editPhone);
        final TextView SourceAddress = (TextView) findViewById(R.id.editExitfrom);
        final TextView DestAddress = (TextView) findViewById(R.id.editDest);
        final TextView Seets = (TextView) findViewById(R.id.editAvaliable_seats);
        final DateEditText TrempDate = (DateEditText) findViewById(R.id.editDate);
        final TimeEditText TrempTime = (TimeEditText) findViewById(R.id.editTime);
        final TextView CarModel = (TextView) findViewById(R.id.editCar_model);
        final ImageView image = (ImageView) findViewById(R.id.editImage);


        //Get the Tremp details
        Intent intent = getIntent();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        try {
            date = format.parse(intent.getExtras().getString("date"));
            //Get the Date properties from the tremp date
            TrempDate.year = date.getYear();
            TrempDate.month = date.getMonth();
            TrempDate.day = date.getDay();

            //Get the Time properties from the tremp time
            TrempTime.hour = date.getHours();
            TrempTime.minute = date.getMinutes();
            TrempTime.second = date.getSeconds();
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






        Button save = (Button) findViewById(R.id.EditBtnSave);
        Button cancel = (Button) findViewById(R.id.EditBtnCancel);
        Button delete = (Button) findViewById(R.id.EditBtnDelete);

        // Edit and save the tremp details
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setContentView(R.layout.edit_tremp);
                final TextView PhoneNumber = (TextView) findViewById(R.id.editPhone);
                final TextView SourceAddress = (TextView) findViewById(R.id.editExitfrom);
                final TextView DestAddress = (TextView) findViewById(R.id.editDest);
                final TextView Seets = (TextView) findViewById(R.id.editAvaliable_seats);
                DateEditText newDate = (DateEditText) findViewById(R.id.editDate);
                TimeEditText newtime = (TimeEditText) findViewById(R.id.editTime);
                final TextView CarModel = (TextView) findViewById(R.id.editCar_model);
                final ImageView image = (ImageView) findViewById(R.id.editImage);



                // Check if the date or time was changed
                if (newDate.didTouchFocusSelect()) {
                    TrempDate.year = newDate.year;
                    TrempDate.month = newDate.month;
                    TrempDate.day = newDate.day;
                }
                if (newtime.didTouchFocusSelect()) {
                    TrempTime.hour =  newtime.hour;
                    TrempTime.minute = newtime.minute;
                    TrempTime.second = newtime.second;
                }

                try {
                    // Update the student (the id can also edited)
                    //TODO: UPDATE TREMP or DELETE AND ADD

                   // this.showSucessAlertDialog( "Success", "Student updated successfuly", true);
                } catch (Exception e) {
                   // this.showSucessAlertDialog( "Error", "Failed to update student", false);
                }
                finish();
            }
        });

        // Cancle the editing
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
            }
        });

        // Delete the student from the list
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //TODO: delete tremp
                  //  MainActivity.showSucessAlertDialog(getActivity(), "Success", "Student deleted successfuly", true);
                } catch (Exception e) {
                   // MainActivity.showSucessAlertDialog(getActivity(), "Error", "Failed to update student", false);
                }
                finish();
            }
        });
    }
}
