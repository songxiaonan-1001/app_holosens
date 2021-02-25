package com.csv.app_holosens;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csv.app_holosens.bean.ChannelResponseBean;

import java.util.List;

/**
 * @author CSV
 * @describe: 通道列表适配器
 * @date: 2021/2/24
 */
public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.ViewHolder> {
    private Context mContext;
    private List<ChannelResponseBean.ChannelsBean> channelLists;

    public ChannelListAdapter(Context mContext, List<ChannelResponseBean.ChannelsBean> channelLists) {
        this.mContext = mContext;
        this.channelLists = channelLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_device_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChannelResponseBean.ChannelsBean channelsBean = channelLists.get(position);

        if (TextUtils.isEmpty(channelsBean.getChannel_name())) {
            holder.textView.setText(channelsBean.getChannel_id());
        } else {
            holder.textView.setText(channelsBean.getChannel_name());
        }

        if ("ONLINE".equals(channelsBean.getChannel_state())) {
            holder.imageView.setImageResource(R.mipmap.ic_device_online);
        } else {
            holder.imageView.setImageResource(R.mipmap.ic_device_offline);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return channelLists != null ? channelLists.size() : 0;
    }

    /**
     * 设置数据并刷新
     *
     * @param channels
     */
    public void setData(List<ChannelResponseBean.ChannelsBean> channels) {
        this.channelLists = channels;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView textView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.ll_item);
            textView = itemView.findViewById(R.id.tv_device_name);
            imageView = itemView.findViewById(R.id.device_state);
        }
    }

    //自定义条目点击事件(接口回调)

    private DeviceListAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        /**
         * 条目点击
         *
         * @param position 下标
         */
        void onItemClick(int position);
    }

    public void setOnItemClickListener(DeviceListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
