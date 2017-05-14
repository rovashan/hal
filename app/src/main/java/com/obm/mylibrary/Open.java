package com.obm.mylibrary;

import java.util.Timer;
import java.util.TimerTask;

//import com.oubama.scandemo;

public class Open {

    private static final int LEVEL_HIGH = 1;
    private static final int LEVEL_LOW = 0;

    class nativeSetGPIO_8_Low_Timer extends TimerTask {

        public void run() {
            Open.this.nativeSetGPIO(8, 0);
        }
    }

    public native int nativeSetGPIO(int i, int i2);

    public void openPrint() {
        nativeSetGPIO(88, LEVEL_HIGH);
    }

    public void closePrint() {
        nativeSetGPIO(88, 0);
    }

    public void openScan() {
        nativeSetGPIO(9, LEVEL_HIGH);
        new Timer().schedule(new nativeSetGPIO_8_Low_Timer(), 150);
    }

    public void closeScan() {
        nativeSetGPIO(9, 0);
    }

    public void startScan() {
        nativeSetGPIO(8, LEVEL_HIGH);
    }

    public void stopScan() {
        nativeSetGPIO(8, 0);
    }

    static {
        System.loadLibrary("setGPIO");
    }
}
