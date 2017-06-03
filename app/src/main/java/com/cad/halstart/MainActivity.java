package com.cad.halstart;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
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

public class MainActivity extends Activity {

    private Handler mHandler;
    private PrintConnect mPrintConnect;
    public ScanConnect mScanConnect;
    private TextView txtBarcode;

    public MyFileObserver fileWatcher = new MyFileObserver("/sdcard/ScanPrintApi/") {
        @Override
        public void onEvent(int event, String path) {
            Log.i("FileObserver.CREATE", "Rover");

            if (path.equals("Scan.txt")) {
                Scan();
                DeleteScanFile();
            }
        }
    };

    class myHandler extends Handler {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (!TextUtils.isEmpty(msg.obj.toString())) {
                String str = msg.obj.toString();
                MainActivity.this.txtBarcode.setText(str);
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

}
