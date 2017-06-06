package com.example.hadar.trempyteam.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ServerValue;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by מנור on 25/03/2017.
 */

public class Tremp {
    String carModel;
    String sourceAddress;
    LatLng source;
    LatLng dest;
    String destAddress;
    String id;
    String driverId;
    long seets;
    String trempDateTime;
    List<String> trempistsList;
    static int count = 0;
    String imageName;
    String phoneNumber;

    public Tremp(){}
    public Tremp(String trempId, long seets, String DriverId, String trempDate, String sourceAdd, String destAdd,String PhoneNumber, String carModel, String ImageName, List<String> passengers){
        this.carModel = carModel;
        this.seets = seets;
        driverId = DriverId;
        this.trempDateTime = trempDate;
        id = trempId;
        sourceAddress = sourceAdd;
        destAddress = destAdd;
        imageName = ImageName;
        phoneNumber = PhoneNumber;

        if(passengers != null)
            trempistsList = passengers;
        else trempistsList = new ArrayList<String>();

    }

    public Tremp(String trempId, long seets, String DriverId, String trempDate, String sourceAdd, String destAdd,String PhoneNumber, String carModel, String ImageName, List<String> passengers, LatLng source, LatLng dest) {
        this.carModel = carModel;
        this.seets = seets;
        driverId = DriverId;
        this.trempDateTime = trempDate;
        id = trempId;
        sourceAddress = sourceAdd;
        destAddress = destAdd;
        imageName = ImageName;
        phoneNumber = PhoneNumber;
        if(passengers != null)
            trempistsList = passengers;
        else trempistsList = new ArrayList<String>();
        this.source = source;
        this.dest = dest;
    }

    public Tremp(long seets, String DriverId, String trempDate, String sourceAdd, String destAdd,String PhoneNumber, String carModel, String ImageName, List<String> passengers) {
        this.carModel = carModel;
        this.seets = seets;
        driverId = DriverId;
        this.trempDateTime = trempDate;
        id = CreateID();
        sourceAddress = sourceAdd;
        destAddress = destAdd;
        imageName = ImageName;
        phoneNumber = PhoneNumber;
        if(passengers != null)
            trempistsList = passengers;
        else trempistsList = new ArrayList<String>();
    }

    public String getId(){return id;}
    public String getDriverId(){return driverId;}
    public long getSeets(){return seets;}
    public String getCarModel(){return carModel;}
    public String getSourceAddress(){return sourceAddress;}
    public String getDestAddress(){return destAddress;}
    public String getTrempDateTime(){
        return trempDateTime;}
    public String getImageName() {
        return imageName;
    }
    public String getPhoneNumber(){return phoneNumber;}
    public List<String> getTrempistsList(){return trempistsList;}

    public void setCarModel(String carModel){
        this.carModel = carModel;}
    public void setSourceAddress(String sourceAdd){
        sourceAddress = sourceAdd;}
    public void setDestAddress(String destAdd){
        destAddress = destAdd;}
    public void setSeets(long seets){
        this.seets = seets;}
    public void settrempDateTime(String TrempDateTimee){
        trempDateTime = TrempDateTimee;}
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    public void setPhoneNumber(String phoneNumber){this.phoneNumber = phoneNumber;}
    public void setNewPassengerToTremp(String user_id) {
        if(trempistsList != null && !trempistsList.contains(user_id))
            this.trempistsList.add(user_id);
        else if(trempistsList == null){
            trempistsList = new ArrayList<String>();
            trempistsList.add(user_id);
        }
    }

    public void removePassenger(String passengerId){
        int index = 0;
        boolean found = false;
        if(trempistsList != null) {
            for (int i = 0; i < trempistsList.size(); i++) {
                if (trempistsList.get(i).equals(passengerId))
                {
                    index = i;
                    found = true;
                    break;
                }
            }

            if(found)
            {
                trempistsList.remove(index);
            }
        }
    }

    private String CreateID(){
        String id =driverId + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + count++;
        return id;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
//        result.put("id", id);
        result.put("driverId", driverId);
        result.put("phoneNumber", phoneNumber);
        result.put("seets", seets);
        result.put("sourceAddress",convertLocationToString(this.source));
        result.put("destAddress", convertLocationToString(this.dest));
        result.put("carModel", carModel);
        result.put("trempDateTime", trempDateTime);
        result.put("imageName", imageName);
//        result.put("Passengers", trempistsList);
        return result;
    }

    private String convertLocationToString(LatLng location){
        HashMap<String, Object> result = new HashMap<>();
        result.put("long", "34");
        result.put("lat", "31");
        JSONObject json = new JSONObject(result);
        String res = json.toString();

        return res;
    }
}
