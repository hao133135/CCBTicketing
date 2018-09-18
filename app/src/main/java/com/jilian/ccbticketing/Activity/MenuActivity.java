package com.jilian.ccbticketing.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.jilian.ccbticketing.Activity.Check.CheckActivity;
import com.jilian.ccbticketing.Activity.Query.QueryActivity;
import com.jilian.ccbticketing.Activity.Sell.SellActivity;
import com.jilian.ccbticketing.R;
import com.jilian.ccbticketing.Uitls.Configuration;
import com.jilian.ccbticketing.Uitls.clickUtils;

public class MenuActivity  extends AppCompatActivity implements View.OnClickListener {
    private Button sellBtn,checkBtn,queryBtn,setBtn,sBtn;
    private Configuration configuration = new Configuration();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        init();
    }

    private void init() {
        sellBtn=findViewById(R.id.ticketing_menu_sell_btn);
        checkBtn=findViewById(R.id.ticketing_menu_check_btn);
        queryBtn=findViewById(R.id.ticketing_menu_query_btn);
        setBtn=findViewById(R.id.ticketing_menu_set_btn);
        sBtn = findViewById(R.id.sell_login_set_btn);
        sBtn.setVisibility(View.GONE);
        sellBtn.setOnClickListener(this);
        checkBtn.setOnClickListener(this);
        queryBtn.setOnClickListener(this);
        setBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ticketing_menu_sell_btn:
                if (clickUtils.isFastClick()) {
                    toSellActivity();
                }
                break;
            case R.id.ticketing_menu_check_btn:
                if (clickUtils.isFastClick()) {
                    toCheckActivity();
                }
                break;
            case R.id.ticketing_menu_query_btn:
                if(clickUtils.isFastClick()) {
                    toQueryActivity();
                }
                break;
            case R.id.ticketing_menu_set_btn:
                if(clickUtils.isFastClick()) {
                    toSetActivity();
                }
                break;
        }
    }

    private void toSellActivity() {
        startActivity(configuration.getIntent(MenuActivity.this,SellActivity.class));
        finish();

    }

    private void toCheckActivity() {
        startActivity(configuration.getIntent(MenuActivity.this,CheckActivity.class));
        finish();
    }

    private void toQueryActivity() {
        startActivity(configuration.getIntent(MenuActivity.this,QueryActivity.class));
        finish();
    }

    private void toSetActivity() {
        startActivity(configuration.getIntent(MenuActivity.this,SetMenuActivity.class));
        finish();
    }
    private void backBtn() {
        startActivity(configuration.getIntent(MenuActivity.this,LoginActivity.class));
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
