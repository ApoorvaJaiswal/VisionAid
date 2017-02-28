package com.example.apoorvajaiswal.visionaid;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Apoorva Jaiswal on 2/25/2017.
 */

public class Blue extends Service {
    int flag=1;


    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    Log l;

    public Blue() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.e("hi","works in onstart");
        findBT();
        if (flag == 1)//set flag to 1 if gesture detected
        {
            try {
                openBT();
            } catch (Exception e) {

            }
            if(startId == Service.START_STICKY) {
                 return super.onStartCommand(intent, flags, startId);
            }else{
                return  Service.START_NOT_STICKY;
            }
        }
        if(startId == Service.START_STICKY) {
            return super.onStartCommand(intent, flags, startId);
        }else{
            return  Service.START_NOT_STICKY;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

Log.e("hi","works in Onbind");
        findBT();
        if (flag == 1)//set flag to 1 if gesture detected
        {
            try {
                openBT();
            } catch (Exception e) {

            }
            return null;
        }
        return null;
    }

    void findBT()
    {
        Log.e("hi","works in findBT");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            Toast.makeText(Blue.this,"Bluetooth not there",Toast.LENGTH_LONG).show();
        }

        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startService(enableBluetooth);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("HC-05"))
                {
                    mmDevice = device;
                    break;
                }
            }
        }
       // myLabel.setText("Bluetooth Device Found");
        Toast.makeText(Blue.this,"Bluetooth Device Found",Toast.LENGTH_SHORT).show();
    }

    void openBT() throws IOException
    {
        Log.e("Hi","open");
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();

        beginListenForData();

        Log.e("Hi","openBt");
        //myLabel.setText("Bluetooth Opened");
    }

    void beginListenForData()
    {
        Log.e("begin","beginlisten");
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    //
                    // Log.e("hgsdf","while");
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        Log.e("bytes",bytesAvailable+"");
                        if(bytesAvailable > 0)
                        {
                            Log.e("greater","hgk");
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            Log.e("packet",packetBytes+"");
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            //myLabel.setText(data);
                                            Toast.makeText(Blue.this,data,Toast.LENGTH_LONG).show();
                                            Log.e("values",data);
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

   /* void sendData() throws IOException
    {
        String msg = myTextbox.getText().toString();
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        myLabel.setText("Data Sent");
    }

    void closeBT() throws IOException
    {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        myLabel.setText("Bluetooth Closed");
    }*/

}
