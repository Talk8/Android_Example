package com.example.talk8.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CrimeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CrimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrimeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String CRIME_ID = "crime_id";
    private static final String VIEW_PAGE= "view_page";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_CODE = 0;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mFirstButton;
    private Button mEndButton;
    private Button mReportButton;
    private Button mChooseSuspectButton;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CrimeFragment() {
        // Required empty public constructor
    }

    //这个是自己定义的方法，使用了简单工厂模式
    //其实使用AST创建Fragment时会自动创建此方法，
    //看来官方找茬使用Fragmetn的参数来传递数据，而不是使用intent
    public static CrimeFragment newInstance(UUID uuid) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CRIME_ID,uuid);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(bundle);

        return crimeFragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CrimeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrimeFragment newInstance(String param1, String param2) {
        CrimeFragment fragment = new CrimeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //mCrime = new Crime();
        //通过Activity的intent来传递数据
        //UUID uuid = (UUID)getActivity().getIntent().getSerializableExtra(CrimeActivity.KEY_UUID);
        //通过Fragment的参数来传递数据
        UUID uuid = (UUID) getArguments().getSerializable(CRIME_ID);
        mCrime = CrimeLab.getCrimeLab(getActivity()).getCrime(uuid);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        mDateButton = (Button)view.findViewById(R.id.crime_date);
        mFirstButton = (Button)view.findViewById(R.id.go_to_first);
        mEndButton = (Button)view.findViewById(R.id.go_to_end);
        mReportButton = (Button) view.findViewById(R.id.crime_report);
        mChooseSuspectButton = (Button)view.findViewById(R.id.crime_suspect);
        mChooseSuspectButton.setEnabled(false);

        //快速跳转到第一个选项的详情页面，这个是书中的扩展练习，我自己添加的
        //想通过Fragment的参数传递Viewpage对象，结果无法put到Bundle中，因此无法传递
        //于自定义getViewPage方法。本质上是通过ViewPage修改当前的索引值
        mFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager viewPager = ((CrimePageActivity)getActivity()).getViewPager();
                if(viewPager != null) {
                    viewPager.setCurrentItem(0);
                }
            }
        });
        //快速跳转到最后一个选项的详情页面，这个是书中的扩展练习，我自己添加的
        //我在模拟器上运行时有闪出其它选项详情的页面，才50个选项就出问题，看来需要优化性能
        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager viewPager = ((CrimePageActivity)getActivity()).getViewPager();
                if(viewPager != null) {
                    int size = CrimeLab.getCrimeLab(getContext()).getCrimes().size()-1;
                    viewPager.setCurrentItem(size);
                }
            }
        });

        updateData();

        PackageManager pm = getActivity().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_SEND);
        //以下代码用来控制是否enable按钮，如果程序没有匹配的Action，使用隐式intent就会导致
        //程序发生crash,注释掉的ACTION没有程序响应，会把按钮灰显
        //Intent intent = new Intent(Intent.CATEGORY_HOME);
        if(pm.resolveActivity(intent,PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mReportButton.setEnabled(false);
        }

        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过隐式Intent启动Activity
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));
                //chooser可以每次都弹出选择发送报告的程序，不过破坏了原来的界面
                //原来界面有，只有一次和一直使用的区别，这个体验好一些
                intent = Intent.createChooser(intent,getString(R.string.send_report));
                startActivity(intent);
            }
        });

        //mDateButton.setEnabled(false);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //以下方法是创建DialogFragment子类对象，并且通过show方法显示alterDialog
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                //FragmentManager fragmentManager = getFragmentManager();
//                DatePickerFragment datePickerFragmentDialog = new DatePickerFragment();
                //使用工厂方法创建dialog子类对象，主要是用来在两个Fragment之间传递数据
                DatePickerFragment alterDialog = DatePickerFragment.newInstance(mCrime.getDate());
                //设置目标Fragment，类似Activity之间的startActivityForResult
                alterDialog.setTargetFragment(CrimeFragment.this,REQUEST_CODE);
                alterDialog.show(fragmentManager,DIALOG_DATE);
                // 以下使用app库的alterDialog，不依赖任何托管Activity
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
//                        .setTitle(R.string.date_picker_title)
//                        .setMessage("AlterDialog")
//                        .setPositiveButton(R.string.alter_dialog_ok,null)
//                        .setNegativeButton(R.string.alter_dialog_cancel,null);
//                alertDialog.show();
            }
        });

        mSolvedCheckBox = (CheckBox)view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        mTitleField = (EditText)view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    //获取从其它Fragment中传递来的数据
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK || requestCode != REQUEST_CODE)
            return;
        Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
        mCrime.setDate(date);
        updateData();
    }

    //这个方法是使用AST自动生成的:快捷键：Ctrl+Alt+m
    private void updateData() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    private String getCrimeReport() {
        String solvedString = null;
        if(mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        }else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE,MMM,dd";
        String dateString = DateFormat.format(dateFormat,mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if(suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        }else {
            suspect = getString(R.string.crime_report_suspect,suspect);
        }

        String report = getString(R.string.crime_report,mCrime.getTitle(),dateString,solvedString,suspect);

        return report;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
