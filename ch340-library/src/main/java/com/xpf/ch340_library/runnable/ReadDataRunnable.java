package com.xpf.ch340_library.runnable;

import com.xpf.ch340_library.driver.InitCH340;
import com.xpf.ch340_library.logger.LogUtils;
import com.xpf.ch340_library.utils.CH340Util;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by xpf on 2017/12/20.
 * Function:ReadDataRunnable
 */
public class ReadDataRunnable implements Runnable {

    private static final String TAG = "ReadDataRunnable";
    private boolean mStop = false; // 是否停止线程
    private static ReadDataListener listener;

    public static void setReadListener(ReadDataListener listener) {
        ReadDataRunnable.listener = listener;
    }

    @Override
    public void run() {
        startReadThread();
    }

    /**
     * 开启读取数据线程
     */
    private void startReadThread() {
        while (!mStop) {
            byte[] receiveBuffer = new byte[300];// 接收数据数组
            // 读取缓存区的数据长度
            int length = InitCH340.getDriver().ReadData(receiveBuffer, 300);

            switch (length) {
                case 0: // 无数据
                    LogUtils.i(TAG, "No data~");
                    break;
                default: // 有数据时的处理
                    // 将此处收到的数组转化为HexString
//                    String str=new String(receiveBuffer,0,length);
//                    String hexString = CH340Util.bytesToHexString(receiveBuffer, length);
                    if (listener != null) {
//                        listener.onReadStr(hexString);
//                        byte[] buffer=receiveBuffer.clone();
                        listener.onReadHex(receiveBuffer,300);
                    }
//                    LogUtils.i(TAG, "ReadHexString===" + hexString + ",length===" + length);
                    break;
            }
            try {
                Thread.sleep(20);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止读取任务
     */
    public void stopTask() {
        mStop = true;
    }


    public interface ReadDataListener {
//        void onReadStr(String str);
        void onReadHex(byte[] hex,int length);
    }
}
