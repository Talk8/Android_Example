package com.example.talk8.criminalintent;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    public CrimeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView)view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //当Model中的数据变化后，刷新View界面中的数据
        updateUI();
    }

    public void updateUI() {
        CrimeLab crimeLab = CrimeLab.getCrimeLab(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;

//        书中的代码在这里使用了错误的构造函数
//        public CrimeHolder(LayoutInflater layoutInflater, ViewGroup parent){
//            super(layoutInflater.inflate(R.layout.list_item_crime,parent,false));
//            View view = layoutInflater.inflate(R.layout.list_item_crime,parent,false);
//            //这两个TextView的id在其它布局中有使用，如何区分？还是书中有错误
//            mTitleTextView = (TextView)view.findViewById(R.id.crime_list_title);
//            mDateTextView= (TextView)view.findViewById(R.id.crime_list_date);
//            view.setOnClickListener(this);
//        }
        public CrimeHolder(View itemView) {
            //这两个TextView的id在其它布局中有使用，书中有错误,我加了list以区分，不然找不到，setText在使用null对象
            super(itemView);
            mTitleTextView = (TextView)itemView.findViewById(R.id.crime_list_title);
            mDateTextView= (TextView)itemView.findViewById(R.id.crime_list_date);
            itemView.setOnClickListener(this);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(getActivity(),mCrime.getTitle()+" Clicked",Toast.LENGTH_LONG).show();
            //点击某个项目跳转到该项目的详细资料中，本质上是从Fragment中跳转到Activity中
            //Intent intent = new Intent(getActivity(),CrimeActivity.class);
            Intent intent = CrimeActivity.newIntent(getActivity(),mCrime.getId());

            startActivity(intent);
         }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime,viewGroup,false);

            return  new CrimeHolder(view);
            //构造函数使用错误了，这里的代码也需要修改
            //return new CrimeHolder(layoutInflater,viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder crimeHolder, int i) {
            Crime crime = mCrimes.get(i);
            crimeHolder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
