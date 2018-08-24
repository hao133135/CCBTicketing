package com.jilian.ccbticketing.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jilian.ccbticketing.Model.TicketModel;
import com.jilian.ccbticketing.R;

import java.util.List;


public class TicketAdapter<T> extends BaseAdapter  {
    private List<TicketModel> ticketList;
    private int resource;   //item的布局
    private Context context;
    private LayoutInflater inflator;
    private int selectedItem = -1;
    /**
     *
     * @param context mainActivity
     * @param ticketList   显示的数据
     * @param resource  一个Item的布局
     */
    public TicketAdapter(Context context, List<TicketModel> ticketList, int resource){
        this.context = context;
        this.ticketList = ticketList;
        this.resource = resource;
    }
    /*
     * 获得数据总数
     * */
    @Override
    public int getCount() {
        return ticketList.size();
    }
    /*
     * 根据索引为position的数据
     * */
    @Override
    public Object getItem(int position) {
        return ticketList.get(position);
    }
    /*
     * 根据索引值获得Item的Id
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }
    /*
     *通过索引值position将数据映射到视图
     *convertView具有缓存功能，在第一页时为null，在第二第三....页时不为null
     * */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(resource, null);
            viewHolder = new ViewHolder();

            //票种
            viewHolder.ticketNameTextView = convertView.findViewById(R.id.share_ticket_name);//为了减少开销，则只在第一页时调用findViewById
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();

        }
        TicketModel ticket = ticketList.get(position);
        viewHolder.ticketNameTextView.setText(ticket.getName());
        return convertView;
    }
    public void setSelectedItem(int selectedItem)
    {
        this.selectedItem = selectedItem;
    }
    class ViewHolder{
        private TextView ticketNameTextView;
    }
    /**
     * 局部刷新
     * @param view
     * @param itemIndex
     */
    public void updateView(View view, int itemIndex) {
        if (view == null) {
            return;
        }else if(itemIndex == selectedItem){
            return;
        }
    }
}
