package com.jdz.translatetool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String a = getString(R.string.a);
        String b = getString(R.string.b);
        String c = getString(R.string.c);
        String d = getString(R.string.d);
        String e = getString(R.string.e);
        String g = getString(R.string.f);
    }
}
