package com.example.hadar.trempyteam.Model;

/**
 * Created by manor on 8/18/2017.
 */

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.util.EventLog;
import android.util.Log;

import com.example.hadar.trempyteam.MainAactivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OptionalDataException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

public class ModelSocketIo {
    private static ModelSocketIo modelSocketIo;
//    private String urlConnection = "http://192.168.1.115:8080";
    private String urlConnection = "http://193.106.55.103:8080";
    private List _listeners = new ArrayList();
    private NotificationListener unJoinListener;


    public static ModelSocketIo getInstance(){
        if(modelSocketIo == null)
            modelSocketIo = new ModelSocketIo();

        return modelSocketIo;
    }

    public ModelSocketIo(){

    }

    public void addUnJoinListener(NotificationListener listener){
        unJoinListener = listener;
    }

    public synchronized void addMoodListener( NotificationListener l ) {
        _listeners.add( l );
    }

    public synchronized void removeMoodListener( NotificationListener l ) {
        _listeners.remove( l );
    }


    private Socket mSocket;

    public void connectToServer(String userId){
        try {
            IO.Options options = new IO.Options();
            options.query = userId;
            mSocket = IO.socket(urlConnection);

//            mSocket = IO.socket(urlConnection);
            mSocket.on("onJoinTremp",onMessageArrived);
            mSocket.on("onUnJoinTremp", onUnJoinMessageArrived);
            mSocket.connect();
            mSocket.emit("onInit", userId);
        }
        catch (URISyntaxException e) {
            Log.d("cant connect to io ", e.getMessage());
        }

        catch (Exception e){
            Log.d("cant connect to io ", e.getMessage());
        }
    }

    private Emitter.Listener onMessageArrived = new Emitter.Listener(){

        @Override
        public void call(Object... args) {
            Log.d("message arrived", args.toString());

                String stringToPrint = "משתמש הצטרף לטרמפ שלך";

                NotificationEvent event = new NotificationEvent( this, stringToPrint );
                Iterator listeners = _listeners.iterator();
                while( listeners.hasNext() ) {
                    ( (NotificationListener) listeners.next() ).notificationReceived(event);
                }
            }



    };


    private Emitter.Listener onUnJoinMessageArrived = new Emitter.Listener(){

        @Override
        public void call(Object... args) {
            Log.d("message arrived", args.toString());

            String stringToPrint = "משתמש ביטל הצטרפות לטרמפ שלך";

            NotificationEvent event = new NotificationEvent( this, stringToPrint );
            unJoinListener.notificationReceived(event);
        }



    };

    //TODO: put it in thread pool and handle it
    private String getUserName(String userId){
        final String[] name = new String[1];
        try {

            new GraphRequest(AccessToken.getCurrentAccessToken(),
                    "/" + userId,
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            try {
                                name[0] = (response.getJSONObject().getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).executeAsync();

        }
        catch (Exception e){
            Log.d("exception", "can't get user name " + e.getMessage());
        }

        return name[0];
    }
}

