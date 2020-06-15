package com.example.talk8.conn;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
//public class DeviceListFragment extends ListFragment implements WifiP2pManager.PeerListListener {
public class DeviceListFragment extends Fragment implements WifiP2pManager.PeerListListener {
    private static final String TAG = "DeviceListFragment xxl";
    private List<WifiP2pDevice> mWifiP2pDeviceList = new ArrayList<>();
    //AST提示有删除线，说明这个类已经在新版本中被废弃了
    private ProgressDialog mProgressDialog = null;
    private View mView;
    private WifiP2pDevice mWifiP2pDevice;

    private RecyclerView mRecyclerView;

    public DeviceListFragment() {
        // Required empty public constructor
    }


//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        //this.setListAdapter(new WiFiPeerListAdapter(getActivity(),R.layout.row_devices,mWifiP2pDeviceList));
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_device_list, container, false);
        //mView = inflater.inflate(R.layout.fragment_device_list,null );
        TextView textView = (TextView)mView.findViewById(R.id.id_empty) ;
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.id_list_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        WiFiPeerListAdapter wiFiPeerListAdapter = new WiFiPeerListAdapter(mWifiP2pDeviceList);
        Log.i(TAG, "onCreateView:  size of device: "+mWifiP2pDeviceList.size());
        mRecyclerView.setAdapter(wiFiPeerListAdapter);

//        mRecyclerView.setAdapter(new WiFiPeerListAdapter(getActivity(),R.layout.row_devices,mWifiP2pDeviceList));

//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.i(TAG, "onItemClick: ");
//                WifiP2pDevice device = (WifiP2pDevice)mWifiP2pDeviceList.get(position);
//                ((DeviceActionListener)getActivity()).showDetails(device);
//            }
//        });
        Log.i(TAG, "onCreateView: ");
        return mView;
    }

    public WifiP2pDevice getWifiP2pDevice() {
        return mWifiP2pDevice;
    }

    private static String getDeviceStatus(int deviceStatus){
        Log.i(TAG, "getDeviceStatus: "+deviceStatus);
        switch (deviceStatus){
        case WifiP2pDevice.AVAILABLE:
            return "available";
        case WifiP2pDevice.INVITED:
            return "inviled";
        case WifiP2pDevice.CONNECTED:
            return "connected";
        case WifiP2pDevice.FAILED:
            return "failed";
        case WifiP2pDevice.UNAVAILABLE:
            return "unAvaiable";
         default:
            return "Unknow";
        }
    }

//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        //super.onListItemClick(l, v, position, id);
//        Log.i(TAG, "onListItemClick: position: "+position+" id: "+id);
//        WifiP2pDevice device = (WifiP2pDevice)getListAdapter().getItem(position);
//        ((DeviceActionListener)getActivity()).showDetails(device);
//        Log.i(TAG, "onListItemClick: ");
//    }

    //private class WiFiPeerListAdapter extends ArrayAdapter<WifiP2pDevice> {
    public class WiFiPeerListAdapter extends RecyclerView.Adapter<WiFiPeerListAdapter.WiFiPeerListViewHolder> {

        private List<WifiP2pDevice> items;

        public WiFiPeerListAdapter(List<WifiP2pDevice> items) {
            this.items = items;
        }

        public class  WiFiPeerListViewHolder extends RecyclerView.ViewHolder {
            private TextView top;
            private TextView bottom;

            public TextView getTop() {
                return top;
            }
            public TextView getBottom() {
                return bottom;
            }

            public WiFiPeerListViewHolder (@NonNull View itemView) {
                super(itemView);
                View view = itemView;
                top = (TextView)view.findViewById(R.id.device_name);
                bottom = (TextView)view.findViewById(R.id.device_details);
            }
        }

        @NonNull
        @Override
        //public WiFiPeerListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        public WiFiPeerListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_devices,viewGroup,false);
            WiFiPeerListViewHolder viewHolder = new WiFiPeerListViewHolder (view);
            Log.i(TAG, "onCreateViewHolder: ");

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull WiFiPeerListViewHolder wiFiPeerListViewHolder, int i) {
            final WifiP2pDevice device = items.get(i);
            Log.i(TAG, "onBindViewHolder: ");
            if(device != null) {
                if (wiFiPeerListViewHolder.top != null) {
                    Log.i(TAG, "onBindViewHolder: set device name");
                    wiFiPeerListViewHolder.getTop().setText(device.deviceName);
                    wiFiPeerListViewHolder.getTop().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((DeviceActionListener)getActivity()).showDetails(device);
                        }
                    });
                }
                if (wiFiPeerListViewHolder.bottom != null) {
                    wiFiPeerListViewHolder.getBottom().setText(getDeviceStatus(device.status));
                    wiFiPeerListViewHolder.getBottom().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((DeviceActionListener)getActivity()).showDetails(device);
                        }
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            Log.i(TAG, "getItemCount: of RecycleView");
            return items.size();
        }
    }
    public void updateThisDevice(WifiP2pDevice device){
        this.mWifiP2pDevice = device;
        TextView textView = (TextView)mView.findViewById(R.id.my_name);
        textView.setText(device.deviceName);

        textView = (TextView)mView.findViewById(R.id.my_status);
        textView.setText(getDeviceStatus(device.status));
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }

        mWifiP2pDeviceList.clear();
        mWifiP2pDeviceList.addAll(peers.getDeviceList());
        Log.i(TAG, "onPeersAvailable: size: "+mWifiP2pDeviceList.size());
        mRecyclerView.getAdapter().notifyDataSetChanged();
        if(mWifiP2pDeviceList.size() == 0){
            Log.i(TAG, "onPeersAvailable: no device find");
            return;
        }
    }

    public void clearPeers(){
        mWifiP2pDeviceList.clear();
        //((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
        mRecyclerView.getAdapter().notifyDataSetChanged();
        Log.i(TAG, "clearPeers: ");
    }

    public void onInitiateDiscovery(){
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = ProgressDialog.show(getActivity(), "Press back to cancel", "finding peers", true,
                true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });
    }

    public interface DeviceActionListener {
        void showDetails(WifiP2pDevice device);
        void cancelDisconnect();
        void connect(WifiP2pConfig config);
        void disconnect();
    }

}
