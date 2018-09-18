package com.jilian.ccbticketing.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.jilian.ccbticketing.R;
import com.jilian.ccbticketing.Uitls.Configuration;
import com.jilian.ccbticketing.Uitls.clickUtils;

public class SetMenuActivity  extends AppCompatActivity implements View.OnClickListener {
    private Button setBtn,exBtn;
    private ImageButton backBtn;
    private Configuration configuration = new Configuration();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_menu);
        init();
    }

    private void init() {
        setBtn = findViewById(R.id.query_set_btn);
        exBtn = findViewById(R.id.query_ex_btn);
        backBtn=findViewById(R.id.set_page_back_btn);
        setBtn.setOnClickListener(this);
        exBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.query_set_btn:
                if(clickUtils.isFastClick()) {
                    toSetPage();
                }
                break;
            case R.id.query_ex_btn:
                if(clickUtils.isFastClick()) {
                    EXIT();
                }
                break;
            case R.id.set_page_back_btn:
                if(clickUtils.isFastClick()){
                    backBtn();
                }
                break;
        }
    }

    private void toSetPage() {
        startActivity(configuration.getIntent(SetMenuActivity.this,SetActivity.class));
        finish();
    }

    private void EXIT() {
        SharedPreferences sp = getSharedPreferences("config", 0);
        SharedPreferences.Editor editor = sp.edit();
        //把数据进行保存
        editor.putString("token", "");
        //提交数据
        editor.commit();
        startActivity(configuration.getIntent(SetMenuActivity.this,LoginActivity.class));
        finish();
    }
    private void backBtn() {
        startActivity(configuration.getIntent(SetMenuActivity.this,MenuActivity.class));
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
