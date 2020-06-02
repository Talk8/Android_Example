package com.example.talk8.app002beatbox;

import android.content.Intent;

public class Sound {
    private String mAssetPath;
    private String mName;

    public Integer getSoundId() {
        return mSoundId;
    }

    public void setSoundId(Integer soundId) {
        mSoundId = soundId;
    }

    private Integer mSoundId;

    public Sound(String assetPath) {
        mAssetPath = assetPath;
        String[] components = assetPath.split("/");
        String fileName = components[components.length-1];
        //mName = fileName.replace(".wav","");
        mName = fileName.replace(".mp3","");
        mName = fileName;
    }

    public String getName() {
        return mName;
    }
}
