package com.anuj.awesomelist.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.anuj.awesomelist.R;

import butterknife.ButterKnife;

/**
 * Created by anuj on 12/18/2015.
 */
public class DescriptionActivity extends AppCompatActivity {
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description_activity);
        ButterKnife.bind(this);
        mContext = DescriptionActivity.this;
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
