package com.bkt.basdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bkt.contract.ba.enums.OderStatus;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OderStatus oderStatus = OderStatus.CANCELED;
    }
}