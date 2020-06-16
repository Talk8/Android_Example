package com.example.talk8.conn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "WiFiDirectBroadcastRece xxl";

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WifiP2pManager.PeerListListener mPeerListListener;
    private WifiActivity mActivity;
    private ArrayList<WifiP2pDevice> mWifiP2pDeviceLists;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       WifiActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
           //主要检查是否enable设备的P2P模式，只有在P2P模式时才能搜索到设备
            Log.i(TAG, "onReceive: WIFI_P2P_STATE_CHANGED_ACTION");
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
            if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Log.i(TAG, "onReceive: WIFI_P2P_STATE_ENABLED");
                mActivity.setIsWifiP2pEnabled(true);
            }else {
                Log.i(TAG, "onReceive: WIFI_P2P_STATE_DISABLED");
                mActivity.setIsWifiP2pEnabled(false);
                mActivity.resetData();
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            //搜索到p2p设备时才能收到此广播
            Log.i(TAG, "onReceive: WIFI_P2P_PEERS_CHANGED_ACTION");
            if (mManager != null) {
                Log.i(TAG, "onReceive: requestPeers");
                mManager.requestPeers(mChannel, (WifiP2pManager.PeerListListener) mActivity.getSupportFragmentManager().findFragmentById(R.id.frag_device_list));
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            //设备的连接状态变化时收到此广播，比如连接，断开设备
            Log.i(TAG, "onReceive: WIFI_P2P_CONNECTION_CHANGED_ACTION");
            if (mManager == null) {
                return;
            }
            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {
                // we are connected with the other device, request connection
                // info to find group owner IP
                DeviceDeatilFragment fragment = (DeviceDeatilFragment)mActivity.getSupportFragmentManager().findFragmentById(R.id.frag_device_detail) ;
                mManager.requestConnectionInfo(mChannel, fragment);
            } else {
                // It's a disconnect
                mActivity.resetData();
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
            //设备变化时收到此广播，比如更换了另外一个设备，通常第一次和某个设备连接时也会收到此消息
            Log.i(TAG, "onReceive: WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
            DeviceListFragment fragment = (DeviceListFragment) mActivity.getSupportFragmentManager().findFragmentById(R.id.frag_device_list);
            fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
        }
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
