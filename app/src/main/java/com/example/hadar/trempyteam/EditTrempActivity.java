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

import java.util.Date;

/**
 * Created by aviac on 4/1/2017.
 */

public class EditTrempActivity extends Activity {
    String stId = " ";
    View view;
    boolean dateEdited=false;

    protected void onCreate(Bundle savedInstanceState, Tremp trempDetails) {
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
        TrempDate.setText("" + trempDetails.getTrempDate().getDay() + "/" + (trempDetails.getTrempDate().getMonth() + 1) + "/" + trempDetails.getTrempDate().getYear());
        TrempTime.setText("" + trempDetails.getTrempDate().getHours() + ":" + trempDetails.getTrempDate().getMinutes() + ":" + trempDetails.getTrempDate().getSeconds());
        PhoneNumber.setText(trempDetails.getPhoneNumber());
        SourceAddress.setText(trempDetails.getSourceAddress());
        DestAddress.setText(trempDetails.getDestAddress());
        Seets.setText(Long.toString(trempDetails.getSeets()));
        CarModel.setText(trempDetails.getCarModel());
        if (trempDetails.getImageName() != null) {
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

        //Get the Date properties from the tremp date
        TrempDate.year = trempDetails.getTrempDate().getYear();
        TrempDate.month = trempDetails.getTrempDate().getMonth();
        TrempDate.day = trempDetails.getTrempDate().getDay();

        //Get the Time properties from the tremp time
        TrempTime.hour = trempDetails.getTrempDate().getHours();
        TrempTime.minute = trempDetails.getTrempDate().getMinutes();
        TrempTime.second = trempDetails.getTrempDate().getSeconds();


        Button save = (Button) view.findViewById(R.id.EditBtnSave);
        Button cancel = (Button) view.findViewById(R.id.EditBtnCancel);
        Button delete = (Button) view.findViewById(R.id.EditBtnDelete);

        // Edit and save the tremp details
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setContentView(R.layout.edit_tremp);
                final TextView PhoneNumber = (TextView) findViewById(R.id.editPhone);
                final TextView SourceAddress = (TextView) findViewById(R.id.editExitfrom);
                final TextView DestAddress = (TextView) findViewById(R.id.editDest);
                final TextView Seets = (TextView) findViewById(R.id.editAvaliable_seats);
                DateEditText date = (DateEditText) findViewById(R.id.editDate);
                TimeEditText time = (TimeEditText) findViewById(R.id.editTime);
                final TextView CarModel = (TextView) findViewById(R.id.editCar_model);
                final ImageView image = (ImageView) findViewById(R.id.editImage);



                // Check if the date or time was changed
                if (date.didTouchFocusSelect()) {
                    TrempDate.year = date.year;
                    TrempDate.month = date.month;
                    TrempDate.day = date.day;
                }
                if (time.didTouchFocusSelect()) {
                    TrempTime.hour =  time.hour;
                    TrempTime.minute = time.minute;
                    TrempTime.second = time.second;
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
