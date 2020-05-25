package com.example.talk8.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

//public class CrimeActivity extends AppCompatActivity implements CrimeFragment.OnFragmentInteractionListener {
public class CrimeActivity extends SingleFragmentActivity implements CrimeFragment.OnFragmentInteractionListener {
    public static final  String KEY_UUID = "uuid";

    //这个函数在实际经常见到，使用了简单工厂模式
    public static Intent newIntent(Context context, UUID uuid) {
        Intent intent = new Intent(context,CrimeActivity.class);
        intent.putExtra(KEY_UUID,uuid);
        return  intent;
    }

/*    @Override
*继承SingleFragmentActivity后只需要实现它的抽象方法createFragment就可以，以下代码被复用了
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //原来布局文件的名字是activity_crime，修改为当前名字是为了通用性考虑.P133
        setContentView(R.layout.activity_fragment);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if(fragment == null) {
            fragment = new CrimeFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }*/

    @Override
    protected Fragment createFragment() {
        //return new CrimeFragment();
        //使用简单工厂模式生成Fragment对象
        UUID uuid = (UUID)getIntent().getSerializableExtra(KEY_UUID);
        return CrimeFragment.newInstance(uuid);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
