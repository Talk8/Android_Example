package com.example.talk8.app002beatbox;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUND_FOLDER = "sample_sounds";
    private AssetManager mAssetManager;
    private List<Sound> mSounds = new ArrayList<>();

    public BeatBox(Context context) {
        mAssetManager = context.getAssets();
        loadSounds();
    }

    private void loadSounds() {
        String[] soundNames = null;
        try {
            soundNames = mAssetManager.list(SOUND_FOLDER);
            Log.i(TAG, "loadSounds: Found "+soundNames.length+ " sounds");
        }catch (IOException e){
            e.printStackTrace();
        }

        if(soundNames != null) {
            for (String fileName : soundNames) {
                String assetPath = SOUND_FOLDER + "/" + fileName;
                Sound sound = new Sound(assetPath);
                mSounds.add(sound);
            }
        }
        return;
    }
    public List<Sound> getSounds() {
        return mSounds;
    }
}
