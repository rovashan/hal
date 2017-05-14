package com.cad.halstart;

import android.app.Activity;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.media.TransportMediator;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.WriterException;
import com.obm.mylibrary.BuildConfig;
import com.obm.mylibrary.PicFromPrintUtils;
import com.obm.mylibrary.PrintConnect;
import com.obm.mylibrary.PrintUtil;
import com.obm.mylibrary.ScanConnect;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MainActivity extends Activity {

    private Bitmap bitmap;
    private ImageView code_img;
    private int con;
    private int count;
    private String data;
    private int height;
    private Handler mHandler;
    private PrintConnect mPrintConnect;
    private ScanConnect mScanConnect;
    private SeekBar sb_con;
    private SeekBar sb_height;
    private SeekBar sb_speed;
    private SeekBar sb_width;
    private int speed;
    private EditText text;
    private TextView tv_con;
    private TextView tv_count;
    private TextView tv_height;
    private TextView tv_speed;
    private TextView tv_width;
    private int width;

    class myHandler extends Handler {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case R.styleable.Toolbar_subtitleTextAppearance /*11*/:
                    String str = msg.obj.toString();
                    if (MainActivity.this.data.equals(str)) {
                        MainActivity.this.count = MainActivity.this.count + 1;
                    } else {
                        MainActivity.this.count = 1;
                    }
                    MainActivity.this.data = str;
                    MainActivity.this.text.setText(str);
                    MainActivity.this.tv_count.setText(MainActivity.this.count + BuildConfig.FLAVOR);
                default:
            }
        }
    }

    public MainActivity() {
        this.data = BuildConfig.FLAVOR;
        this.con = 1;
        this.speed = 7;
        this.width = 1;
        this.height = 1;
        this.count = 0;
        this.bitmap = null;
        this.mHandler = new myHandler();
    }

    //@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        getWindow().setSoftInputMode(32);
        this.mScanConnect = new ScanConnect(this, this.mHandler);
        this.mPrintConnect = new PrintConnect(this);
        this.sb_height.setProgress(39);
    }

    public void onClick(View v) {
        String data = this.text.getText().toString();
        if (TextUtils.isEmpty(data)) {
            Toast.makeText(this, "\u8bf7\u8f93\u5165\u5185\u5bb9", 0).show();
            return;
        }
        PrintUtil.setSpeed(this.mPrintConnect.os, this.speed);
        PrintUtil.setConcentration(this.mPrintConnect.os, this.con);
        int i;
        switch (v.getId()) {
            case C0191R.id.btn_checktwo /*2131558490*/:
                createTwoCode(data);
            case C0191R.id.btn_checkone /*2131558491*/:
                createOneCode(data);
            case C0191R.id.btn_printtwo /*2131558493*/:
                createTwoCode(data);
                if (this.bitmap != null) {
                    this.mPrintConnect.send(this.bitmap);
                    try {
                        this.mPrintConnect.os.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            case C0191R.id.btn_printone /*2131558494*/:
                createOneCode(data);
                if (this.bitmap != null) {
                    this.mPrintConnect.send(this.bitmap);
                    try {
                        this.mPrintConnect.os.flush();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            case C0191R.id.btn_print /*2131558495*/:
                int size1 = data.length();
                i = 0;
                while (i < size1) {
                    int c = data.charAt(i);
                    if (19968 > c || c >= 40623) {
                        i++;
                    } else {
                        Toast.makeText(this, "\u751f\u6210\u6761\u5f62\u7801\u7684\u65f6\u4e0d\u80fd\u5305\u542b\u4e2d\u6587", 0).show();
                        return;
                    }
                }
                this.mPrintConnect.sendCode128(data, this.width, this.height);
            case C0191R.id.btn_line /*2131558496*/:
                StringBuffer sb = new StringBuffer();
                sb.delete(0, sb.length());
                for (i = 0; i < 160; i++) {
                    sb.append("-");
                }
                sb.append("\n");
                this.mPrintConnect.send(sb.toString());
            default:
        }
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
}
