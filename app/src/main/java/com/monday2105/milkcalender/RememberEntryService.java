package com.monday2105.milkcalender;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.monday2105.milkcalender.ui.main.Calender;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class RememberEntryService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Date curent_time= new Date(System.currentTimeMillis());
        final Calendar cal = Calendar.getInstance();
        cal.setTime(curent_time);
        int hour = cal.get(Calendar.HOUR);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);

        long when = (18 * 3600)
                - ( (hour*3600) + (min *60) + sec);
        // interval should be based on miliseconds so
        long interval = 24*60*60*1000;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Calender.postNotif();
            }
        }, when, interval);

        return super.onStartCommand(intent, flags, startId);
    }
}
