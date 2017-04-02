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
import com.example.hadar.trempyteam.Model.ModelSql;
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
        String cameFrom = (String) getIntent().getExtras().get("cameFrom");
        if (cameFrom != null && cameFrom.equals("personalArea")) {
            String isCreated = (String) getIntent().getExtras().get("isCreated");
            ModelSql modelSql = ModelSql.getInstance();

            if(isCreated.equals("true"))
                trempsList = modelSql.getAllTremps(true);
            else trempsList = modelSql.getAllTremps(false);

            CreateList();


        } else {

            // Hadar Part
            final String dest = (String) getIntent().getExtras().get("dest");
            final String from = (String) getIntent().getExtras().get("from");

            ModelFirebase fbModel = new ModelFirebase();
            fbModel.getAllTrempsByFilter(dest, from, new Model.GetAllTrempsByFilerListener() {
                @Override
                public void onComplete(List<Tremp> tremps) {

                    trempsList = tremps;
                    CreateList();
                }
            });


        }

    }

    public void CreateList()
    {
        ListView list = (ListView) findViewById(R.id.Tremps_listView);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //send avia tremp object
                Tremp tremp =  trempsList.get(i);

                Intent intent = new Intent(ListTrempActivity.this, TrempDetailsActivity.class);
                intent.putExtra("id",  tremp.getId());
                intent.putExtra("phone",  tremp.getPhoneNumber());
                intent.putExtra("source",  tremp.getSourceAddress());
                intent.putExtra("dest",  tremp.getDestAddress());
                intent.putExtra("seets",  tremp.getSeets());
                intent.putExtra("car",  tremp.getCarModel());
                intent.putExtra("image",  tremp.getImageName());
                if (tremp.getTrempDateTime() != null) {
                    intent.putExtra("date", tremp.getTrempDateTime().toString());

                }
                startActivity(intent);
            }
        });

    }
//check its the master
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
            final TextView time = (TextView) view.findViewById(R.id.TrempExitTime);
            final TextView seats = (TextView) view.findViewById(R.id.ava_seats);
            final Tremp st = trempsList.get(i);

            seats.setText(String.valueOf(st.getSeets()));


            // until solve the proble with driver id

           final String driver_id = st.getDriverId();
            /*// until solve the proble with driver id
           if (st.getPhoneNumber().contains("342743") || st.getPhoneNumber().contains("93022164"))
            {*/
            try {
                //until solve the problem with droiver id
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
