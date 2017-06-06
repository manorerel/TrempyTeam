package com.example.hadar.trempyteam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hadar.trempyteam.Model.Model;
import com.example.hadar.trempyteam.Model.ModelFirebase;
import com.example.hadar.trempyteam.Model.ModelRest;
import com.example.hadar.trempyteam.Model.ModelSql;
import com.example.hadar.trempyteam.Model.Tremp;
import com.example.hadar.trempyteam.Model.User;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListTrempActivity extends Activity {

    String detailsSet = "";
    List<Tremp> trempsList ;
    Boolean check = false;
    final TrempsAdapter adapter = new TrempsAdapter();
    final String user_connected_id = AccessToken.getCurrentAccessToken().getUserId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tremps);
        
        String cameFrom = (String) getIntent().getExtras().get("cameFrom");
        if (cameFrom != null && cameFrom.equals("personalArea")) {
            String isCreated = (String) getIntent().getExtras().get("isCreated");
            ModelSql modelSql = ModelSql.getInstance();
            ModelRest modelRest = ModelRest.getInstance();
            trempsList = modelRest.getTremps(User.GetAppUser().Id);

            if(isCreated.equals("true"))
                trempsList = modelSql.getAllTremps(true);
            else trempsList = modelSql.getAllTremps(false);

            CreateList();

            detailsSet = "personalArea";
        }
        else
        {
            final String dest = (String) getIntent().getExtras().get("dest");
            final String from = (String) getIntent().getExtras().get("from");
            final String date = (String) getIntent().getExtras().get("date");
            final String time = (String) getIntent().getExtras().get("time");

            ModelFirebase fbModel = new ModelFirebase();
            fbModel.getAllTrempsByFilter(time,date,dest, from, new Model.GetAllTrempsByFilerListener() {
                @Override
                public void onComplete(List<Tremp> tremps) {

                    trempsList = tremps;
                    CreateList();

                    if(trempsList.size() == 0)
                    {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ListTrempActivity.this);
                        dlgAlert.setMessage("לא נמצאו טרמפים התואמים את בקשת החיפוש");
                        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener()  {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                dialog.dismiss();
                            }
                        });
                        dlgAlert.show();
                    }
                }
            });
            detailsSet = "Search";
       }
    }

    public void CreateList()
    {
        ListView list = (ListView) findViewById(R.id.Tremps_listView);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Tremp tremp =  trempsList.get(i);

                Intent intent = new Intent(ListTrempActivity.this, TrempDetailsActivity.class);
                intent.putExtra("id",  tremp.getId());
                intent.putExtra("phone",  tremp.getPhoneNumber());
                intent.putExtra("source",  tremp.getSourceAddress());
                intent.putExtra("dest",  tremp.getDestAddress());
                intent.putExtra("seets",  tremp.getSeets());
                intent.putExtra("car",  tremp.getCarModel());
                intent.putExtra("image",  tremp.getImageName());
                intent.putExtra("driverId",  tremp.getDriverId());
                intent.putExtra("date",  tremp.getTrempDateTime());
                intent.putExtra("cameFrom", detailsSet);

                startActivityForResult(intent, 1);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            ModelSql modelSql = ModelSql.getInstance();
            trempsList = modelSql.getAllTremps(true);

            CreateList();

        }

        if(requestCode == Activity.RESULT_FIRST_USER){
            finish();
        }
    }
    class TrempsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return trempsList.size();
        }

        @Override
        public Object getItem(int i) {

            return trempsList.get(i);
        }

        @Override
        public long getItemId(int i) {

            return i;
        }

        @Override
        public View  getView(final int i, View view, ViewGroup viewGroup) {

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.tremp_list_raw, null);
            }
            final TextView name = (TextView) view.findViewById(R.id.DriverName);
            //final ImageView image = (ImageView) view.findViewById(R.id.android);
            final com.example.hadar.trempyteam.ProfilePictureView profilePictureView = (com.example.hadar.trempyteam.ProfilePictureView) view.findViewById(R.id.friendProfilePicture);
            final TextView time = (TextView) view.findViewById(R.id.TrempExitTime);
            final TextView seats = (TextView) view.findViewById(R.id.ava_seats);
            final Tremp st = trempsList.get(i);
            final String driver_id = st.getDriverId();
            final String avilableSeats = String.valueOf(st.getSeets());
              seats.setText(avilableSeats + " מקומות פנויים ");

            String dateTime = st.getTrempDateTime();
            String newTime = "";
            String newDate = "";
                String[] splitDate = dateTime.split(" ");
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


               time.setText(newDate + "  " + newTime);
               profilePictureView.setProfileId(driver_id);

            try {

                new GraphRequest(AccessToken.getCurrentAccessToken(),
                        "/" + driver_id,
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                try {
                                    name.setText(response.getJSONObject().getString("name"));
                                    } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).executeAsync();

            }
            catch (Exception e){
                Log.d("exception", "can't get user name " + e.getMessage());
            }
            return view;
        }
    }


}
