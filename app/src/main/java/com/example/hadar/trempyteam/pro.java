package com.example.hadar.trempyteam;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class pro extends Activity {

    private ImageView profileImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro);

        final String name = (String) getIntent().getExtras().get("user_name");
        TextView nn = (TextView) findViewById(R.id.user_name);
        nn.setText("Hello " + name);

        String url = (String) getIntent().getExtras().get("urlimage");
        Log.d("url: ", url);

        profileImgView = (ImageView) findViewById(R.id.profile_img);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        {
        }
    }
}
