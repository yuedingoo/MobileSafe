package com.yueding.mobilesafe;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.yueding.mobilesafe.utils.HttpUtil;
import com.yueding.mobilesafe.utils.SpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final int VERSION_UPDATE = 100;
    private static final int ENTER_HOME = 101;
    private static final int JSON_ERROR = 200;

    private TextView versionName;
    private ConstraintLayout rootLayout;

    private String currVersionName;
    private int currVersionCode;
    private String serverVersionName;
    private String serverVersionDes;
    private String serverVersionCode;
    private String downloadUrl;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VERSION_UPDATE:
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    enterHome();
                    break;
                case JSON_ERROR:
                    Toast.makeText(MainActivity.this, "JSON解析错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        getVersionInfo();
        String disVersion = "版本名称：" + currVersionName;
        versionName.setText(disVersion);
        if (SpUtil.getBoolean(this, getString(R.string.open_update), false)) {
            checkUpdates();
        } else {
            //在发送消息3s后再去执行ENTER_HOME状态码指定的消息
            handler.sendEmptyMessageDelayed(ENTER_HOME, 3000);
        }
        initAnimation();
    }

    /**
     * 弹出对话框，提示用户更新
     */
    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setIcon();
        builder.setTitle("版本更新");
        builder.setMessage("新版本: " + serverVersionName + "\n" + serverVersionDes);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //处理下载事件
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
//                    downloadApk();
                }
            }
        });
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
        //点击取消事件监听,就是在出现对话框时，点击返回（Back）键时的处理
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

        /* 开启新界面，把欢迎界面finish */
        finish();
    }


    /**
     * 实现淡入动画效果
     */
    private void initAnimation() {
                                                    //完全透明   ，      原始状态，完全不透明
        AlphaAnimation animation = new AlphaAnimation(Animation.ABSOLUTE, Animation.RESTART);
        animation.setDuration(3000);
        rootLayout.startAnimation(animation);
    }

    /**
     * 检查是否有版本更新
     * 用本地版本号versionCode对比服务器上的版本号
     */
    private void checkUpdates() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String updateCheckUrl = "http://192.168.0.126:8088/data/update.json";
//                Message message = new Message();
                final Message message = Message.obtain();
                final long startTime = System.currentTimeMillis();
                HttpUtil.sendOkHttpRequest(updateCheckUrl, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("yueding", "获取服务器信息失败");
                        long endTime = System.currentTimeMillis();
                        //保证欢迎界面至少停留4s
                        if (endTime - startTime < 2000) {
                            try {
                                Thread.sleep(2000 - (endTime - startTime));
                            } catch (Exception ee) {
                                ee.printStackTrace();
                            }
                        }
                        enterHome();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
//                        Log.i("yueding", responseData);
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            serverVersionName = jsonObject.getString("versionName");
                            serverVersionCode = jsonObject.getString("versionCode");
                            serverVersionDes = jsonObject.getString("versionDes");
                            downloadUrl = jsonObject.getString("downloadUrl");

                            /**对比版本号*/
                            if (currVersionCode < Integer.parseInt(serverVersionCode)) {
                                //提示更新，弹出对话框
                                message.what = VERSION_UPDATE;
                            } else {
                                //无需更新，进入主界面
                                message.what = ENTER_HOME;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            message.what = JSON_ERROR;
                        } finally {
                            long endTime = System.currentTimeMillis();
                            //保证欢迎界面至少停留4s
                            if (endTime - startTime < 2000) {
                                try {
                                    Thread.sleep(2000 - (endTime - startTime));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            handler.sendMessage(message);
                        }
                    }
                });

            }
        }).start();
    }


    /**
     * 获取版本信息
     */
    private void getVersionInfo() {
        PackageManager manager = getPackageManager();
        try {
//            flag传入0代表获取最基本的包信息
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            currVersionName = info.versionName;
            currVersionCode = info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化控件
     */
    private void bindView() {
        versionName = (TextView) findViewById(R.id.tv_version_name);
        rootLayout = (ConstraintLayout) findViewById(R.id.root_layout);
    }

    /**
     * 获取运行时权限结果处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    downloadApk();
                } else {
                    Toast.makeText(MainActivity.this, "你拒绝了读写内存卡权限，无法下载新版本。",
                            Toast.LENGTH_SHORT).show();
                    enterHome();
                }
        }
    }
}
