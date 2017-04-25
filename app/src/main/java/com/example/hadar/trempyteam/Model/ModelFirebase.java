package com.example.hadar.trempyteam.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Locale;

public class ModelFirebase {

    public void addTremp(Tremp tremp){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Tremp").child(tremp.getId());

        myRef.setValue(tremp.toMap());
    }


    public void getPassengersByTrempId(final String tremp_id, final Model.GetPassengersListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Tremp").child(tremp_id);
        final String tt = tremp_id;

        Boolean empty = true;
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> pass_ids=new LinkedList<String>();;
                Map<String, String> td = ( HashMap<String, String>) dataSnapshot.child("Passengers").getValue();


                if (td != null)
                {
                    for (Map.Entry<String,String> entry : td.entrySet())
                    {
                        String ss = entry.getValue();
                        pass_ids.add(ss);
                    }

                    listener.onComplete(pass_ids);

                }
                else
                {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onComplete(null);

            }
        });

    }


    public void updateTremp(String id, String dest, String source, String phone, String date, String car){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference trempToUpdate = database.getReference("Tremp").child(id);

        if(trempToUpdate != null){
            trempToUpdate.child("phoneNumber").setValue(phone);
            trempToUpdate.child("sourceAddress").setValue(source);
            trempToUpdate.child("destAddress").setValue(dest);
            trempToUpdate.child("trempDateTime").setValue(date);
            trempToUpdate.child("carModel").setValue(car);
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


    public void UpdateSeatsTremp(final String Trempid, final String passenger_id, final boolean isJoin, final Model.UpdateSeatsTrempListener listener){

        final String tremp_id = Trempid;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Tremp").child(tremp_id);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //for (DataSnapshot trSnapshot : dataSnapshot.getChildren()) {


                Tremp currTremp = null;

                try {
                    currTremp = dataSnapshot.getValue(Tremp.class);
                } catch (Exception e) {
                    Log.d("Exception", "Can't create tremp " + e.getMessage());
                }


                if(isJoin) {

                    Long currSeats = currTremp.getSeets();
                    dataSnapshot.getRef().child("seets").setValue(currSeats - 1);
                    dataSnapshot.getRef().child("Passengers").push().setValue(passenger_id);
                    currTremp.setSeets(currSeats - 1);
                    currTremp.setNewPassengerToTremp(passenger_id);
                    ModelSql.getInstance().addTremp(currTremp, false);
                    User.GetAppUser().addTrempToJoinList(currTremp.getId());
                    listener.onComplete();
                }
                else{
                    Long currSeats = currTremp.getSeets();
                    dataSnapshot.getRef().child("seets").setValue(currSeats + 1);
                    dataSnapshot.getRef().child("Passengers").push().setValue(passenger_id);
                    currTremp.setSeets(currSeats + 1);
                    currTremp.removePassenger(passenger_id);
                    dataSnapshot.getRef().child("Passengers").removeValue();
                    dataSnapshot.getRef().child("Passengers").setValue(currTremp.getTrempistsList());
                    ModelSql.getInstance().deleteTremp(currTremp.id);
                    User.GetAppUser().addTrempToJoinList(currTremp.getId());
                    listener.onComplete();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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


    public void getAllTrempsByFilter(final String time, final String searchDateTime, final String dest, final String from ,final Model.GetAllTrempsByFilerListener listener) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Tremp");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Tremp> tremps = new LinkedList<Tremp>();


                for (DataSnapshot trSnapshot : dataSnapshot.getChildren()) {
                    String d = trSnapshot.child("destAddress").getValue().toString();
                    String f = trSnapshot.child("sourceAddress").getValue().toString();

                    List<String> wordsDestUserSearch = Arrays.asList(dest.split(" "));
                    List<String> wordsDestInFireBase = Arrays.asList(d.split(" "));
                    List<String> wordsSourceInFireBase = Arrays.asList(f.split(" "));
                    List<String> wordsSourceUserSearch = Arrays.asList(from.split(" "));

                    for (int j = 0; j < wordsSourceUserSearch.size(); j++)
                    {
                        if (wordsSourceInFireBase.contains(wordsSourceUserSearch.get(j)) || wordsSourceUserSearch.get(j) == "" )
                        {
                            for (int i = 0; i < wordsDestUserSearch.size(); i++)
                            {
                                if (wordsDestUserSearch.get(i) == "" || wordsDestInFireBase.contains(wordsDestUserSearch.get(i)))
                                {
                                    String trempDate = "";
                                    Tremp t = trSnapshot.getValue(Tremp.class);
                                    if ( t.getSeets() != 0)
                                    {

                                        String[] searchTtempHour = time.split(":");
                                        Integer searchHour_ = Integer.valueOf(searchTtempHour[0]);

                                        Integer searchFollowHour;
                                        Integer searchBeforeHour;
                                        if (searchHour_!=0)
                                        {
                                            if (searchHour_ == 23)
                                            {
                                                searchFollowHour = 0;
                                                searchBeforeHour =searchHour_-1;
                                            }
                                            else
                                            {
                                                searchFollowHour =searchHour_+1;
                                                searchBeforeHour =searchHour_-1;
                                            }

                                        }
                                        else
                                        {
                                            searchBeforeHour = 23;
                                            searchFollowHour =searchHour_+1;
                                        }


                                        String currentTrempdate = t.getTrempDateTime();

                                        String[] split = currentTrempdate.split(" ");
                                        String currentDate  = split[0];
                                        String currentTime = split[1];

                                        String[] currentTtempHour = currentTime.split(":");
                                        Integer currentHour_ = Integer.valueOf(currentTtempHour[0]);

                                        if (currentDate.equals(searchDateTime) && (currentHour_ == searchHour_ || searchFollowHour == currentHour_ ||searchBeforeHour == currentHour_ ))
                                        {
                                            tremps.add(t);
                                        }
                                    }

                                    break;
                                }
                            }
                            break;
                        }
                    }
                }

                listener.onComplete(tremps);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onComplete(null);

            }
        });
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
        StorageReference httpsReference = storage.getReferenceFromUrl(url);

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
