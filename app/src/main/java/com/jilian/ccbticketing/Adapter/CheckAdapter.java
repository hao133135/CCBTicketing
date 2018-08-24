package com.jilian.ccbticketing.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jilian.ccbticketing.Model.TicketModel;
import com.jilian.ccbticketing.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckAdapter<T> extends BaseAdapter {
    private int resource;   //item的布局
    private Context context;
    private LayoutInflater inflator;
    private int selectedItem = -1;
    public List<Boolean> mChecked;
    public List<TicketModel> ticketModels;
    private CallBack mcallBack;
    public Map<Integer,Boolean> map=new HashMap<>();// 存放已被选中的CheckBox
    public CheckAdapter(Context context, List<TicketModel> list, int check_items,CallBack mcallBack){
        this.context = context;
        this.ticketModels = list;
        this.resource = check_items;
        ticketModels = new ArrayList<>();
        ticketModels = list;
        this.mcallBack = mcallBack;
        mChecked = new ArrayList<>();
        for(int i=0;i<list.size();i++)
        {
            if (list.get(i).getIsCheck()==0&&list.get(i).getIsRefund()==0)
            {
                mChecked.add(i,true);
            }else {
                mChecked.add(i,false);
            }
        }
    }

    @Override
    public int getCount() {
        return ticketModels==null?0:ticketModels.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if(convertView==null){
            inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(resource, null);
            viewHolder = new CheckAdapter.ViewHolder();
            viewHolder.id = convertView.findViewById(R.id.check_id_items);
            //序号
            viewHolder.sequenceTextView = convertView.findViewById(R.id.check_sequence_items);//为了减少开销，则只在第一页时调用findViewById
            //票种
            viewHolder.ticketTextView = convertView.findViewById(R.id.check_ticket_items);
            //状态
            viewHolder.stateTextView = convertView.findViewById(R.id.check_state_items);
            viewHolder.checkBox = convertView.findViewById(R.id.check_state_isCheck);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (CheckAdapter.ViewHolder)convertView.getTag();

        }
        final int i = position;
        final TicketModel payment = ticketModels.get(position);
        viewHolder.id.setText(payment.getID());
        viewHolder.sequenceTextView.setText(""+(i+1));
        viewHolder.ticketTextView.setText(payment.getName());
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox)v;
                mChecked.set(i, checkBox.isChecked());
                //点击切换checkbook的状态
                mcallBack.onClick(v);
            }
        });
        viewHolder.checkBox.setTag(position);
        if(payment.getIsCheck()==0&&payment.getIsRefund()==0){
            viewHolder.stateTextView.setText("未检票");
            viewHolder.stateTextView.setTextColor(Color.GREEN);
        }else {
            viewHolder.stateTextView.setText("已检票");
            viewHolder.stateTextView.setTextColor(Color.RED);
            viewHolder.checkBox.setClickable(mChecked.get(i));
        }
        viewHolder.checkBox.setChecked(mChecked.get(i));

        // viewHolder.checkBox.setChecked((ticketModels.get(position) == null ? false : true));
        return convertView;
    }
    public void setSelectedItem(int selectedItem)
    {
        this.selectedItem = selectedItem;
    }

    /**接口回调 监听复选框*/
    public interface CallBack {
        public void onClick(View v) ;

    }
    class ViewHolder{
        private TextView sequenceTextView,ticketTextView,stateTextView,id;
        private CheckBox checkBox;
    }
}
