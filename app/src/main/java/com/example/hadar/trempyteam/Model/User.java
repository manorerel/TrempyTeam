package com.example.hadar.trempyteam.Model;

import android.graphics.Paint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by מנור on 25/03/2017.
 */

public class User {
    public String Id;
    public String FirstName;
    public String LastName;

    List<String> JoinToTremps;
    List<String> SuggestedTremps;
    static User AppUser;

    public User(){

    }

    public User(String id, String firstName, String lastName)
    {
        Id = id;
        FirstName = firstName;
        LastName = lastName;
    }

    public User(String id)
    {
        Id = id;
        JoinToTremps = new ArrayList<String>();
    }

    public void addTrempToJoinList(String trempId){
        if(!JoinToTremps.contains(trempId))
            JoinToTremps.add(trempId);
    }

    public void removeTremp(String trempId){
        int index = 0;
        boolean found = false;
        if(JoinToTremps != null) {
            for (int i = 0; i < JoinToTremps.size(); i++) {
                if (JoinToTremps.get(i).equals(trempId))
                {
                    index = i;
                    found = true;
                    break;
                }
            }

            if(found)
            {
                JoinToTremps.remove(index);
            }
        }
    }

    public boolean isTrempContains(String trempId)
    {
        return JoinToTremps.contains(trempId);
    }

    public String getUserId(){return Id;}
    public String getFirst(){return FirstName;}
    public String getLast(){return LastName;}

    public void setFirst(String name){FirstName = name;}
    public void setlast(String name){LastName = name;}

    public static User GetAppUser(){return AppUser;}
    public static void CreateAppUser(String id){AppUser = new User(id);}

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("Id", Id);
        result.put("FirstName", FirstName);
        result.put("LastName", LastName);
        return result;
    }

}
