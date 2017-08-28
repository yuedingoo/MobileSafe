package com.yueding.mobilesafe.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yueding.mobilesafe.R;

/**
 * Created by yueding on 2017/8/27.
 *
 */

public class SettingItemView extends RelativeLayout {

    private static final String APP_NAMESPACE = "http://schemas.android.com/apk/res-auto";
    private final CheckBox cb_box;
    private final TextView tv_des;
    private final TextView tv_title;
    private String mDestitle;
    private String mDesoff;
    private String mDeson;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //将xml转换成view对象
        //下一行代码等同于注释的两行代码
        View.inflate(context, R.layout.setting_item_view, this);
        /*View view = View.inflate(context, R.layout.setting_item_view, null);
        this.addView(view);*/

        //控件绑定
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        cb_box = (CheckBox) findViewById(R.id.cb_box);

        //初始化自定义属性（获取自定义属性值）
        initAttrs(attrs);

        tv_title.setText(mDestitle);
    }

    private void initAttrs(AttributeSet attrs) {
        mDestitle = attrs.getAttributeValue(APP_NAMESPACE, "destitle");
        mDesoff = attrs.getAttributeValue(APP_NAMESPACE, "desoff");
        mDeson = attrs.getAttributeValue(APP_NAMESPACE, "deson");
    }

    /**
     * 判断CheckBox是否选中
     * @return true（选中） false（未选中）
     */
    public boolean isChecked(){
        return cb_box.isChecked();
    }

    /**
     * 设置CheckBox选中状态
     * @param isChecked 是否要选中，true选中， false不选中
     */
    public void setChecked(boolean isChecked) {
        cb_box.setChecked(isChecked);
        //文字描述跟随CheckBox选中状态改变
        if (isChecked) {
            tv_des.setText(mDeson);
        } else {
            tv_des.setText(mDesoff);
        }
    }

}
