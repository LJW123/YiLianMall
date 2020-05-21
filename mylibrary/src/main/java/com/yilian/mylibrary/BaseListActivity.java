package com.yilian.mylibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BaseListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_module_activity_base_list);
    }
}
