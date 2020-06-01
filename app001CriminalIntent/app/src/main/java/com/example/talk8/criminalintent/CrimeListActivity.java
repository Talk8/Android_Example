package com.example.talk8.criminalintent;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks,CrimeFragment.OnFragmentInteractionListener{
    @Override
    protected Fragment createFragment() {
        return  new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_twopane;
    }

    //此方法来自CrimeListFragment,当List中某个项目被选择后把它展示出来
    //如果没有详细布局，也就是说不是平板，那么和以前一样在CrimePageActivity中显示详细内容
    //如果有详细布局，也就是是平板，那么动态加载详细页面的Fragment;
    @Override
    public void onCrimeSelected(Crime crime) {
        if(findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePageActivity.newIntent(this,crime.getId());
            startActivity(intent);
        }else {
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container,newDetail).commit();
        }
    }

    //此方法和上面的方法功能类似，当详细界面中也就是CrimeFragment中某个项目更新时
    //更新的内容需要实时同步到List界面上，这在平板上特别重要，因为两个Fragment
    //都在用户视线中。
    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment fragment = (CrimeListFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        fragment.updateUI();
    }

    //此方法是为了解决FATA而添加的，系统自动添加的callback中的方法
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
