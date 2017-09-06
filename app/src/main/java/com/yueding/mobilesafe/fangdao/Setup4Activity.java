package com.yueding.mobilesafe.fangdao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.yueding.mobilesafe.R;
import com.yueding.mobilesafe.utils.SpUtil;

public class Setup4Activity extends AppCompatActivity {

    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        initUI();
    }

    private void initUI() {
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        boolean open_security = SpUtil.getBoolean(getApplicationContext(), getString(R.string.open_security), false);
        checkBox.setChecked(open_security);
        if (open_security) {
            checkBox.setText("防盗保护已开启");
        } else {
            checkBox.setText("防盗保护已关闭");
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox.setText("防盗保护已开启");
                } else {
                    checkBox.setText("防盗保护已关闭");
                }
                SpUtil.setBoolean(getApplicationContext(), getString(R.string.open_security), isChecked);
            }
        });
    }

    public void nextPage(View view) {
        if (checkBox.isChecked()) {
            Intent intent = new Intent(this, SetupOverActivity.class);
            startActivity(intent);
            finish();
            SpUtil.setBoolean(this, getString(R.string.setupOver), true);
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        } else {
            Toast.makeText(Setup4Activity.this, "防盗保护未开启", Toast.LENGTH_SHORT).show();
        }

    }

    public void prePage(View view) {
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }
}
