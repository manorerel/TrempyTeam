package com.example.hadar.trempyteam.Model;

import java.util.List;

/**
 * Created by מנור on 25/03/2017.
 */

public class User {
    String Id;
    String FirstName;
    String LastName;

    List<String> JoinToTremps;
    List<String> SuggestedTremps;
    static User AppUser;

    public User(){

    }

    public User(String id, String firstName, String lastName, String facebookId)
    {
        Id = id;
        FirstName = firstName;
        LastName = lastName;
    }

    public User(String id){
        Id = id;
    }

    public String getId(){return Id;}
    public String getFirstName(){return FirstName;}
    public String getLastName(){return LastName;}

    public void setFirstName(String name){FirstName = name;}
    public void setLastName(String name){LastName = name;}

    public static User GetAppUser(){return AppUser;}
    public static void CreateAppUser(String id){AppUser = new User(id);}

}
