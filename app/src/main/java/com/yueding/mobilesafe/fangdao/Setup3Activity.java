package com.yueding.mobilesafe.fangdao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yueding.mobilesafe.R;
import com.yueding.mobilesafe.utils.SpUtil;

public class Setup3Activity extends AppCompatActivity {

    private EditText edit_phone;
    private Button button_contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        edit_phone = (EditText) findViewById(R.id.editText);
        String phone = SpUtil.getString(getApplicationContext(), getString(R.string.phone_number), "");
        if (!TextUtils.isEmpty(phone)) {
            edit_phone.setText(phone);
        }
        button_contact = (Button) findViewById(R.id.bt_select_number);
        button_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setup3Activity.this ,ContactsActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (data != null) {
                    String phone = data.getStringExtra("phone");
                    phone = phone.replace("-", "").replace(" ", "").trim();
                    edit_phone.setText(phone);
                }
                break;
        }
    }

    public void nextPage(View view) {
        String phone = edit_phone.getText().toString();
        SpUtil.setString(getApplicationContext(), getString(R.string.phone_number), phone);
        Intent intent = new Intent(this, Setup4Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

    public void prePage(View view) {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }
}
