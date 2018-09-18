package com.jilian.ccbticketing.Activity.Query;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.jilian.ccbticketing.Activity.LoginActivity;
import com.jilian.ccbticketing.Activity.MenuActivity;
import com.jilian.ccbticketing.Adapter.RefundAdapter;
import com.jilian.ccbticketing.Model.BaseModel;
import com.jilian.ccbticketing.Model.GuideModel;
import com.jilian.ccbticketing.Model.QryorderModel;
import com.jilian.ccbticketing.Model.TicketModel;
import com.jilian.ccbticketing.R;
import com.jilian.ccbticketing.Uitls.Commontools;
import com.jilian.ccbticketing.Uitls.Configuration;
import com.jilian.ccbticketing.Uitls.Constant;
import com.jilian.ccbticketing.Uitls.HttpUtils;
import com.jilian.ccbticketing.Uitls.clickUtils;
import com.jilian.ccbticketing.Uitls.zxing.activity.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefundActivity extends Commontools implements View.OnClickListener {
    private Button refundBtn;
    private ImageButton backBtn;
    private ImageView queryBtn,scanBtn;
    private EditText editText;
    private boolean isScaning,isRefund;
    private String barcode,resultData,bankReturn,payMethod,Message,phone,orderNo,transDate,transData;
    private ListView paymentLists;
    private QryorderModel qryorderModel ;
    private List<String > sslist = new ArrayList<>();
    private List<GuideModel> guideModels = new ArrayList<>();
    private List<TicketModel> ticketModels = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Spinner refundSpinner;
    private BaseModel baseModel;
    private final static int REQUESTCODE = 1; // 返回的结果码
    private Map<String, Object> map = new HashMap<String, Object>();
    private HttpUtils httpUtils = new HttpUtils();
    private Configuration configuration = new Configuration();
    private int type;
    private String traceNo,msg,payOrder,DPDSvcID;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);
        init();
    }

    private void init() {
        isRefund =false;
        isScaning =false;
        scanBtn = findViewById(R.id.refund_scan_btn);
        queryBtn = findViewById(R.id.refund_query_btn);
        editText = findViewById(R.id.refund_query_edit);
        refundBtn = findViewById(R.id.refund_refund_btn);
        backBtn = findViewById(R.id.query_refund_page_back_btn);
        paymentLists = findViewById(R.id.refund_query_listview);
        refundSpinner= findViewById(R.id.refund_ticket_message);
        scanBtn.setOnClickListener(this);
        queryBtn.setOnClickListener(this);
        refundBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        SharedPreferences sharedPreferences=getSharedPreferences("config",0);
        String serviceip=sharedPreferences.getString("ip","");
        String serviceport=sharedPreferences.getString("port","");
        String mac = sharedPreferences.getString("mac","");
        String serialNo = sharedPreferences.getString("serialNo","");
        String operatorId = sharedPreferences.getString("posuser","");
        String token = sharedPreferences.getString("token","");
        baseModel=new BaseModel();
        baseModel.setIp(serviceip);
        baseModel.setPort(serviceport);
        baseModel.setMachineID(mac);
        baseModel.setSerialNo(serialNo);
        baseModel.setToken(token);
        baseModel.setOperatorId(operatorId);
        new SpinnerTask().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.refund_scan_btn:
                if(clickUtils.isFastClick()) {
                    ticketModels.clear();
                    scanMethod();
                }
                break;
            case R.id.refund_query_btn:
                if(clickUtils.isFastClick()) {
                    if (editText.getText().toString() != null && !"".equals(editText.getText().toString())) {
                        ticketModels.clear();
                        barcode = editText.getText().toString();
                        getData();
                    } else {
                        msg = "请扫码或者输入二维码";
                        handler.post(toast);
                    }
                }
                break;
            case R.id.refund_refund_btn:
                if(clickUtils.isFastClick()) {
                    if (editText.getText().toString() != null && !"".equals(editText.getText().toString())) {
                        new AlertDialog.Builder(RefundActivity.this)
                                .setTitle("退票提示")
                                .setMessage("\n\t\t\t\t是否退票！\t\n")
                                .setNegativeButton("确认",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                refundResult();
                                                dialog.dismiss();
                                            }
                                        })
                                .setPositiveButton("取消",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                    } else {
                        msg = "请扫码或者输入二维码";
                        handler.post(toast);
                    }
                }
                break;
            case R.id.query_refund_page_back_btn:
                if(clickUtils.isFastClick()){
                    backBtn();
                }
                break;
        }
    }
    private void getData(){
        JSONObject data = new JSONObject();
        try {
            data.put("payOrder",barcode);
            data.put("func",1);
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
                resultData = httpUtils.baseHttp(RefundActivity.this,baseModel,"qryOrder",map);
                // resultData ="{\"code\":200,\"msg\":\"success\",\"data\":{\"tickets\":[{\"ispay\":true,\"price\":200,\"name\":\"门票-成人\",\"paytime\":\"2018-08-15\",\"id\":\"B00188F9V85JDM8K\",\"isCheck\":0,\"isRefund\":0},{\"ispay\":true,\"price\":100,\"name\":\"门票-儿童半价\",\"paytime\":\"2018-08-15\",\"id\":\"B00188F97Z9KPYZ2\",\"isCheck\":0,\"isRefund\":0}],\"payOrder\":\"A00188F9V85D958K\"}}";
                returnedValue(resultData,"query");
            }
        }).start();

    }
    class paymentTask extends AsyncTask<Object, Void, List<TicketModel>> {

        @Override
        protected List<TicketModel> doInBackground(Object... objects) {
            return ticketModels;
        }

        @Override
        protected void onPostExecute(List<TicketModel> ticketModels) {
            super.onPostExecute(ticketModels);
            if(ticketModels.size()!=0){
                listViewClick();
            }else {
                return;
            }
        }
    }
    class SpinnerTask extends AsyncTask<Object, Void, List<String>>
    {
        @Override
        protected List<String> doInBackground(Object... params) {
            sslist.add("售票员操作失误");
            sslist.add("顾客行程改变");
            return sslist;
        }
        @Override
        protected void onPostExecute(List<String> result) {
            // TODO Auto-generated method stub
            spinnerClick();
        }
    }
    private void spinnerClick() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sslist);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        refundSpinner.setAdapter(adapter);
    }
    private void listViewClick() {
        final RefundAdapter<TicketModel> myArrayAdapter = new RefundAdapter<TicketModel>
                (RefundActivity.this,ticketModels,R.layout.refund_items);
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
    private void returnedValue(String data,String mc) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(data);
           if (jsonObject.getInt("code")==200){
                if(mc.equals("query")) {
                    try {
                        JSONObject tickets = jsonObject.getJSONObject("data");
                        ticketModels.clear();
                        JSONArray jsonArray = (JSONArray) tickets.get("tickets");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                            TicketModel t = new TicketModel(jsonObject1.getString("id"), jsonObject1.getString("name"), jsonObject1.getString("paytime"), jsonObject1.getDouble("price"), jsonObject1.getBoolean("ispay"), jsonObject1.getInt("isCheck"), jsonObject1.getInt("isRefund"));
                            ticketModels.add(t);
                        }
                        new paymentTask().execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(mc.equals("refund")){
                    try {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        type = jsonObject1.getInt("payType");
                        traceNo=jsonObject1.getString("traceNo");
                        payOrder = jsonObject1.getString("payOrder");
                        DPDSvcID = jsonObject1.getString("DPDSvcID");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(ticketModels!=null){
                        if(type==3){
                            refundSuccess();
                        }else {
                            bankResult();
                        }
                    }else {
                        msg = "请扫描二维码";
                        handler.post(toast);
                    }
                }
                qryorderModel= new QryorderModel(barcode,ticketModels,guideModels);
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
                startActivity(configuration.getIntent(RefundActivity.this,LoginActivity.class));
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
    private void backBtn() {
        startActivity(configuration.getIntent(RefundActivity.this,MenuActivity.class));
        finish();
    }

    private void bankResult()
    {
        JSONObject jsonObject =new JSONObject();
        try {
            if (type==1){
                jsonObject.put("lsOrderNo", payOrder);
                jsonObject.put("inputRemarkInfo","银行卡退款");
            }else if (type==2) {
                if(DPDSvcID!=""&&!"".equals(DPDSvcID)){
                    jsonObject.put("DPDSvcID",DPDSvcID);
                    jsonObject.put("lsOrderNo", payOrder);
                    jsonObject.put("inputRemarkInfo","龙支付退款");
                }else {
                    jsonObject.put("amt", "000000000001");//0.01元
                    jsonObject.put("inputRemarkInfo","微信/支付宝退款");
                }
            }else if(type==3) {
                refundResult();
                return;
            }
        } catch (JSONException e) {
            msg = e.getMessage();
            handler.post(toast);
            e.printStackTrace();
        }

        Intent intent = new Intent();
        ComponentName compName = new ComponentName("com.ccb.smartpos.bankpay", "com.ccb.smartpos.bankpay.ui.MainActivity");
        intent.setComponent(compName);
        intent.putExtra("appName", "建行收单应用");
        if (type == 1) {
            intent.putExtra("transId", "撤销");
        } else if (type == 2) {
            if(DPDSvcID!=""&&!"".equals(DPDSvcID)){
                intent.putExtra("transId", "龙支付退货");
            }else {
                intent.putExtra("transId", "微信/支付宝退货");
            }
        }
        intent.putExtra("transData", jsonObject.toString());
        isScaning =false;
        isRefund = true;
        startActivityForResult(intent, 2);
    }
    private void scanMethod() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(RefundActivity.this, new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(RefundActivity.this, CaptureActivity.class);
        isRefund = false;
        isScaning = true;
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (isScaning) {
            if ( resultCode > 0) {
                Bundle bundle = data.getExtras();
                barcode = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
                //将扫描出的信息显示出来
                editText.setText(barcode.toCharArray(), 0, barcode.length());
                getData();
                isScaning = false;
            }
        }
        if (isRefund) {
            if (resultCode == -1) {
                if (requestCode == 2) {
                    Bundle MarsBuddle = data.getExtras();
                    bankReturn = MarsBuddle.getString("returnCode");
                    Message = MarsBuddle.getString("resultMsg");
                    transData = MarsBuddle.getString("transData");
                    refundSuccess();
                }
            } else  if(data!=null){
                    Bundle MarsBuddle = data.getExtras();
                    bankReturn = MarsBuddle.getString("resultCode");
                    Message = MarsBuddle.getString("resultMsg");
                    refundFial();
            }else {
                msg = "获取POS机数据失败";
                handler.post(toast);
            }
        }
    }

    private void refundFial() {
        Intent i = new Intent(RefundActivity.this, RefundFailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("qry", qryorderModel);
        i.putExtras(bundle);
        i.putExtra("Message",Message);
        startActivity(i);
        finish();
    }

    private void refundSuccess() {
        Intent i = new Intent(RefundActivity.this, RefundSuccessActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("qry", qryorderModel);
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }

    private void refundResult() {
        map.clear();
        JSONObject data = new JSONObject();
        try {
            data.put("payOrder",barcode);
            data.put("reason",refundSpinner.getSelectedItem().toString());
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
                resultData = httpUtils.baseHttp(RefundActivity.this, baseModel, "refundOrder", map);
                returnedValue(resultData, "refund");
            }
        }).start();
    }

    Runnable toast = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    scanMethod();
                } else {
                    // 被禁止授权
                    msg="请至权限中心打开本应用的相机访问权限";
                    handler.post(toast);
                }
                break;
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
