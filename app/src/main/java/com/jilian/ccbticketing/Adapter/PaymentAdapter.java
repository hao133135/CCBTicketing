package com.jilian.ccbticketing.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jilian.ccbticketing.Model.DetailsModel;
import com.jilian.ccbticketing.R;

import java.util.List;

public class PaymentAdapter<T> extends BaseAdapter {
    private List<DetailsModel> detailsModelList;
    private int resource;   //item的布局
    private Context context;
    private LayoutInflater inflator;
    private int selectedItem = -1;
    private String coler;
    
    /**
     *
     * @param context mainActivity
     * @param detailsModelList   显示的数据
     * @param resource  一个Item的布局
     */
    public PaymentAdapter(Context context, List<DetailsModel> detailsModelList, int resource){
        this.context = context;
        this.detailsModelList = detailsModelList;
        this.resource = resource;
    }
    @Override
    public int getCount() {
        return detailsModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return detailsModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(resource, null);
            viewHolder = new PaymentAdapter.ViewHolder();

            //票种
            viewHolder.ticketNameTextView = convertView.findViewById(R.id.share_ticket_name);//为了减少开销，则只在第一页时调用findViewById
            //价格
            viewHolder.ticketPriceTextView = convertView.findViewById(R.id.share_ticket_price);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (PaymentAdapter.ViewHolder)convertView.getTag();

        }
        DetailsModel payment = detailsModelList.get(position);
        viewHolder.ticketNameTextView.setText(payment.getTicketOne());
        viewHolder.ticketPriceTextView.setText(payment.getTicketTwo());
        viewHolder.ticketPriceTextView.setTextColor(Color.RED);
        return convertView;
    }

    public void setSelectedItem(int selectedItem)
    {
        this.selectedItem = selectedItem;
    }
    class ViewHolder{
        private TextView ticketNameTextView,ticketPriceTextView;
    }

    /**
     * 局部刷新
     * @param view
     * @param itemIndex
     */
    public void updateView(View view, int itemIndex,int coler) {
        if (view == null) {
            return;
        }else if(itemIndex == selectedItem){

            return;
        }
    }
}
