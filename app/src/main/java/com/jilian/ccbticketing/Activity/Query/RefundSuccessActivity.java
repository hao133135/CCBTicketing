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
import com.jilian.ccbticketing.Model.DetailsModel;
import com.jilian.ccbticketing.Model.QryorderModel;
import com.jilian.ccbticketing.R;
import com.jilian.ccbticketing.Uitls.Configuration;

import java.util.ArrayList;
import java.util.List;

public class RefundSuccessActivity extends AppCompatActivity implements View.OnClickListener {
    private Button homeBtn;
    private ImageButton backBtn;
    private ListView detailsLists;
    private List<DetailsModel> paymentlist = new ArrayList<>();
    private TextView oder;
    private QryorderModel qryorderModel;
    private Configuration configuration = new Configuration();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_success);
        init();
    }

    private void init() {
        qryorderModel = (QryorderModel) getIntent().getSerializableExtra("qry");
        homeBtn = findViewById(R.id.refund_success_home_btn);
        backBtn = findViewById(R.id.refund_result_page_back_btn);
        detailsLists = findViewById(R.id.refund_success_listview);
        oder = findViewById(R.id.refund_success_payOder);
        oder.setText("退款详情："+qryorderModel.getPayOrder());
        homeBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        new paymentTask().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refund_success_home_btn:
                homeMethod();
                break;
            case R.id.refund_result_page_back_btn:
                backBtn();
                break;
        }
    }

    class paymentTask extends AsyncTask<Object, Void, List<DetailsModel>> {

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
        DetailsModel t = new DetailsModel();
        t.setTicketOne("退款：");
        Double count= 0.0;
        for(int i=0;i<qryorderModel.getTicketModels().size();i++)
        {
            count+=qryorderModel.getTicketModels().get(i).getPrice();
        }
        t.setTicketTwo("￥"+count.toString());
        paymentlist.add(t);

        final DetailsAdapter<DetailsModel> myArrayAdapter = new DetailsAdapter<DetailsModel>
                (RefundSuccessActivity.this,paymentlist,R.layout.share_items);
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
        startActivity(configuration.getIntent(RefundSuccessActivity.this,RefundActivity.class));
        finish();
    }
    private void homeMethod() {
        startActivity(configuration.getIntent(RefundSuccessActivity.this,MenuActivity.class));
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
