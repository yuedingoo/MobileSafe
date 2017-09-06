package com.yueding.mobilesafe.fangdao;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.yueding.mobilesafe.R;
import com.yueding.mobilesafe.utils.SpUtil;
import com.yueding.mobilesafe.views.SettingItemView;

public class Setup2Activity extends AppCompatActivity {

    private SettingItemView sim_band;
    private boolean isCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        initUI();
    }

    private void initUI() {
        sim_band = (SettingItemView)findViewById(R.id.band_sim_setting);
        String sim_number = SpUtil.getString(getApplicationContext(), getString(R.string.sim_number), "");
        if (TextUtils.isEmpty(sim_number)) {
            sim_band.setChecked(false);
        } else {
            sim_band.setChecked(true);
        }
        sim_band.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取原来的状态isCheck
                isCheck = sim_band.isChecked();
                //!isCheck 点击后的状态
                sim_band.setChecked(!isCheck);
                //获取运行时权限
                if (ContextCompat.checkSelfPermission(Setup2Activity.this, Manifest.permission.
                        READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Setup2Activity.this, new String[]{Manifest.
                            permission.READ_PHONE_STATE}, 1);
                } else {
                    requestSimNumber(isCheck);
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED) {
                    requestSimNumber(isCheck);
                } else {
                    Toast.makeText(Setup2Activity.this, "你拒绝了权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void requestSimNumber(boolean isCheck) {
        if (!isCheck) {
            TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String simSerialNumber = manager.getSimSerialNumber();
            SpUtil.setString(getApplicationContext(), getString(R.string.sim_number), simSerialNumber);
        } else {
            SpUtil.remove(getApplicationContext(), getString(R.string.sim_number));
        }
    }

    public void nextPage(View view) {
        String serialNumber = SpUtil.getString(getApplicationContext(), getString(R.string.sim_number), "");
        if (!TextUtils.isEmpty(serialNumber)) {
            Intent intent = new Intent(this, Setup3Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        } else {
            Toast.makeText(Setup2Activity.this, "请绑定SIM卡", Toast.LENGTH_SHORT).show();
        }
    }

    public void prePage(View view) {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }
}
