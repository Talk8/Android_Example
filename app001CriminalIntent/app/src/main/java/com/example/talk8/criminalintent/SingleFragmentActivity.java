package com.example.talk8.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

//此类是为了方便代码复用p134
public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_fragment);
        setContentView(getLayoutResId());

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if(fragment == null) {
            //fragment = new CrimeFragment();
            //子类实现抽象方法后，可以依据不同的子类返回不同的fragment
            fragment =  createFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }

    //开放接口让子类继承，如果子类有切换布局的需求，可以重写此方法，
    //这样就可以获取不同子类的不同资源id,以便切换布局
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }
}
