package com.example.hadar.trempyteam;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hadar.trempyteam.Model.Model;
import com.example.hadar.trempyteam.Model.ModelFirebase;
import com.example.hadar.trempyteam.Model.ModelSql;
import com.example.hadar.trempyteam.Model.Tremp;
import com.example.hadar.trempyteam.Model.User;

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
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final TextView PhoneNumber = (TextView) findViewById(R.id.editPhone);
        final TextView SourceAddress = (TextView) findViewById(R.id.editExitfrom);
        final TextView DestAddress = (TextView) findViewById(R.id.editDest);
//        final TextView Seets = (TextView) findViewById(R.id.editAvaliable_seats);
        final DateEditText TrempDate = (DateEditText) findViewById(R.id.editDate);
        final TimeEditText TrempTime = (TimeEditText) findViewById(R.id.editTime);
        final TextView CarModel = (TextView) findViewById(R.id.editCar_model);
//        final ImageView image = (ImageView) findViewById(R.id.editImage);


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
//        Seets.setText(Long.toString(intent.getExtras().getLong("seets")));
        CarModel.setText(intent.getExtras().getString("car"));
//        String imageName = intent.getExtras().getString("image");
//        if ((imageName != null)&&(!imageName.equals(""))) {
//            Model.getInstance().loadImage(imageName, new Model.GetImageListener() {
//                @Override
//                public void onSccess(Bitmap imageBmp) {
//                    if (imageBmp != null) {
//                        image.setImageBitmap(imageBmp);
//                    }
//                }
//
//                @Override
//                public void onFail() {
//
//                }
//            });
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_buttons, menu);


        MenuItem save = menu.findItem(R.id.saveTremp);
        save.setVisible(true);

        MenuItem cancel = menu.findItem(R.id.cancelTremp);
        cancel.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.saveTremp:{
                final TextView PhoneNumber = (TextView) findViewById(R.id.editPhone);
                final TextView SourceAddress = (TextView) findViewById(R.id.editExitfrom);
                final TextView DestAddress = (TextView) findViewById(R.id.editDest);
//                final TextView Seets = (TextView) findViewById(R.id.editAvaliable_seats);
                DateEditText newDate = (DateEditText) findViewById(R.id.editDate);
                TimeEditText newtime = (TimeEditText) findViewById(R.id.editTime);
                final TextView CarModel = (TextView) findViewById(R.id.editCar_model);
//                final ImageView image = (ImageView) findViewById(R.id.editImage);

                // Check if the date or time was changed
//                if (newDate.didTouchFocusSelect()) {
//                    TrempDate.year = newDate.year;
//                    TrempDate.month = newDate.month;
//                    TrempDate.day = newDate.day;
//                }
//                if (newtime.didTouchFocusSelect()) {
//                    TrempTime.hour =  newtime.hour;
//                    TrempTime.minute = newtime.minute;
//                    TrempTime.second = newtime.second;
//                }

                try {
                    // Update the student (the id can also edited)
                    ModelFirebase fb = new ModelFirebase();
                    Intent currIntent = getIntent();
                    String id = currIntent.getExtras().getString("id");
                    fb.updateTremp(id, DestAddress.getText().toString(), SourceAddress.getText().toString(), PhoneNumber.getText().toString(), null);
                    ModelSql.getInstance().updateTremp(id, DestAddress.getText().toString(), SourceAddress.getText().toString(), PhoneNumber.getText().toString(), null);

                    // this.showSucessAlertDialog( "Success", "Student updated successfuly", true);
                } catch (Exception e) {
                    // this.showSucessAlertDialog( "Error", "Failed to update student", false);
                }

                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
                return true;}
            case R.id.cancelTremp:{
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
