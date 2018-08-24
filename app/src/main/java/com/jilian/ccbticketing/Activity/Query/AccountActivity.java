package com.jilian.ccbticketing.Activity.Query;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jilian.ccbticketing.Activity.LoginActivity;
import com.jilian.ccbticketing.Adapter.AccountAdapter;
import com.jilian.ccbticketing.Model.BaseModel;
import com.jilian.ccbticketing.Model.CheckParameterModel;
import com.jilian.ccbticketing.R;
import com.jilian.ccbticketing.Uitls.Configuration;
import com.jilian.ccbticketing.Uitls.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    private Button queryBtn,homeBtn;
    private ImageButton backBtn;
    private int mYear,mMonth,mDay;
    private TextView timeView,count;
    private ListView paymentLists;
    private List<CheckParameterModel> checkParameterModels = new ArrayList<>();
    private Map<String, Object> map = new HashMap<String, Object>();
    private HttpUtils httpUtils;
    private String queryTime,resultData,totalNum;
    private BaseModel baseModel=new BaseModel();
    private Configuration configuration = new Configuration();
    private Handler handler = new Handler();
    private int cNum;
    private String msg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        init();
    }

    private void init() {
        handler = new Handler();
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        httpUtils = new HttpUtils();
        queryBtn = findViewById(R.id.account_query_btn);
        homeBtn = findViewById(R.id.account_home_btn);
        backBtn  = findViewById(R.id.account_page_back_btn);
        timeView = findViewById(R.id.account_time_btn);
        count = findViewById(R.id.account_pay_count);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        timeView.setText(c.get(Calendar.YEAR)+"年"+(c.get(Calendar.MONTH)+1)+"月"+c.get(Calendar.DATE)+"日");
        paymentLists = findViewById(R.id.account_pay_listview);
        queryBtn.setOnClickListener(this);
        homeBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        timeView.setOnClickListener(this);
        SharedPreferences sharedPreferences=getSharedPreferences("config",0);
        String serviceip=sharedPreferences.getString("ip","");
        String serviceport=sharedPreferences.getString("port","");
        String mac = sharedPreferences.getString("mac","");
        String serialNo = sharedPreferences.getString("serialNo","");
        String token = sharedPreferences.getString("token","");
        baseModel=new BaseModel();
        baseModel.setIp(serviceip);
        baseModel.setPort(serviceport);
        baseModel.setMachineID(mac);
        baseModel.setSerialNo(serialNo);
        baseModel.setToken(token);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.account_query_btn:
                queryMethod();
                break;
            case R.id.account_home_btn:
                backBtn();
                break;
            case R.id.account_page_back_btn:
                backBtn();
                break;
            case R.id.account_time_btn:
                setTime();
                break;
        }
    }
    class paymentTask extends AsyncTask<Object, Void, List<CheckParameterModel>> {
        @Override
        protected List<CheckParameterModel> doInBackground(Object... objects) {
            return checkParameterModels;
        }

        @Override
        protected void onPostExecute(List<CheckParameterModel> checkParameterModels) {
            super.onPostExecute(checkParameterModels);
            if(checkParameterModels!=null){
                listViewClick();
            }else {
                return;
            }
        }
    }

    private void listViewClick() {
        final AccountAdapter<CheckParameterModel> myArrayAdapter = new AccountAdapter<CheckParameterModel>
                (AccountActivity.this,checkParameterModels,R.layout.account_items);
        paymentLists.setAdapter(myArrayAdapter);
        paymentLists.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
                // 进行点击事件后的逻辑操作
                myArrayAdapter.setSelectedItem(position);
                myArrayAdapter.notifyDataSetChanged();
            }

        });
    }

    private void setTime() {
        new DatePickerDialog(AccountActivity.this, onDateSetListener, mYear, mMonth, mDay).show();
    }
    /**
     * 日期选择器对话框监听
     */
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }
            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }
            }
            timeView.setText(days);
        }
    };
    private void queryMethod() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String date = mYear+"-"+(mMonth+1)+"-"+mDay;
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("date", date);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                map.put("channel", "POS");
                map.put("machineID", baseModel.getMachineID());
                map.put("operatorId", "123");
                map.put("data", jsonObject);
                resultData = httpUtils.baseHttp(AccountActivity.this, baseModel, "qrySum", map);
                returnedValue(resultData);
            }
        }).start();
    }
    private void returnedValue(String data) {
        JSONObject jsonObject ;
        checkParameterModels.clear();
        try {
            jsonObject = new JSONObject(data);
           if (jsonObject.getInt("code")==200){
               JSONObject jsonArraydata = jsonObject.getJSONObject("data");
               if(jsonArraydata.getInt("totalNum")>0){
                   JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                   JSONArray jsonArray = jsonObject1.getJSONArray("bytime");
                   cNum = jsonObject1.getInt("totalNum");
                   handler.post(countMethod);
                   JSONObject jsonObject2 = (JSONObject) jsonArray.get(0);
                   JSONArray jsonArray1 = (JSONArray) jsonObject2.get("pay");
                   for (int i = 0; i < jsonArray1.length(); i++) {
                       JSONObject jsonObject3 = (JSONObject) jsonArray1.get(i);
                       CheckParameterModel t = new CheckParameterModel();
                       if (jsonObject3.get("type").equals("1")) {
                           t.setParam1("银行卡");
                       } else if (jsonObject3.get("type").equals("2")) {
                           t.setParam1("聚合支付");
                       } else if (jsonObject3.get("type").equals("3")) {
                           t.setParam1("现金");
                       }
                       t.setParam2(String.valueOf(jsonObject3.getDouble("gather")));
                       t.setParam3(String.valueOf(jsonObject3.getDouble("refund")));
                       t.setParam4(String.valueOf(jsonObject3.getDouble("gather")-jsonObject3.getDouble("refund")));
                       checkParameterModels.add(t);
                   }
               }
               if(checkParameterModels.size()>0) {
                   new paymentTask().execute();
               }
            }else if(jsonObject.getInt("code")==201)
            {
                SharedPreferences sp = getSharedPreferences("config", 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("token", "");
                editor.commit();
                msg = "登陆过期，请重新登录";
                handler.post(toast);
                startActivity(configuration.getIntent(AccountActivity.this,LoginActivity.class));
                finish();
            }else if (jsonObject.getInt("code")>0)
           {
               cNum = 0;
               handler.post(countMethod);
               checkParameterModels =new ArrayList<>();;
               new paymentTask().execute();
               msg = jsonObject.getString("msg");
               handler.post(toast);
           }
        } catch (JSONException e) {
            msg = "服务器链接失败";
            handler.post(toast);
        }

    }


    Runnable toast = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    };
    Runnable countMethod = new Runnable(){
        @Override
        public void run() {
            count.setText("票总数："+cNum);
        }
    };
    private void backBtn() {
        startActivity(configuration.getIntent(AccountActivity.this,QueryActivity.class));
        finish();
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
