package com.example.hadar.trempyteam.Model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by manor on 6/2/2017.
 */

public class Utils {

    public static Tremp currentChosenTremp;

    public static LatLng getLocationFromAddress(Context context, String strAddress)
    {

        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;
    }

    public static String getAddressFromLocation(Context context, LatLng location){
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        String strAddress = "";

        try
        {
            address = coder.getFromLocation(location.latitude, location.longitude, 1);;
            if(address!=null && address.size() > 0)
            {
                strAddress = address.get(0).getAddressLine(0) + ", " + address.get(0).getAddressLine(1) + ", " + address.get(0).getAddressLine(2);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return strAddress;
    }
}
