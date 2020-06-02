package com.example.talk8.app002beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUND_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;
    private AssetManager mAssetManager;
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;

    public BeatBox(Context context) {
        mAssetManager = context.getAssets();
        mSoundPool = new SoundPool(MAX_SOUNDS,AudioManager.STREAM_MUSIC,0);
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
                //try {
                    String assetPath = SOUND_FOLDER + "/" + fileName;
                    Sound sound = new Sound(assetPath);
                  //  load(sound);
                    mSounds.add(sound);
                //}catch (IOException e){
                    //e.printStackTrace();
                //}
            }
        }
        return;
    }

    private void load(Sound sound)throws IOException {
        //这里的getAssetsPath是旧API，会引起编译错误.
        //AssetFileDescriptor afd = mAssetManager.openFd(sound.getAssetsPath());
        //getName没有编译错误，但是使用时会发生找不到文件的异常
        //以后找到替换getAssetsPath的API后再实践此项目
        AssetFileDescriptor afd = mAssetManager.openFd(sound.getName());
        int soundId = mSoundPool.load(afd,1);
        sound.setSoundId(soundId);
    }
    public List<Sound> getSounds() {
        return mSounds;
    }

    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if(soundId != null){
            mSoundPool.play(soundId,1.0f,1.0f,1,0,1.0f);
        }
    }

    public void release() {
        mSoundPool.release();
    }
}
