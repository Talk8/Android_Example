package com.example.talk8.app002beatbox;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.talk8.app002beatbox.databinding.FragmentBeatBoxBinding;
import com.example.talk8.app002beatbox.databinding.ListItemSoundBinding;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BeatBoxFragment extends Fragment{

    private BeatBox mBeatBox;

    public static BeatBoxFragment newInstace()  {
        return new BeatBoxFragment();
    }
    public BeatBoxFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBeatBox = new BeatBox(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_beat_box, container, false);
        FragmentBeatBoxBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_beat_box,container,false);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        //binding.recyclerView.setAdapter(new SoundAdapter());
        binding.recyclerView.setAdapter(new SoundAdapter(mBeatBox.getSounds()));

        return binding.getRoot();
    }

    //定义ViewHolder类
    private class SoundHolder extends RecyclerView.ViewHolder {
        private ListItemSoundBinding binding;
        private SoundHolder(ListItemSoundBinding listItemSoundBinding) {
            super(listItemSoundBinding.getRoot());
            binding = listItemSoundBinding;
            binding.setViewModel(new SoundViewModel(mBeatBox));
        }
        public void bind(Sound sound) {
            binding.getViewModel().setSound(sound);
            binding.executePendingBindings();
        }
    }
    private class SoundAdapter extends RecyclerView.Adapter<SoundHolder> {
        private List<Sound> mSounds;
        public SoundAdapter(List<Sound> sounds) {
            mSounds = sounds;
        }

        @NonNull
        @Override
        public SoundHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            ListItemSoundBinding binding = DataBindingUtil.inflate(inflater,
                    R.layout.list_item_sound,viewGroup,false);
            return new SoundHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull SoundHolder soundHolder, int i) {
            Sound sound  = mSounds.get(i);
            soundHolder.bind(sound);
        }

        @Override
        public int getItemCount() {
            return mSounds.size();
        }
    }
}
