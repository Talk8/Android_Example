package com.example.talk8.app004;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class PhotoGalleryActivity extends SingleFragmentActivity{

    private static final String TAG = "PhotoGalleryActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photo_gallery);
        Log.i(TAG, "onCreate: ");
        //requestInternetPermisson();
    }

    @Override
    protected Fragment createFragment() {
        return new PhotoGalleryFragment();
    }

    //INTERNET为非危险性权限，只需要在配置文件中添加权限声明就可以，不需要动态申请
    private void requestInternetPermisson() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "requestInternetPermisson: ");
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.INTERNET},1);
        }else {
            Log.i(TAG, "requestInternetPermisson: Permission has been approved");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==1 ) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.i(TAG, "onRequestPermissionsResult: Permission approved by user");
            }else {
                Log.i(TAG, "onRequestPermissionsResult: Permission rejected by user");
            }
        }
    }
}
