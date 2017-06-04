package com.cad.halstart;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

public class FileWatcherService extends Service {

    private Handler mHandler;
    private PrintConnect mPrintConnect;
    public ScanConnect mScanConnect;

    public MyFileObserver fileWatcher = new MyFileObserver("/sdcard/ScanPrintApi/") {
        @Override
        public void onEvent(int event, String path) {
            if ((FileObserver.CREATE & event)!=0) {
                Log.i("FileObserver.CREATE", "Rover");

                if (path.equals("Scan.txt")) {
                    Scan();
                    DeleteFile("Scan.txt");
                }

                if (path.equals("pls_print.txt")) {
                    PrintFile();
                    DeleteFile("pls_print.txt");
                }
            }
        }
    };

    class myHandler extends Handler {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (!TextUtils.isEmpty(msg.obj.toString())) {
                String str = msg.obj.toString();
                CreateScanResultFile(str);
            }
        }
    }

    @Override
    public void onCreate() {
        this.mHandler = new myHandler();
        this.mScanConnect = new ScanConnect(this, this.mHandler);
        this.mPrintConnect = new PrintConnect(this);
    }

    @Override
    public void onStart(Intent intent, int startid) {
        fileWatcher.startWatching();
    }
    @Override
    public void onDestroy() {
        fileWatcher.stopWatching();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void Scan() {
        this.mScanConnect.scan();
    }

    public void DeleteFile(String fileName) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.e("SDCARD ", "No SD Card present");
        } else {
            try {
                File newFolder = new File(Environment.getExternalStorageDirectory(), "ScanPrintApi");
                try {
                    File file = new File(newFolder, fileName);

                    if (file.exists()) {
                        file.delete();
                    }
                } catch (Exception ex) {
                    System.out.println("ex: " + ex);
                }
            } catch (Exception e) {
                System.out.println("e: " + e);
            }
        }
    }

    public void PrintFile() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.e("SDCARD ", "No SD Card present");
        } else {
            try {
                File newFolder = new File(Environment.getExternalStorageDirectory(), "ScanPrintApi");
                if (!newFolder.exists()) {
                    newFolder.mkdir();
                }
                try {
                    File file = new File(newFolder, "pls_print" + ".txt");
                    byte[] printContent = ReadBytesFromFile(file);
                    String str = new String(printContent, "UTF-8");

                    this.mPrintConnect.send(str);
                } catch (Exception ex) {
                    System.out.println("ex: " + ex);
                }
            } catch (Exception e) {
                System.out.println("e: " + e);
            }
        }
    }

    public void CreateScanResultFile(String scanStr) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.e("SDCARD ", "No SD Card present");
        } else {
            try {
                File newFolder = new File(Environment.getExternalStorageDirectory(), "ScanPrintApi");
                if (!newFolder.exists()) {
                    newFolder.mkdir();
                }
                try {
                    File file = new File(newFolder, "ScanResult" + ".txt");
                    file.createNewFile();

                    FileOutputStream fOut = new FileOutputStream(file);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    myOutWriter.append(scanStr);

                    myOutWriter.close();

                } catch (Exception ex) {
                    System.out.println("ex: " + ex);
                }
            } catch (Exception e) {
                System.out.println("e: " + e);
            }
        }
    }

    public static byte[] ReadBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            throw new IOException("Could not completely read file " + file.getName() + " as it is too long (" + length + " bytes, max supported " + Integer.MAX_VALUE + ")");
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

}
