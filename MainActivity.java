package com.example.krishnanathv.complete_project;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends Service  {

    private TCPClient mTcpClient;
    String location = "*", date = "*", time = "*", subject = "*";
    static int flag=0,connected=0;
    Context appcontext;
    String message;
    Thread mythread;
    Handler handler;


    private static final String ACTION="android.provider.Telephony.SMS_RECEIVED";
    private BroadcastReceiver yourReceiver;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(ACTION);
        theFilter.setPriority(1000);
        System.out.println("activated service");
        this.yourReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // Do whatever you need it to do when it receives the broadcast
                // Example show a Toast message...
                System.out.println("got the msg nice!!");
                final Bundle bundle = intent.getExtras();
                appcontext = context;

                try {

                    if (bundle != null) {

                        final Object[] pdusObj = (Object[]) bundle.get("pdus");

                        for (int i = 0; i < pdusObj.length; i++) {

                            SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                            String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                            String senderNum = phoneNumber;
                            message = currentMessage.getDisplayMessageBody();

                            Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);


                        } // end for loop


                       // new connectTask().execute("");
                        mythread=new Thread(new connectTask());
                        mythread.start();


                        flag=0;

                        while (flag == 0);
                        if (mTcpClient != null) {
                            System.out.println("passed message1 " + message);
                            mTcpClient.sendMessage(message);


                        }
                        System.out.println("passed message2 " + message);
             /*   Intent in = new Intent(appcontext, intent.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                appcontext.startActivity(in);*/


                    }


                    // bundle is null

                } catch (Exception e) {
                    Log.e("SmsReceiver", "Exception smsReceiver" + e);


                }
            }


        };
        // Registers the receiver so that your service will listen for
        // broadcasts
        this.registerReceiver(this.yourReceiver, theFilter);
        System.out.println("registered broadcast");

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String s = (String) msg.obj;
                System.out.println(s);

                System.out.println(s);
                Intent in = new Intent(appcontext, intent.class);
                in.putExtra("val",s);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                appcontext.startActivity(in);


            }


        };

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Do not forget to unregister the receiver!!!
        this.unregisterReceiver(this.yourReceiver);
    }

    private void showSuccessfulBroadcast() {
        System.out.println("got the msg nice!!");
    }




    public class connectTask implements Runnable {
        Message ms = Message.obtain();

        @Override
        public void run() {

            System.out.println("work inside async task");

            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    ms.obj=message;
                    handler.sendMessage(ms);




                }
            });
            mTcpClient.run();


        }



      /*  @Override
        protected TCPClient doInBackground(String... message) {

            Log.i("work","inside async task");

            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);




                }
            });
            mTcpClient.run();
            flag=1;

            return null;
        }*/

     /*  @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
           String s=values[0];
           System.out.println(s);
              Intent in = new Intent(appcontext, intent.class);
                in.putExtra("val",s);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                appcontext.startActivity(in);

     /*       //in the arrayList we add the messaged received from server
            String s = values[0];
            System.out.println(s);
            int nosub = 0, noloc = 0, nodate = 0, notime = 0;


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
           /* System.out.println(location);
            System.out.println(subject);
            System.out.println(date);
            System.out.println(time);
            System.out.println(hr);
            System.out.println(min);


            Calendar cal;
            cal= Calendar.getInstance();
            if(nodate==0) {

                cal.set(yr, month, day, hr, min);
                cal.set(Calendar.AM_PM, evening);
                double startTime = cal.getTimeInMillis();
            }
                 */


            // in.setType("vnd.android.cursor.item/event");
            // if(nodate==0) {
            //   in.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());
            //}
            // in.putExtra("allDay", true);
            //in.putExtra("rrule", "FREQ=YEARLY");
            // if(nosub==0)
            //in.putExtra("title", "hello");
            //  if(noloc==0)
            // in.putExtra(CalendarContract.Events.EVENT_LOCATION, location);

            //in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            //Toast.makeText(getApplicationContext(),location,Toast.LENGTH_LONG).show();

            // arrayList.add(values[0]);
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            // mAdapter.notifyDataSetChanged();
        }


    }



