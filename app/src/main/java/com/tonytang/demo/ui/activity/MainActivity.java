package com.tonytang.demo.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tonytang.demo.R;
import com.tonytang.demo.ui.fragment.MovieSearchFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction().replace(R.id.fragment_holder,
                MovieSearchFragment.newInstance()).commit();
    }
}
