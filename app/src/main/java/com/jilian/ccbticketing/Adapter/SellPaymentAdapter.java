package com.jilian.ccbticketing.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jilian.ccbticketing.Model.DetailsModel;
import com.jilian.ccbticketing.Model.SellModel;
import com.jilian.ccbticketing.R;

import java.util.List;

public class SellPaymentAdapter<T> extends BaseAdapter implements View.OnClickListener {
    private List<SellModel> sellPaymentList;
    private int resource;   //item的布局
    private Context context;
    private LayoutInflater inflator;
    private int selectedItem = -1,ticketNum;
    private DetailsModel detailsModel = new DetailsModel();
    private boolean isSelect;
    private static final String TAG = "ContentAdapter";
    private List<String> mContentList;
    private LayoutInflater mInflater;
    private Callback mCallback;//接口

    @Override
    public void onClick(View v) {
        //将点击事件传递出来
        mCallback.click(v);
    }

    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     * @author
     */
    public interface Callback {
        public void click(View v);
    }

   /* public SellPaymentAdapter(Context context, LayoutInflater inflator, Callback mCallback) {
        this.context = context;
        this.inflator = inflator;
        this.mCallback = mCallback;
    }*/

    /**
     *
     * @param context mainActivity
     * @param paymentModelList   显示的数据
     * @param resource  一个Item的布局
     */
    public SellPaymentAdapter(Context context, List<SellModel> paymentModelList, int resource, Callback mCallback){
        this.context = context;
        this.sellPaymentList = paymentModelList;
        this.resource = resource;
        this.mCallback = mCallback;
    }
    @Override
    public int getCount() {
        return sellPaymentList.size();
    }

    @Override
    public Object getItem(int position) {
        return sellPaymentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){
            inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(resource, null);
            viewHolder = new SellPaymentAdapter.ViewHolder();
            viewHolder.checkBox = convertView.findViewById(R.id.sell_check_box);
            //票种
            viewHolder.ticketNameTextView = convertView.findViewById(R.id.pay_ticket_name);//为了减少开销，则只在第一页时调用findViewById
            //价格
            viewHolder.ticketPriceTextView = convertView.findViewById(R.id.pay_ticket_price);
            //票数
            viewHolder.numEdit = convertView.findViewById(R.id.pay_ticket_number);
            //增加
            viewHolder.pushBtn = convertView.findViewById(R.id.pay_ticket_plus);
            //减少
            viewHolder.reduceBtn = convertView.findViewById(R.id.pay_ticket_reduce);
            viewHolder.id = convertView.findViewById(R.id.pay_ticket_id);
            viewHolder.realprice = convertView.findViewById(R.id.pay_ticket_realprice);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (SellPaymentAdapter.ViewHolder)convertView.getTag();
        }
        SellModel payment = sellPaymentList.get(position);
        viewHolder.checkBox.setChecked(payment.isSelect());
        viewHolder.ticketNameTextView.setText(payment.getName());
        viewHolder.id.setText(payment.getId());
        viewHolder.realprice.setText(String.valueOf(payment.getRealPrice()));
        viewHolder.ticketPriceTextView.setText("￥ "+String.valueOf(payment.getRealPrice()));
        viewHolder.pushBtn.setOnClickListener(this);//将需要设置点击事件的View在这里设置
        viewHolder.pushBtn.setTag(position);//把点击的位置信息也要打包
        viewHolder.numEdit.setText(String.valueOf(payment.getNum()));
        viewHolder.reduceBtn.setOnClickListener(this);//将需要设置点击事件的View在这里设置
        viewHolder.reduceBtn.setTag(position);//把点击的位置信息也要打包
        return convertView;
    }

    public void setSelectedItem(int selectedItem)
    {
        this.selectedItem = selectedItem;
    }
    class ViewHolder{
        private TextView ticketNameTextView,ticketPriceTextView,id,realprice,numEdit;
        private ImageView pushBtn,reduceBtn;
        private CheckBox checkBox;
    }

}
