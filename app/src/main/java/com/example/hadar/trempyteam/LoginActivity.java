package com.example.hadar.trempyteam;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
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

public class LoginActivity extends Activity {

//check
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    final int main = 1;
    private FirebaseAuth firebaseAuth;

    String userId ="";
    private FirebaseAuth.AuthStateListener firebaseAuthListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_facebook_login);

            // Initialize Facebook Login button
            callbackManager = CallbackManager.Factory.create();
            final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
            loginButton.setReadPermissions("email", "public_profile", "user_friends");
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {


                    Intent intent = new Intent(LoginActivity.this, MainAactivity.class);
                    startActivityForResult(intent, main);

                        }

/*
                *//**//* make the API call *//**//*
                final GraphRequestAsyncTask graphRequestAsyncTask =    new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/" +userId + "/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {





                                *//**//* try
                                {
                                    Log.d("userid", userId);
                                    //  ArrayList a = new ArrayList();
                                    JSONArray rawName = response.getJSONObject().getJSONArray("data");
                                    for(int i=0; i<rawName.length(); i++){
                                        {

                                            //  a.add(i, rawName.getJSONObject(i).getString("name"));
                                            Log.d("name: ", rawName.getJSONObject(i).getString("name"));
                                        }
                                    }
                                }
                                catch ( Throwable t )
                                {
                                    t.printStackTrace();
                                }*//**//**/









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
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
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
