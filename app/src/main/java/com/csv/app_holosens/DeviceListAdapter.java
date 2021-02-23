package com.csv.app_holosens;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csv.app_holosens.bean.DeviceResponseBean;

import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.ViewHolder;

/**
 * @author CSV
 * @describe:
 * @date: 2021/2/23
 */
public class DeviceListAdapter extends RecyclerView.Adapter {

    List<DeviceResponseBean.DevicesBean> deviceLists;


    public DeviceListAdapter(List<DeviceResponseBean.DevicesBean> deviceLists) {
        this.deviceLists = deviceLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_device_list, parent, false);
        ListViewHolder listViewHolder = new ListViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private class ListViewHolder {
        public ListViewHolder(View view) {

        }
    }
}
