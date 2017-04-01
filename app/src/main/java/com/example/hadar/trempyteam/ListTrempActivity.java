package com.example.hadar.trempyteam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hadar.trempyteam.Model.Model;
import com.example.hadar.trempyteam.Model.ModelFirebase;
import com.example.hadar.trempyteam.Model.Tremp;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.jar.Attributes;

public class ListTrempActivity extends Activity {

    String name = "";


    List<Tremp> trempsList ;

     Boolean check = false;
    final TrempsAdapter adapter = new TrempsAdapter();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tremps);


        // Hadar Part
        final String dest = (String) getIntent().getExtras().get("dest");
        final String from = (String) getIntent().getExtras().get("from");

            ModelFirebase fbModel = new ModelFirebase();
            fbModel.getAllTrempsByFilter(dest ,from ,new Model.GetAllTrempsByFilerListener() {
                @Override
                public void onComplete(List<Tremp> tremps) {

                    trempsList = tremps;
                    CreateList();
                }
            });


        //ManorPart

        }

    public void CreateList()
    {
        ListView list = (ListView) findViewById(R.id.Tremps_listView);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //send avia tremp object
                // trempsList.get(i);

                Intent intent = new Intent(ListTrempActivity.this, TrempDetailsActivity.class);
                //  intent.putExtra(   )
                startActivity(intent);
            }
        });

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


        public String getUserName(String userId)
        {
           GraphRequest d = new GraphRequest(AccessToken.getCurrentAccessToken(),
                    "/" + userId,
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {

                }
            });


            return name;

        }

        @Override
        public View  getView(final int i, View view, ViewGroup viewGroup) {

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.tremp_list_raw, null);

            }
           final TextView name = (TextView) view.findViewById(R.id.DriverName);
          final   TextView time = (TextView) view.findViewById(R.id.TrempExitTime);
            final   TextView seats = (TextView) view.findViewById(R.id.ava_seats);
            final Tremp st = trempsList.get(i);

            // until solve the proble with driver id
            if (st.getPhoneNumber().contains("342743"))
            {
                //until solve the problem with droiver id
                new GraphRequest(AccessToken.getCurrentAccessToken(),
                        "/" + st.getPhoneNumber(),
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {

                                try {
                                    name.setText(response.getJSONObject().getString("name"));
                                    seats.setText(String.valueOf(st.getSeets()));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).executeAsync();
            }
            else
            {
                //just until solve thr bug
                name.setText("null");
                seats.setText(String.valueOf(st.getSeets()));
            }

            return view;
        }
    }

}
