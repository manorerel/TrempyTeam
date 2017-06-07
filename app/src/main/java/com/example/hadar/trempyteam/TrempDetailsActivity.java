package com.example.hadar.trempyteam;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hadar.trempyteam.Model.ModelFirebase;
import com.example.hadar.trempyteam.Model.ModelRest;
import com.example.hadar.trempyteam.Model.ModelSql;
import com.example.hadar.trempyteam.Model.Tremp;
import com.example.hadar.trempyteam.Model.Model;
import com.example.hadar.trempyteam.Model.User;
import com.example.hadar.trempyteam.Model.Utils;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by aviac on 3/31/2017.
 */

public class TrempDetailsActivity extends Activity {
    boolean wasEdited = false;
    String cameFrom = "";

    final String user_connected_id = AccessToken.getCurrentAccessToken().getUserId();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tremp_details);
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        final TextView PhoneNumber = (TextView) findViewById(R.id.detailsPhone);
        final TextView SourceAddress = (TextView) findViewById(R.id.detailsExitfrom);
        final TextView DestAddress = (TextView) findViewById(R.id.detailsDest);
        final TextView Seets = (TextView) findViewById(R.id.detailsAvaliable_seats);
        DateEditText TrempDate = (DateEditText) findViewById(R.id.detailsDate);
        TimeEditText TrempTime = (TimeEditText) findViewById(R.id.detailsTime);
        final TextView CarModel = (TextView) findViewById(R.id.detailsCar_model);

        String date = intent.getExtras().getString("date");
        String newDate = "";
        String newTime = "";
        try {
            String[] splitDate = date.split(" ");
            newDate = splitDate[0];
            newTime = splitDate[1];

            String[] splitTime = newTime.split(":");
            if (splitTime[1].length() < 2)
            {
                newTime = splitTime[0] + ":0" + splitTime[1];
            }
            else
            {
                newTime = splitTime[0] + ":" + splitTime[1];
            }


        } catch (Exception e) {

        }
        TrempDate.setText(newDate);
        TrempTime.setText(newTime);

        PhoneNumber.setText(intent.getExtras().getString("phone"));
        SourceAddress.setText(intent.getExtras().getString("source"));
        DestAddress.setText(intent.getExtras().getString("dest"));
        Seets.setText(Long.toString(intent.getExtras().getLong("seets")));
        CarModel.setText(intent.getExtras().getString("car"));
        String imageName = intent.getExtras().getString("image");

        final ImageButton showImage = (ImageButton) findViewById(R.id.showImage);

        showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDialog();
            }
        });


        final String de;
        final String so;
        final String seet;
        final String id;

        de = intent.getExtras().getString("dest");
        so = intent.getExtras().getString("source");
        id = intent.getExtras().getString("id");
        seet = (Long.toString(intent.getExtras().getLong("seets")));


        ImageButton btnMap = (ImageButton) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tremp_id = getIntent().getExtras().getString("id");

                LatLng c = getLocationFromAddress(TrempDetailsActivity.this, de);
                LatLng s = getLocationFromAddress(TrempDetailsActivity.this, so);

                Intent intent = new Intent(TrempDetailsActivity.this, MapsActivity.class);
                intent.putExtra("DestLocation", c);
                intent.putExtra("SourceLocation", s);
                intent.putExtra("trempId", tremp_id);
                startActivity(intent);
            }
        });


        final ImageButton friends_passengers = (ImageButton) findViewById(R.id.show_friends);


        friends_passengers.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TrempDetailsActivity.this, ListPassengersActivity.class);
                intent.putExtra("tremp_id", id);
                startActivity(intent);

            }
        });
    }

    private void showDialog() {



        final Dialog dialog;

        dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.picture_dualog);


        // set the custom dialog components - text, image and button
       final ImageView  pic = (ImageView) dialog.findViewById(R.id.pic);
        ImageButton close = (ImageButton) dialog.findViewById(R.id.btnClose);


        String imageNamee = getIntent().getExtras().getString("image");
        try {
            Model.getInstance().loadImage(imageNamee, new Model.GetImageListener() {
                @Override
                public void onSccess(Bitmap imageBmp) {
                    if (imageBmp != null) {
                        pic.setImageBitmap(imageBmp);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                    }

                    }


                @Override
                public void onFail() {

                }
            });
        } catch (Exception e) {

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(TrempDetailsActivity.this);
            dlgAlert.setMessage("הנהג לא צירף תמונה");
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dlgAlert.show();
            Log.d("EX", e.getMessage());
        }

        // Close Button
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

    }

    private void joinTremp(){
        final String tremp_id = getIntent().getExtras().getString("id");


        String user_id = AccessToken.getCurrentAccessToken().getUserId();

        ModelRest modelRest = ModelRest.getInstance();
        modelRest.joinOrUnjoinTremp(tremp_id, user_id, true);

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(TrempDetailsActivity.this);
        dlgAlert.setMessage("יש לך מקום שמור בטרמפ (:");
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener()  {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_FIRST_USER,returnIntent);
                finish();

                dialog.dismiss();
            }
        });
        dlgAlert.show();
    }

    private void exitTremp(){
        final String tremp_id = getIntent().getExtras().getString("id");

        String user_id = AccessToken.getCurrentAccessToken().getUserId();

        ModelRest modelRest = ModelRest.getInstance();
        modelRest.joinOrUnjoinTremp(tremp_id, user_id, false);

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(TrempDetailsActivity.this);
        dlgAlert.setMessage("יצאת מהטרמפ בהצלחה");
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener()  {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_FIRST_USER,returnIntent);
                finish();

                dialog.dismiss();
            }
        });
        dlgAlert.show();

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
        cameFrom = (String) intent.getExtras().get("cameFrom");


        if(driverId.equals(User.GetAppUser().Id)) {

            MenuItem edit = menu.findItem(R.id.editTremp);
            edit.setVisible(true);

            MenuItem delete = menu.findItem(R.id.deleteTremp);
            delete.setVisible(true);
        }
        else if(!driverId.equals(User.GetAppUser().Id))
        {

            String trempId = intent.getExtras().getString("id");
            Tremp t = Utils.currentChosenTremp;
            if(t != null && t.getTrempistsList().contains(User.GetAppUser().Id))
            {
                MenuItem remove = menu.findItem(R.id.removeTrempist);
                remove.setVisible(true);
            }
            else{
                MenuItem join = menu.findItem(R.id.joinTremp);
                join.setVisible(true);
            }
        }

        View view = (View) LayoutInflater.from(getBaseContext() ).inflate(R.layout.check, null);
        com.example.hadar.trempyteam.ProfilePictureView editText =  (com.example.hadar.trempyteam.ProfilePictureView) view.findViewById(R.id.friendProfilePicture);
        editText.setProfileId(user_connected_id);

        MenuItem personalArea =  menu.findItem(R.id.personalArea);
        personalArea.setVisible(true);
        personalArea.setActionView(view);







        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.deleteTremp:{


                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(TrempDetailsActivity.this);
                dlgAlert.setMessage("האם אתה בטוח שברצונך למחוק את הטרמפ?");
                dlgAlert.setNegativeButton("מחק", new DialogInterface.OnClickListener()  {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ModelRest modelRest = ModelRest.getInstance();
                        Intent resultIntent = getIntent();
                        modelRest.deleteTremp(Utils.currentChosenTremp.getId());
                        setResult(Activity.RESULT_OK,resultIntent);
                        finish();
                        dialog.dismiss();
                    }
                });
                dlgAlert.setPositiveButton("בטל", new DialogInterface.OnClickListener()  {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                });
                dlgAlert.show();
                return true;}
            case R.id.editTremp:{
                startEdit();
                return true;
            }
            case R.id.joinTremp:{
                joinTremp();
                return true;
            }
            case R.id.removeTrempist:{
                exitTremp();
                return true;
            }
            default:{
                if(cameFrom.equals("personalArea"))
                {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK,returnIntent);
                }

                finish();
                return true;
            }

        }
    }

    private void startEdit()
    {
        Intent currentIntent = getIntent();
        Intent intent = new Intent(TrempDetailsActivity.this, EditTrempActivity.class);
        String id = currentIntent.getExtras().getString("id");
        Tremp trempToEdit = ModelSql.getInstance().getTrempById(id);
        intent.putExtra("id",  id);
        intent.putExtra("phone",  currentIntent.getExtras().getString("phone"));
        intent.putExtra("source",  currentIntent.getExtras().getString("source"));
        intent.putExtra("dest",  currentIntent.getExtras().getString("dest"));
        intent.putExtra("date",  currentIntent.getExtras().getString("date"));
        intent.putExtra("car", currentIntent.getExtras().getString("car"));

        try {
            startActivityForResult(intent,1);
        }
        catch (Exception e){
            Log.d("Exception:" , e.getMessage());
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            refresh();
        }
    }

    private void refresh() {
        Intent currIntent = getIntent();
        String trempId = currIntent.getExtras().getString("id");

        if(!trempId.isEmpty()){
            wasEdited = true;
            Tremp currTremp = Utils.currentChosenTremp;

            if(currTremp != null){
                final TextView PhoneNumber = (TextView) findViewById(R.id.detailsPhone);
                final TextView SourceAddress = (TextView) findViewById(R.id.detailsExitfrom);
                final TextView DestAddress = (TextView) findViewById(R.id.detailsDest);
                final TextView Seets = (TextView) findViewById(R.id.detailsAvaliable_seats);
                DateEditText TrempDate = (DateEditText) findViewById(R.id.detailsDate);
                TimeEditText TrempTime = (TimeEditText) findViewById(R.id.detailsTime);
                final TextView CarModel = (TextView) findViewById(R.id.detailsCar_model);
               // final ImageView image = (ImageView) findViewById(R.id.DetailsImage);
                final String de;
                final  String so;
                final String seet;

                Intent intent = getIntent();

                String MyDate = currTremp.getTrempDateTime();
                String newDate = "";
                String newTime = "";
                try {
                    String[] splitDate = MyDate.split(" ");
                    newDate = splitDate[0];
                    newTime = splitDate[1];
                }
                catch (Exception e)
                {

                }

                TrempDate.setText(newDate);
                TrempTime.setText(newTime);
                PhoneNumber.setText(currTremp.getPhoneNumber());
                SourceAddress.setText(Utils.getAddressFromLocation(TrempDetailsActivity.this, currTremp.getSource()));
                DestAddress.setText(Utils.getAddressFromLocation(TrempDetailsActivity.this, currTremp.getDest()));
                CarModel.setText(currTremp.getCarModel());
            }
        }
    }

    @Override
    public void onBackPressed() {

        // Check if the tremp details was edited
        if (cameFrom.equals("personalArea"))
        {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK,returnIntent);
        }

        finish();
    }

}

