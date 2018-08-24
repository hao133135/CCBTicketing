package com.jilian.ccbticketing.Activity.Sell;

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
import com.jilian.ccbticketing.Model.SellListModel;
import com.jilian.ccbticketing.Model.SellModel;
import com.jilian.ccbticketing.R;
import com.jilian.ccbticketing.Uitls.Configuration;

import java.util.ArrayList;
import java.util.List;

public class PayFail extends AppCompatActivity implements View.OnClickListener {
    private ImageButton backBtn;
    private Button returnBtn;
    private ListView paymentLists;
    private List<DetailsModel> paymentlist = new ArrayList<>();
    private String message;
    private TextView messageText,oderText;
    private SellListModel sellListModel;
    private List<SellModel> sellModels;
    private Configuration configuration = new Configuration();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_fail);
        init();

    }

    private void init() {
        Bundle bundle =this.getIntent().getExtras();
        sellListModel = (SellListModel) getIntent().getSerializableExtra("sellList");
        message = String.valueOf(bundle.get("Message"));
        returnBtn = findViewById(R.id.pay_fail_return_btn);
        backBtn = findViewById(R.id.sell_pay_result_page_back_btn);
        paymentLists = findViewById(R.id.pay_fail_listview);
        messageText = findViewById(R.id.pay_fail_message);
        oderText = findViewById(R.id.pay_fail_oder);
        oderText.setText("订单详情：");
        messageText.setText(message);
        new paymentTask().execute();
        returnBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.pay_fail_return_btn:
                returnMethod();
                break;
            case R.id.sell_pay_result_page_back_btn:
                backBtn();
                break;
        }
    }
    private void backBtn() {
        startActivity(configuration.getIntent(PayFail.this,SellActivity.class));
        finish();
    }
    private void returnMethod() {
        startActivity(configuration.getIntent(PayFail.this,MenuActivity.class));
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
        sellModels =sellListModel.getSellModels();
        for (int i = 0 ;i<sellModels.size();i++)
        {
            t.setTicketOne(sellModels.get(i).getName()+"  x"+sellModels.get(i).getNum());
            t.setTicketTwo("￥"+String.valueOf(sellModels.get(i).getTotal()));
            paymentlist.add(t);
        }

        final DetailsAdapter<DetailsModel> myArrayAdapter = new DetailsAdapter<DetailsModel>
                (PayFail.this,paymentlist,R.layout.share_items);
        paymentLists.setAdapter(myArrayAdapter);
        paymentLists.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
                myArrayAdapter.setSelectedItem(position);
                myArrayAdapter.notifyDataSetChanged();
            }

        });
    }

}
