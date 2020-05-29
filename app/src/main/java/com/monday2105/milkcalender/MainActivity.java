package com.monday2105.milkcalender;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.monday2105.milkcalender.ui.main.SectionsPagerAdapter;


public class MainActivity extends AppCompatActivity {

    public static NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        startService(new Intent(MainActivity.this,RememberEntryService.class));

        notificationHelper = new NotificationHelper(this,"MakeEnrty",
                "Entry","You did not make milk entry today","Remembering making entry"
                ,1);
        notificationHelper.createNotificationChannel();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    sectionsPagerAdapter.fBill.setVolume();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if(!checkPermissionForReadWriteExternalStorage()){
            try {
                requestPermissionForReadWriteExternalStorage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkPermissionForReadWriteExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int result2 = this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return (result == PackageManager.PERMISSION_GRANTED) &&
                    (result2 == PackageManager.PERMISSION_GRANTED);
        }
        return false;
    }

    public void requestPermissionForReadWriteExternalStorage() {
        try {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        try {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}