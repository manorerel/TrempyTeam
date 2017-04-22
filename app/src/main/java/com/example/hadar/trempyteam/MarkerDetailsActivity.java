package com.example.hadar.trempyteam;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;

import com.example.hadar.trempyteam.Model.Model;
import com.example.hadar.trempyteam.Model.ModelFirebase;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
    //  final ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.friendProfilePicture);
        ModelFirebase fbModel = new ModelFirebase();
       final String names = "";

        t.setText("\n\n Yet there are no passengers");

        fbModel.getPassengersByTrempId(id, new Model.GetPassengersListener() {

            String ddddd;
             String names = "";
            String name;

                @Override
                public void onComplete (List < String > list) {

                    int p = list.size();

                    if (list.size() != 0) {

                        for (int i = 0; i < list.size(); i++) {
                             ddddd = list.get(i).toString();


                            // change to a list view  instead a string onother list view
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
                                              //  profilePictureView.setProfileId(ddddd);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).executeAsync();
                        }


                        //public static Bitmap getFacebookProfilePicture(String userID){
                      //  URL imageURL = null;
                       // try {
                          //  imageURL = new URL("https://graph.facebook.com/" + ddddd + "/picture?type=large");
                    //        Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());


                      //  } catch (MalformedURLException e) {
                     //       e.printStackTrace();
                     //   } catch (IOException e) {
                     //       e.printStackTrace();
                      //  }


                   //     }





                    }
                }
});






    }
}
