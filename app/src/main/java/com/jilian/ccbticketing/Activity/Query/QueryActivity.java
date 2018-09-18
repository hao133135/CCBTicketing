package com.jilian.ccbticketing.Activity.Query;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.jilian.ccbticketing.Activity.MenuActivity;
import com.jilian.ccbticketing.Activity.SetActivity;
import com.jilian.ccbticketing.Model.BaseModel;
import com.jilian.ccbticketing.R;
import com.jilian.ccbticketing.Uitls.clickUtils;

public class QueryActivity extends AppCompatActivity implements View.OnClickListener {
    private Button refundBtn,accountBtn;
    private ImageButton backBtn;
    private BaseModel baseModel =new BaseModel();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        init();
    }

    private void init() {
        refundBtn = findViewById(R.id.query_refund_btn);
        accountBtn = findViewById(R.id.query_account_btn);
        backBtn = findViewById(R.id.query_page_back_btn);
        refundBtn.setOnClickListener(this);
        accountBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.query_refund_btn:
                if(clickUtils.isFastClick()){
                    toRefund();
                }
                break;
            case R.id.query_account_btn:
                if(clickUtils.isFastClick()){
                    toAccount();
                }
                break;
            case R.id.query_page_back_btn:
                if(clickUtils.isFastClick()){
                    backBtn();
                }
                break;
        }
    }

    private void toRefund() {
        Intent i = new Intent(QueryActivity.this, RefundActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SetActivity.SER_KEY,baseModel);
        startActivity(i);
        finish();
    }

    private void toAccount() {
        Intent i = new Intent(QueryActivity.this, AccountActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SetActivity.SER_KEY,baseModel);
        startActivity(i);
        finish();
    }

    private void backBtn() {
        Intent i = new Intent(QueryActivity.this, MenuActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SetActivity.SER_KEY,baseModel);
        startActivity(i);
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
