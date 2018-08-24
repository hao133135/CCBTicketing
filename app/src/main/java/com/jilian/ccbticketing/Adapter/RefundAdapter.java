package com.jilian.ccbticketing.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jilian.ccbticketing.Model.TicketModel;
import com.jilian.ccbticketing.R;

import java.util.List;

public class RefundAdapter<T> extends BaseAdapter implements View.OnClickListener {
    private List<TicketModel> ticketModels;
    private int resource;   //item的布局
    private Context context;
    private LayoutInflater inflator;
    private int selectedItem = -1;
    private Callback mCallback;//接口

    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     * @author
     */
    public interface Callback {
        public void click(View v);
    }
    /**
     *
     * @param context mainActivity
     * @param ticketModels   显示的数据
     * @param resource  一个Item的布局
     */
    public RefundAdapter(Context context, List<TicketModel> ticketModels, int resource){
        this.context = context;
        this.ticketModels = ticketModels;
        this.resource = resource;
    }
    @Override
    public int getCount() {
        return ticketModels.size();
    }

    @Override
    public Object getItem(int position) {
        return ticketModels.get(position);
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
            viewHolder = new RefundAdapter.ViewHolder();

            //序号
            viewHolder.sequenceTextView = convertView.findViewById(R.id.check_sequence_items);//为了减少开销，则只在第一页时调用findViewById
            //票种
            viewHolder.ticketTextView = convertView.findViewById(R.id.check_ticket_items);
            //状态
            viewHolder.stateTextView = convertView.findViewById(R.id.check_state_items);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (RefundAdapter.ViewHolder)convertView.getTag();

        }
        int i = position;
        TicketModel payment = ticketModels.get(position);
        viewHolder.sequenceTextView.setText(""+(i+1));
        viewHolder.ticketTextView.setText(payment.getName());

        if (payment.getIsRefund()==0)
        {
            viewHolder.stateTextView.setText("未退票");
            viewHolder.stateTextView.setTextColor(Color.GREEN);

        }else
        {
            viewHolder.stateTextView.setText("已退票");
            viewHolder.stateTextView.setTextColor(Color.RED);
        }
        return convertView;
    }
    public void setSelectedItem(int selectedItem)
    {
        this.selectedItem = selectedItem;
    }

    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }

    class ViewHolder{
        private TextView sequenceTextView,ticketTextView,stateTextView;
    }
}
