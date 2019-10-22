package com.ly.wifitest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_scan)
    Button btnScan;
    @BindView(R.id.btn_stop)
    Button btnStop;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.lv_list)
    ListView lvList;

    private List<ScanResult> scanResultsCopy = new ArrayList<>();
    private WifiAdapter adapter;
    private WifiManager mWifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        scanResultsCopy.addAll(getWifiList(this));
        adapter = new WifiAdapter(this,scanResultsCopy);
        lvList.setAdapter(adapter);
        // 取得WifiManager对象
        mWifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

    }


    @OnClick({R.id.btn_scan, R.id.btn_stop, R.id.btn_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                scanResultsCopy.clear();
                scanResultsCopy.addAll(getWifiList(this));
                filterScanResult(scanResultsCopy);
                adapter.notifyDataSetChanged();
                checkState(this);
                break;
            case R.id.btn_stop:
                setWifiEnabled(false);
                break;
            case R.id.btn_start:
                setWifiEnabled(true);

                Intent intent= getIntent();
                if(intent != null) {
                    Bundle b = intent.getExtras();
                    String value=b.getString("arge1"); //从A传过来的参数
                    Toast.makeText(this,value,Toast.LENGTH_LONG);
                    b.putString("str1","return string");
                    setResult(RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                }
                break;
        }
    }

    // 检查当前WIFI状态
    public void checkState(Context context) {
        if (mWifiManager.getWifiState() == 0) {
            Toast.makeText(context,"Wifi正在关闭", Toast.LENGTH_SHORT).show();
        } else if (mWifiManager.getWifiState() == 1) {
            Toast.makeText(context,"Wifi已经关闭", Toast.LENGTH_SHORT).show();
        } else if (mWifiManager.getWifiState() == 2) {
            Toast.makeText(context,"Wifi正在开启", Toast.LENGTH_SHORT).show();
        } else if (mWifiManager.getWifiState() == 3) {
            Toast.makeText(context,"Wifi已经开启", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"没有获取到WiFi状态", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开或关闭wifi
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>}</p>
     *
     * @param enabled {@code true}: 打开<br>{@code false}: 关闭
     */
    public void setWifiEnabled(final boolean enabled) {
        @SuppressLint("WifiManagerLeak")
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (enabled) {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
        } else {
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
            }
        }
    }

    /**
     *
     * 获取WIFI列表
     * <p>需要权限{@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/> <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>}</p>
     * <p>注意Android6.0上需要主动申请定位权限，并且打开定位开关</p>
     *
     * @param context 上下文
     * @return wifi列表
     */
    public List<ScanResult> getWifiList(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> scanResults = wm.getScanResults();

        Collections.sort(scanResults, new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult scanResult1, ScanResult scanResult2) {
                return scanResult2.level - scanResult1.level;
            }
        });
        return scanResults;
    }

    /**
     * 以 SSID 为关键字，过滤掉信号弱的选项
     * @param list
     * @return
     */
    public static List<ScanResult> filterScanResult(final List<ScanResult> list) {
        LinkedHashMap<String, ScanResult> linkedMap = new LinkedHashMap<>(list.size());
        for (ScanResult rst : list) {
            if (linkedMap.containsKey(rst.SSID)) {
                if (rst.level > linkedMap.get(rst.SSID).level) {
                    linkedMap.put(rst.SSID, rst);
                }
                continue;
            }
            linkedMap.put(rst.SSID, rst);
        }
        list.clear();
        list.addAll(linkedMap.values());
        return list;
    }


}
