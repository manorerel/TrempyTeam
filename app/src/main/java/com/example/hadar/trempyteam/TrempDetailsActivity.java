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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hadar.trempyteam.Model.ModelFirebase;
import com.example.hadar.trempyteam.Model.ModelSql;
import com.example.hadar.trempyteam.Model.Tremp;
import com.example.hadar.trempyteam.Model.Model;
import com.example.hadar.trempyteam.Model.User;
import com.facebook.AccessToken;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by aviac on 3/31/2017.
 */

public class TrempDetailsActivity extends Activity {
    AlertDialog.Builder dlgAlert;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dlgAlert = new AlertDialog.Builder(TrempDetailsActivity.this);

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

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat TimeFormat = new SimpleDateFormat("HH:mm:ss");

        String newDate = "";
        String newTime = "";
        try {
            Date date = convertStringToDate(intent.getExtras().getString("date"));
            newDate = dateFormat.format(date);
            newTime = TimeFormat.format(date);
        }
        catch (Exception e)
        {

        }


        TrempDate.setText(newDate);
        TrempTime.setText(newTime);

        PhoneNumber.setText(intent.getExtras().getString("phone"));
        SourceAddress.setText(intent.getExtras().getString("source"));
        DestAddress.setText(intent.getExtras().getString("dest"));

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

        Button btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tremp_id = getIntent().getExtras().getString("id");

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
            }

    private void joinTremp(){
        final String tremp_id = getIntent().getExtras().getString("id");


        String user_id = AccessToken.getCurrentAccessToken().getUserId();

        ModelFirebase fbModel = new ModelFirebase();

        fbModel.UpdateSeatsTremp(tremp_id, user_id,true , new Model.UpdateSeatsTrempListener() {
            @Override
            public void onComplete() {
                dlgAlert.setMessage("יש לך מקום בטרמפ, נסיעה טובה (:");
                dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(TrempDetailsActivity.this, MainAactivity.class);
                        startActivity(intent);

                        dialog.dismiss();
                    }
                });
                dlgAlert.show();
            }
        });
    }

    private void exitTremp(){
        final String tremp_id = getIntent().getExtras().getString("id");

        String user_id = AccessToken.getCurrentAccessToken().getUserId();

        ModelFirebase fbModel = new ModelFirebase();

        fbModel.UpdateSeatsTremp(tremp_id, user_id ,false, new Model.UpdateSeatsTrempListener() {
            @Override
            public void onComplete() {
                dlgAlert = new AlertDialog.Builder(TrempDetailsActivity.this);
                dlgAlert.setMessage("יצאת מהטרמפ בהצלחה");
                dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(TrempDetailsActivity.this, MainAactivity.class);
                        startActivity(intent);

                        dialog.dismiss();
                    }
                });
                dlgAlert.show();
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
        if(driverId.equals(User.GetAppUser().Id)) {

            MenuItem edit = menu.findItem(R.id.editTremp);
            edit.setVisible(true);

            MenuItem delete = menu.findItem(R.id.deleteTremp);
            delete.setVisible(true);
        }
        else{

            String trempId = intent.getExtras().getString("id");
            Tremp t = ModelSql.getInstance().getTrempById(trempId);
            if(t != null)
            {
//            if(User.GetAppUser().isTrempContains(trempId)){
                MenuItem remove = menu.findItem(R.id.removeTrempist);
                remove.setVisible(true);
            }
            else{
                MenuItem join = menu.findItem(R.id.joinTremp);
                join.setVisible(true);
            }
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

                dlgAlert.setMessage("הטרמפ נמחק בהצלחה!");
                dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK,returnIntent);
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
        intent.putExtra("phone",  trempToEdit.getPhoneNumber());
        intent.putExtra("source",  trempToEdit.getSourceAddress());
        intent.putExtra("dest",  trempToEdit.getDestAddress());
        intent.putExtra("date", trempToEdit.getTrempDate());
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
            Tremp currTremp = ModelSql.getInstance().getTrempById(trempId);
            if(currTremp != null){
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
                PhoneNumber.setText(currTremp.getPhoneNumber());
                SourceAddress.setText(currTremp.getSourceAddress());
                DestAddress.setText(currTremp.getDestAddress());
//                Seets.setText(currTremp.getSeets());
                CarModel.setText(currTremp.getCarModel());

            }
        }
    }

    private void refreshValues()
    {

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

