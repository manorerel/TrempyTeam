package com.example.hadar.trempyteam.Model;

import java.util.EventObject;

/**
 * Created by manor on 9/10/2017.
 */

public class NotificationEvent extends EventObject {

    private String _text;

    public NotificationEvent(Object source, String text) {
        super(source);
        _text = text;
    }

    public String getText(){
        return _text;
    }
}
