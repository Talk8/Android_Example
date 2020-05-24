package com.example.talk8.criminalintent;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//public class CrimeActivity extends AppCompatActivity implements CrimeFragment.OnFragmentInteractionListener {
public class CrimeActivity extends SingleFragmentActivity implements CrimeFragment.OnFragmentInteractionListener {

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
        return new CrimeFragment();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
