package com.cad.halstart;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends Activity {

    private Handler mHandler;
    private PrintConnect mPrintConnect;
    public ScanConnect mScanConnect;
    private TextView txtBarcode;

    public MyFileObserver fileWatcher = new MyFileObserver("/sdcard/ScanPrintApi/") {
        @Override
        public void onEvent(int event, String path) {
            if ((FileObserver.CREATE & event)!=0) {
                Log.i("FileObserver.CREATE", "Rover");

                if (path.equals("Scan.txt")) {
                    Scan();
                    DeleteScanFile();
                }

                if (path.equals("pls_print.txt")) {
                    PrintFile();
                }
            }
        }
    };

    class myHandler extends Handler {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (!TextUtils.isEmpty(msg.obj.toString())) {
                String str = msg.obj.toString();
                MainActivity.this.txtBarcode.setText(str);
                CreateScanResultFile(str);
            }
        }
    }

    public void Scan() {
        this.mScanConnect.scan();
    }

    public MainActivity() {
        this.mHandler = new myHandler();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);

        this.mScanConnect = new ScanConnect(this, this.mHandler);
        this.mPrintConnect = new PrintConnect(this);
        //this.fileWatcher = new MyFileObserver("/sdcard/");
        this.txtBarcode = (TextView) findViewById(R.id.txtBarcode);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case 223:
                this.mScanConnect.scan();
                break;
            case 224:
                this.mScanConnect.scan();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mScanConnect.stop();
        this.mPrintConnect.stop();
    }

    public void scanOnClick(View v) {
        this.mScanConnect.scan();
    }

    public void printOnClick(View v) {
        String barcodeStr = this.txtBarcode.getText().toString();
        this.mPrintConnect.send(barcodeStr);
    }

    public void startFileWatcher(View v) {
        this.fileWatcher.startWatching();
    }

    public void stopFileWatcher(View v) {
        this.fileWatcher.stopWatching();
    }

    public void onClickCreateScanFile(View v) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.e("SDCARD ", "No SD Card present");
        } else {
            try {
                File newFolder = new File(Environment.getExternalStorageDirectory(), "ScanPrintApi");
                if (!newFolder.exists()) {
                    newFolder.mkdir();
                }
                try {
                    File file = new File(newFolder, "Scan" + ".txt");
                    file.createNewFile();
                } catch (Exception ex) {
                    System.out.println("ex: " + ex);
                }
            } catch (Exception e) {
                System.out.println("e: " + e);
            }
        }
    }

    public void onClickDeleteScanFile(View v) {
        DeleteScanFile();
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

    public void DeleteScanFile() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.e("SDCARD ", "No SD Card present");
        } else {
            try {
                File newFolder = new File(Environment.getExternalStorageDirectory(), "ScanPrintApi");
                try {
                    File file = new File(newFolder, "Scan" + ".txt");

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
