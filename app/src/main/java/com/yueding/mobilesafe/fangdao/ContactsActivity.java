package com.yueding.mobilesafe.fangdao;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yueding.mobilesafe.R;
import com.yueding.mobilesafe.utils.SpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private ListView contactView;
    private List<HashMap<String, String>> contactMapList = new ArrayList<>();
    private MyAdapter mAdapter = new MyAdapter();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        contactView = (ListView) findViewById(R.id.contacts_view);
        contactView.setAdapter(mAdapter);
        contactView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter != null) {
                    Intent intent = new Intent();
                    HashMap<String, String> hashMap = contactMapList.get(position);
                    intent.putExtra("phone", hashMap.get("phone"));
                    setResult(1, intent);
                    finish();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(ContactsActivity.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(ContactsActivity.this,
                    Manifest.permission.READ_CONTACTS)){
                Toast.makeText(ContactsActivity.this, "没有读取通讯录权限", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(ContactsActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS}, 1);
            }
        } else {
            readContacts();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readContacts();
                } else {
                    Toast.makeText(ContactsActivity.this, "你拒绝了读取通讯录权限请求！",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }

    }

    private void readContacts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, null, null, null);
                if (cursor != null){
                    contactMapList.clear();
                    while (cursor.moveToNext()){
                        HashMap<String, String> contactsHashMap = new HashMap<>();
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactsHashMap.put("name", name);
                        contactsHashMap.put("phone", number);
                        contactMapList.add(contactsHashMap);
                        mHandler.sendEmptyMessage(0);
                    }
                    cursor.close();
                }
            }
        }).start();
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return contactMapList.size();
        }

        @Override
        public HashMap<String, String> getItem(int position) {
            return contactMapList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = LayoutInflater.from(ContactsActivity.this).inflate(R.layout.activity_contacts_item, null);
            } else {
                view = convertView;
            }
            TextView text_name = (TextView) view.findViewById(R.id.contact_name);
            TextView text_phone = (TextView) view.findViewById(R.id.contact_number);

            text_name.setText(getItem(position).get("name"));
            text_phone.setText(getItem(position).get("phone"));
            return view;
        }
    }
}
