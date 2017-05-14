package com.cad.halstart;


import java.io.IOException;
import java.io.OutputStream;

public class PrintUtil {
    public static final int LINE_BYTE_SIZE = 32;

    public static void setInitialize(OutputStream os) {
        try {
            byte[] d = new byte[]{(byte) 27, (byte) 64};
            os.write(d, 0, d.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setPagerLine(OutputStream os, int n) {
        int i = 0;
        while (i < n) {
            try {
                os.write(10);
                i++;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public static void setConcentration(OutputStream os, int con) {
        try {
            byte[] d = new byte[]{(byte) 16, (byte) -46, (byte) con};
            os.write(d, 0, d.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setSpeed(OutputStream os, int speed) {
        try {
            byte[] d = new byte[]{(byte) 16, (byte) -47, (byte) speed};
            os.write(d, 0, d.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setDefaultLinewidth(OutputStream os) {
        try {
            byte[] d = new byte[]{(byte) 27, (byte) 50};
            os.write(d, 0, d.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setLinewidth(OutputStream os, int width) {
        try {
            byte[] d = new byte[]{(byte) 16, (byte) -47, (byte) width};
            os.write(d, 0, d.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setRigthwidth(OutputStream os, int width) {
        try {
            byte[] d = new byte[]{(byte) 27, (byte) 32, (byte) width};
            os.write(d, 0, d.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setAlignment(OutputStream os, int n) {
        try {
            byte[] d = new byte[]{(byte) 27, (byte) 97, (byte) n};
            os.write(d, 0, d.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setTextSize(OutputStream os, int size) {
        try {
            byte[] d = new byte[]{(byte) 29, (byte) 33, (byte) size};
            os.write(d, 0, d.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setTypeFace(OutputStream os, int n) {
        try {
            byte[] d = new byte[]{(byte) 27, (byte) 77, (byte) n};
            os.write(d, 0, d.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setUnderline(OutputStream os, int n) {
        try {
            byte[] d = new byte[]{(byte) 27, (byte) 45, (byte) n};
            os.write(d, 0, d.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setBold(OutputStream os, int n) {
        try {
            byte[] d = new byte[]{(byte) 27, (byte) 69, (byte) n};
            os.write(d, 0, d.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setReverse(OutputStream os, int n) {
        try {
            byte[] d = new byte[]{(byte) 29, (byte) 66, (byte) n};
            os.write(d, 0, d.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setRotate(OutputStream os, int n) {
        try {
            byte[] d = new byte[]{(byte) 27, (byte) 86, (byte) n};
            os.write(d, 0, d.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setAbsoluteLocation(OutputStream os, int nl, int nh) {
        try {
            byte[] d = new byte[]{(byte) 27, (byte) 36, (byte) nl, (byte) nh};
            os.write(d, 0, d.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
