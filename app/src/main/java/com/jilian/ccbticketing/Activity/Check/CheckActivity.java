package com.jilian.ccbticketing.Activity.Check;

import android.Manifest;
import android.content.Context;
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
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jilian.ccbticketing.Activity.LoginActivity;
import com.jilian.ccbticketing.Activity.MenuActivity;
import com.jilian.ccbticketing.Adapter.CheckAdapter;
import com.jilian.ccbticketing.Adapter.PaymentAdapter;
import com.jilian.ccbticketing.Model.BaseModel;
import com.jilian.ccbticketing.Model.DetailsModel;
import com.jilian.ccbticketing.Model.GuideModel;
import com.jilian.ccbticketing.Model.QryorderModel;
import com.jilian.ccbticketing.Model.SellModel;
import com.jilian.ccbticketing.Model.TicketModel;
import com.jilian.ccbticketing.R;
import com.jilian.ccbticketing.Uitls.Commontools;
import com.jilian.ccbticketing.Uitls.Configuration;
import com.jilian.ccbticketing.Uitls.Constant;
import com.jilian.ccbticketing.Uitls.HttpUtils;
import com.jilian.ccbticketing.Uitls.zxing.activity.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckActivity extends Commontools implements View.OnClickListener,CheckAdapter.CallBack{
    private Button confirmBtn;
    private ImageButton backBtn;
    private ImageView queryBtn,scanBtn;
    private EditText editText;
    private boolean isScaning,isCheck=false;
    private String barcode,resultData,resultCheckData,msg;
    private LinearLayout guideLayout;
    private HttpUtils httpUtils;
    private Context context;
    private ListView detailsLists,paymentLists;
    private List<DetailsModel> paymentlist = new ArrayList<>();
    private List<TicketModel> ticketModels = new ArrayList<>();
    private List<TicketModel> isTicketModels = new ArrayList<>();
    private List<SellModel> sellModelArrayList = new ArrayList<>();
    private TextView guideName,guideID;
    private BaseModel baseModel = new BaseModel();
    private Map<String, Object> map = new HashMap<String, Object>();
    private QryorderModel qryorderModel ;
    private List<GuideModel> guideModels = new ArrayList<>();
    private Configuration configuration =new Configuration();
    private CheckAdapter<TicketModel> checkAdapter;
    private PaymentAdapter<DetailsModel> myArrayAdapter;
    private Handler handler;
    //打开扫描界面请求码
    private int REQUEST_CODE = 0x01;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        init();
    }

    private void init() {
        Intent intent = this.getIntent();
        handler = new Handler();
        context = this;
        httpUtils = new HttpUtils();
        scanBtn=findViewById(R.id.check_scan_btn);
        queryBtn = findViewById(R.id.check_query_btn);
        editText = findViewById(R.id.check_query_edit);
        confirmBtn= findViewById(R.id.check_confirm_btn);
        backBtn = findViewById(R.id.check_page_back_btn);
        detailsLists = findViewById(R.id.check_details_listview);
        paymentLists = findViewById(R.id.check_ticket_listview);
        guideName = findViewById(R.id.check_guide_name);
        guideID = findViewById(R.id.check_guide_ID);
        guideLayout = findViewById(R.id.check_guide_layout);
        if (guideModels.size()!=0)
        {
           /* guideName.setText(guideModels.get(0).getName());
            guideID.setText(guideModels.get(0).getId());
            guideLayout.setVisibility(View.VISIBLE);*/
        }
        scanBtn.setOnClickListener(this);
        queryBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
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
            case R.id.check_scan_btn:
                scanMethod();
                break;
            case R.id.check_query_btn:
                if (editText.getText().toString()!=null &&!"".equals(editText.getText().toString()))
                {
                    barcode= editText.getText().toString();
                    getData();
                }else {
                    msg = "请扫码或者输入二维码";
                    handler.post(toast);
                }
                break;
            case R.id.check_confirm_btn:
                if (editText.getText().toString()!=null&&!"".equals(editText.getText().toString()))
                {
                    successMethod();
                }else {
                    msg = "请扫码或者输入二维码";
                    handler.post(toast);
                }
                break;
            case R.id.check_page_back_btn:
                backBtn();
                break;
            case R.id.check_state_isCheck:
                isTicketModels.clear();
                for(int i=0;i<checkAdapter.mChecked.size();i++){
                    if(checkAdapter.mChecked.get(i)){
                        isTicketModels.add(ticketModels.get(i));
                    }
                }
                paymentlist.clear();
                listViewClick();
                break;
        }
    }

    private void getData(){
        JSONObject data = new JSONObject();
        try {
            data.put("payOrder",barcode);
            data.put("func",0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        map.put("channel","POS");
        map.put("machineID",baseModel.getMachineID());
        map.put("operatorId","123");
        map.put("data",data);
        new Thread(new Runnable() {
            @Override
            public void run() {
                resultData = httpUtils.baseHttp(CheckActivity.this,baseModel,"qryOrder",map);
                // resultData = "{\"code\":200,\"msg\":\"success\",\"data\":{\"tickets\":[{\"ispay\":true,\"price\":200,\"name\":\"门票-成人\",\"paytime\":\"2018-08-15\",\"id\":\"B00188F9V85JDM8K\",\"isCheck\":0,\"isRefund\":0},{\"ispay\":true,\"price\":100,\"name\":\"门票-儿童半价\",\"paytime\":\"2018-08-15\",\"id\":\"B00188F97Z9KPYZ2\",\"isCheck\":0,\"isRefund\":0}],\"PID\":[{\"name\":\"王五\",\"id\":\"123123123123123\"}],\"payOrder\":\"A00188F9V85D958K\"}}";
                //resultData ="{\"code\":200,\"msg\":\"success\",\"data\":{\"tickets\":[{\"date\":\"2018-08-20 00:00:00\",\"isCheck\":0,\"ispay\":true,\"price\":0,\"isRefund\":0,\"name\":\"门票-军人\",\"paytime\":\"2018-08-20\",\"id\":\"B00188LW4RMKL4ZY\"},{\"date\":\"2018-08-20 00:00:00\",\"isCheck\":0,\"ispay\":true,\"price\":50,\"isRefund\":0,\"name\":\"门票-儿童半价\",\"paytime\":\"2018-08-20\",\"id\":\"B00188LP4ZJ7NVRY\"},{\"date\":\"2018-08-20 00:00:00\",\"isCheck\":0,\"ispay\":true,\"price\":60,\"isRefund\":0,\"name\":\"门票-残疾人\",\"paytime\":\"2018-08-20\",\"id\":\"B00188L9Y8P62JRM\"},{\"date\":\"2018-08-20 00:00:00\",\"isCheck\":0,\"ispay\":true,\"price\":100,\"isRefund\":0,\"name\":\"门票-成人\",\"paytime\":\"2018-08-20\",\"id\":\"B00188L97899Y582\"},{\"date\":\"2018-08-20 00:00:00\",\"isCheck\":1,\"ispay\":true,\"price\":100,\"isRefund\":0,\"name\":\"门票-成人\",\"paytime\":\"2018-08-20\",\"id\":\"B00188LEXRNJ6K8Y\"},{\"date\":\"2018-08-20 00:00:00\",\"isCheck\":1,\"ispay\":true,\"price\":0,\"isRefund\":0,\"name\":\"门票-军人\",\"paytime\":\"2018-08-20\",\"id\":\"B00188LPJREJY48M\"},{\"date\":\"2018-08-20 00:00:00\",\"isCheck\":1,\"ispay\":true,\"price\":60,\"isRefund\":0,\"name\":\"门票-学生\",\"paytime\":\"2018-08-20\",\"id\":\"B00188LM782KNMRW\"},{\"date\":\"2018-08-20 00:00:00\",\"isCheck\":1,\"ispay\":true,\"price\":60,\"isRefund\":0,\"name\":\"门票-残疾人\",\"paytime\":\"2018-08-20\",\"id\":\"B00188L9VR1N9M8M\"},{\"date\":\"2018-08-20 00:00:00\",\"isCheck\":1,\"ispay\":true,\"price\":0,\"isRefund\":0,\"name\":\"门票-老人\",\"paytime\":\"2018-08-20\",\"id\":\"B00188L2DRY6LLZK\"},{\"date\":\"2018-08-20 00:00:00\",\"isCheck\":1,\"ispay\":true,\"price\":60,\"isRefund\":0,\"name\":\"门票-学生\",\"paytime\":\"2018-08-20\",\"id\":\"B00188LG2RXLJ6RN\"},{\"date\":\"2018-08-20 00:00:00\",\"isCheck\":1,\"ispay\":true,\"price\":0,\"isRefund\":0,\"name\":\"门票-儿童免票\",\"paytime\":\"2018-08-20\",\"id\":\"B00188L42RG6DWZ7\"},{\"date\":\"2018-08-20 00:00:00\",\"isCheck\":1,\"ispay\":true,\"price\":50,\"isRefund\":0,\"name\":\"门票-儿童半价\",\"paytime\":\"2018-08-20\",\"id\":\"B00188LKWZLN7YRN\"},{\"date\":\"2018-08-20 00:00:00\",\"isCheck\":1,\"ispay\":true,\"price\":100,\"isRefund\":0,\"name\":\"门票-成人\",\"paytime\":\"2018-08-20\",\"id\":\"B00188LV6ZK9KJZJ\"},{\"date\":\"2018-08-20 00:00:00\",\"isCheck\":1,\"ispay\":true,\"price\":100,\"isRefund\":0,\"name\":\"门票-成人\",\"paytime\":\"2018-08-20\",\"id\":\"B00188L9VR56LYZK\"},{\"date\":\"2018-08-20 00:00:00\",\"isCheck\":1,\"ispay\":true,\"price\":50,\"isRefund\":0,\"name\":\"门票-儿童半价\",\"paytime\":\"2018-08-20\",\"id\":\"B00188L9QZ6X6X8K\"},{\"date\":\"2018-08-20 00:00:00\",\"isCheck\":1,\"ispay\":true,\"price\":0,\"isRefund\":0,\"name\":\"门票-老人\",\"paytime\":\"2018-08-20\",\"id\":\"B00188LYQ8QXYJRX\"},{\"date\":\"2018-08-20 00:00:00\",\"isCheck\":1,\"ispay\":true,\"price\":0,\"isRefund\":0,\"name\":\"门票-儿童免票\",\"paytime\":\"2018-08-20\",\"id\":\"B00188LVXR4LJ9RW\"}],\"payOrder\":\"A00188LV6RK91JRJ\"}}";
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
                ticketlistViewClick();
                listViewClick();
            }else {
                return;
            }
        }
    }
    private void listViewClick() {
        List<String> names = new ArrayList<>();
        List<String> name = new ArrayList<>();
        for (int i=0;i<isTicketModels.size();i++)
        {
            names.add(isTicketModels.get(i).getName());
            name.add(isTicketModels.get(i).getName());
        }
        for  ( int  i  =   0 ; i  < name.size()  -   1 ; i ++ )  {
            int count = 1;
            for  ( int  j  =   name.size()  -   1 ; j  >  i; j -- )  {
                if  (name.get(j).equals(name.get(i))) {
                    name.remove(j);
                }
            }
        }
        for(int i = 0; i < name.size(); i++) {
            int agesd = Collections.frequency(names, name.get(i));
            DetailsModel p = new DetailsModel();
            p.setTicketOne(name.get(i));
            p.setTicketTwo(agesd+"人");
            paymentlist.add(p);
        }
        myArrayAdapter = new PaymentAdapter<DetailsModel>
                (CheckActivity.this,paymentlist,R.layout.share_items);
        detailsLists.setAdapter(myArrayAdapter);
        detailsLists.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {

                // 进行点击事件后的逻辑操作
                myArrayAdapter.setSelectedItem(position);
                myArrayAdapter.notifyDataSetChanged();
            }

        });

    }
    private void ticketlistViewClick() {
        checkAdapter = new CheckAdapter<TicketModel>
                (CheckActivity.this,ticketModels,R.layout.check_items,this);
        paymentLists.setAdapter(checkAdapter);
        isTicketModels.clear();
        for(int i=0;i<checkAdapter.mChecked.size();i++){
            if(checkAdapter.mChecked.get(i)){
                isTicketModels.add(ticketModels.get(i));
            }
        }

    }

    private void returnedValue(String data,String ms) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(data);
            if (jsonObject.getInt("code")==200){
                if (ms.equals("query")) {
                    try {
                        JSONObject tickets = jsonObject.getJSONObject("data");
                        ticketModels.clear();
                        paymentlist.clear();
                        JSONArray jsonArray = (JSONArray) tickets.get("tickets");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                            TicketModel t = new TicketModel(jsonObject1.getString("id"), jsonObject1.getString("name"), jsonObject1.getString("paytime"), jsonObject1.getDouble("price"), jsonObject1.getBoolean("ispay"), jsonObject1.getInt("isCheck"), jsonObject1.getInt("isRefund"));
                            ticketModels.add(t);
                        }
                        if(ticketModels.size()>0) {
                            new paymentTask().execute();
                        }
                        JSONArray jsonArray1 = tickets.getJSONArray("PID");
                        JSONObject jsonObject1 = (JSONObject) jsonArray1.get(0);
                        if (jsonObject1 != null) {
                            guideModels.add(new GuideModel(jsonObject1.getString("name"), jsonObject1.getString("id"), ""));
                            handler.post(openGuide);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }if (ms.equals("check")) {
                    if (jsonObject.getString("msg").equals("success")) {
                        handler.post(closeGuide);
                        msg = "检票成功";
                        handler.post(toast);
                        startActivity(configuration.getIntent(CheckActivity.this, MenuActivity.class));
                        finish();
                    }else
                    {
                        msg = "检票失败";
                        handler.post(toast);
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
                msg ="登陆过期，请重新登录";
                handler.post(toast);
                startActivity(configuration.getIntent(CheckActivity.this,LoginActivity.class));
                finish();
            }else if (jsonObject.getInt("code")>0)
            {
                msg =jsonObject.getString("msg");
                handler.post(toast);
            }
        } catch (JSONException e) {
            msg ="服务器链接失败";
            handler.post(toast);
        }
    }

    Runnable openGuide = new Runnable() {
        @Override
        public void run() {
            guideID.setText(guideModels.get(0).getId());
            guideName.setText(guideModels.get(0).getName());
            guideLayout.setVisibility(View.VISIBLE);
        }
    };
    Runnable closeGuide = new Runnable() {
        @Override
        public void run() {
            guideID.setText("");
            guideName.setText("");
            guideLayout.setVisibility(View.INVISIBLE);
        }
    };
    private void backBtn() {
        startActivity(configuration.getIntent(CheckActivity.this,MenuActivity.class));
        finish();
    }

    private void successMethod() {
        isTicketModels.clear();
        for(int i=0;i<checkAdapter.mChecked.size();i++){
            if(checkAdapter.mChecked.get(i)){
                isTicketModels.add(ticketModels.get(i));
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONArray jsonArray = new JSONArray();
                if(isTicketModels.size()==0){
                    msg ="请选择票种";
                    handler.post(toast);
                }else{
                   for (int i=0;i<isTicketModels.size();i++)
                   {
                       try {
                           for (int j=0;j<ticketModels.size();j++)
                           {
                               if(isTicketModels.get(i).getID().equals(ticketModels.get(j).getID()))
                               {
                                   JSONObject jsonObject = new JSONObject(ticketModels.get(j).toString());
                                   jsonArray.put(jsonObject);
                               }
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
                }

                JSONObject data = new JSONObject();
                try {
                    data.put("payOrder",barcode);
                    data.put("tickets",jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
                map.put("channel","POS");
                map.put("machineID",baseModel.getMachineID());
                map.put("operatorId","123");
                map.put("data",data);
                resultCheckData = httpUtils.baseHttp(CheckActivity.this,baseModel,"chkOrder",map);
                returnedValue(resultCheckData,"check");
            }
        }).start();

    }

    private void scanMethod() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(CheckActivity.this, new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(CheckActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if ( resultCode >0) {
            Bundle bundle = data.getExtras();
            barcode = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            getData();
            editText.setText(barcode.toCharArray(),0,barcode.length());
        }
    }

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
                    msg ="请至权限中心打开本应用的相机访问权限";
                    handler.post(toast);
                    //Toast.makeText(CheckActivity.this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
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
