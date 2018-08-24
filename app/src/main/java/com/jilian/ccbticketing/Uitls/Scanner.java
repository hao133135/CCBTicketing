package com.jilian.ccbticketing.Uitls;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.ccb.deviceservice.aidl.IDeviceService;
import com.ccb.deviceservice.aidl.scanner.Constant;
import com.ccb.deviceservice.aidl.scanner.IScanner;
import com.ccb.deviceservice.aidl.scanner.OnScanListener;

import java.util.concurrent.CountDownLatch;


/**
 * Created by YahuG1 on 2017/10/31.
 */

public class Scanner{
    private static final String TAG = "Scanner";
    CountDownLatch latch = new CountDownLatch(1);
    String scanResult = "";

    public Scanner() throws RemoteException {
    }

    public IScanner getScan(IDeviceService deviceService,int cameraID){
        Log.i(TAG,"getScan executed");
        try {

            return IScanner.Stub.asInterface(deviceService.getScanner(cameraID));
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String startBackScan(IDeviceService deviceService,int timeout,int paytype,String title) throws RemoteException {
        Log.i(TAG,"startBackScan executed");
        scanResult = "";
        Bundle bundle = new Bundle();
        bundle.putInt("timeout",timeout);
        bundle.putInt("payType",paytype);

        if(title != null){
            bundle.putString("title",title);
        }

        getScan(deviceService,Constant.CameraID.BACK).startScan(bundle,new OnScanListener.Stub() {
            @Override
            public void onSuccess(Bundle bundle) throws RemoteException {
                Log.d(TAG,"onSuccess,scanResult = " + bundle.getString("barcode"));
                scanResult = bundle.getString("barcode");
                latch.countDown();
            }

            @Override
            public void onError(int i, String s) throws RemoteException {
                Log.d(TAG,"onError,errorCode = " + i);
                Log.d(TAG,"onError,errorMessage = " + s);
                scanResult = s;
                latch.countDown();
            }

            @Override
            public void onTimeout() throws RemoteException {
                Log.d(TAG,"onTimeout");
                scanResult = "扫码超时";
                latch.countDown();
            }

            @Override
            public void onCancel() throws RemoteException {
                Log.d(TAG,"onCancel");
                scanResult = "扫码取消";
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return scanResult;
    }
    public String startFrontScan(IDeviceService deviceService,int timeout,int paytype,String title) throws RemoteException {
        Log.i(TAG,"startFrontScan executed");
        scanResult = "";
        Bundle bundle = new Bundle();
        bundle.putInt("timeout",timeout);
        bundle.putInt("payType",paytype);
        if(title != null){
            bundle.putString("title",title);
        }

        getScan(deviceService,Constant.CameraID.FRONT).startScan(bundle,new OnScanListener.Stub() {
            @Override
            public void onSuccess(Bundle bundle) throws RemoteException {
                Log.d(TAG,"onSuccess,scanResult = " + bundle.getString("barcode"));
                scanResult = bundle.getString("barcode");
                latch.countDown();
            }

            @Override
            public void onError(int i, String s) throws RemoteException {
                Log.d(TAG,"onError,errorCode = " + i);
                Log.d(TAG,"onError,errorMessage = " + s);
                scanResult = s;
                latch.countDown();
            }

            @Override
            public void onTimeout() throws RemoteException {
                Log.d(TAG,"onTimeout");
                scanResult = "扫码超时";
                latch.countDown();
            }

            @Override
            public void onCancel() throws RemoteException {
                Log.d(TAG,"onCancel");
                scanResult = "扫码取消";
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return scanResult;
    }


  /*  @TestDetailCaseAnnotation(
            id = 0,
            apiName = "startScan",
            popMsgWin = true,
            resWinTitle = "startScan接口测试",
            resWinMessage = "启动后置扫码",
            itemClass = Scanner.class
    )
    public String CASE_0_startScan_CN001() throws RemoteException {
        Log.i(TAG,"CASE_0_startScan_CN001");
        return startBackScan(60,0,"使用后置扫码");
    }

    @TestDetailCaseAnnotation(
            id = 0,
            apiName = "startScan",
            popMsgWin = true,
            resWinTitle = "startScan接口测试",
            resWinMessage = "启动后置扫码(支付方式:通用支付)",
            itemClass = Scanner.class
    )
    public String CASE_0_startScan_CN002() throws RemoteException {
        Log.i(TAG,"CASE_0_startScan_CN002");
        return startBackScan(60,0,null);
    }

    @TestDetailCaseAnnotation(
            id = 0,
            apiName = "startScan",
            popMsgWin = true,
            resWinTitle = "startScan接口测试",
            resWinMessage = "启动后置扫码(支付方式:龙支付/银联)",
            itemClass = Scanner.class
    )
    public String CASE_0_startScan_CN003() throws RemoteException {
        Log.i(TAG,"CASE_0_startScan_CN003");
        return startBackScan(60,1,null);
    }

    @TestDetailCaseAnnotation(
            id = 0,
            apiName = "startScan",
            popMsgWin = true,
            resWinTitle = "startScan接口测试",
            resWinMessage = "启动后置扫码(支付方式:支付宝/微信)",
            itemClass = Scanner.class
    )
    public String CASE_0_startScan_CN004() throws RemoteException {
        Log.i(TAG,"CASE_0_startScan_CN004");
        return startBackScan(60,2,null);
    }

    @TestDetailCaseAnnotation(
            id = 0,
            apiName = "startScan",
            popMsgWin = true,
            resWinTitle = "startScan接口测试",
            resWinMessage = "启动前置扫码",
            itemClass = Scanner.class
    )
    public String CASE_0_startScan_CN005() throws RemoteException {
        Log.i(TAG,"CASE_0_startScan_CN005");
        return startFrontScan(60,0,"使用前置扫码");
    }

    @TestDetailCaseAnnotation(
            id = 0,
            apiName = "startScan",
            popMsgWin = true,
            resWinTitle = "startScan接口测试",
            resWinMessage = "启动前置扫码(支付方式:通用支付)",
            itemClass = Scanner.class
    )
    public String CASE_0_startScan_CN006() throws RemoteException {
        Log.i(TAG,"CASE_0_startScan_CN006");
        return startFrontScan(60,0,null);
    }

    @TestDetailCaseAnnotation(
            id = 0,
            apiName = "startScan",
            popMsgWin = true,
            resWinTitle = "startScan接口测试",
            resWinMessage = "启动前置扫码(支付方式:龙支付/银联)",
            itemClass = Scanner.class
    )
    public String CASE_0_startScan_CN007() throws RemoteException {
        Log.i(TAG,"CASE_0_startScan_CN007");
        return startFrontScan(60,1,null);
    }

    @TestDetailCaseAnnotation(
            id = 0,
            apiName = "startScan",
            popMsgWin = true,
            resWinTitle = "startScan接口测试",
            resWinMessage = "启动前置扫码(支付方式:支付宝/微信)",
            itemClass = Scanner.class
    )
    public String CASE_0_startScan_CN008() throws RemoteException {
        Log.i(TAG,"CASE_0_startScan_CN008");
        return startFrontScan(60,2,null);
    }

    @TestDetailCaseAnnotation(
            id = 0,
            apiName = "startScan",
            popMsgWin = true,
            resWinTitle = "startScan接口测试",
            resWinMessage = "启动后置扫码(入参timeout传入错误的超时时间)",
            itemClass = Scanner.class
    )
    public String CASE_0_startScan_CN009() throws RemoteException {
        Log.i(TAG,"CASE_0_startScan_CN009");
        return startBackScan(0,0,"使用后置扫码");
    }

    @TestDetailCaseAnnotation(
            id = 0,
            apiName = "startScan",
            popMsgWin = true,
            resWinTitle = "startScan接口测试",
            resWinMessage = "启动后置扫码(入参timeout传入错误的超时时间)",
            itemClass = Scanner.class
    )
    public String CASE_0_startScan_CN010() throws RemoteException {
        Log.i(TAG,"CASE_0_startScan_CN010");
        return startBackScan(-1,0,"使用后置扫码");
    }

    @TestDetailCaseAnnotation(
            id = 0,
            apiName = "startScan",
            popMsgWin = true,
            resWinTitle = "startScan接口测试",
            resWinMessage = "启动后置扫码(入参payType传入错误的扫码支付方式)",
            itemClass = Scanner.class
    )
    public String CASE_0_startScan_CN011() throws RemoteException {
        Log.i(TAG,"CASE_0_startScan_CN011");
        return startBackScan(60,-1,"使用后置扫码");
    }

    @TestDetailCaseAnnotation(
            id = 0,
            apiName = "startScan",
            popMsgWin = true,
            resWinTitle = "startScan接口测试",
            resWinMessage = "启动后置扫码(入参payType传入错误的扫码支付方式)",
            itemClass = Scanner.class
    )
    public String CASE_0_startScan_CN012() throws RemoteException {
        Log.i(TAG,"CASE_0_startScan_CN012");
        return startBackScan(60,3,"使用后置扫码");
    }

    @TestDetailCaseAnnotation(
            id = 0,
            apiName = "startScan",
            popMsgWin = true,
            resWinTitle = "startScan接口测试",
            resWinMessage = "启动后置扫码(入参title传入错误的扫码界面标题)",
            itemClass = Scanner.class
    )
    public String CASE_0_startScan_CN013() throws RemoteException {
        Log.i(TAG,"CASE_0_startScan_CN013");
        return startBackScan(60,0,"");
    }

    @TestDetailCaseAnnotation(
            id = 0,
            apiName = "startScan",
            popMsgWin = true,
            resWinTitle = "startScan接口测试",
            resWinMessage = "启动后置扫码(入参title传入错误的扫码界面标题)",
            itemClass = Scanner.class
    )
    public String CASE_0_startScan_CN014() throws RemoteException {
        Log.i(TAG,"CASE_0_startScan_CN014");
        scanResult = "";
        Bundle bundle = new Bundle();
        bundle.putInt("timeout",60);
        bundle.putInt("payType",0);
        bundle.putString("title",null);
        getScan(Constant.CameraID.BACK).startScan(bundle,new OnScanListener.Stub() {
            @Override
            public void onSuccess(Bundle bundle) throws RemoteException {
                Log.d(TAG,"onSuccess,scanResult = " + bundle.getString("barcode"));
                scanResult = bundle.getString("barcode");
                latch.countDown();
            }

            @Override
            public void onError(int i, String s) throws RemoteException {
                Log.d(TAG,"onError,errorCode = " + i);
                Log.d(TAG,"onError,errorMessage = " + s);
                scanResult = s;
                latch.countDown();
            }

            @Override
            public void onTimeout() throws RemoteException {
                Log.d(TAG,"onTimeout");
                scanResult = "扫码超时";
                latch.countDown();
            }

            @Override
            public void onCancel() throws RemoteException {
                Log.d(TAG,"onCancel");
                scanResult = "扫码取消";
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return scanResult;
    }

    @TestDetailCaseAnnotation(
            id = 0,
            apiName = "startScan",
            popMsgWin = true,
            resWinTitle = "startScan接口测试",
            resWinMessage = "启动后置扫码(过长的扫码界面标题(入参35个汉字))",
            itemClass = Scanner.class
    )
    public String CASE_0_startScan_CN015() throws RemoteException {
        Log.i(TAG,"CASE_0_startScan_CN015");
        return startBackScan(60,0,"用于测试过长的扫码界面标题内容是否会正常显示出来最大可显示的长度是多少");
    }

    @TestDetailCaseAnnotation(
            id = 0,
            apiName = "startScan",
            popMsgWin = true,
            resWinTitle = "startScan接口测试",
            resWinMessage = "启动后置扫码(监听为null)",
            itemClass = Scanner.class
    )
    public String CASE_0_startScan_CN016() throws RemoteException {
        Log.i(TAG,"CASE_0_startScan_CN016");
        Bundle bundle = new Bundle();
        bundle.putInt("timeout",60);
        bundle.putInt("payType",0);
        bundle.putString("title","使用后置扫码");
        getScan(Constant.CameraID.BACK).startScan(bundle,null);
        return "查看service日志是否有判断";
    }

    @TestDetailCaseAnnotation(
            id = 0,
            apiName = "startScan",
            popMsgWin = true,
            resWinTitle = "startScan接口测试",
            resWinMessage = "启动后置扫码(Bundle对象为null)",
            itemClass = Scanner.class
    )
    public String CASE_0_startScan_CN017() throws RemoteException {
        Log.i(TAG,"CASE_0_startScan_CN017");
        getScan(Constant.CameraID.BACK).startScan(null, new OnScanListener.Stub() {
            @Override
            public void onSuccess(Bundle bundle) throws RemoteException {
                Log.d(TAG,"onSuccess");
            }

            @Override
            public void onError(int i, String s) throws RemoteException {
                Log.d(TAG,"onError");
            }

            @Override
            public void onTimeout() throws RemoteException {
                Log.d(TAG,"onTimeout");
            }

            @Override
            public void onCancel() throws RemoteException {
                Log.d(TAG,"onCancel");
            }
        });
        return "查看service日志是否有判断";
    }


    @TestDetailCaseAnnotation(
            id = 0,
            apiName = "stopScan",
            popMsgWin = true,
            resWinTitle = "stopScan接口测试",
            resWinMessage = "启动前置扫码，10秒后停止前置扫码",
            itemClass = Scanner.class
    )
    public String CASE_1_stopScan_CN001() throws RemoteException {
        Log.i(TAG,"CASE_1_stopScan_CN001");
        Bundle bundle = new Bundle();
        bundle.putInt("timeout",60);
        bundle.putInt("payType",0);
        bundle.putString("title","使用前置扫码");
        getScan(Constant.CameraID.FRONT).startScan(bundle, new OnScanListener.Stub() {
            @Override
            public void onSuccess(Bundle bundle) throws RemoteException {

            }

            @Override
            public void onError(int i, String s) throws RemoteException {

            }

            @Override
            public void onTimeout() throws RemoteException {

            }

            @Override
            public void onCancel() throws RemoteException {

            }
        });
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getScan(Constant.CameraID.FRONT).stopScan();
        return "停止前置扫码";
    }

    @TestDetailCaseAnnotation(
            id = 0,
            apiName = "stopScan",
            popMsgWin = true,
            resWinTitle = "stopScan接口测试",
            resWinMessage = "启动后置扫码，10秒后停止后置扫码",
            itemClass = Scanner.class
    )
    public String CASE_1_stopScan_CN002() throws RemoteException {
        Log.i(TAG,"CASE_1_stopScan_CN002");
        Bundle bundle = new Bundle();
        bundle.putInt("timeout",60);
        bundle.putInt("payType",0);
        bundle.putString("title","使用后置扫码");
        getScan(Constant.CameraID.BACK).startScan(bundle, new OnScanListener.Stub() {
            @Override
            public void onSuccess(Bundle bundle) throws RemoteException {

            }

            @Override
            public void onError(int i, String s) throws RemoteException {

            }

            @Override
            public void onTimeout() throws RemoteException {

            }

            @Override
            public void onCancel() throws RemoteException {

            }
        });
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getScan(Constant.CameraID.BACK).stopScan();
        return "停止后置扫码";
    }
*/
}