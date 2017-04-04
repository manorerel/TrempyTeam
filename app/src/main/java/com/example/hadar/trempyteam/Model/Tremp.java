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
    String CarModel;
    String SourceAddress;
    String DestAddress;
    String id;
    String driverId;
    long seets;
    Date TrempDateTime;
    Date CreationTime;
    List<String> trempistsList;
    static int count = 0;
    String imageName;
    String phoneNumber;

    public Tremp(){}
    public Tremp(String trempId, long seets, String DriverId, Date trempDate, String sourceAdd, String destAdd,String PhoneNumber, String carModel, String ImageName, List<String> passengers){
        CarModel = carModel;
        this.seets = seets;
        driverId = DriverId;
        TrempDateTime = trempDate;
        id = trempId;
        SourceAddress = sourceAdd;
        DestAddress = destAdd;
        imageName = ImageName;
        phoneNumber = PhoneNumber;

        if(passengers != null)
            trempistsList = passengers;
        else trempistsList = new ArrayList<String>();

    }

    public Tremp(long seets, String DriverId, Date trempDate, String sourceAdd, String destAdd,String PhoneNumber, String carModel, String ImageName, List<String> passengers) {
        CarModel = carModel;
        this.seets = seets;
        driverId = DriverId;
        TrempDateTime  = trempDate;
        id = CreateID();
        SourceAddress = sourceAdd;
        DestAddress = destAdd;
        imageName = ImageName;
        phoneNumber = PhoneNumber;
        if(passengers != null)
            trempistsList = passengers;
        else trempistsList = new ArrayList<String>();
    }

    public String getId(){return id;}
    public String getDriverId(){return driverId;}
    public long getSeets(){return seets;}
    public String getcarModel(){return CarModel;}
    public String getSourceAddress(){return SourceAddress;}
    public String getDestAddress(){return DestAddress;}
    public Date getTrempDateTime(){return TrempDateTime;}
    public Date getCreationTime(){return CreationTime;}
    public String getImageName() {
        return imageName;
    }
    public String getPhoneNumber(){return phoneNumber;}
    public List<String> getTrempistsList(){return trempistsList;}

    public void setCar(String carModel){CarModel = carModel;}
    public void setSourceAddress(String sourceAdd){SourceAddress = sourceAdd;}
    public void setDestAddress(String destAdd){DestAddress = destAdd;}
    public void setSeets(long seets){
        this.seets = seets;}
    public void settrempDateTime(Date TrempDateTime){TrempDateTime = TrempDateTime;}
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
        result.put("SourceAddress", SourceAddress);
        result.put("DestAddress", DestAddress);
        result.put("CarModel", CarModel);
        result.put("seets", seets);
        result.put("creationTime", ServerValue.TIMESTAMP);
        String NewDate = convertDateToString(TrempDateTime);
        result.put("trempDateTime", NewDate);
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
