package com.example.talk8.conn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WifiActivity extends AppCompatActivity implements View.OnClickListener,
        WifiP2pManager.ChannelListener,DeviceListFragment.DeviceActionListener {
    private static final String TAG = "WifiActivity xxl";

    private Button mButtonOpenWifi;
    private Button mButtonSearchDevice;

    private boolean isWifiP2pEnabled = false;
    private boolean retryChannel = false;

    IntentFilter mIntentFilter;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    WiFiDirectBroadcastReceiver mWiFiDirectBroadcastReceiver ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        Log.i(TAG, "onCreate: ");
        mButtonOpenWifi = (Button)findViewById(R.id.bt_connect_wifi) ;
        mButtonSearchDevice = (Button)findViewById(R.id.bt_search_device) ;
        mButtonOpenWifi.setOnClickListener(this);
        mButtonSearchDevice.setOnClickListener(this);

        mManager = (WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this,getMainLooper(),null);
        mWiFiDirectBroadcastReceiver = new WiFiDirectBroadcastReceiver(mManager,mChannel,this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: register BroadcastReceiver");
        registerReceiver(mWiFiDirectBroadcastReceiver,mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: unRegister BroadcastReceiver");
        unregisterReceiver(mWiFiDirectBroadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //开始搜索WIFI设备
            case R.id.bt_search_device:
                Log.i(TAG, "onClick: Connect WIFI Device.");
                if (!isWifiP2pEnabled) {
                    Toast.makeText(WifiActivity.this,"Wifi is not Enable", Toast.LENGTH_SHORT).show();
                }
                //静态加载Fragment
                final DeviceListFragment fragment = (DeviceListFragment)getSupportFragmentManager().findFragmentById(R.id.frag_device_list) ;
                fragment.onInitiateDiscovery();
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(WifiActivity.this, "Discovery Device", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        Toast.makeText(WifiActivity.this, "Discovery Failed : " + reasonCode,Toast.LENGTH_SHORT).show();
                    }
                });
               break;
               //如果没有打开WIFI，跳转到设置界面中打开wifi开关
            case R.id.bt_connect_wifi:
                Log.i(TAG, "onClick: Go to Setting and Open WIFI.");
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            break;
            default:
                break;
        }
    }
    //ChannelListenerI接口的回调方法
    @Override
    public void onChannelDisconnected() {
        if (mManager != null && !retryChannel) {
            Log.i(TAG, "onChannelDisconnected: channel lost,Try again");
            resetData();
            retryChannel = true;
            mManager.initialize(this, getMainLooper(), this);
        } else {
            //Toast.makeText(this, "Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.", Toast.LENGTH_LONG).show();
            Log.i(TAG, "onChannelDisconnected: Try Disable/Re-Enable P2P.");
        }
    }
    @Override
    public void showDetails(WifiP2pDevice device) {
        DeviceDeatilFragment fragment = (DeviceDeatilFragment)getSupportFragmentManager().findFragmentById(R.id.frag_device_detail) ;
        Log.i(TAG, "showDetails: ");
        fragment.showDetails(device);
    }

    @Override
    public void connect(WifiP2pConfig config) {
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, " connect onSuccess: ");
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
            }

            @Override
            public void onFailure(int reason) {
                //Toast.makeText(WifiActivity.this, "Connect failed. Retry.", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "connect onFailure: ");
            }
        });
    }

    @Override
    public void disconnect() {
        final DeviceDeatilFragment fragment = (DeviceDeatilFragment) getSupportFragmentManager().findFragmentById(R.id.frag_device_detail);
        fragment.resetViews();
        mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onFailure(int reasonCode) {
                Log.d(TAG, "Disconnect failed. Reason :" + reasonCode);
            }

            @Override
            public void onSuccess() {
                fragment.getView().setVisibility(View.GONE);
                Log.i(TAG, "Disconnect onSuccess: set Frag gone");
            }
        });
    }

    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    public void resetData() {
        DeviceListFragment fragmentList =(DeviceListFragment)getSupportFragmentManager().findFragmentById(R.id.frag_device_list) ;
        DeviceDeatilFragment fragmentDetails = (DeviceDeatilFragment)getSupportFragmentManager().findFragmentById(R.id.frag_device_detail);
        if (fragmentList != null) {
            fragmentList.clearPeers();
        }
        if (fragmentDetails != null) {
            fragmentDetails.resetViews();
        }
    }
    @Override
    public void cancelDisconnect() {
        /*
         * A cancel abort request by user. Disconnect i.e. removeGroup if
         * already connected. Else, request WifiP2pManager to abort the ongoing
         * request
         */
        if (mManager != null) {
            final DeviceListFragment fragment = (DeviceListFragment)getSupportFragmentManager().findFragmentById(R.id.frag_device_list);
            if (fragment.getWifiP2pDevice() == null
                    || fragment.getWifiP2pDevice().status == WifiP2pDevice.CONNECTED) {
                disconnect();
            } else if (fragment.getWifiP2pDevice().status == WifiP2pDevice.AVAILABLE
                    || fragment.getWifiP2pDevice().status == WifiP2pDevice.INVITED) {

                mManager.cancelConnect(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(WifiActivity.this, "Aborting connection",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        Toast.makeText(WifiActivity.this,
                                "Connect abort request failed. Reason Code: " + reasonCode,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
