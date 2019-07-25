package com.ly.wifitest;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * @创建者 ly
 * @创建时间 2019/7/24
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class WifiAdapter extends BaseAdapter {

    private Context mContext;
    private List<ScanResult> scanResults;

    public WifiAdapter(Context mContext,List<ScanResult> scanResults) {
        this.scanResults = scanResults;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return scanResults.size();
    }

    @Override
    public Object getItem(int position) {
        return scanResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_wifi_list,null);
            holder.tvSsid = convertView.findViewById(R.id.tv_ssid);
            holder.tvBssid = convertView.findViewById(R.id.tv_bssid);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvSsid.setText(scanResults.get(position).SSID);
        holder.tvBssid.setText(scanResults.get(position).BSSID);
        return convertView;
    }

    class Holder{
        public TextView tvSsid;
        public TextView tvBssid;

    }
}
