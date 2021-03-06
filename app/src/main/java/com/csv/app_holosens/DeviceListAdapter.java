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

import com.csv.app_holosens.bean.DeviceResponseBean;

import java.util.List;

/**
 * @author CSV
 * @describe:
 * @date: 2021/2/23
 */
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {
    private List<DeviceResponseBean.DevicesBean> deviceLists;
    private Context mContext;

    public DeviceListAdapter(List<DeviceResponseBean.DevicesBean> deviceLists, Context mContext) {
        this.deviceLists = deviceLists;
        this.mContext = mContext;
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
        DeviceResponseBean.DevicesBean devicesBean = deviceLists.get(position);

        if (TextUtils.equals(devicesBean.getDevice_state(), "ONLINE")) {
            //设备在线
            if ("NVR".equals(devicesBean.getDevice_type())) {
                //设备类型:NVR
                holder.imageView.setImageResource(R.mipmap.ic_nvr_online);
            } else {
                //设备类型:其他
                holder.imageView.setImageResource(R.mipmap.ic_device_online);
            }
        } else {
            //设备离线
            if ("NVR".equals(devicesBean.getDevice_type())) {
                //设备类型:NVR
                holder.imageView.setImageResource(R.mipmap.ic_nvr_offline);
            } else {
                //设备类型:其他
                holder.imageView.setImageResource(R.mipmap.ic_device_offline);
            }
        }

        if (!TextUtils.isEmpty(devicesBean.getDevice_name())) {
            //判断设备名是否为null
            holder.textView.setText(devicesBean.getDevice_name());
        } else {
            //设备名为null时,设置设备id作为设备名
            holder.textView.setText(devicesBean.getDevice_id());
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
        return deviceLists != null ? deviceLists.size() : 0;
    }

    /**
     * 自定义方法(设置数据并刷新)
     *
     * @param deviceLists
     */
    public void setData(List<DeviceResponseBean.DevicesBean> deviceLists) {
        this.deviceLists = deviceLists;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.ll_item);
            textView = itemView.findViewById(R.id.tv_device_name);
            imageView = itemView.findViewById(R.id.device_state);
        }
    }

    //自定义条目点击事件(接口回调)

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        /**
         * 条目点击
         *
         * @param position 下标
         */
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
