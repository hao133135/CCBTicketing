package com.jilian.ccbticketing.Activity.Sell;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jilian.ccbticketing.Activity.LoginActivity;
import com.jilian.ccbticketing.Activity.MenuActivity;
import com.jilian.ccbticketing.Adapter.SellPaymentAdapter;
import com.jilian.ccbticketing.Model.BaseModel;
import com.jilian.ccbticketing.Model.SellListModel;
import com.jilian.ccbticketing.Model.SellModel;
import com.jilian.ccbticketing.Model.SettingModel;
import com.jilian.ccbticketing.R;
import com.jilian.ccbticketing.Uitls.Commontools;
import com.jilian.ccbticketing.Uitls.Configuration;
import com.jilian.ccbticketing.Uitls.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellActivity extends Commontools implements View.OnClickListener,AdapterView.OnItemClickListener,SellPaymentAdapter.Callback {

    private List<SellModel> sellModelArrayList = new ArrayList<>();
    private ListView mylistview;
    private Button btn;
    private ImageButton backButton;
    private HttpUtils httpUtils;
    private SettingModel settingModel;
    private Context context;
    private String resultData;
    private SellPaymentAdapter<SellModel> myArrayAdapter;
    private EditText phoneText;
    private Map<String, Object> map = new HashMap<String, Object>();
    private SellModel sellModel;
    private SellListModel sellListModel;
    private BaseModel baseModel = new BaseModel();
    private Configuration configuration = new Configuration();
    private Handler handler = new Handler();
    private String msg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        init();

    }

    private void init() {
        Intent intent = this.getIntent();
        context = this;
        httpUtils = new HttpUtils();
        mylistview = findViewById(R.id.sell_ticket_listview);
        btn = findViewById(R.id.sell_page_pay_btn);
        backButton = findViewById(R.id.sell_page_back_btn);
        phoneText = findViewById(R.id.sell_phone_edit_text);
        btn.setOnClickListener(this);
        backButton.setOnClickListener(this);
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
        getData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sell_page_pay_btn:
                toPayMent();
                break;
            case R.id.sell_page_back_btn:
                backBtn();
                break;
        }
    }



    private void toPayMent() {
        final List<SellModel> ss =new ArrayList<>();
        for (int i=0;i<sellModelArrayList.size();i++)
        {
            SellModel s = sellModelArrayList.get(i);
            if (s.isSelect())
            {
                ss.add(s);
            }
        }
        if(ss.size()<1){
            msg="请选择需要购买的票种";
            handler.post(toast);
        }else {
            if(false){
                msg="请输入正确的手机号码";
                handler.post(toast);
            }else {
                map.put("channel","POS");
                map.put("machineID",baseModel.getMachineID());
                map.put("operatorId","123");
                map.put("data","");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        resultData = httpUtils.baseHttp(SellActivity.this,baseModel,"getPayOrder",map);
                        try {
                            JSONObject jsonObject = new JSONObject(resultData);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            sellListModel = new SellListModel();
                            sellListModel.setThirdId(jsonObject1.getString("payOrder"));
                            sellListModel.setPhone(phoneText.getText().toString());
                            sellListModel.setSellModels(ss);
                            Intent i = new Intent(SellActivity.this, SellPaymentActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("sellList", sellListModel);
                            i.putExtras(bundle);
                            startActivity(i);
                            finish();
                        } catch (JSONException e) {
                            msg = "请输入正确的手机号码";
                            handler.post(toast);
                        }
                    }
                }).start();
            }
        }
    }

    private void backBtn() {
        startActivity(configuration.getIntent(SellActivity.this,MenuActivity.class));
        finish();
    }

    private void  getData(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        JSONObject data = new JSONObject();
        try {
            data.put("date",dateString);
        } catch (JSONException e) {
            msg = "获取时间失败";
            handler.post(toast);
            e.printStackTrace();
        }
        map.put("channel","POS");
        map.put("machineID",baseModel.getMachineID());
        map.put("operatorId","123");
        map.put("data",data);
        new Thread(new Runnable() {
            @Override
            public void run() {
                resultData = httpUtils.baseHttp(SellActivity.this,baseModel,"qrySale",map);
                returnValue(resultData);
            }
        }).start();

    }
    class ticketListTask extends AsyncTask<Object, Void, List<SellModel>> {
        @Override
        protected List<SellModel> doInBackground(Object... objects) {
            return sellModelArrayList;
        }

        @Override
        protected void onPostExecute(List<SellModel> sellModels) {
            super.onPostExecute(sellModels);
            if(sellModels!=null){
                listViewClick();
            }else {
                return;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            if(isCancelled()) return;
        }
    }

    public void returnValue(String data)
    {
        JSONObject jsonObject ;
        try {
            jsonObject = new JSONObject(data);
            sellModelArrayList.clear();
            if (jsonObject.getInt("code")==200){
                JSONObject tickets = jsonObject.getJSONObject("data");
                JSONArray jsonArray = (JSONArray) tickets.get("tickets");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                    SellModel s = new SellModel(jsonObject1.getString("id"),jsonObject1.getString("name"), jsonObject1.getDouble("price"), jsonObject1.getDouble("realPrice"));
                    sellModelArrayList.add(s);
                }
                if(sellModelArrayList.size()>0) {
                    new ticketListTask().execute();
                }
            }else if(jsonObject.getInt("code")==201)
            {
                SharedPreferences sp = getSharedPreferences("config", 0);
                SharedPreferences.Editor editor = sp.edit();
                //把数据进行保存
                editor.putString("token", "");
                //提交数据
                editor.commit();
                msg = "登陆过期，请重新登录";
                handler.post(toast);
                startActivity(configuration.getIntent(SellActivity.this,LoginActivity.class));
                finish();
            }else if (jsonObject.getInt("code")>0)
            {
                msg = jsonObject.getString("msg");
                handler.post(toast);
            }
        } catch (JSONException e) {
            msg = "服务器链接失败";
            handler.post(toast);
        }
    }
    /**
     *填充数据到listview
     */
    private void listViewClick() {
        myArrayAdapter = new SellPaymentAdapter<SellModel>
                (this,sellModelArrayList,R.layout.item_sell,this);
        mylistview.setAdapter(myArrayAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView list= findViewById(R.id.sell_ticket_listview);//获得listview
        sellModelArrayList.clear();
        for (int i = 0; i < list.getChildCount(); i++) {
            LinearLayout layout = (LinearLayout)list.getChildAt(i);// 获得子item的layout
            // EditText et = (EditText) layout.getChildAt(1)//或者根据位置,在这我假设TextView在前，EditText在后
            CheckBox isSelect = layout.findViewById(R.id.sell_check_box);// 从layout中获得控件,根据其id
            TextView ticketid = layout.findViewById(R.id.pay_ticket_id);
            TextView name= layout.findViewById(R.id.pay_ticket_name);
            TextView price = layout.findViewById(R.id.pay_ticket_price);
            TextView realprice= layout.findViewById(R.id.pay_ticket_realprice);
            EditText num = layout.findViewById(R.id.pay_ticket_number);
            sellModel.setId(ticketid.getText().toString());
            sellModel.setName(name.getText().toString());
            sellModel.setPrice(Double.parseDouble(String.valueOf(price.getText())));
            sellModel.setRealPrice(Double.parseDouble(String.valueOf(realprice.getText())));
            sellModel.setSelect(isSelect.isChecked());
            sellModel.setNum(Integer.parseInt(String.valueOf(num.getText())));
            if (isSelect.isChecked()){
                sellModelArrayList.add(sellModel);
            }
        }
    }
    /***
     *  对传过来的点击事件进行处理
     * @param view
     */
    @Override
    public void click(View view) {
        switch (view.getId()){
            case R.id.pay_ticket_reduce:
                int position=(Integer)view.getTag();//得到点击的位置
                SellModel coursePaySub=sellModelArrayList.get(position);//得到item的数据
                int countSub=coursePaySub.getNum();//得到当前数量
                if (countSub>=1){
                    countSub--;
                }
                if(countSub!=0&&countSub>0){
                    coursePaySub.setSelect(true);
                }else {
                    coursePaySub.setSelect(false);
                }
                coursePaySub.setNum(countSub);//设置最新的数量
                if(countSub>0){
                }
                myArrayAdapter.notifyDataSetChanged();
                break;
            case R.id.pay_ticket_plus:
                int positionAdd=(Integer)view.getTag();//得到点击的位置
                SellModel coursePayAdd=sellModelArrayList.get(positionAdd);//得到item的数据
                int countAdd=coursePayAdd.getNum();//得到当前数量
                if (countAdd>=0){
                    countAdd+=1;
                }
                if(countAdd!=0&&countAdd>0){
                    coursePayAdd.setSelect(true);
                }else {
                    coursePayAdd.setSelect(false);
                }
                coursePayAdd.setNum(countAdd);//设置最新的数量
                myArrayAdapter.notifyDataSetChanged();
                break;
        }
    }
    Runnable toast = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    };
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
