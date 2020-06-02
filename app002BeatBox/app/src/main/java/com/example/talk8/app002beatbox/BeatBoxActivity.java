package com.example.talk8.app002beatbox;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BeatBoxActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return BeatBoxFragment.newInstace();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_beat_box;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
    }
}
