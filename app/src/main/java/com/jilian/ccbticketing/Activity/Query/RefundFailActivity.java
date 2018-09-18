package com.jilian.ccbticketing.Activity.Query;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jilian.ccbticketing.Activity.MenuActivity;
import com.jilian.ccbticketing.Adapter.DetailsAdapter;
import com.jilian.ccbticketing.Model.BaseModel;
import com.jilian.ccbticketing.Model.DetailsModel;
import com.jilian.ccbticketing.Model.QryorderModel;
import com.jilian.ccbticketing.R;
import com.jilian.ccbticketing.Uitls.Configuration;
import com.jilian.ccbticketing.Uitls.clickUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RefundFailActivity extends AppCompatActivity implements View.OnClickListener {
    private Button homeBtn;
    private ImageButton backBtn;
    private ListView detailsLists;
    private List<DetailsModel> paymentlist = new ArrayList<>();
    private QryorderModel qryorderModel;
    private BaseModel baseModel = new BaseModel();
    private TextView oder,message;
    private Configuration configuration = new Configuration();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_fail);
        init();
    }

    private void init() {
        Bundle bundle =this.getIntent().getExtras();
        qryorderModel = (QryorderModel) getIntent().getSerializableExtra("qry");
        homeBtn = findViewById(R.id.refund_fail_home_btn);
        backBtn = findViewById(R.id.refund_result_page_back_btn);
        detailsLists = findViewById(R.id.refund_fail_listview);
        message = findViewById(R.id.refund_fail_message);
        oder = findViewById(R.id.refund_fail_payOrder);
        message.setText(String.valueOf(bundle.get("Message")));
        homeBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        new paymentTask().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refund_fail_home_btn:
                if(clickUtils.isFastClick()){
                    homeMethod();
                }
                break;
            case R.id.refund_result_page_back_btn:
                if(clickUtils.isFastClick()){
                    backBtn();
                }
                break;
        }
    }

    class paymentTask extends AsyncTask<Object, Void, List<DetailsModel>> {

        @Override
        protected List<DetailsModel> doInBackground(Object... objects) {
            List<String> names = new ArrayList<>();
            List<String> name = new ArrayList<>();
            for (int i=0;i<qryorderModel.getTicketModels().size();i++)
            {
                names.add(qryorderModel.getTicketModels().get(i).getName());
                name.add(qryorderModel.getTicketModels().get(i).getName());
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
        final DetailsAdapter<DetailsModel> myArrayAdapter = new DetailsAdapter<DetailsModel>
                (RefundFailActivity.this,paymentlist,R.layout.share_items);
        detailsLists.setAdapter(myArrayAdapter);
        detailsLists.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
                myArrayAdapter.setSelectedItem(position);
                myArrayAdapter.notifyDataSetChanged();
            }

        });

    }
    private void backBtn() {
        startActivity(configuration.getIntent(RefundFailActivity.this,RefundActivity.class));
        finish();
    }
    private void homeMethod() {
        startActivity(configuration.getIntent(RefundFailActivity.this,MenuActivity.class));
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
