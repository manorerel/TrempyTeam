package com.example.hadar.trempyteam;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Hadar on 17-Dec-16.
 */

public class TimeEditText extends EditText {
    int hour;
    int minute;
    int second;

    public TimeEditText(int y, int m, int d) {
        super(null);
        this.init();
    }

    private void init() {
        Calendar calendar = new GregorianCalendar();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);

        // to check why the hour not true
        setText("" + (hour) + ":" + minute + ":" + second);
    }

    public interface OnTimeSetListener {
        public void TimeSet(int hour, int minutes);
    }

    OnTimeSetListener listener;

    public void setOnTimeSetListener(OnTimeSetListener listener) {
        this.listener = listener;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public TimeEditText(Context context) {
        super(context);
        init();
    }

    public TimeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && isEnabled()) {
            InnerTimePicker timePicker = new InnerTimePicker();
            timePicker.init(hour, minute, second);
            timePicker.setListener(new InnerTimePicker.Listener() {
                @Override
                public void done(int hour, int minute) {
                    TimeEditText.this.hour = hour;
                    TimeEditText.this.minute = minute;

                    setText("" + hour + ":" + minute + ":" + second);
                    if (listener != null) {
                        listener.TimeSet(hour, minute);
                    }
                }
            });
            timePicker.show(((Activity) getContext()).getFragmentManager(), "GGG");
        }
        return super.onTouchEvent(event);
    }

    public static class InnerTimePicker extends DialogFragment {
        int hour;
        int minute;
        int second;

        public void init(int year, int month, int day) {
            this.hour = year;
            this.minute = month;
            this.second = day;
        }

        interface Listener {
            public void done(int hour, int minute);
        }

        Listener listener;

        public void setListener(Listener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            TimePickerDialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    listener.done(hourOfDay, minute);
                }
            }, hour, minute, true);

            return dialog;
        }
    }
}
