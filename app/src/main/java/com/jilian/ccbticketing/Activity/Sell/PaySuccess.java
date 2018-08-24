package com.jilian.ccbticketing.Activity.Sell;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.jilian.ccbticketing.Uitls.ZXingUtils;
import com.socsi.smartposapi.printer.Align;
import com.socsi.smartposapi.printer.FontLattice;
import com.socsi.smartposapi.printer.FontType;
import com.socsi.smartposapi.printer.Printer2;
import com.socsi.smartposapi.printer.TextEntity;

import java.util.ArrayList;
import java.util.List;

public class PaySuccess extends AppCompatActivity implements View.OnClickListener {
    private Button printBtn,homeBtn;
    private Bitmap bitmap;
    private ImageButton backBtn;
    private ListView paymentLists;
    private SellListModel sellListModel;
    private List<SellModel> sellModels;
    private List<DetailsModel> paymentlist = new ArrayList<>();
    private TextView oderText;
    private Configuration configuration = new Configuration();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        init();
    }

    private void init() {
        sellListModel = (SellListModel) getIntent().getSerializableExtra("sellList");
        printBtn = findViewById(R.id.pay_success_print);
        homeBtn = findViewById(R.id.pay_success_return_home);
        backBtn = findViewById(R.id.sell_pay_result_page_back_btn);
        oderText = findViewById(R.id.pay_success_text);
        oderText.setText("订单详情："+sellListModel.getThirdId());
        paymentLists = findViewById(R.id.pay_success_listview);
        new paymentTask().execute();
        printBtn.setOnClickListener(this);
        homeBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pay_success_print:
                printMethod();
                break;
            case R.id.pay_success_return_home:
                returnMethod();
                break;
            case R.id.sell_pay_result_page_back_btn:
                backBtn();
                break;
        }
    }
    private void backBtn() {
        startActivity(configuration.getIntent(PaySuccess.this,MenuActivity.class));
        finish();
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
        sellModels =sellListModel.getSellModels();
        for (int i = 0 ;i<sellModels.size();i++)
        {
            DetailsModel t = new DetailsModel();
            t.setTicketOne(sellModels.get(i).getName()+"  x"+sellModels.get(i).getNum());
            t.setTicketTwo("￥"+String.valueOf(sellModels.get(i).getTotal()));
            paymentlist.add(t);
        }

        final DetailsAdapter<DetailsModel> myArrayAdapter = new DetailsAdapter<DetailsModel>
                (PaySuccess.this,paymentlist,R.layout.share_items);
        paymentLists.setAdapter(myArrayAdapter);
        paymentLists.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
                myArrayAdapter.setSelectedItem(position);
                myArrayAdapter.notifyDataSetChanged();

            }

        });
    }

    private void printMethod() {
        bitmap = ZXingUtils.createQRImage(sellListModel.getThirdId(), 300, 300);
        String text = "购票成功" +"\n\n"+"泸沽湖欢迎您！";
        String text3="";
        for(int i=0;i<sellListModel.getSellModels().size();i++)
        {
            text3 +=sellModels.get(i).getName()+"  x"+sellModels.get(i).getNum()+"人"+"\n";
        }
        String text1 = sellListModel.getThirdId()+"\n\n";
        String text2 = "购票时间  "+sellListModel.getPayTime()+"\n\n";
        boolean isBoldFont = true;
        FontLattice fontNum = FontLattice.TWENTY_FOUR;
        final Align align = Align.CENTER;
        Align left = Align.LEFT;
        final TextEntity entity = getTextEntity(text, isBoldFont, fontNum, left);
        final TextEntity entity1 = getTextEntity(text1, isBoldFont, fontNum, align);
        final TextEntity entity2 = getTextEntity(text2, isBoldFont, fontNum, align);
        final TextEntity entity3 = getTextEntity(text3, isBoldFont, FontLattice.SIXTEEN, left);
        new Thread() {
            @Override
            public void run() {
                super.run();
                Printer2.getInstance().appendTextEntity2(entity);
                Printer2.getInstance().appendTextEntity2(entity3);
                Printer2.getInstance().setLineSpace(2);
                Printer2.getInstance().appendImage(bitmap,align);
                Printer2.getInstance().appendTextEntity2(entity1);
                Printer2.getInstance().setLineSpace(3);
                Printer2.getInstance().appendTextEntity2(entity2);
                Printer2.getInstance().startPrint();
                Printer2.getInstance().takePaper((byte) 0x00, 2);
            }
        }.start();
    }
    @NonNull
    private TextEntity getTextEntity(String text, boolean isBoldFont, FontLattice fontNum, Align align) {
        final TextEntity entity = new TextEntity();
        entity.fontsize = fontNum;
        entity.isBoldFont = isBoldFont;
        entity.chnFontType = FontType.SIMSUM;
        entity.engFontType = FontType.SIMSUM;
        entity.text = text  + "\n";
        entity.align = align;
        return entity;
    }

    private void returnMethod() {
        startActivity(configuration.getIntent(PaySuccess.this,MenuActivity.class));
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
