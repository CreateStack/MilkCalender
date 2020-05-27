package com.monday2105.milkcalender.ui.main;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.monday2105.milkcalender.R;
import com.monday2105.milkcalender.SqlHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;


public class Bill extends Fragment {

    private EditText vRate;
    private TextView vTotVol;
    private TextView vBill;
    private DatePicker datePicker;

    private DecimalFormat formatter;

    private String TAG = "Bill";

    private SqlHelper sqlHelper;

    private float totalVol;

    public Bill() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bill,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        datePicker = Objects.requireNonNull(getView()).findViewById(R.id.simpleDatePicker);
        vRate = getView().findViewById(R.id.rate);
        vTotVol = getView().findViewById(R.id.totalvol);
        vBill = getView().findViewById(R.id.bill);
        sqlHelper = new SqlHelper(getContext());
        Button vCalBill = getView().findViewById(R.id.calculate);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) vCalBill.setVisibility(View.INVISIBLE);

        int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
        if (daySpinnerId != 0) {
            View daySpinner = datePicker.findViewById(daySpinnerId);
            if (daySpinner != null) {
                daySpinner.setVisibility(View.GONE);
            }
        }

        int month = datePicker.getMonth();
        formatter = new DecimalFormat("00");
        String fMonth = formatter.format(month);
        ArrayList<String> data = sqlHelper.getMonthData(fMonth);
        calTotVol(data);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String fMonth = formatter.format(monthOfYear);
                    ArrayList<String> data = sqlHelper.getMonthData(fMonth);
                    calTotVol(data);
                    vBill.setText("");
                    vRate.setText("");
                }
            });
        }

        vRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "onTextChanged: "+s);
                if(s.toString().isEmpty()) return;
                float bill = totalVol*Float.parseFloat(s.toString());
                Log.i(TAG, "onTextChanged: "+bill);
                vBill.setText(String.format(Locale.ENGLISH,"Rs. %.2f",bill));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        vRate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });

        vCalBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int month = datePicker.getMonth();
                formatter = new DecimalFormat("00");
                String fMonth = formatter.format(month);
                ArrayList<String> data = sqlHelper.getMonthData(fMonth);
                calTotVol(data);
                String s = vRate.getText().toString();
                if(s.isEmpty()) {
                    Snackbar.make(Objects.requireNonNull(getView()),"Please enter rate",Snackbar.LENGTH_LONG).show();
                    return;
                }
                float bill = totalVol*Float.parseFloat(s);
                Log.i(TAG, "onTextChanged: "+bill);
                vBill.setText(String.format(Locale.ENGLISH,"Rs. %.2f",bill));
            }
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void calTotVol(ArrayList<String> data){
        totalVol = 0;
        for(int i=0;i<data.size();i++){
            totalVol = totalVol + Float.parseFloat(data.get(i));
        }
        vTotVol.setText(String.format(Locale.ENGLISH,"%.2f Litres",totalVol));
        Log.i(TAG, "onViewCreated: "+totalVol);
    }
}
