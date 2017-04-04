package com.example.hadar.trempyteam;

import android.app.Activity;
import android.content.Intent;
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
import com.example.hadar.trempyteam.Model.ModelSql;
import com.example.hadar.trempyteam.Model.Tremp;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
                intent.putExtra("id",  tremp.getTrempId());
                intent.putExtra("phone",  tremp.getTrempPhoneNumber());
                intent.putExtra("source",  tremp.getTrempSourceAddress());
                intent.putExtra("dest",  tremp.getTrempDestAddress());
                intent.putExtra("seets",  tremp.getTrempSeets());
                intent.putExtra("car",  tremp.getTrempcarModel());
                intent.putExtra("image",  tremp.getTrempImageName());
                intent.putExtra("driverId",  tremp.getTrempDriverId());
                if (tremp.getTrempDate() != null) {
                    intent.putExtra("date", convertDateToString(tremp.getTrempDate()));


                }
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            ModelSql modelSql = ModelSql.getInstance();
            trempsList = modelSql.getAllTremps(true);

            CreateList();
        }
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

        @Override
        public View  getView(final int i, View view, ViewGroup viewGroup) {

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.tremp_list_raw, null);
            }
            final TextView name = (TextView) view.findViewById(R.id.DriverName);
            final TextView time = (TextView) view.findViewById(R.id.TrempExitTime);
            final TextView seats = (TextView) view.findViewById(R.id.ava_seats);
            final Tremp st = trempsList.get(i);

              seats.setText(String.valueOf(st.getTrempSeets()));


           final String driver_id = st.getTrempDriverId();

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

    private static String convertDateToString(Date date){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateText = df.format(date);

        return dateText;
    }
}
