package com.example.hadar.trempyteam;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hadar.trempyteam.Model.ModelFirebase;
import com.example.hadar.trempyteam.Model.Tremp;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.json.JSONObject;

import java.util.Date;

public class MainActivity extends Activity {

//check
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    final int PRO = 1;
    private FirebaseAuth firebaseAuth;
//just check
    private FirebaseAuth.AuthStateListener firebaseAuthListner;
    ModelFirebase modelFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_facebook_login);

        //Try add tremp to fireBase table tremp
//        modelFB = new ModelFirebase();
//        Tremp t = new Tremp(3,"manor", new Date(2017,05,15),"Kfar Ahim", "Yavne", "Kia");
//        modelFB.addTremp(t);
//        Log.d("FB", "Add new tremp to db");

            // Initialize Facebook Login button
            callbackManager = CallbackManager.Factory.create();
            final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
            loginButton.setReadPermissions("email", "public_profile");
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    final AccessToken accessToken = loginResult.getAccessToken();


                    handleFacebookAccessToken(loginResult.getAccessToken());
                    GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject user, GraphResponse graphResponse) {



                            String userId = loginResult.getAccessToken().getUserId();
                            String accessToken = loginResult.getAccessToken().getToken();
                            String profileImgUrl = "https://graph.facebook.com/" + userId + "/picture?type=large";

                            Intent intent = new Intent(MainActivity.this, pro.class);
                            intent.putExtra("user_name",user.optString("name"));
                            intent.putExtra("urlimage",profileImgUrl);
                            startActivityForResult(intent, PRO);


                        }


                    }).executeAsync();

                }

                @Override
                public void onCancel() {
                      Log.d("OnCancle", "facebook:onCancel");

                }

                @Override
                public void onError(FacebookException error) {
                      Log.d("OnError", "facebook:onError", error);
                    // ...
                }
            });


            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuthListner = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        // Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    } else {
                        // User is signed out
                        // Log.d(TAG, "onAuthStateChanged:signed_out");
                    }
                    // ...
                }
            };
        }



    private void handleFacebookAccessToken(AccessToken token) {
        // Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //   Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //    Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListner);
    }
}
