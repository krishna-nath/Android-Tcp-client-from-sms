package com.example.krishnanathv.complete_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;

import java.util.Calendar;

/**
 * Created by Krishna Nath V on 25-Mar-16.
 */
public class intent extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        String s = getIntent().getExtras().getString("val");
        int nosub = 0, noloc = 0, nodate = 0, notime = 0;
        String subject,location,date,time;

        location = s.substring(0, s.indexOf("#"));
        System.out.println(location);
        if (location.contentEquals("-"))
            noloc = 1;


        subject = s.substring(s.indexOf("#") + 1, s.indexOf("+"));
        if (subject.contentEquals("-"))
            nosub = 1;


        date = s.substring(s.indexOf("+") + 1, s.indexOf("*"));
        if (date.contentEquals("-"))
            nodate = 1;

        time = s.substring(s.indexOf("*") + 1, s.indexOf("/"));
        if (time.contentEquals("-"))
            notime = 1;


        int yr = 0, day = 0, month = 0, min = 0, hr = 12, evening = 1;
        String[] out;
        if (nodate == 0) {
            if (nodate == 0) {
                out = date.split("-");
                day = Integer.parseInt(out[0]);
                month = Integer.parseInt(out[1]);
                yr = Integer.parseInt(out[2]);
                System.out.println(month);
            }
        }
        String tm;
        if (notime == 0) {
            if (time.contains("am") || time.contains("AM"))
                evening = 0;

            tm = time.replaceAll("(am|pm|AM|PM)", "");

            out = tm.split(":");
            hr = Integer.parseInt(out[0]);
            min = Integer.parseInt(out[1]);
        }

        Calendar cal;
        cal= Calendar.getInstance();
        if(nodate==0) {
           if(month<=12&&day<=31) {
               cal.set(yr, month - 1, day, hr, min);
               cal.set(Calendar.AM_PM, evening);
               double startTime = cal.getTimeInMillis();
           }

        }






        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
         if(nodate==0) {
             if(month<=12&&day<=31)
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());

            }
             intent.putExtra("allDay", true);
            intent.putExtra("rrule", "FREQ=YEARLY");
        if(nosub==0)
            intent.putExtra("title", subject);
        if(noloc==0)
             intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location);
        startActivity(intent);





    }
}
