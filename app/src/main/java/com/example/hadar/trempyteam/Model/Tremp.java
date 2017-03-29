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
    String TrempTime;
    String CreationDate;
    String CreationTime;
    List<String> TrempistsList;
    static int count = 0;
    String imageName;


    public Tremp(){}
    public Tremp(int seets, String driverId, Date trempDate, String sourceAdd, String destAdd, String carModel, String ImageName){
        CarModel = carModel;
        //Seets = long.class.cast(seets);
        Seets = 2;
        DriverId = driverId;
       // TrempDate = trempDate;
       // CreationDate = new Date(2017,03,25);
        TrempDate = trempDate;
        Calendar d = Calendar.getInstance();
        CreationDate = new Date(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH)).toString();
        CreationTime = new Time(d.get(Calendar.HOUR), d.get(Calendar.MINUTE), d.get(Calendar.SECOND)).toString();
        Id = CreateID();
        SourceAddress = sourceAdd;
        DestAddress = destAdd;
        imageName = ImageName;
    }

    public String getId(){return Id;}
    public String getDriverId(){return DriverId;}
    public long getSeets(){return Seets;}
    public String getCarModel(){return CarModel;}
    public String getSourceAddress(){return SourceAddress;}
    public String getDestAddress(){return DestAddress;}
    public Date getTrempDate(){return TrempDate;}
    public String getCreationDate(){return CreationDate;}
    public String getImageName() {
        return imageName;
    }

    public void setCarModel(String carModel){CarModel = carModel;}
    public void setSourceAddress(String sourceAdd){SourceAddress = sourceAdd;}
    public void setDestAddress(String destAdd){DestAddress = destAdd;}
    public void setSeets(long seets){Seets = seets;}
    public void setTrempDate(Date trempDate){TrempDate = trempDate;}
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    private String CreateID(){

        String id = getDriverId()+ count++;
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
        return result;
    }
}
