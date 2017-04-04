package com.example.hadar.trempyteam;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.hadar.trempyteam.Model.Model;
import com.example.hadar.trempyteam.Model.ModelFirebase;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

public class MarkerDetailsActivity extends Activity {

    final List<String> passengers_Names = new LinkedList<String>();
     String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_details);

        String id = (String) getIntent().getExtras().get("tremp_id");

        final TextView t = (TextView) findViewById(R.id.textView3);
        ModelFirebase fbModel = new ModelFirebase();
       final String names = "";

        fbModel.getPassengersByTrempId(id, new Model.GetPassengersListener() {

             String names = "";
            String name;
            @Override
            public void onComplete(List<String> list) {

                for (int i = 0; i< list.size(); i++)
                {
                    String ddddd = list.get(i).toString();


                    new GraphRequest(AccessToken.getCurrentAccessToken(),
                            "/" + ddddd,
                            null,
                            HttpMethod.GET,
                            new GraphRequest.Callback() {
                                @Override
                                public void onCompleted(GraphResponse response) {
                                    try {
                                        names += "\n\n" + response.getJSONObject().getString("name");
                                        t.setText(names);
                                        //passengers_Names.add(response.getJSONObject().getString("name"));


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).executeAsync();
                }
            }
        });






    }
}
