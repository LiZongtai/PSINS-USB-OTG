package com.xpf.ch340_host;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xpf.ch340_library.CH340Master;
import com.xpf.ch340_library.driver.InitCH340;
import com.xpf.ch340_library.runnable.ReadDataRunnable;
import com.xpf.ch340_library.utils.CH340Util;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements InitCH340.IUsbPermissionListener, ReadDataRunnable.ReadDataListener {

    private boolean isFirst;//判断是否打开
    private Button btnSend, btnFormat,floatOrHexBtn;
    private EditText etContent;
    private TextView recvText;
    private static final String ACTION_USB_PERMISSION = "com.linc.USB_PERMISSION";
    private byte[] readBuffer;

    private TextView data1;
    private TextView data2;
    private TextView data3;
    private TextView data4;
    private TextView data5;
    private TextView data6;
    private TextView data7;
    private TextView data8;
    private TextView data9;
    private TextView data10;
    private TextView data11;
    private TextView data12;
    private TextView data13;
    private TextView data14;
    private TextView data15;
    private TextView data16;
    private TextView data17;
    private TextView data18;
    private TextView data19;
    private TextView data20;
    private TextView data21;
    private TextView data22;
    private TextView data23;
    private TextView data24;
    private TextView data25;
    private TextView data26;
    private TextView data27;
    private TextView data28;



    private boolean isFloat=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSend = findViewById(R.id.btnSend);
        btnFormat = findViewById(R.id.btnFormat);
        floatOrHexBtn=findViewById(R.id.floatOrHexBtn);
        etContent = findViewById(R.id.etContent);
        recvText = findViewById(R.id.recvText);
        recvText.setGravity(Gravity.BOTTOM);
        recvText.setMovementMethod(ScrollingMovementMethod.getInstance());

        data1=findViewById(R.id.data1);
        data2=findViewById(R.id.data2);
        data3=findViewById(R.id.data3);
        data4=findViewById(R.id.data4);
        data5=findViewById(R.id.data5);
        data6=findViewById(R.id.data6);
        data7=findViewById(R.id.data7);
        data8=findViewById(R.id.data8);
        data9=findViewById(R.id.data9);
        data10=findViewById(R.id.data10);
        data11=findViewById(R.id.data11);
        data12=findViewById(R.id.data12);
        data13=findViewById(R.id.data13);
        data14=findViewById(R.id.data14);
        data15=findViewById(R.id.data15);
        data16=findViewById(R.id.data16);
        data17=findViewById(R.id.data17);
        data18=findViewById(R.id.data18);
        data19=findViewById(R.id.data19);
        data20=findViewById(R.id.data20);
        data21=findViewById(R.id.data21);
        data22=findViewById(R.id.data22);
        data23=findViewById(R.id.data23);
        data24=findViewById(R.id.data24);
        data25=findViewById(R.id.data25);
        data26=findViewById(R.id.data26);
        data27=findViewById(R.id.data27);
        data28=findViewById(R.id.data28);

        initData();
        initListener();
    }

    private void initListener() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });
        btnFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String format = btnFormat.getText().toString().toLowerCase();
                if ("ascii".equals(format)) {
                    btnFormat.setText("hex");
                } else if ("hex".equals(format)) {
                    btnFormat.setText("ascii");
                }
            }
        });
    }

    private void sendData() {
        String string = etContent.getText().toString();
        if (!TextUtils.isEmpty(string)) {
            String format = btnFormat.getText().toString().toLowerCase();
            CH340Util.writeData(string.getBytes(), format);
        } else {
            Toast.makeText(MainActivity.this, "发送的数据不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
//        readBuffer = new byte[520];
        InitCH340.setListener(this);
        ReadDataRunnable.setReadListener(this);
        if (!isFirst) {
            isFirst = true;
            // 初始化 ch340-library
            CH340Master.initialize(MyApplication.getContext());
        }
    }

    @Override
    public void result(boolean isGranted) {
        if (!isGranted) {
            PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
            InitCH340.getmUsbManager().requestPermission(InitCH340.getUsbDevice(), mPermissionIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter usbFilter = new IntentFilter();
        usbFilter.addAction(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, usbFilter);
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            Toast.makeText(MainActivity.this, "EXTRA_PERMISSION_GRANTED~", Toast.LENGTH_SHORT).show();
                            InitCH340.loadDriver(MyApplication.getContext(), InitCH340.getmUsbManager());
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "EXTRA_PERMISSION_GRANTED null!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mUsbReceiver);
    }

//    @Override
//    public void onReadStr(final String str) {
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                //定时器
//                Timer timer = new Timer();
//                TimerTask task = new TimerTask() {
//                    @Override
//                    public void run() {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if(!isFloat){
//                                    recvText.setText(str);
//                                }
//
//                            }
//                        });
//                    }
//                };
//                timer.schedule(task, 1000, 10);//3秒后每隔一秒刷新一次
//            }
//        }.start();
//    }

    @Override
    public void onReadHex(byte[] hex, final int length) {
//        final byte[] header=new byte[4];
//        header[0]=hex[0];
//        header[1]=hex[1];
//        header[2]=hex[2];
//        header[3]=hex[3];
//        final String headerString = CH340Util.bytesToHexString(header, 4);
        if(length!=140){
            return;
        }
//        if(headerString!="aa55aa56"){
//            return;
//        }
        final byte[] hexbuffer=hex.clone();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                                data1.setText(hexString);

                if(!isFloat){
                    String hexString = CH340Util.bytesToHexString(hexbuffer, length);
                    recvText.setText(hexString);
                }
                if(isFloat){
                    float[] floatArray= new float[length/4];
                    hex2Float(floatArray,hexbuffer,length);
                    recvText.setText(Arrays.toString(floatArray));
                    data1.setText(""+floatArray[1]);
                    data2.setText(""+floatArray[2]);
                    data3.setText(""+floatArray[3]);
                    data4.setText(""+floatArray[4]);
                    data5.setText(""+floatArray[5]);
                    data6.setText(""+floatArray[6]);
                    data7.setText(""+floatArray[7]);
                    data8.setText(""+floatArray[8]);
                    data9.setText(""+floatArray[9]);
                    data10.setText(""+floatArray[10]);
                    data11.setText(""+floatArray[11]);
                    data12.setText(""+floatArray[12]);
                    data13.setText(""+floatArray[13]);
                    data14.setText(""+floatArray[14]);
                    data15.setText(""+floatArray[15]);
                    data16.setText(""+floatArray[16]);
                    data17.setText(""+floatArray[17]);
                    data18.setText(""+(floatArray[18]+floatArray[19]));
                    data19.setText(""+(floatArray[20]+floatArray[21]));
                    data20.setText(""+floatArray[23]);
                    data21.setText(""+floatArray[24]);
                    data22.setText(""+floatArray[25]);
                    data23.setText(""+(floatArray[26]+floatArray[27]));
                    data24.setText(""+(floatArray[28]+floatArray[29]));
                    data25.setText(""+floatArray[31]);
                    data26.setText(""+floatArray[32]);
                    data27.setText(""+floatArray[33]);
                    data28.setText(""+System.currentTimeMillis());
                }

            }
        });
    }

    private void hex2Float(float[] floats,byte[] bytes,int length) {
        for (int i=0;i<length/4;i++){
            floats[i] = byte2float(bytes,4*i);
        }
    }


    public static float byte2float(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    public void floatOrHex(View view) {
        if(isFloat){
            floatOrHexBtn.setText("to Float");
        }else {
            floatOrHexBtn.setText("to Hex");
        }
        isFloat=!isFloat;
    }
}
