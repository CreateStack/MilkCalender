package com.monday2105.milkcalender.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
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
import java.util.Objects;

public class Calender extends Fragment {

    private EditText amount;
    private TextView oldAmount;
    private Button save;
    private SqlHelper sqlHelper;

    private String date;

    private String TAG = "Calender";

    private View[] views;

    public  Calender(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calender,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sqlHelper = new SqlHelper(getContext());

        TextView newTitle = Objects.requireNonNull(getView()).findViewById(R.id.newtitle);
        TextView oldTitle = getView().findViewById(R.id.oldtitle);
        amount = getView().findViewById(R.id.amount);
        oldAmount = getView().findViewById(R.id.oldamount);
        save = getView().findViewById(R.id.save);
        Button cancel = getView().findViewById(R.id.cancel);
        Button delete = getView().findViewById(R.id.delete);
        CalendarView calendarView = getView().findViewById(R.id.calendarView);

        views = new View[] {newTitle, oldTitle,amount,oldAmount,save, cancel, delete};

        setVisibility();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount.getText().toString().isEmpty()){
                    Snackbar.make(Objects.requireNonNull(getView()),"Volume can not be empty",
                            Snackbar.LENGTH_LONG).show();
                }
                else{
                    float volume = Float.parseFloat(amount.getText().toString());
                    Log.i(TAG, "onClick: "+save.getText().toString());
                    if(save.getText().toString().equals("Save")) {
                        long res = sqlHelper.insertData(date,volume);
                        if(res==-1){
                            Snackbar.make(Objects.requireNonNull(getView()),"Error in making entry",
                                    Snackbar.LENGTH_LONG)
                                    .show();
                        }

                    }
                    else {
                        int res = sqlHelper.updateData(date, volume);
                        if(res==0){
                            Snackbar.make(Objects.requireNonNull(getView()),"Error in making update",
                                    Snackbar.LENGTH_LONG)
                                    .show();
                            save.setText(R.string.save);
                            setVisibility();
                            return;
                        }
                        save.setText(R.string.save);
                    }
                    Snackbar.make(Objects.requireNonNull(getView()), "Volume set to: " + volume,
                            Snackbar.LENGTH_LONG).show();
                    setVisibility();
                    amount.getText().clear();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount.getText().clear();
                setVisibility();
                save.setText(R.string.save);
                Snackbar.make(Objects.requireNonNull(getView()),"Cancelled",
                        Snackbar.LENGTH_LONG).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res = sqlHelper.deleteData(date);
                if(res==0){
                    Snackbar.make(Objects.requireNonNull(getView()),"Delete Failed",
                            Snackbar.LENGTH_LONG).show();
                    setVisibility();
                    save.setText(R.string.save);
                    return;
                }
                Snackbar.make(Objects.requireNonNull(getView()),"Deleted Entry",
                        Snackbar.LENGTH_LONG).show();
                setVisibility();
                save.setText(R.string.save);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                setVisibility();
                setVisibility(new int[]{0,2,4,5});
                save.setText(R.string.save);
                date = makeDate(year,month,dayOfMonth);
                Log.i(TAG, "onSelectedDayChange: "+date);
                checkPreviousEntry(date);
            }
        });
    }

    private void setVisibility(){
        for(View view : views){
            view.setVisibility(View.INVISIBLE);
        }

        /*else{
            for(View view : views){
                view.setVisibility(View.INVISIBLE);
            }
        }*/
    }

    private void setVisibility(int[] indices){
        for (int index : indices) {
            views[index].setVisibility(View.VISIBLE);

        }
        /*else{
            for(int i=0;i<indices.length;i++){
                views[indices[i]].setVisibility(View.INVISIBLE);
            }
        }*/
    }

    private void checkPreviousEntry(String date){

        ArrayList<String> data = sqlHelper.readData(date);
        if (data.size() != 0) {
            setVisibility(new int[]{1,3,6});
            save.setText(R.string.update);
            oldAmount.setText(data.get(0));
            Snackbar.make(Objects.requireNonNull(getView()),"You have saved data",
                    Snackbar.LENGTH_LONG);

        }

    }

    private String makeDate(int year, int month, int dayOfMonth){
        DecimalFormat formatter = new DecimalFormat("00");
        String fMonth = formatter.format(month);
        String fDay = formatter.format(dayOfMonth);
        return year+"-"+fMonth+"-"+fDay;
    }
}
