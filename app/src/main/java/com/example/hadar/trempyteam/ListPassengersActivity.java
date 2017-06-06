package com.example.hadar.trempyteam;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ListView;
import android.widget.TextView;

import com.example.hadar.trempyteam.Model.Model;
import com.example.hadar.trempyteam.Model.ModelFirebase;
import com.example.hadar.trempyteam.Model.Tremp;
import com.example.hadar.trempyteam.Model.Utils;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ListPassengersActivity extends Activity {

    List<String> passengersList;
    final PassengersAdapter adapter = new PassengersAdapter();
    final String user_connected_id = AccessToken.getCurrentAccessToken().getUserId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_passengers);

        ModelFirebase fbModel = new ModelFirebase();

        Intent intent = getIntent();
        String trempId = intent.getExtras().getString("tremp_id");
        passengersList = Utils.currentChosenTremp.getTrempistsList();
        CreateList();

        if(passengersList.size() == 0)
                {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ListPassengersActivity.this);
                    dlgAlert.setMessage("אף אחד לא הצטרף עדיין לטרמפ זה");
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

    public void CreateList() {
        ListView list = (ListView) findViewById(R.id.Passengers_listView);

        list.setAdapter(adapter);
    }


        class PassengersAdapter extends BaseAdapter {

            @Override
            public int getCount() {
                return passengersList.size();
            }

            @Override
            public Object getItem(int i) {

                return passengersList.get(i);
            }

            @Override
            public long getItemId(int i) {

                return i;
            }

            @Override
            public View  getView(final int i, View view, ViewGroup viewGroup) {

                if (view == null) {
                    view = getLayoutInflater().inflate(R.layout.passenger_list_raw, null);
                }
                final TextView name = (TextView) view.findViewById(R.id.PassengerName);
                final com.example.hadar.trempyteam.ProfilePictureView profilePictureView = (com.example.hadar.trempyteam.ProfilePictureView) view.findViewById(R.id.PassengerProfilePicture);

                final String user_id = passengersList.get(i);

                profilePictureView.setProfileId(user_id);


                try {

                    new GraphRequest(AccessToken.getCurrentAccessToken(),
                            "/" + user_id,
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

