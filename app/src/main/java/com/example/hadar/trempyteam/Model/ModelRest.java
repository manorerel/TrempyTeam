package com.example.hadar.trempyteam.Model;

/**
 * Created by manor on 5/26/2017.
 */

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpDelete;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.protocol.BasicHttpContext;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.EntityUtils;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;

public class ModelRest {
    private String RIDE_URL="http://193.106.55.103:80/api/rides";
    private String USER_URL="http://193.106.55.103:80/api/users";
    private String FB_ID="fbId";
    private String DRIVER="driver";
    private String SOURCE="src";
    private String DEST="dst";
    private String DATE="date";
    private String RIDE_ID="ride_id";
    private String JOIN="join";
    private String UNJOIN="unjoin";

    private ExecutorService createThreadPool = Executors.newFixedThreadPool(5);
    private ExecutorService updateThreadPool = Executors.newFixedThreadPool(5);
    private ExecutorService deleteThreadPool = Executors.newFixedThreadPool(5);
    private ExecutorService readThreadPool = Executors.newFixedThreadPool(5);
    final String user_connected_id = AccessToken.getCurrentAccessToken().getUserId();
    String save_server_response = "s";
    private static ModelRest modelRest;
    JSONArray ret_tremps = new JSONArray();
    String to = "";

    public static ModelRest getInstance() {
        if (modelRest == null)
            modelRest = new ModelRest();

        return modelRest;
    }

