package com.example.talk8.criminalintent;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment{
    private static final String DATE = "date_of_crime";
    public static final String EXTRA_DATE = "date_of_crime";
    private DatePicker mDatePicker;


    public DatePickerFragment() {
        // Required empty public constructor
    }

    public static DatePickerFragment newInstance(Date date) {
        Bundle bundleParam = new Bundle();
        bundleParam.putSerializable(DATE,date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(bundleParam);

        return fragment;
    }

    //封装传递数据的函数
    private void sendResult(int resultCode,Date date){
        if(getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,date);
        //第一个参数是requestCode,第二个参数是resultCode,它们不一样
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date_picker, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date_picker,null);

        //Fragment之间的数据传递：CrimeFragment到当前Fragment
        //这里传递的数据是日期，把Crime中存放的日期传递到DatePicker控件上
        Date date = (Date)getArguments().getSerializable(DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

       mDatePicker = (DatePicker) view.findViewById(R.id.dialog_date_picker);
       mDatePicker.init(year,month,day,null);

        return new AlertDialog.Builder(getActivity()).setTitle(R.string.date_picker_title)
                .setView(view)
                .setPositiveButton(R.string.alter_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(year,month,day).getTime();
                        sendResult(Activity.RESULT_OK,date);
                    }
                })
                .setNegativeButton(R.string.alter_dialog_cancel,null).create();
    }
}
