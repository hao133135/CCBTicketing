package com.jilian.ccbticketing.Activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jilian.ccbticketing.Model.BaseModel;
import com.jilian.ccbticketing.R;
import com.jilian.ccbticketing.Uitls.Base64Utils;
import com.jilian.ccbticketing.Uitls.Commontools;
import com.jilian.ccbticketing.Uitls.Configuration;
import com.jilian.ccbticketing.Uitls.HttpUtils;
import com.jilian.ccbticketing.Uitls.clickUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends Commontools implements View.OnClickListener {
    private EditText userEdit, pwdEdit;
    private Button loginBtn,setBtn;
    private String user, pwd,resultData,posuser,pospwd ;
    private HttpUtils httpUtils;
    private Map<String, Object> map = new HashMap<String, Object>();
    private Configuration configuration = new Configuration();
    private BaseModel baseModel ;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private final long ONE_DAY = 2 * 60 * 60 * 1000;
    private String bankReturn,localUser,localPwd;
    private String Message,msg;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        baseModel = new BaseModel();
        httpUtils = new HttpUtils();
        baseModel = (BaseModel) getIntent().getSerializableExtra(SetActivity.SER_KEY);
        userEdit = findViewById(R.id.ticketing_login_username);
        pwdEdit = findViewById(R.id.ticketing_login_password);
        loginBtn = findViewById(R.id.ticketing_login_btn);
        setBtn = findViewById(R.id.sell_login_set_btn);
        loginBtn.setOnClickListener(this);
        setBtn.setOnClickListener(this);
        SharedPreferences sharedPreferences=getSharedPreferences("config",0);
        String serviceip=sharedPreferences.getString("ip","");
        String serviceport=sharedPreferences.getString("port","");
        String mac = sharedPreferences.getString("mac","");
        String serialNo = sharedPreferences.getString("serialNo","");
        String token = sharedPreferences.getString("token","");
        localUser = sharedPreferences.getString("user","");
        localPwd = sharedPreferences.getString("pwd","");
        posuser = sharedPreferences.getString("posuser","");
        pospwd = sharedPreferences.getString("pospwd","");
        String installDate = sharedPreferences.getString("InstallDate", null);
        userEdit.setText(localUser.toCharArray(),0,localUser.length());
        String operatorId = sharedPreferences.getString("posuser","");
        baseModel=new BaseModel();
        baseModel.setOperatorId(operatorId);
        baseModel.setIp(serviceip);
        baseModel.setPort(serviceport);
        baseModel.setMachineID(mac);
        baseModel.setSerialNo(serialNo);
        baseModel.setToken(token);
        if (!isNetworkAvailable(LoginActivity.this))
        {
            msg="当前没有可用网络";
            handler.post(toast);

        }
       // getDate(sharedPreferences, installDate);

    }
    Runnable toast = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    };
    public void getSign(){
        JSONObject jsonObject =new JSONObject();
        try {
            jsonObject.put("userName", posuser);
            jsonObject.put("userPasswd",  pospwd);
            jsonObject.put("isDefaultUser", true);
            jsonObject.put("projectTag", "LuguLake");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        ComponentName compName = new ComponentName("com.ccb.smartpos.bankpay", "com.ccb.smartpos.bankpay.ui.MainActivity");
        intent.setComponent(compName);
        intent.putExtra("appName", "建行收单应用");
        intent.putExtra("transId", "签到");
        intent.putExtra("transData", jsonObject.toString());
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == 2) {
                Bundle MarsBuddle = data.getExtras();
                bankReturn = MarsBuddle.getString("returnCode");
                Message = MarsBuddle.getString("resultMsg");
                msg=Message;
                handler.post(toast);
            }
        }else if (data!=null){
                Bundle MarsBuddle = data.getExtras();
                bankReturn = MarsBuddle.getString("resultCode");
                Message = MarsBuddle.getString("resultMsg");
                msg=Message;
                handler.post(toast);
        } else {
            msg = "获取POS机数据失败";
            handler.post(toast);
        }
    }

    private void getDate(SharedPreferences sharedPreferences, String installDate) {
        if(installDate != null) {
            Date before = null;
            try {
                before = (Date)formatter.parse(installDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date now = new Date();
            long diff = now.getTime() - before.getTime();
            long days = diff / ONE_DAY;
            if(days > 1) {
                SharedPreferences sp = getSharedPreferences("config", 0);
                SharedPreferences.Editor editor = sp.edit();
                //把数据进行保存
                editor.putString("InstallDate", null);
                //提交数据
                editor.commit();
            }

            getSign();
        }else {
            if(installDate == null) {
                // First run, so save the current date
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Date now = new Date();
                String dateString = formatter.format(now);
                editor.putString("InstallDate", dateString);
                //Commit the edits!
                editor.commit();
            }
           init();
        }
    }

    /**
     * 检查当前网络是否可用
     *
     * @param
     * @return
     */

    public boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ticketing_login_btn:
                if(clickUtils.isFastClick()){
                    login();
                }
                break;
            case R.id.sell_login_set_btn:
                if(clickUtils.isFastClick()) {
                    configuration();
                }
                break;
        }
    }

    private void returnedValue(String data) {
        if(data!=null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject = new JSONObject(data);
                if (jsonObject.getInt("code") == 200) {
                    Map map = json2map(data);
                    SharedPreferences sp = getSharedPreferences("config", 0);
                    SharedPreferences.Editor editor = sp.edit();
                    //把数据进行保存
                    editor.putString("token", map.get("data").toString());
                    editor.putString("user", userEdit.getText().toString());
                    editor.putString("pwd", pwdEdit.getText().toString());
                    Date now = new Date();
                    String dateString = formatter.format(now);
                    editor.putString("InstallDate", dateString);
                    //提交数据
                    editor.commit();
                    startActivity(configuration.getIntent(LoginActivity.this, MenuActivity.class));
                    finish();
                } else if (jsonObject.getInt("code") > 0) {
                    msg=jsonObject.getString("msg");
                    handler.post(toast);
                }
            } catch (JSONException e) {
                msg = "获取服务端数据失败";
                handler.post(toast);
                e.printStackTrace();
            }
        }
    }

    private void login() {
        user = userEdit.getText().toString();
        pwd = pwdEdit.getText().toString();
        if (user.equals("") || pwd.equals("")) {
            msg="请输入用户名和密码";
            handler.post(toast);
        } else  {
            map.clear();
            map.put("username", user);
            String p = Base64Utils.getBase64(pwd);
            map.put("password", p);
            map.put("type", "0");//0：登陆，1：注销
            OkHttpClient mOkHttpClient = new OkHttpClient();//创建OkHttpClient对象。
            FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
            Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, Object> entry = entries.next();
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                formBody.add( entry.getKey(),entry.getValue().toString());
            }
            try{
                Request request = new Request.Builder()//创建Request 对象。
                        .url(  baseModel.getIp() + "/lgh/mpos/" + "login")
                        .header("teller_code",baseModel.getSerialNo())
                        .addHeader("jwt", baseModel.getToken())
                        .post(formBody.build())//传递请求体
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        msg="服务器链接失败";
                        handler.post(toast);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        resultData = response.body().string();
                        if(resultData!=null) {
                            returnedValue(resultData);
                        }
                    }
                });
            }catch (Exception e)
            {
                msg=e.getMessage();
                handler.post(toast);
            }
        }
    }

    public static Map<String, Object> json2map(String str_json) {
        Map<String, Object> res = null;
        try {
            Gson gson = new Gson();
            res = gson.fromJson(str_json, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
        }
        return res;
    }
    public void configuration() {
        Intent i = new Intent(LoginActivity.this, SetActivity.class);
        i.putExtra("activity","login");
        startActivity(i);
        finish();
    }


}
