package com.example.hadar.trempyteam.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class ModelFirebase {

    public void addTremp(Tremp tremp){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Tremp").child(tremp.getId());

        myRef.setValue(tremp.toMap());
    }

    public void updateTremp(String id, String dest, String source, String phone, Date date){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference trempToUpdate = database.getReference("Tremp").child(id);

        if(trempToUpdate != null){
            trempToUpdate.child("phoneNumber").setValue(phone);
            trempToUpdate.child("SourceAddress").setValue(source);
            trempToUpdate.child("DestAddress").setValue(dest);
            trempToUpdate.child("trempDateTime").setValue(date);
        }
    }

    public void deleteTremp(Tremp tremp){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
       database.getReference("Tremp").child(tremp.getId()).removeValue();
    }
    public void deleteTremp(String id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("Tremp").child(id).removeValue();
    }
    public void deleteTremp(String id, String image){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("Tremp").child(id).removeValue();

        //check if there is an image to delete
        if(!image.isEmpty())
        {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference imageToDelete = storage.getReference().child("images").child(image);

            if(imageToDelete != null)
                imageToDelete.delete();
        }

    }
    public void getAllTremps(final Model.GetAllTrempsListener listener)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Tremp");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Tremp> tremps = new LinkedList<Tremp>();

                for (DataSnapshot trSnapshot : dataSnapshot.getChildren())
                {
                    Tremp t = trSnapshot.getValue(Tremp.class);
                    tremps.add(t);
                }
                listener.onComplete(tremps);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onComplete(null);

            }
        });
    }


    public void getAllTrempsByFilter(final String dest, final String from ,final Model.GetAllTrempsByFilerListener listener) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Tremp");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Tremp> tremps = new LinkedList<Tremp>();

                for (DataSnapshot trSnapshot : dataSnapshot.getChildren()) {
                    String d = trSnapshot.child("DestAddress").getValue().toString();
                    String f = trSnapshot.child("SourceAddress").getValue().toString();

                    List<String> wordsDestUserSearch = Arrays.asList(dest.split(" "));
                    List<String> wordsDestInFireBase = Arrays.asList(d.split(" "));
                    List<String> wordsSourceInFireBase = Arrays.asList(f.split(" "));
                    List<String> wordsSourceUserSearch = Arrays.asList(from.split(" "));

                    for (int j = 0; j < wordsSourceUserSearch.size(); j++)
                    {
                        if (wordsSourceInFireBase.contains(wordsSourceUserSearch.get(j)))
                        {
                            for (int i = 0; i < wordsDestUserSearch.size(); i++)
                            {
                                if (wordsDestUserSearch.get(i) == "" || wordsDestInFireBase.contains(wordsDestUserSearch.get(i)))
                                {
                                    Tremp t;
                                    try {
                                        t = trSnapshot.getValue(Tremp.class);
                                    }
                                    catch (Exception e){
                                        Log.d("Exception", "Can't create tremp " + e.getMessage());

                                        String id = (String)trSnapshot.child("id").getValue();
                                        String driverId = (String) trSnapshot.child("driverId").getValue();
//                                        Date trempDate = (Date)trSnapshot.child("trempDateTime").getValue();
                                        String carModel = (String) trSnapshot.child("CarModel").getValue();
                                        String source = (String) trSnapshot.child("SourceAddress").getValue();
                                        String dest = (String) trSnapshot.child("DestAddress").getValue();
                                        long seets = (long) trSnapshot.child("seets").getValue();
                                        String phone = (String) trSnapshot.child("phoneNumber").getValue();
                                        String imageName = (String) trSnapshot.child("imageName").getValue();
                                        t = new Tremp(id, seets, driverId, null, source, dest, phone, carModel, imageName);
                                    }

                                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                                    Date date = new Date();
                                    try {
                                        date = format.parse( trSnapshot.getValue(Tremp.class).getCreationDate().toString());
                                    }
                                    catch (Exception e)
                                    {

                                    }

                                    t.CreationDate = date;
                                    tremps.add(t);
                                 //   String dd = trSnapshot.getValue(Tremp.class).getTrempDateTime().toString();

                                    break;
                                }
                            }
                            break;
                        }
                    }

                    listener.onComplete(tremps);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onComplete(null);

            }
        });
    }

    public void addUser(User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User").child(user.getId());
        myRef.setValue(user);
    }

    public void saveImage(Bitmap imageBitmap, String name, final Model.SaveImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference imagesRef = storage.getReference().child("images").child(name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.fail();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                listener.complete(downloadUrl.toString());
            }
        });
    }



    public void getImage(String url, final Model.GetImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReference().child("images/" + url);

        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(3*ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                listener.onSccess(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                // Handle any errors
                Log.d("TAG",exception.getMessage());
                listener.onFail();
            }
        });

    }

}
