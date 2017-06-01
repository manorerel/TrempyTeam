package com.example.hadar.trempyteam.Model;

/**
 * Created by manor on 5/26/2017.
 */
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

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
    private String RIDE_URL="http://127.0.0.1:8080/api/ride/";
    private String FB_ID="fb_id";
    private String SOURCE="src";
    private String DEST="dst";
    private String DATE="date";
    private String RIDE_ID="ride_id";
    private String JOIN="join";
    private String UNJOIN="unjoin";

    public void getTremps(String userID, LatLng source, LatLng dest, String rideTime){
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet(String.format("{0}?{1}={2}&{3}={4}&{5}={6}&{7}={8}", RIDE_URL, FB_ID, userID,
                SOURCE, convertLocationToString(source), DEST, convertLocationToString(dest), DATE, rideTime));

        String text = null;
        try {
            JSONObject data = null;
            HttpResponse response = httpClient.execute(httpGet, localContext);

            HttpEntity entity = response.getEntity();
            if (entity instanceof HttpEntity)
                data = new JSONObject(EntityUtils.toString(entity));

            } catch (Exception e) {
//            return e.getLocalizedMessage();
        }

    }

    private String convertLocationToString(LatLng location){
        HashMap<String, Object> result = new HashMap<>();
        result.put("longitude", location.longitude);
        result.put("latitude", location.latitude);
        JSONObject json = new JSONObject(result);
        return json.toString();
    }

    public void createTremp(Tremp newTremp) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(RIDE_URL);
        JSONObject tremp = new JSONObject(newTremp.toMap());
        httpPost.setEntity(new StringEntity(convertJsonToString(tremp)));
        HttpResponse response = httpClient.execute(httpPost);
    }

    public void updateTremp(Tremp trempToUpdate){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(String.format("{0}{1}", RIDE_URL, trempToUpdate.id));
        JSONObject tremp = new JSONObject(trempToUpdate.toMap());
        String trempString = convertJsonToString(tremp);
        try {
            httpPut.setEntity(new StringEntity(trempString));
            HttpResponse response = httpClient.execute(httpPut);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteTremp(String trempToDeleteId){
        HttpClient httpClient = new DefaultHttpClient();
        HttpDelete httpDelete = new HttpDelete(String.format("{0}{1}",RIDE_URL, trempToDeleteId));
        try {
            HttpResponse response = httpClient.execute(httpDelete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void joinOrUnjoinTremp(String trempToJoinId, String useId, boolean isJoin){
        HttpClient httpClient = new DefaultHttpClient();
        String url = "";
        if(isJoin)
            url = String.format("{0}{1}",RIDE_URL, JOIN);
        else url = String.format("{0}{1}",RIDE_URL, UNJOIN);

        HttpPut httpPut = new HttpPut(url);
        try {
            JSONObject body = new JSONObject(String.format("ride_id:{0}, user_id:{1}",trempToJoinId, useId));
            httpPut.setEntity(new StringEntity(convertJsonToString(body)));
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



    private String convertJsonToString(JSONObject jsonObj){
        String b = jsonObj.toString().replace("{", "");
        return b.toString().replace("}", "");
    }
}
