package com.jilian.ccbticketing.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ccb.deviceservice.aidl.IDeviceService;
import com.ccb.deviceservice.aidl.deviceinfo.IDeviceInfo;
import com.jilian.ccbticketing.Model.BaseModel;
import com.jilian.ccbticketing.R;
import com.jilian.ccbticketing.Uitls.Configuration;

public class SetActivity extends AppCompatActivity implements View.OnClickListener {
    public  final static String SER_KEY = "com.jilian.ccbticketing.Model.SettingModel";
    private ImageButton backBtn;
    private Button saveBtn;
    private EditText ip,port,machineID,posUserEd,posPwdEd;
    private BaseModel baseModel = new BaseModel();
    private String activity;
    private IDeviceInfo deviceInfo;
    private IDeviceService deviceService;
    private Configuration configuration = new Configuration();
    private String serialNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        init();
    }

    private void init() {
        Intent intent = new Intent();
        intent.setAction("com.ccb.device_service");
        intent.setPackage("com.ccb.deviceservice");
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        Bundle bundle = getIntent().getExtras();
        activity = bundle.getString("activity");
        if(activity==null)
        {
            activity="";
        }
        ip = findViewById(R.id.set_ip);
        port = findViewById(R.id.set_port);
        machineID = findViewById(R.id.set_machine);
        posUserEd = findViewById(R.id.set_posuser);
        posPwdEd = findViewById(R.id.set_pospwd);
        backBtn = findViewById(R.id.set_page_back_btn);
        saveBtn = findViewById(R.id.set_save_btn);
        saveBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        SharedPreferences sharedPreferences=getSharedPreferences("config",0);
        String serviceip=sharedPreferences.getString("ip","192.168.0.200");
        String serviceport=sharedPreferences.getString("port","8090");
        String mac = sharedPreferences.getString("mac","23");
        String serialNo = sharedPreferences.getString("serialNo","");
        String posUser = sharedPreferences.getString("posuser","");
        String posPwd = sharedPreferences.getString("pospwd","");
        ip.setText(serviceip.toCharArray(),0,serviceip.length());
        port.setText(serviceport.toCharArray(),0,serviceport.length());
        machineID.setText(mac.toCharArray(),0,mac.length());
        posUserEd.setText(posUser.toCharArray(),0,posUser.length());
        posPwdEd.setText(posPwd.toCharArray(),0,posPwd.length());
        try{
            if(activity.equals("login"))
            {
                backBtn.setVisibility(View.GONE);
            }
        }catch (Exception e)
        {
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_page_back_btn:
                backBtn();
                break;
            case R.id.set_save_btn:
                successMethod();
                break;
        }
    }
    /**
     * 获取设备序列号
     */
    public String getDeviceService() {
        if (baseModel.getSerialNo()==null) {
            try {
                //IBinder s= deviceService.getDeviceInfo();
                serialNo = deviceInfo.getSerialNo();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unbindService(connection);
        }
        return serialNo;
    }

    ServiceConnection connection = new ServiceConnection() {
        //绑定服务成功会调用的方法
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            deviceService = IDeviceService.Stub.asInterface(iBinder);
            try {
                deviceInfo = IDeviceInfo.Stub.asInterface(deviceService.getDeviceInfo());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        //绑定服务失败会调用的方法
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    private void successMethod() {
        String serialNo= getDeviceService();
        baseModel.setSerialNo(serialNo);
        baseModel.setIp(ip.getText().toString());
        baseModel.setPort(port.getText().toString());
        baseModel.setMachineID(machineID.getText().toString());
        SharedPreferences sp=getSharedPreferences("config",0);
        SharedPreferences.Editor editor=sp.edit();
        //把数据进行保存
        StringBuffer output = new StringBuffer();
        editor.putString("ip",ip.getText().toString());
        editor.putString("port",port.getText().toString());
        editor.putString("mac",machineID.getText().toString());
        editor.putString("serialNo",serialNo);
        editor.putString("posuser",posUserEd.getText().toString());
        editor.putString("pospwd",posPwdEd.getText().toString());
        //提交数据
        editor.commit();
        try{
            if(activity.equals("login")){
                startActivity(configuration.getIntent(SetActivity.this,LoginActivity.class));
                finish();
            }else
            {
                startActivity(configuration.getIntent(SetActivity.this,SetMenuActivity.class));
                finish();
            }
        }catch (Exception e)
        {
            startActivity(configuration.getIntent(SetActivity.this,SetMenuActivity.class));
            finish();
        }
    }
    private void backBtn() {
        if(activity.equals("login")){
            startActivity(configuration.getIntent(SetActivity.this,LoginActivity.class));
            finish();
        }else
        {
            startActivity(configuration.getIntent(SetActivity.this,SetMenuActivity.class));
            finish();
        }
    }
    /**
     * 监听返回键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            backBtn();
        }
        return super.onKeyDown(keyCode, event);
    }
}
