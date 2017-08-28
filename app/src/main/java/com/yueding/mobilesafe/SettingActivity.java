package com.yueding.mobilesafe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yueding.mobilesafe.utils.SpUtil;
import com.yueding.mobilesafe.views.SettingItemView;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUpdate();
    }

    private void initUpdate() {
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
        boolean open_update = SpUtil.getBoolean(this, getString(R.string.open_update), false);
        siv_update.setChecked(open_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击该控件需要让CheckBox状态切换（文字描述切换已经封装在setChecked方法中）
                boolean isSelect = siv_update.isChecked(); //获取CheckBox状态
                siv_update.setChecked(!isSelect); //取反状态
                SpUtil.setBoolean(getApplicationContext(), getString(R.string.open_update), !isSelect);
            }
        });
    }
}
