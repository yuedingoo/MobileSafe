package com.yueding.mobilesafe;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yueding.mobilesafe.utils.Md5Util;
import com.yueding.mobilesafe.utils.SpUtil;

public class HomeActivity extends AppCompatActivity {

    private GridView mGridView;
    private String[] mNames;
    private int[] mImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initData();
        mGridView = (GridView) findViewById(R.id.gd_list);
        mGridView.setAdapter(new MyAdapter());
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showMyDialog();
                        break;
                    case 8:
                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void showMyDialog() {
        //判断本地是否存储密码（sp 字符串）
        String pwd = SpUtil.getString(this, getString(R.string.pwd), "");
        if (TextUtils.isEmpty(pwd)) {
            //初始设置密码对话框
            showSetPwdDialog();
        } else {
            //确认密码对话框
            showConfirmPwdDialog();
        }
    }

    /**
     * 确认密码对话框
     */
    private void showConfirmPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_confirm_pwd, null);
        dialog.setView(view);
        dialog.show();
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        //取消按钮点击事件
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //确认按钮点击事件
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = SpUtil.getString(getApplicationContext(), getString(R.string.pwd), "");
                EditText confirmPwd = (EditText) view.findViewById(R.id.edit_confirm_pwd);
                String confirmPassword = confirmPwd.getText().toString();
                //对密码进行相同MD5算法加密，因为需要验证密码正确性，而存储在手机的密码经过了MD5加密
                String ss = confirmPassword + "mobileSafeByYueding";
                String safePwd = Md5Util.getMD5Pro(ss);
                if (!TextUtils.isEmpty(confirmPassword)) {
                    //确认密码输入框输入不为空
                    if (password.equals(safePwd)) {
                        //设置密码与确认密码一致
                        //进入手机防盗模块
                        //隐藏对话框
                        dialog.dismiss();
                    } else {
                        //清空确认密码输入框
                        confirmPwd.setText("");
                        Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 设置初始密码对话框
     */
    private void showSetPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_set_pwd, null);
        dialog.setView(view);
        dialog.show();
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        //取消按钮点击事件
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //确认按钮点击事件
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText setPwd = (EditText) view.findViewById(R.id.edit_set_pwd);
                EditText confirmPwd = (EditText) view.findViewById(R.id.edit_confirm_pwd);
                String password = setPwd.getText().toString();
                String confirmPassword = confirmPwd.getText().toString();
                if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword)) {
                    //任一密码输入框输入不为空
                    if (password.equals(confirmPassword)) {
                        //设置密码与确认密码一致
                        //进入手机防盗模块
                        //隐藏对话框
                        dialog.dismiss();
                        //密码加密
                        String ss = password + "mobileSafeByYueding";
                        String safePwd = Md5Util.getMD5Pro(ss);
                        SpUtil.setString(getApplicationContext(), getString(R.string.pwd), safePwd);
                    } else {
                        //清空确认密码输入框
                        confirmPwd.setText("");
                        Toast.makeText(getApplicationContext(), "确认密码输入错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData() {
        mNames = new String[]{"手机防盗", "通信卫士", "软件管理", "进程管理", "流量统计",
                "手机杀毒", "缓存清理", "高级工具", "设置中心"};
        mImages = new int[]{R.mipmap.home_safe, R.mipmap.home_callmsgsafe,
                            R.mipmap.home_apps, R.mipmap.home_taskmanager,
                            R.mipmap.home_netmanager, R.mipmap.home_trojan,
                            R.mipmap.home_sysoptimize, R.mipmap.home_tools,
                            R.mipmap.home_settings};
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mNames.length;
        }

        @Override
        public Object getItem(int position) {
            return mNames[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            //进行view判断，优化运行速度
            if (convertView == null) {
                view = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.gridview_item, parent, false);
            } else {
                view = convertView;
            }
            ImageView item_image = (ImageView) view.findViewById(R.id.item_image);
            TextView item_title = (TextView) view.findViewById(R.id.item_title);
            item_image.setImageResource(mImages[position]);
            item_title.setText(mNames[position]);
            return view;
        }
    }
}
