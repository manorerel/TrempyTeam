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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aviac on 4/1/2017.
 */

public class EditTrempActivity extends Activity {

    boolean dateEdited=false;
    String dateString = "";
    String timeString = "";
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

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat TimeFormat = new SimpleDateFormat("HH:mm:ss");

        try {
            Date date = convertStringToDate(intent.getExtras().getString("date"));
            dateString = dateFormat.format(date);
            timeString = TimeFormat.format(date);


        }
        catch (Exception e)
        {

        }

        TrempDate.setText(dateString);
        TrempTime.setText(timeString);
        PhoneNumber.setText(intent.getExtras().getString("phone"));
        SourceAddress.setText(intent.getExtras().getString("source"));
        DestAddress.setText(intent.getExtras().getString("dest"));
        CarModel.setText(intent.getExtras().getString("car"));


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

                if(newDate.didTouchFocusSelect())
                {
                    dateString = newDate.getText().toString();
                }
                if (newtime.didTouchFocusSelect())
                {
                    timeString = newtime.getText().toString();
                }

                String fullDateString = dateString + " " + timeString;


                try {
                    // Update the student (the id can also edited)
                    ModelFirebase fb = new ModelFirebase();
                    Intent currIntent = getIntent();
                    String id = currIntent.getExtras().getString("id");
                    fb.updateTremp(id, DestAddress.getText().toString(), SourceAddress.getText().toString(), PhoneNumber.getText().toString(), fullDateString);
                    ModelSql.getInstance().updateTremp(id, DestAddress.getText().toString(), SourceAddress.getText().toString(), PhoneNumber.getText().toString(), fullDateString);

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

    private static Date convertStringToDate(String dateText){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateText);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertedDate;
    }
}
