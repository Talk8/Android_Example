package com.example.talk8.app002beatbox;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class SoundViewModel extends BaseObservable {
    private Sound mSound;
    private BeatBox mBeatBox;

    public SoundViewModel(BeatBox beatBox){
        mBeatBox = beatBox;
    }

    public Sound getSound() {
        return mSound;
    }
    public void setSound(Sound sound){
        mSound = sound;
        notifyChange();
    }
    @Bindable
    public String getTitle(){
        if (mSound != null) return mSound.getName();
        return null;
    }
}
