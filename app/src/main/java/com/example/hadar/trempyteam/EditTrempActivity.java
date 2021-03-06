package com.example.hadar.trempyteam;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.example.hadar.trempyteam.Model.ModelRest;
import com.example.hadar.trempyteam.Model.ModelSql;
import com.example.hadar.trempyteam.Model.Tremp;
import com.example.hadar.trempyteam.Model.User;
import com.example.hadar.trempyteam.Model.Utils;
import com.facebook.AccessToken;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aviac on 4/1/2017.
 */

public class EditTrempActivity extends Activity {

    boolean dateEdited=false;

    AlertDialog.Builder dlgAlert;
    View view;

    String dateString = "";
    String timeString = "";
    final String user_connected_id = AccessToken.getCurrentAccessToken().getUserId();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_tremp);
        ActionBar actionBar = getActionBar();

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#212121"));
        getActionBar().setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(colorDrawable);
        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        actionBar.setBackgroundDrawable(colorDrawable);


        dlgAlert = new AlertDialog.Builder(EditTrempActivity.this);
        final TextView PhoneNumber = (TextView) findViewById(R.id.editPhone);
        final TextView SourceAddress = (TextView) findViewById(R.id.editExitfrom);
        final TextView DestAddress = (TextView) findViewById(R.id.editDest);
        final DateEditText TrempDate = (DateEditText) findViewById(R.id.editDate);
        final TimeEditText TrempTime = (TimeEditText) findViewById(R.id.editTime);
        final TextView CarModel = (TextView) findViewById(R.id.editCar_model);

        //Get the Tremp details
        Intent intent = getIntent();

        String MyDate = intent.getExtras().getString("date");
        try {
            String[] splitDate = MyDate.split(" ");
            dateString = splitDate[0];
            timeString = splitDate[1];
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
        Button save = (Button) findViewById(R.id.EditBtnSave);
        Button cancel = (Button) findViewById(R.id.EditBtnCancle);

        // Edit and save the tremp details
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TextView PhoneNumber = (TextView) findViewById(R.id.editPhone);
                final TextView SourceAddress = (TextView) findViewById(R.id.editExitfrom);
                final TextView DestAddress = (TextView) findViewById(R.id.editDest);
                DateEditText newDate = (DateEditText) findViewById(R.id.editDate);
                TimeEditText newtime = (TimeEditText) findViewById(R.id.editTime);
                final TextView CarModel = (TextView) findViewById(R.id.editCar_model);

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
                    // Update the  tremp
                    ModelRest modelRest = ModelRest.getInstance();
                    Intent currIntent = getIntent();
                    String id = currIntent.getExtras().getString("id");
//                    Tremp trempToEdit = ModelSql.getInstance().getTrempById(id);
                    Tremp trempToEdit = Utils.currentChosenTremp;
                    trempToEdit.setPhoneNumber(PhoneNumber.getText().toString());
                    trempToEdit.setCarModel(CarModel.getText().toString());
                    trempToEdit.settrempDateTime(fullDateString);
                    LatLng source = Utils.getLocationFromAddress(EditTrempActivity.this, SourceAddress.getText().toString());
                    LatLng dest = Utils.getLocationFromAddress(EditTrempActivity.this, DestAddress.getText().toString());
                    trempToEdit.setSource(source);
                    trempToEdit.setDest(dest);
                    modelRest.updateTremp(trempToEdit);

                    dlgAlert.setMessage("השינויים נשמרו בהצלחה!");
                    dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                    });
                    dlgAlert.show();



                } catch (Exception e) {
                    dlgAlert.setMessage("ארעה שגיאה בעת השמירה, שינוייך לא נשמרו");
                    dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                    });
                    dlgAlert.show();
                }
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
}
