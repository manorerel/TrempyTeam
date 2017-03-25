package com.example.hadar.trempyteam.Model;

import java.util.List;

/**
 * Created by מנור on 25/03/2017.
 */

public class User {
    String Id;
    String FirstName;
    String LastName;
    String FacebookId;
    List<String> JoinToTremps;
    List<String> SuggestedTremps;

    public User(){

    }

    public User(String id, String firstName, String lastName, String facebookId)
    {
        Id = id;
        FirstName = firstName;
        LastName = lastName;
        FacebookId = facebookId;
    }

    public String getId(){return Id;}
    public String getFirstName(){return FirstName;}
    public String getLastName(){return LastName;}
    public String getFacebookId(){return FacebookId;}

    public void setFirstName(String name){FirstName = name;}
    public void setLastName(String name){LastName = name;}
    public void setFacebookId(String fbId){FacebookId = fbId;}
}
