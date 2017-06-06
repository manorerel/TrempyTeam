package com.example.hadar.trempyteam.Model;

/**
 * Created by manor on 5/26/2017.
 */
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.gcm.Task;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.HttpResponseException;
import cz.msebera.android.httpclient.client.methods.HttpDelete;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.entity.mime.content.StringBody;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.protocol.BasicHttpContext;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.EntityUtils;

public class ModelRest {
    private String RIDE_URL="http://trempydb.cloudapp.net:8080/api/rides";
    private String FB_ID="fb_id";
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


    private static ModelRest modelRest;

    public static ModelRest getInstance(){
        if(modelRest == null)
            modelRest = new ModelRest();

        return modelRest;
    }

    public List<Tremp> getTremps(final String userID, final LatLng source, final LatLng dest, final String rideTime){

        final List<Tremp> tremps = new LinkedList<Tremp>();
        readThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
//                HttpGet httpGet = new HttpGet(String.format("{0}?{1}={2}&{3}={4}&{5}={6}&{7}={8}", RIDE_URL, FB_ID, userID,
//                        SOURCE, convertLocationToString(source), DEST, convertLocationToString(dest), DATE, rideTime));
                HttpGet httpGet = new HttpGet(RIDE_URL);

                try {
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



                } catch (Exception e) {
//            return e.getLocalizedMessage();
                }
            }
        });

        return tremps;
    }

    public List<Tremp> getTremps(final String driverID){
        final List<Tremp> tremps = new LinkedList<Tremp>();

        Future<?> future = readThreadPool.submit(new Runnable() {
            @Override
            public void run() {
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
                }
                catch (Exception e){
                    e.printStackTrace();

                }
            }
        });


        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return tremps;
    }

    private String convertLocationToString(LatLng location){
        HashMap<String, Object> result = new HashMap<>();
        result.put("longitude", location.longitude);
        result.put("latitude", location.latitude);
        JSONObject json = new JSONObject(result);
        return json.toString();
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
//                    HttpGet httpGet = new HttpGet(RIDE_URL);
                    HttpResponse response = httpClient.execute(httpPost);
//                    HttpResponse response = httpClient.execute(httpGet);
                    String server_response="";
                    InputStream instream;

                    if(response.getStatusLine().getStatusCode()==200){
                        server_response = EntityUtils.toString(response.getEntity());
                        instream = response.getEntity().getContent();
                        Log.d("instream",instream.toString());
                        JSONObject json = new JSONObject(server_response);
                        Object v = json.getJSONObject("ride");

                    }



                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String line = "";

                    while ((line = rd.readLine()) != null) {
                        System.out.println(line);
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

    private Tremp convertJsonToTremp(JSONObject j) {
        try {
            return new Tremp(j.getString("_id"),j.getLong("seets"),j.getString("driverId"), j.getString("trempDateTime"),"", "", j.getString("phoneNumber"),j.getString("carModel"), j.getString("imageName"), null,new LatLng(31,32),new LatLng(31,32));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateTremp(final Tremp trempToUpdate){
        updateThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPut httpPut = new HttpPut(String.format("{0}{1}", RIDE_URL, trempToUpdate.id));

                try {
                    JSONObject tremp = convertTrempToJson(trempToUpdate);
                    StringEntity trempString = new StringEntity(tremp.toString(), "UTF8");
                    httpPut.setEntity(trempString);
                    httpPut.setHeader("Content-type", "application/json");
                    HttpResponse response = httpClient.execute(httpPut);
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
                HttpDelete httpDelete = new HttpDelete(String.format("{0}{1}",RIDE_URL, trempToDeleteId));
                try {
                    HttpResponse response = httpClient.execute(httpDelete);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void joinOrUnjoinTremp(final String trempToJoinId, final String useId, final boolean isJoin){
        updateThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                String url = "";
                if(isJoin)
                    url = String.format("{0}{1}",RIDE_URL, JOIN);
                else url = String.format("{0}{1}",RIDE_URL, UNJOIN);

                HttpPut httpPut = new HttpPut(url);
                try {
                    JSONObject body = new JSONObject();
                    body.put("ride_id", trempToJoinId);
                    body.put("user_id", useId);
                    httpPut.setEntity(new StringEntity(body.toString(), "UTF8"));
                    httpPut.setHeader("Content-type", "application/json");
                    HttpResponse response = httpClient.execute(httpPut);
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

    private JSONObject convertTrempToJson(Tremp newTremp){
        JSONObject tremp = new JSONObject();

        try {
            JSONObject sourceJson = new JSONObject();
            sourceJson.put("long", 31);
            sourceJson.put("lat", 41);

            JSONObject destJson = new JSONObject();
            destJson.put("long", 31);
            destJson.put("lat", 41);


            tremp.put("driverId", newTremp.driverId);
            tremp.put("phoneNumber", newTremp.phoneNumber);
            tremp.put("seets", newTremp.seets);
            tremp.put("sourceAddress", sourceJson);
            tremp.put("destAddress", destJson);
            tremp.put("carModel", newTremp.carModel);
            tremp.put("trempDateTime", newTremp.trempDateTime);
            tremp.put("imageName", newTremp.imageName);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return tremp;
    }

    private String convertJsonToString(JSONObject jsonObj){
        String b = jsonObj.toString().replace("{", "");
        return b.toString().replace("}", "");
    }
}
