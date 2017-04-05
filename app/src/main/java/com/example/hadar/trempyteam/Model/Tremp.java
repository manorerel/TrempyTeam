package com.example.hadar.trempyteam.Model;

import com.google.firebase.database.ServerValue;

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
    String destAddress;
    String id;
    String driverId;
    long seets;
    Date trempDate;
    Date creationTime;
    List<String> trempistsList;
    static int count = 0;
    String imageName;
    String phoneNumber;

    public Tremp(){}
    public Tremp(String trempId, long seets, String DriverId, Date trempDate, String sourceAdd, String destAdd,String PhoneNumber, String carModel, String ImageName, List<String> passengers){
        this.carModel = carModel;
        this.seets = seets;
        driverId = DriverId;
        this.trempDate = trempDate;
        id = trempId;
        sourceAddress = sourceAdd;
        destAddress = destAdd;
        imageName = ImageName;
        phoneNumber = PhoneNumber;

        if(passengers != null)
            trempistsList = passengers;
        else trempistsList = new ArrayList<String>();

    }

    public Tremp(long seets, String DriverId, Date trempDate, String sourceAdd, String destAdd,String PhoneNumber, String carModel, String ImageName, List<String> passengers) {
        this.carModel = carModel;
        this.seets = seets;
        driverId = DriverId;
        this.trempDate = trempDate;
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
    public Date getTrempDate(){
        return trempDate;}
    public Date getCreationTime(){return creationTime;}
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
    public void settrempDateTime(Date TrempDateTimee){
        trempDate = TrempDateTimee;}
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    public void setPhoneNumber(String phoneNumber){this.phoneNumber = phoneNumber;}
    public void setNewPassengerToTremp(String user_id) {
        if(trempistsList != null && !trempistsList.contains(user_id))
            this.trempistsList.add(user_id);
    }

    private String CreateID(){
        String id =driverId + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + count++;
        return id;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("sourceAddress", sourceAddress);
        result.put("destAddress", destAddress);
        result.put("carModel", carModel);
        result.put("seets", seets);
        result.put("creationTime", ServerValue.TIMESTAMP);
        String trempDateTime = convertDateToString(trempDate);
        result.put("trempDateTime", trempDateTime);
        result.put("driverId", driverId);
        result.put("phoneNumber", phoneNumber);
        result.put("driverId",driverId);
        result.put("imageName", imageName);
        result.put("Passengers", trempistsList);
        return result;
    }

    private static String convertDateToString(Date date){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateText = df.format(date);

        return dateText;
    }
}
