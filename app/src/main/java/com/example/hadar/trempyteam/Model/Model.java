package com.example.hadar.trempyteam.Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.example.hadar.trempyteam.TrempyApp;
/**
 * Created by aviac on 3/25/2017.
 */

public class Model {

    private final static Model instance = new Model();
    ModelFirebase modelFirebase;
    Context Trempy;

    private Model(){
        modelFirebase = new ModelFirebase();
        Trempy = TrempyApp.getAppContext();

    }

    public static Model getInstance(){
        return instance;
    }

    public interface SaveImageListener{
        void complete(String url);
        void fail();
    }
    public void saveImage(final Bitmap imageBitmap, String name, final SaveImageListener listener) {
        //1. save the image remotly
        modelFirebase.saveImage(imageBitmap, name, new SaveImageListener() {
            @Override
            public void complete(String url) {
                // 2. saving the file localy
                String localName = getLocalImageFileName(url);
                Log.d("TAG","cach image: " + localName);
                saveImageToFile(imageBitmap,localName); // synchronously save image locally
                listener.complete(url);
            }
            @Override
            public void fail() {
                listener.fail();
            }
        });
    }


    public interface GetImageListener{
        void onSccess(Bitmap image);
        void onFail();
    }

    private String getLocalImageFileName(String url) {
        String name = URLUtil.guessFileName(url, null, null);
        return name;
    }

    public void loadImage(final String url, final GetImageListener listener) {
        //1. first try to find the image on the device
        String localFileName = getLocalImageFileName(url);
        Bitmap image = loadImageFromFile(localFileName);
        if (image == null) {                                      //if image not found - try downloading it from parse
            Log.d("TAG","fail reading cache image: " + localFileName);

            modelFirebase.getImage(url, new GetImageListener() {
                @Override
                public void onSccess(Bitmap image) {
                    //2.  save the image localy
                    String localFileName = getLocalImageFileName(url);
                    Log.d("TAG","save image to cache: " + localFileName);
                    saveImageToFile(image,localFileName);
                    //3. return the image using the listener
                    listener.onSccess(image);
                }

                @Override
                public void onFail() {
                    listener.onFail();
                }
            });
        }else {
            Log.d("TAG","OK reading cache image: " + localFileName);
            listener.onSccess(image);
        }
    }


    private void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageFileName);
            imageFile.createNewFile();

            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            addPicureToGallery(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addPicureToGallery(File imageFile){
        //add the picture to the gallery so we dont need to manage the cache size
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        TrempyApp.getAppContext().sendBroadcast(mediaScanIntent);
    }

    private Bitmap loadImageFromFile(String imageFileName){
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir,imageFileName);

            //File dir = context.getExternalFilesDir(null);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("tag","got image from cache: " + imageFileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
