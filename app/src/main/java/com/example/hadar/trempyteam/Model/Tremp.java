package com.example.hadar.trempyteam.Model;

import android.net.NetworkInfo;

import com.google.firebase.database.ServerValue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.sql.Time;
import java.util.Calendar;
/**
 * Created by מנור on 25/03/2017.
 */

public class Tremp {
    String CarModel;
    String SourceAddress;
    String DestAddress;
    String Id;
    String DriverId;
    long Seets;
    Date TrempDate;
    Date CreationDate;
    List<String> TrempistsList;
    static int count = 0;
    String imageName;
    String PhoneNumber;

    public Tremp(){}
    public Tremp(long seets, String driverId, Date trempDate, String sourceAdd, String destAdd,String phoneNumber, String carModel, String ImageName){
        CarModel = carModel;
        Seets = seets;
        DriverId = driverId;
       TrempDate = trempDate;
       // CreationDate = new Date(2017,03,25);
        TrempDate = trempDate;
        Id = CreateID();
        SourceAddress = sourceAdd;
        DestAddress = destAdd;
        imageName = ImageName;
        PhoneNumber = phoneNumber;
    }

    public String getId(){return Id;}
    public String getDriverId(){return DriverId;}
    public long getSeets(){return Seets;}
    public String getCarModel(){return CarModel;}
    public String getSourceAddress(){return SourceAddress;}
    public String getDestAddress(){return DestAddress;}
    public Date getTrempDate(){return TrempDate;}
    public Date getCreationDate(){return CreationDate;}
    public String getImageName() {
        return imageName;
    }
    public String getPhoneNumber(){return PhoneNumber;}

    public void setCarModel(String carModel){CarModel = carModel;}
    public void setSourceAddress(String sourceAdd){SourceAddress = sourceAdd;}
    public void setDestAddress(String destAdd){DestAddress = destAdd;}
    public void setSeets(long seets){Seets = seets;}
    public void setTrempDate(Date trempDate){TrempDate = trempDate;}
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    public void setPhoneNumber(String phoneNumber){this.PhoneNumber = phoneNumber;}

    private String CreateID(){
        String id =ServerValue.TIMESTAMP.toString()+ getDriverId()+ count++;
        return id;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", Id);
        result.put("SourceAddress", SourceAddress);
        result.put("DestAddress", DestAddress);
        result.put("CarModel", CarModel);
        result.put("seets", Seets);
        result.put("creationTime", ServerValue.TIMESTAMP);
        result.put("trempDateTime", TrempDate);
        result.put("driverId", DriverId);
        result.put("phoneNumber", PhoneNumber);
        return result;
    }
}
