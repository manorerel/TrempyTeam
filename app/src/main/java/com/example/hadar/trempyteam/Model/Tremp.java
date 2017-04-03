package com.example.hadar.trempyteam.Model;

import android.net.NetworkInfo;

import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
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
    String id;
    String driverId;
    long Seets;
    Date trempDateTime;
    Date CreationDate;
    List<String> TrempistsList;
    static int count = 0;
    String imageName;
    String PhoneNumber;

    public Tremp(){}
    public Tremp(String trempId, long seets, String DriverId, Date trempDate, String sourceAdd, String destAdd,String phoneNumber, String carModel, String ImageName, List<String> passengers){
        CarModel = carModel;
        Seets = seets;
        driverId = DriverId;
        trempDateTime = trempDate;
        trempDateTime = trempDate;
        id = trempId;
        SourceAddress = sourceAdd;
        DestAddress = destAdd;
        imageName = ImageName;
        PhoneNumber = phoneNumber;
        TrempistsList = passengers;


    }
    public Tremp(long seets, String DriverId, Date trempDate, String sourceAdd, String destAdd,String phoneNumber, String carModel, String ImageName, List<String> passengers) {
        CarModel = carModel;
        Seets = seets;
        driverId = DriverId;
        trempDateTime = trempDate;
        trempDateTime = trempDate;
        id = CreateID();
        SourceAddress = sourceAdd;
        DestAddress = destAdd;
        imageName = ImageName;
        PhoneNumber = phoneNumber;
        TrempistsList = passengers;
    }

    public String getId(){return id;}
    public String getDriverId(){return driverId;}
    public long getSeets(){return Seets;}
    public String getCarModel(){return CarModel;}
    public String getSourceAddress(){return SourceAddress;}
    public String getDestAddress(){return DestAddress;}
    public Date getTrempDateTime(){return trempDateTime;}
    public Date getCreationDate(){return CreationDate;}
    public String getImageName() {
        return imageName;
    }
    public String getPhoneNumber(){return PhoneNumber;}
    public List<String> getTrempistsList(){return TrempistsList;}

    public void setCarModel(String carModel){CarModel = carModel;}
    public void setSourceAddress(String sourceAdd){SourceAddress = sourceAdd;}
    public void setDestAddress(String destAdd){DestAddress = destAdd;}
    public void setSeets(long seets){Seets = seets;}
    public void settrempDateTime(Date trempDateTime){trempDateTime = trempDateTime;}
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    public void setPhoneNumber(String phoneNumber){this.PhoneNumber = phoneNumber;}
    public void setNewPassengerToTremp(String user_id) {this.TrempistsList.add(user_id);}

    private String CreateID(){
        String id =driverId + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + count++;
        return id;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("SourceAddress", SourceAddress);
        result.put("DestAddress", DestAddress);
        result.put("CarModel", CarModel);
        result.put("seets", Seets);
        result.put("creationTime", ServerValue.TIMESTAMP);
        result.put("trempDateTime", trempDateTime);
        result.put("driverId", driverId);
        result.put("phoneNumber", PhoneNumber);
        result.put("driverId",driverId);
        result.put("imageName", imageName);
        result.put("Passengers", TrempistsList);
        return result;
    }
}
