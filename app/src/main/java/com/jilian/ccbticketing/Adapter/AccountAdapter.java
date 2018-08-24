package com.jilian.ccbticketing.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jilian.ccbticketing.Model.CheckParameterModel;
import com.jilian.ccbticketing.R;

import java.util.List;

public class AccountAdapter<T> extends BaseAdapter {
    private List<CheckParameterModel> checkParameterModels;
    private int resource;   //item的布局
    private Context context;
    private LayoutInflater inflator;
    private int selectedItem = -1;

    /**
     *
     * @param context mainActivity
     * @param checkParameterModels   显示的数据
     * @param resource  一个Item的布局
     */
    public AccountAdapter(Context context, List<CheckParameterModel> checkParameterModels, int resource){
        this.context = context;
        this.checkParameterModels = checkParameterModels;
        this.resource = resource;
    }
    @Override
    public int getCount() {
        return checkParameterModels.size();
    }

    @Override
    public Object getItem(int position) {
        return checkParameterModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView==null){
            inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(resource, null);
            viewHolder = new AccountAdapter.ViewHolder();

            //收款方式
            viewHolder.methodTextView = convertView.findViewById(R.id.account_pay_method_textview);//为了减少开销，则只在第一页时调用findViewById
            //收款额
            viewHolder.receivablesTextView = convertView.findViewById(R.id.account_pay_receivables_textview);
            //退款额
            viewHolder.refundTextView = convertView.findViewById(R.id.account_pay_refund_textview);
            //实收
            viewHolder.receiptsTextView = convertView.findViewById(R.id.account_pay_receipts_textview);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (AccountAdapter.ViewHolder)convertView.getTag();

        }
        CheckParameterModel payment = checkParameterModels.get(position);
        viewHolder.methodTextView.setText(payment.getParam1());
        viewHolder.receivablesTextView.setText(payment.getParam2());
        viewHolder.refundTextView.setText(payment.getParam3());
        viewHolder.receiptsTextView.setText(payment.getParam4());
        return convertView;
    }
    public void setSelectedItem(int selectedItem)
    {
        this.selectedItem = selectedItem;
    }
    class ViewHolder{
        private TextView methodTextView,receivablesTextView,refundTextView,receiptsTextView;
    }
}
