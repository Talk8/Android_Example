package com.example.talk8.app003;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NerdLauncherActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return NerdLauncherFragment.newInstance();
    }
}
