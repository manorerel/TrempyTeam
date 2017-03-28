package com.example.hadar.trempyteam.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by מנור on 25/03/2017.
 */

public class ModelFirebase {

    public void addTremp(Tremp tremp){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Trempy").child(tremp.getId());
        myRef.setValue(tremp);
    }


    public void addUser(User user)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User").child(user.getId());
        myRef.setValue(user);
    }

}
