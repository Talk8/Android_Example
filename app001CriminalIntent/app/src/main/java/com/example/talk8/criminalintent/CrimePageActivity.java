package com.example.talk8.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.UUID;

public class CrimePageActivity extends AppCompatActivity
        implements CrimeFragment.OnFragmentInteractionListener ,CrimeFragment.Callbacks{
    private static final String VP_CRIME_ID = "view_page_crime_id";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    public static Intent newIntent(Context context, UUID uuid) {
        Intent intent = new Intent(context,CrimePageActivity.class);
        intent.putExtra(VP_CRIME_ID,uuid);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_page);

        UUID uuid = (UUID)getIntent().getSerializableExtra(VP_CRIME_ID);

        mViewPager = (ViewPager)findViewById(R.id.activity_crime_pager_view_pager);

        mCrimes = CrimeLab.getCrimeLab(this).getCrimes();

       FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int i) {
                Crime crime = mCrimes.get(i);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        //位置不对的话，每次进入都是第一个，而不是当前选择的内容
        //不理解为什么要在setAdapter后面设置索引
        for(int i=0; i<mCrimes.size();++i) {
            if(mCrimes.get(i).getId().equals(uuid)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public ViewPager getViewPager() {
        if(mViewPager != null)
            return mViewPager;

        return null;
    }

    @Override
    public void onCrimeUpdated(Crime crime) {

    }
}
