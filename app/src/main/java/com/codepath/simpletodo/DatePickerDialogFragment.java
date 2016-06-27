package com.codepath.simpletodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by scirupati on 6/27/2016.
 */
public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static final String TAG = DatePickerDialogFragment.class.getSimpleName();
    public static String DUE_DATE = "due_date";

    private long mDueDateTS;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Callbacks mCallbacks;

    public interface Callbacks {
        /**
         * Callback for when Submit button is clicked.
         */
        void onDateSet(long dueDateTS, String dueDateString);

    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onDateSet(long dueDateTS, String dueDateString) {
            // TODO Auto-generated method stub

        }
    };


    public DatePickerDialogFragment(){
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    public static DatePickerDialogFragment newInstance(long dueDate){
        DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
        Bundle args = new Bundle();
        args.putLong(DUE_DATE, dueDate);
        datePickerDialogFragment.setArguments(args);
        return datePickerDialogFragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDueDateTS = getArguments().getLong(DUE_DATE,0);
        Calendar calendar = Calendar.getInstance();
        if(mDueDateTS != 0){
            calendar.setTimeInMillis(mDueDateTS);
        }
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),this,mYear,mMonth,mDay);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTime().getTime());
        datePickerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button cancel = datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //sendResult(EXPIRY_SET_RESPONSE_CODE,0);
                        datePickerDialog.dismiss();
                    }
                });
            }
        });
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth,23,59,59);
        Toast.makeText(getActivity(),"Date set is - "+((monthOfYear+1)+"-"+dayOfMonth+"-"+year),Toast.LENGTH_SHORT).show();

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
        String formattedDate = dateFormat.format(new Date(calendar.getTimeInMillis()));
        mCallbacks.onDateSet(calendar.getTimeInMillis(),formattedDate);

    }
}

