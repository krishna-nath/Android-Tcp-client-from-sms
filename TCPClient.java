package com.example.krishnanathv.complete_project;

import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient  {

    private String serverMessage;
    Socket socket;
    public static final String SERVERIP = "192.168.43.90"; //your computer IP address
    public static final int SERVERPORT = 5007;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;

    PrintWriter out;
    BufferedReader in;

    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message){
        System.out.println(message);
        System.out.println(out);
        if (out != null && !out.checkError()) {
            out.println(message);
            System.out.println("inside out");
            out.flush();
        }
    }

    public void stopClient(){
        mRun = false;
    }

    public void run() {

        mRun = true;


            //here you must put your computer's IP address.
        InetAddress serverAddr = null;
        try {
            serverAddr = InetAddress.getByName(SERVERIP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server


        try {
            socket = new Socket(serverAddr, SERVERPORT);
        } catch (IOException e1) {
           System.out.println("socket cant be created");
        }

        System.out.println(socket);





            try {

                //send the message to the server
                do {


                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                }while(out==null);
                MainActivity.flag=1;
                System.out.println(out);

                Log.e("TCP Client", "C: Sent.");

                Log.e("TCP Client", "C: Done.");

                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    serverMessage = in.readLine();

                    if (serverMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;

                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
                //  System.out.println(serverMessage);
                socket.close();

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                System.out.println("socket closed not further data");
               // socket.close();

            }



    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}