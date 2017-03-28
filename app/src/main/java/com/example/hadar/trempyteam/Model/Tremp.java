package com.example.hadar.trempyteam.Model;

import android.net.NetworkInfo;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
    String TrempDate;
    String TrempTime;
    String CreationDate;
    String CreationTime;
    List<String> TrempistsList;
    static int count = 0;

    public Tremp(){}
    public Tremp(int seets, String driverId, Date trempDate, String sourceAdd, String destAdd, String carModel){
        CarModel = carModel;
        //Seets = long.class.cast(seets);
        Seets = 2;
        DriverId = driverId;
        TrempDate = trempDate.toString();
        Calendar d = Calendar.getInstance();
        CreationDate = new Date(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH)).toString();
        CreationTime = new Time(d.get(Calendar.HOUR), d.get(Calendar.MINUTE), d.get(Calendar.SECOND)).toString();

        Id = CreateID();
        SourceAddress = sourceAdd;
        DestAddress = destAdd;
    }

    public String getId(){return Id;}
    public String getDriverId(){return DriverId;}
    public Long getSeets(){return Seets;}
    public String getCarModel(){return CarModel;}
    public String getSourceAddress(){return SourceAddress;}
    public String getDestAddress(){return DestAddress;}
    public String getTrempDate(){return TrempDate;}
    public String getCreationDate(){return CreationDate;}

    public void setCarModel(String carModel){CarModel = carModel;}
    public void setSourceAddress(String sourceAdd){SourceAddress = sourceAdd;}
    public void setDestAddress(String destAdd){DestAddress = destAdd;}
    public void setSeets(long seets){Seets = seets;}
    public void setTrempDate(String trempDate){TrempDate = trempDate;}

    private String CreateID(){

        String id = getDriverId()+ count++;
        return id;
    }
}
