package com.example.talk8.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
        //在chapter113中添加工具栏菜单后，不在自动添加项目，调用addCrime手动添加Crime
//        for(int i=0; i<50;++i) {
//            Crime crime = new Crime();
//            crime.setTitle("Crime #"+i);
//            crime.setSolved(i%2 == 0);
//            mCrimes.add(crime);
//        }
    }

    public static CrimeLab getCrimeLab(Context context) {
        if(sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }

        return sCrimeLab;
    }

    public List<Crime> getCrimes() {
        return (mCrimes == null)? null:mCrimes;
    }

    public Crime getCrime(UUID id) {
        for(Crime crime: mCrimes) {
            if(crime.getId().equals(id)){
                return crime;
            }
        }
        return null;
    }
    public void addCrime(Crime c) {
        mCrimes.add(c);
    }
}