    public void connectToServer(final String userId) {
        createThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                String url = USER_URL + "/" + userId;
                HttpPost httpPost = new HttpPost(url);

                try {
                    HttpResponse response = httpClient.execute(httpPost);
                    if(response.getStatusLine().getStatusCode()==200){
                        Log.d("connect to server", "success");
                    }
                    else{
                        Log.d("connect to server", "not success");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public List<Tremp> getTremps(final String userID, final LatLng source, final LatLng dest, final String rideTime){

        final List<Tremp> tremps = new LinkedList<Tremp>();
        final Future<List<Tremp>> futureTremps;
        final Future<List<Tremp>> testFT;


        futureTremps = readThreadPool.submit(new Callable<List<Tremp>>() {
            @Override
            public List<Tremp> call() throws Exception{
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                String url = RIDE_URL + "/params?" + FB_ID +"=" +userID +"&" + SOURCE + "=" + convertLocationToString(source) + "&" + DEST + "=" +
                        convertLocationToString(dest) + "&" + DATE + "=" + rideTime;

//                HttpGet httpGet = new HttpGet(RIDE_URL);

                try {
                    HttpGet httpGet = new HttpGet(url);
                    HttpResponse response = httpClient.execute(httpGet, localContext);

                    if(response.getStatusLine().getStatusCode()==200){
                        String server_response = EntityUtils.toString(response.getEntity());
                        ret_tremps = new JSONArray(server_response);
                        save_server_response = server_response;
                        final JSONArray trempsJson = new JSONArray(server_response);
                        final JSONArray trempsJson_to_return = new JSONArray(save_server_response);

                        Thread[] threads = new Thread[trempsJson.length()];
                        for (int i = 0; i < trempsJson.length(); i++) {
                            int j = i;
                           checkIfRelevant(trempsJson, trempsJson_to_return, j, tremps);
                        }

                        Log.i("Server response", server_response);
                    } else {
                        Log.i("Server response", "Failed to get server response");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }


                final List<Tremp> trempsToReturn_ = callToKNN();

                return trempsToReturn_;

            }
        });

        List<Tremp> trempsToReturn;

        try {
            trempsToReturn = futureTremps.get();
            return (trempsToReturn);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e){
            Log.d("exception: ", e.getMessage());
            return null;
        }}

    private void checkIfRelevant(final JSONArray trempsJson, final JSONArray trempsJson_to_return, int i, final List<Tremp> tremps) throws JSONException {
        final int j = i;
        final Tremp t = convertJsonToTremp(trempsJson.getJSONObject(j).getJSONObject("ride"));

        try {
            new GraphRequest(AccessToken.getCurrentAccessToken(),
                    user_connected_id + "/friends/" + t.driverId,
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {

                            final JSONObject resObject = response.getJSONObject();
                            JSONArray friendData = null;
                            try {
                                friendData = resObject.getJSONArray("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (friendData.length() > 0) {
                                callToMutualFriends(t, trempsJson_to_return, j, tremps);
                            }
                            else
                            {
                                JSONObject js = null;
                                try {
                                    js = trempsJson_to_return.getJSONObject(j);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    js.put("isFriends", 0);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    ret_tremps.put(j, js);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        }
                    }).executeAndWait();
        } catch (Exception e) {
            Log.d("exception", "can't get user name " + e.getMessage());
        }
    }

    private void callToMutualFriends(Tremp t, final JSONArray trempsJson_to_return, final int j, final List<Tremp> tremps) {
        try {

            Bundle params = new Bundle();
            params.putString("fields", "context.fields(mutual_friends)");

            new GraphRequest(AccessToken.getCurrentAccessToken(),
                    t.driverId,
                    params,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse res) {
                            JSONObject friensdData = null;
                            try {
                                friensdData =  res.getJSONObject().getJSONObject("context").getJSONObject("mutual_friends").getJSONObject("summary");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                final String total_count = friensdData.getString("total_count");
                                JSONObject js = trempsJson_to_return.getJSONObject(j);
                                js.put("mutualFriends", Integer.parseInt(total_count));
                                js.put("isFriends", 1);
                                ret_tremps.put(j, js);

                                Log.d("lemgth", Integer.toString( ret_tremps.length()));
                                int l = ret_tremps.length();
                                int i = 0;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).executeAndWait();

        } catch (Exception e) {
            Log.d("exception", "can't get user name " + e.getMessage());
        }
    }

    private List<Tremp> callToKNN() throws JSONException {

        final List<Tremp> tremps = new LinkedList<Tremp>();
        final Future<List<Tremp>> futureTremps;

                JSONObject ret_Obj = new JSONObject();
                ret_Obj.put("fbId", user_connected_id);
                ret_Obj.put("extendedRides", ret_tremps);
                final String s = ret_Obj.toString();

                HttpClient httpClient = new DefaultHttpClient();
                HttpPut httpPost = new HttpPut(RIDE_URL + "/knn");

                try {
                    JSONObject body = new JSONObject();
                    body.put("Source_Array_preferences", s);

                    httpPost.setEntity(new StringEntity(body.toString(), "UTF8"));
                    httpPost.setHeader("Content-type", "application/json");

                    HttpResponse responsee = httpClient.execute(httpPost);
                    String server_response = "";
                    InputStream instream;

                    if (responsee.getStatusLine().getStatusCode() == 200) {
                        server_response = EntityUtils.toString(responsee.getEntity());
                        to = server_response;
                        JSONArray trempsJson = new JSONArray(server_response);
                        for (int i = 0; i < trempsJson.length(); i++) {
                            Tremp t = convertJsonToTremp(trempsJson.getJSONObject(i).getJSONObject("ride"));
                            tremps.add(t);
                        }

                        return tremps;
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        return tremps;
    }


    public void test(String tremps) throws JSONException {

        JSONObject ret_Obj = new JSONObject();
        ret_Obj.put("fbId", user_connected_id);
        ret_Obj.put("extendedRides", tremps);
        final String s = ret_Obj.toString();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPut httpPost = new HttpPut(RIDE_URL + "/knn");

                try {
                    JSONObject body = new JSONObject();
                    body.put("Source_Array_preferences", s);

                    httpPost.setEntity(new StringEntity(body.toString(), "UTF8"));
                    httpPost.setHeader("Content-type", "application/json");

                    HttpResponse responsee = httpClient.execute(httpPost);
                    String server_response = "";
                    InputStream instream;

                    if (responsee.getStatusLine().getStatusCode() == 200) {
                        server_response = EntityUtils.toString(responsee.getEntity());
                        instream = responsee.getEntity().getContent();
                        Log.d("instream", instream.toString());
                        JSONObject json = new JSONObject(server_response);
                        Object v = json.getJSONObject("ride");
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        });


        t.start();
    }

    public List<Tremp> getTrempsJoined(final String userId){
        final List<com.example.hadar.trempyteam.Model.Tremp> tremps = new LinkedList<com.example.hadar.trempyteam.Model.Tremp>();
        final Future<List<com.example.hadar.trempyteam.Model.Tremp>> futureTremps;

        futureTremps = readThreadPool.submit(new Callable<List<com.example.hadar.trempyteam.Model.Tremp>>() {
            @Override
            public List<com.example.hadar.trempyteam.Model.Tremp> call() throws Exception {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                String url = RIDE_URL + "/joined/" + userId;
                HttpGet httpGet = new HttpGet(url);

                try{
                    HttpResponse response = httpClient.execute(httpGet, localContext);

                    if(response.getStatusLine().getStatusCode()==200){
                        String server_response = EntityUtils.toString(response.getEntity());
                        JSONArray trempsJson = new JSONArray(server_response);
                        for(int i=0; i< trempsJson.length(); i++){
                            com.example.hadar.trempyteam.Model.Tremp t = convertJsonToTremp(trempsJson.getJSONObject(i));
                            tremps.add(t);
                        }
                        Log.i("Server response", server_response );
                    } else {
                        Log.i("Server response", "Failed to get server response" );
                    }

                    return tremps;
                }
                catch (Exception e){
                    e.printStackTrace();

                }
                return tremps;
            }
        });

        List<com.example.hadar.trempyteam.Model.Tremp> trempsToReturn;

        try {
            trempsToReturn = futureTremps.get();
            return (trempsToReturn);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e){
            Log.d("exception: ", e.getMessage());
            return null;
        }

    }

    public List<Tremp> getTremps(final String driverID){
        final List<Tremp> tremps = new LinkedList<Tremp>();
        final Future<List<Tremp>> futureTremps;

        futureTremps = readThreadPool.submit(new Callable<List<Tremp>>() {
            @Override
            public List<Tremp> call() throws Exception {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                String url = RIDE_URL + "/" + DRIVER + "/" + driverID;
                HttpGet httpGet = new HttpGet(url);

                try{
                    HttpResponse response = httpClient.execute(httpGet, localContext);

                    if(response.getStatusLine().getStatusCode()==200){
                        String server_response = EntityUtils.toString(response.getEntity());
                        JSONArray trempsJson = new JSONArray(server_response);
                        for(int i=0; i< trempsJson.length(); i++){
                            Tremp t = convertJsonToTremp(trempsJson.getJSONObject(i));
                            tremps.add(t);
                        }
                        Log.i("Server response", server_response );
                    } else {
                        Log.i("Server response", "Failed to get server response" );
                    }

                    return tremps;
                }
                catch (Exception e){
                    e.printStackTrace();

                }
                return tremps;
            }
        });

        List<Tremp> trempsToReturn;

        try {
            trempsToReturn = futureTremps.get();
            return (trempsToReturn);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e){
            Log.d("exception: ", e.getMessage());
            return null;
        }

    }




    public void createTremp(final Tremp newTremp) {

        createThreadPool.submit(new Runnable() {
            @Override
            public void run() {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(RIDE_URL);

                try {
                    JSONObject tremp = convertTrempToJson(newTremp);
                    StringEntity body = new StringEntity(tremp.toString(), "UTF8");
                    httpPost.setEntity(body);
                    httpPost.setHeader("Content-type", "application/json");
                    HttpResponse response = httpClient.execute(httpPost);
                    String server_response="";
                    InputStream instream;

                    if(response.getStatusLine().getStatusCode()==200){
                        server_response = EntityUtils.toString(response.getEntity());
                        instream = response.getEntity().getContent();
                        Log.d("instream",instream.toString());
                        JSONObject json = new JSONObject(server_response);
                        Object v = json.getJSONObject("ride");
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            });
    }

    public void updateTremp(final Tremp trempToUpdate){
        updateThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                String url = RIDE_URL + "/" + trempToUpdate.id;
                HttpPut httpPut = new HttpPut(url);

                try {
                    JSONObject tremp = convertTrempToJson(trempToUpdate);
                    StringEntity trempString = new StringEntity(tremp.toString(), "UTF8");
                    httpPut.setEntity(trempString);
                    httpPut.setHeader("Content-type", "application/json");
                    HttpResponse response = httpClient.execute(httpPut);
                    if(response.getStatusLine().getStatusCode()==200){
                        Log.d("update ride", "success");
                    } else Log.d("update ride", "not success");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void deleteTremp(final String trempToDeleteId){
        deleteThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                String url = RIDE_URL + "/" + trempToDeleteId;
                HttpDelete httpDelete = new HttpDelete(url);
                try {
                    HttpResponse response = httpClient.execute(httpDelete);

                    if(response.getStatusLine().getStatusCode()==200){
                        Log.d("delete ride", "success");
                    } else Log.d("delete ride", "not success");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void joinOrUnjoinTremp(final String trempToJoinId, final String useId, final String choose_index, final boolean isJoin) {
        updateThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                String url = "";
                if(isJoin)
                    url = RIDE_URL + "/" + JOIN;
                else url = RIDE_URL + "/" + UNJOIN;

                // what to do with unjoin ???

                HttpPut httpPut = new HttpPut(url);
                try {
                    JSONObject body = new JSONObject();
                    // body.put("rideId", trempToJoinId);
                    body.put("userId", useId);
                    body.put("choose_index", choose_index);
                    body.put("Source_Array_preferences", to);
                    httpPut.setEntity(new StringEntity(body.toString(), "UTF8"));
                    httpPut.setHeader("Content-type", "application/json");
                    HttpResponse response = httpClient.execute(httpPut);

                    if(response.getStatusLine().getStatusCode()==200){
                        Log.d("join or unjoin", "success");
                    } else Log.d("join or unjoin", "not success");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private Tremp convertJsonToTremp(JSONObject j) {
        try {
            JSONObject sourceJson = j.getJSONObject("sourceAddress");
            LatLng source = new LatLng(sourceJson.getDouble("lat"), sourceJson.getDouble("long"));

            JSONObject destJson = j.getJSONObject("destAddress");
            LatLng dest = new LatLng(destJson.getDouble("lat"), destJson.getDouble("long"));

            String dateFromJson = j.getString("trempDateTime");
//            String dateTime = convertDateTime(dateFromJson);
            String dateTime = j.getString("trempDateTime");
            ArrayList<String> passengers = new ArrayList<String>();
            JSONArray passengersJson = j.getJSONArray("Passengers");
            for(int i=0; i<passengersJson.length(); i++){
                passengers.add(passengersJson.getString(i));
            }

            return new Tremp(j.getString("_id"),j.getLong("seets"),j.getString("driverId"), dateTime, source, dest, j.getString("phoneNumber"),j.getString("carModel"), j.getString("imageName"), passengers);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String convertLocationToString(LatLng location){

        //first long second lat
        if(location == null)
            return 0 + "T" + 0;
        return  location.longitude + "T" + location.latitude;
    }

    private String convertDateTime(String dateToConvert){
        String[] dates = dateToConvert.split("T");
        String date="";
        String time="";
        if(dates.length == 2){
            String[] part1 = dates[0].split("-");
            String[] part2 = dates[1].split(":");
            if(part1.length == 3 && part2.length > 2)
            {
                date = part1[2] + "/" + part1[1] + "/" + part1[0];
                time = part2[0] + ":" + part2[1];
            }
        }

        return date + " " + time;
    }

    private JSONObject convertTrempToJson(Tremp newTremp){
        JSONObject tremp = new JSONObject();

        try {
            JSONObject sourceJson = new JSONObject();
            sourceJson.put("long", newTremp.getSource().longitude);
            sourceJson.put("lat", newTremp.getSource().latitude);

            JSONObject destJson = new JSONObject();
            destJson.put("long", newTremp.getDest().longitude);
            destJson.put("lat", newTremp.getDest().latitude);


            tremp.put("driverId", newTremp.driverId);
            tremp.put("phoneNumber", newTremp.phoneNumber);
            tremp.put("seets", newTremp.seets);
            tremp.put("sourceAddress", sourceJson);
            tremp.put("destAddress", destJson);
            tremp.put("carModel", newTremp.carModel);
            tremp.put("trempDateTime", newTremp.trempDateTime);
            tremp.put("imageName", newTremp.imageName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tremp;
    }
}
