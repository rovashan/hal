package com.cad.halstart;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private Handler mHandler;
    private PrintConnect mPrintConnect;
    private ScanConnect mScanConnect;
    private TextView txtBarcode;

    class myHandler extends Handler {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (!TextUtils.isEmpty(msg.obj.toString())) {
                String str = msg.obj.toString();
                MainActivity.this.txtBarcode.setText(str);
            }
        }
    }

    public MainActivity() {
        this.mHandler = new myHandler();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        this.mScanConnect = new ScanConnect(this, this.mHandler);
        this.mPrintConnect = new PrintConnect(this);
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
}
