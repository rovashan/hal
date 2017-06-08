package com.cad.halstart;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.obm.mylibrary.Open;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.regex.Pattern;

import android_serialport_api.SerialPort;

public class ScanConnect {
    public int RECV_SCAN;
    private SerialPort comSerialport;
    private Handler handler;
    private boolean isScan;
    public InputStream is;
    private Open open;
    public OutputStream os;
    byte[] responseData;

    class InitSerialPort implements Runnable {

        public void run() {
            try {
                ScanConnect.this.comSerialport = new SerialPort(new File("/dev/ttyHSL2"), 9600, 8, "None", 1, "None");
                ScanConnect.this.is = ScanConnect.this.comSerialport.getInputStream();
                ScanConnect.this.os = ScanConnect.this.comSerialport.getOutputStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ScanThread extends Thread {

        public void run() {
            if (ScanConnect.this.isScan) {
                ScanConnect.this.isScan = false;
                ScanConnect.this.readscanpdata();
            }
        }
    }

    public ScanConnect(Context context, Handler handler) {
        this.open = new Open();
        this.isScan = true;
        this.RECV_SCAN = 11;
        this.responseData = new byte[1025];
        this.handler = handler;
        this.open.openScan();
        test();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private void test() {
        new Thread(new InitSerialPort()).start();
    }

    public void scan() {
        new ScanThread().start();
    }

    private void readscanpdata() {
        String code = BuildConfig.FLAVOR;
        Arrays.fill(this.responseData, (byte) 0);
        //FlushUartBuffer();
        flush();
        this.open.startScan();
        int readcount = read();
        if (readcount > 0) {
            code = new String(this.responseData, 0, readcount);
        }
        if (!TextUtils.isEmpty(code)) {
            code = replaceBlank(code);
            Message msg = new Message();
            msg.what = this.RECV_SCAN;
            msg.obj = code;
            this.handler.sendMessage(msg);
        }
        this.isScan = true;
        this.open.stopScan();
    }

    private void FlushUartBuffer() {
        try {
            this.is.read(new byte[1024]);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private int read() {
        int available = 0;
        int index = 0;
        while (index < 11) {
            try {
                Thread.sleep(100);
                available = this.is.available();
                if (available > 0) {
                    break;
                }
                index++;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        if (available > 0) {
            try {
                available = this.is.read(this.responseData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return available;
    }

    private int flush() {
        int available = 0;
        int index = 0;
        while (index < 11) {
            try {
                Thread.sleep(100);
                available = this.is.available();
                if (available > 0) {
                    break;
                }
                index++;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        if (available > 0) {
            try {
                available = this.is.read(new byte[1024]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return available;
    }

    private String replaceBlank(String str) {
        String dest = BuildConfig.FLAVOR;
        if (str != null) {
            return Pattern.compile("\\s*|\t|\r|\n").matcher(str).replaceAll(BuildConfig.FLAVOR);
        }
        return dest;
    }

    public void stop() {
        try {
            this.open.closeScan();
            if (this.comSerialport != null) {
                this.is.close();
                this.os.close();
                this.comSerialport.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
