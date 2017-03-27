package com.example.hadar.trempyteam;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.hadar.trempyteam.Model.Model;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aviac on 3/25/2017.
 */

public class ShareImageActivity extends Activity{
    private static final int REQUEST_WRITE_STORAGE = 112;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    ImageView imageView;
    String imageFileName = null;
    Bitmap imageBitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_image);
        imageView = (ImageView) findViewById(R.id.Image);
        final EditText idIm = (EditText) findViewById(R.id.idImageName);

        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }


        Button add = (Button) findViewById(R.id.addImage);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageBitmap != null){
                    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    String imName = "image_" + idIm.getText().toString() + "_" + timeStamp + ".jpg";
                    Model.getInstance().saveImage(imageBitmap, imName, new Model.SaveImageListener() {
                        @Override
                        public void complete(String url) {
                            saveAndClose();
                        }

                        @Override
                        public void fail() {
                            saveAndClose();
                        }
                    });
                }else{
                    saveAndClose();
                }

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takingPicture();
            }
        });

    }
    private void saveAndClose(){

        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }


    private void takingPicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

}
