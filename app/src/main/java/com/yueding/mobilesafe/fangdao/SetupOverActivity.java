package com.yueding.mobilesafe.fangdao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yueding.mobilesafe.R;
import com.yueding.mobilesafe.utils.SpUtil;

public class SetupOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isSetupOver = SpUtil.getBoolean(this, getString(R.string.setupOver), false);
        if (isSetupOver) {
            setContentView(R.layout.activity_setup_over);
        } else {
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
