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
import com.example.hadar.trempyteam.Model.ModelFirebase;
import com.example.hadar.trempyteam.Model.ModelSql;
import com.example.hadar.trempyteam.Model.Tremp;

import java.text.SimpleDateFormat;
import java.util.Date;
public class CreateNewTrempActivity extends Activity {
    private static final int REQUEST_WRITE_STORAGE = 112;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    ImageView imageView;
    String imageFileName = null;
    Bitmap imageBitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_tremp);
        final ModelFirebase fbModel = new ModelFirebase();
        imageView = (ImageView) findViewById(R.id.Image);

        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }


        Button saveBtn = (Button) findViewById(R.id.btnSave);
        Button cancleBtn = (Button) findViewById(R.id.btnCancel);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText phone = (EditText)findViewById(R.id.editTextPhone);
                EditText source = (EditText)findViewById(R.id.exitfrom);
                EditText dest = (EditText)findViewById(R.id.dest);
                EditText seetsText = (EditText)findViewById(R.id.avaliable_seats);
                DateEditText dateText = (DateEditText)findViewById(R.id.date);
                TimeEditText time = (TimeEditText)findViewById(R.id.time);
                EditText carModel = (EditText)findViewById(R.id.car_model);


                long seets = Long.parseLong(seetsText.getText().toString());
                Date date = new Date(dateText.getYear(), dateText.getMonth(), dateText.getDay(), time.getHour(),time.getMinute(), time.getSecond());
                Tremp newTremp = new Tremp(seets, "dd", date, source.getText().toString(), dest.getText().toString(),phone.getText().toString(), carModel.getText().toString(),"imageUrl");
                fbModel.addTremp(newTremp);

                if(imageBitmap != null){
                    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    String imName = "image_" + newTremp.getId() + "_" + timeStamp + ".jpg";
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

//                ModelSql sqlLight = new ModelSql();
//                sqlLight.addTremp(newTremp);
                Log.d("TAG", "Create new tremp and save to db");
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takingPicture();
            }
        });

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
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
