package com.example.hadar.trempyteam.Model;

import android.net.NetworkInfo;

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
    int Seets;
    Date TrempDate;
    Date CreationDate;
    List<String> TrempistsList;
    static int count = 0;
    String imageName;

    public Tremp(){}
    public Tremp(int seets, String driverId, Date trempDate, String sourceAdd, String destAdd, String carModel, String ImageName){
        CarModel = carModel;
        Seets = seets;
        DriverId = driverId;
        TrempDate = trempDate;
        CreationDate = new Date(2017,03,25);
        Id = CreateID();
        SourceAddress = sourceAdd;
        DestAddress = destAdd;
        imageName = ImageName;
    }

    public String getId(){return Id;}
    public String getDriverId(){return DriverId;}
    public int getSeets(){return Seets;}
    public String getCarModel(){return CarModel;}
    public String getSourceAddress(){return SourceAddress;}
    public String getDestAddress(){return DestAddress;}
    public Date getTrempDate(){return TrempDate;}
    public Date getCreationDate(){return CreationDate;}
    public String getImageName() {
        return imageName;
    }

    public void setCarModel(String carModel){CarModel = carModel;}
    public void setSourceAddress(String sourceAdd){SourceAddress = sourceAdd;}
    public void setDestAddress(String destAdd){DestAddress = destAdd;}
    public void setSeets(int seets){Seets = seets;}
    public void setTrempDate(Date trempDate){TrempDate = trempDate;}
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    private String CreateID(){

        String id = getDriverId()+ count++;
        return id;
    }
}
