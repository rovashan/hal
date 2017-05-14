package com.cad.halstart;

import android.util.Log;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPortModel {
    private static final String LOG_TAG;
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    private static native FileDescriptor open(String str, int i, int i2, String str2, int i3, String str3);

    public native void close();

    static {
        LOG_TAG = SerialPortModel.class.getSimpleName();
        System.loadLibrary("serial_port");
    }

    public SerialPortModel(File device, int baudRate, int dataBits, String checkingMode, int stopBits, String flowMode) throws SecurityException, IOException {
        if (!(device.canRead() && device.canWrite())) {
            try {
                Process su = Runtime.getRuntime().exec("/system/bin/su");
                su.getOutputStream().write(("chmod 666 " + device.getAbsolutePath() + "\n" + "exit\n").getBytes());
                if (!(su.waitFor() == 0 && device.canRead() && device.canWrite())) {
                    throw new SecurityException("Can't root!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }
        this.mFd = open(device.getAbsolutePath(), baudRate, dataBits, checkingMode, stopBits, flowMode);
        if (this.mFd == null) {
            Log.e(LOG_TAG, "Error:Open serial device failed.");
            throw new IOException();
        }
        this.mFileInputStream = new FileInputStream(this.mFd);
        this.mFileOutputStream = new FileOutputStream(this.mFd);
    }

    public InputStream getInputStream() {
        return this.mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return this.mFileOutputStream;
    }
}
