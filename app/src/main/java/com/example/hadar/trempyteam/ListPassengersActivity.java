package com.example.hadar.trempyteam;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hadar.trempyteam.Model.Model;
import com.example.hadar.trempyteam.Model.ModelFirebase;
import com.example.hadar.trempyteam.Model.Tremp;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;

import java.util.List;

public class ListPassengersActivity extends Activity {

    List<String> passengersList;
    final PassengersAdapter adapter = new PassengersAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_passengers);

        ModelFirebase fbModel = new ModelFirebase();

        Intent intent = getIntent();
        String trempId = intent.getExtras().getString("tremp_id");
        fbModel.getPassengersByTrempId(trempId, new Model.GetPassengersListener() {

            @Override
            public void onComplete (List < String > listPassengers) {


                passengersList = listPassengers;
                CreateList();

                }
        });

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
                final ProfilePictureView profilePictureView = (ProfilePictureView) view.findViewById(R.id.PassengerProfilePicture);

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
    }

