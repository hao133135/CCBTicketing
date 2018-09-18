package com.jilian.ccbticketing.Activity.Sell;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jilian.ccbticketing.Activity.LoginActivity;
import com.jilian.ccbticketing.Adapter.DetailsAdapter;
import com.jilian.ccbticketing.Model.BaseModel;
import com.jilian.ccbticketing.Model.DetailsModel;
import com.jilian.ccbticketing.Model.SellListModel;
import com.jilian.ccbticketing.Model.SellModel;
import com.jilian.ccbticketing.Model.SettingModel;
import com.jilian.ccbticketing.R;
import com.jilian.ccbticketing.Uitls.Configuration;
import com.jilian.ccbticketing.Uitls.HttpUtils;
import com.jilian.ccbticketing.Uitls.clickUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellPaymentActivity extends AppCompatActivity implements View.OnClickListener {
    private Button payButton;
    private ImageButton backButton;
    private RadioGroup radioGroup;
    private ListView paymentLists;
    private List<DetailsModel> paymentlist = new ArrayList<>();
    private RadioButton scancodeBtn,bankcardBtn,cashBtn;
    private final static int REQUESTCODE = 1; // 返回的结果码
    private HttpUtils httpUtils;
    private SettingModel settingModel;
    private Context context;
    private String resultData,bankReturn,payMethod,Message,phone,msg;
    private Bundle bundle;
    private TextView phoneText,countPrice;
    private SellListModel sellListModel;
    private List<SellModel> sellModels;
    private double count = 0.0;
    private Map<String, Object> map = new HashMap<String, Object>();
    private String orderNo,transDate,transData;
    private BaseModel baseModel = new BaseModel();
    private Configuration configuration = new Configuration();
    private boolean flag;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_payment);
        init();
    }

    private void init() {
        Intent intent = this.getIntent();
        bundle=this.getIntent().getExtras();
        sellListModel = (SellListModel) getIntent().getSerializableExtra("sellList");
        httpUtils = new HttpUtils();
        context = this;
        payButton = findViewById(R.id.sell_payment_btn);
        backButton = findViewById(R.id.sell_pay_page_back_btn);
        phoneText = findViewById(R.id.sell_payment_phone_text);
        radioGroup = findViewById(R.id.sell_payment_radio);
        scancodeBtn = findViewById(R.id.sell_check_btn);
        bankcardBtn = findViewById(R.id.sell_back_btn);
        cashBtn = findViewById(R.id.sell_cash_btn);
        scancodeBtn.setChecked(true);
        payMethod=scancodeBtn.getText().toString();
        paymentLists = findViewById(R.id.sell_payment_listview);
        countPrice = findViewById(R.id.sell_payment_count_textview);
        phoneText.setText(sellListModel.getPhone());
        payButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(mylistener);
        SharedPreferences sharedPreferences=getSharedPreferences("config",0);
        String serviceip=sharedPreferences.getString("ip","");
        String serviceport=sharedPreferences.getString("port","");
        String mac = sharedPreferences.getString("mac","");
        String serialNo = sharedPreferences.getString("serialNo","");
        String token = sharedPreferences.getString("token","");
        String operatorId = sharedPreferences.getString("posuser","");
        baseModel=new BaseModel();
        baseModel.setOperatorId(operatorId);
        baseModel.setIp(serviceip);
        baseModel.setPort(serviceport);
        baseModel.setMachineID(mac);
        baseModel.setSerialNo(serialNo);
        baseModel.setToken(token);
        new paymentTask().execute();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sell_payment_btn:
                if(clickUtils.isFastClick()){
                    payMethod();
                }
                break;
            case R.id.sell_pay_page_back_btn:
                if(clickUtils.isFastClick()){
                    backBtn();
                }
                break;
        }
    }

    class paymentTask extends AsyncTask<Object, Void, List<DetailsModel>>{

        @Override
        protected List<DetailsModel> doInBackground(Object... objects) {
            return paymentlist;
        }

        @Override
        protected void onPostExecute(List<DetailsModel> detailsModels) {
            super.onPostExecute(detailsModels);
            if(detailsModels !=null){
                listViewClick();
            }else {
                return;
            }
        }
    }

    private void listViewClick() {
        sellModels =sellListModel.getSellModels();
        for (int i = 0 ;i<sellModels.size();i++)
        {
            DetailsModel t = new DetailsModel();
            t.setTicketOne(sellModels.get(i).getName()+"  x"+sellModels.get(i).getNum());
            t.setTicketTwo("￥"+String.valueOf(sellModels.get(i).getTotal()));
            count +=sellModels.get(i).getTotal();
            paymentlist.add(t);

        }
        countPrice.setText(" ￥ "+doubleToString(count));
        if(count==0.0)
        {
            cashBtn.setChecked(true);
            scancodeBtn.setEnabled(false);
            bankcardBtn.setEnabled(false);
        }
        final DetailsAdapter<DetailsModel> myArrayAdapter = new DetailsAdapter<DetailsModel>
                (SellPaymentActivity.this,paymentlist,R.layout.share_items);
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

    private void payMethod() {
        try {
            if(payMethod.isEmpty()){
                msg = "请选择支付方式";
                handler.post(toast);
            }
        }catch (Exception e)
        {
            msg = "请选择支付方式";
            handler.post(toast);
        }
        if(payMethod.equals("现金")){
            sellPayment(true);
        }else {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("amt", "000000000001");
                jsonObject.put("counterNo", baseModel.getMachineID());
                jsonObject.put("orderNo", sellListModel.getThirdId());
                jsonObject.put("lsOrderNo", sellListModel.getThirdId());
                jsonObject.put("inputRemarkInfo", "收款");
                jsonObject.put("projectTag", "LuguLake");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent();
            ComponentName compName = new ComponentName("com.ccb.smartpos.bankpay", "com.ccb.smartpos.bankpay.ui.MainActivity");
            intent.setComponent(compName);
            intent.putExtra("appName", "建行收单应用");
            if (payMethod.equals("聚合支付")) {
                intent.putExtra("transId", "聚合扫码支付");
            } else if (payMethod.equals("银行卡")) {
                intent.putExtra("transId", "消费");
            }
            intent.putExtra("transData", jsonObject.toString());
            startActivityForResult(intent, 2);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == 2) {
                Bundle MarsBuddle = data.getExtras();
                bankReturn = MarsBuddle.getString("returnCode");
                Message = MarsBuddle.getString("resultMsg");
                transData =MarsBuddle.getString("transData");
                sellPayment(true);
            }
        }else if (data!=null) {
                Bundle MarsBuddle = data.getExtras();
                bankReturn = MarsBuddle.getString("resultCode");
                Message = MarsBuddle.getString("resultMsg");
                paymentResult(Message,false,false);
        }else {
            msg = "获取POS机数据失败";
            handler.post(toast);
        }
    }
    private void sellPayment(final boolean b)
    {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        JSONArray jsonArray = new JSONArray();
        for (int i=0;i<sellListModel.getSellModels().size();i++)
        {
            try {
                JSONObject jsonObject = new JSONObject(sellListModel.getSellModels().get(i).toString());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONObject data = new JSONObject();
        try {
            if(payMethod.equals("聚合支付")){
                data.put("payType",2);
            }else if(payMethod.equals("银行卡")){
                data.put("payType",1);
            }else if(payMethod.equals("现金")){
                data.put("payType",3);
            }
            if (transData!=null)
            {
                JSONObject jsonObject = new JSONObject(transData);
                data.put("thirdMsg",jsonObject);
            }else {
                data.put("thirdMsg","");
            }
            data.put("payTime",dateString);
            data.put("thirdId","");
            data.put("terminalID",baseModel.getMachineID());
            data.put("phone",phoneText.getText().toString());
            data.put("tickets",jsonArray);
            data.put("payOrder",sellListModel.getThirdId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.clear();
        map.put("channel","POS");
        map.put("machineID",baseModel.getMachineID());
        map.put("operatorId",baseModel.getOperatorId());
        map.put("data",data);
        new Thread(new Runnable() {
            @Override
            public void run() {
                resultData = httpUtils.baseHttp(SellPaymentActivity.this,baseModel,"saleTickets",map);
                paymentResult(resultData,true,b);
            }
        }).start();

    }

    private void paymentResult(String data,boolean result,boolean b){
        if (b){
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
                if (jsonObject.getInt("code")==200){
                    if(data!=null&&!"".equals(data)) {
                        try {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            sellListModel.setThirdId(jsonObject1.getString("payOrder"));
                            sellListModel.setPayTime(jsonObject1.getString("payTime"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result && sellListModel.getThirdId() != null) {
                            Intent i = new Intent(SellPaymentActivity.this, PaySuccess.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("sellList", sellListModel);
                            i.putExtras(bundle);
                            startActivity(i);
                            finish();
                        }
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
                    startActivity(configuration.getIntent(SellPaymentActivity.this,LoginActivity.class));
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
        }else {
            Intent i = new Intent(SellPaymentActivity.this, PayFail.class);
            i.putExtra("Message", data);
            Bundle bundle = new Bundle();
            bundle.putSerializable("sellList", sellListModel);
            i.putExtras(bundle);
            startActivity(i);
            finish();
        }

    }

    private void backBtn() {
        startActivity(configuration.getIntent(SellPaymentActivity.this,SellActivity.class));
        finish();
    }

    RadioGroup.OnCheckedChangeListener mylistener=new RadioGroup.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(RadioGroup Group, int Checkid) {
            // TODO Auto-generated method stub
            //设置TextView的内容显示CheckBox的选择结果
            RadioButton radioButton = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
            //int id= radioGroup.getCheckedRadioButtonId();
            payMethod = radioButton.getText().toString();
        }
    };

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

    /**
     * double转String,保留小数点后两位
     * @param num
     * @return
     */
    public static String doubleToString(double num){
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }
}
