package com.cad.halstart;

import java.io.IOException;
import android.content.Context;
import android.graphics.Bitmap;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class PrintConnect {
    private SerialPortModel comSerialport;
    private Context context;
    public InputStream is;
    private Open open;
    public OutputStream os;

    class InitSerialPort extends Thread {


        public void run() {
            try {
                PrintConnect.this.comSerialport = new SerialPortModel(new File("/dev/ttyHSL1"), 115200, 8, "None", 1, "None");
                PrintConnect.this.is = PrintConnect.this.comSerialport.getInputStream();
                PrintConnect.this.os = PrintConnect.this.comSerialport.getOutputStream();
                byte[] a = new byte[]{(byte) 27, (byte) 64, (byte) 27, (byte) 82, (byte) 0};
                try {
                    PrintConnect.this.os.write(a, 0, a.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public PrintConnect(Context context) {
        this.open = new Open();
        this.context = context;
        this.open.openPrint();
        test();
    }

    private void test() {
        new InitSerialPort().start();
    }

    public void stop() {
        try {
            if (this.comSerialport != null) {
                this.is.close();
                this.os.close();
                this.comSerialport.close();
            }
            this.open.closePrint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String sendData) {
        try {
            byte[] data = sendData.getBytes("gbk");
            byte[] c = new byte[]{(byte) 27, (byte) 74, (byte) 3};
            byte[] b = new byte[]{(byte) 27, (byte) 100, (byte) 2};
            this.os.write(data, 0, data.length);
            this.os.write(b, 0, b.length);
            this.os.write(c, 0, c.length);
            this.os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Bitmap bit) {
        //PicFromPrintUtils.writeBit(bit, this.os);
    }

    public void send(byte[] data) {
        try {
            this.os.write(data, 0, data.length);
            this.os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCode128(String data, int width, int heigth) {
        try {
            byte[] n = new byte[]{(byte) 29, (byte) 107, (byte) 73};
            byte[] m = data.getBytes();
            byte[] code = new byte[]{(byte) 123, (byte) 66};
            byte[] o = new byte[]{(byte) 29, (byte) 104, (byte) heigth};
            byte[] p = new byte[]{(byte) 29, (byte) 119, (byte) width};
            this.os.write(o, 0, o.length);
            this.os.write(p, 0, p.length);
            this.os.write(n, 0, n.length);
            this.os.write((m.length + code.length) + 1);
            this.os.write(code);
            this.os.write(m);
            this.os.write(10);
            this.os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEnter(int count) {
        int i = 0;
        while (i < count) {
            try {
                this.os.write(10);
                i++;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
