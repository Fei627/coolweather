package com.coolweather.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.coolweather.android.service.AutoUpdateService;

import java.util.Set;

public class SetActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SetActivity";

    private Button backBtn;
    private Button voiceBtn;
    private Button bgBtn;

    private RelativeLayout cityManagerLayout;
    private RelativeLayout notifyLayout;

    private Button autoUpdateSwitch;

    static public void actionStart(Context context) {
        Intent intent = new Intent(context, SetActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        //初始化控件
        backBtn = (Button) findViewById(R.id.set_nav_back);
        voiceBtn = (Button) findViewById(R.id.btn_set_header_voice);
        bgBtn = (Button) findViewById(R.id.btn_set_header_bg);
        cityManagerLayout = (RelativeLayout) findViewById(R.id.set_list_city_manager);
        notifyLayout = (RelativeLayout) findViewById(R.id.set_list_notify);
        autoUpdateSwitch = (Button) findViewById(R.id.background_update_weather_switch);

        backBtn.setOnClickListener(this);
        voiceBtn.setOnClickListener(this);
        bgBtn.setOnClickListener(this);
        cityManagerLayout.setOnClickListener(this);
        notifyLayout.setOnClickListener(this);
        autoUpdateSwitch.setOnClickListener(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isSelected = preferences.getBoolean("isSelected", Boolean.parseBoolean(null));

        if (isSelected) {
            autoUpdateSwitch.setBackgroundResource(R.mipmap.ic_switch_open);
            autoUpdateSwitch.setSelected(true);
        } else {
            autoUpdateSwitch.setBackgroundResource(R.mipmap.ic_switch_close);
            autoUpdateSwitch.setSelected(false);
        }
    }

    @Override
    public void onClick(View view) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();

        switch (view.getId()) {
            case R.id.set_nav_back:
                finish();
                break;
            case R.id.btn_set_header_voice:
                Toast.makeText(SetActivity.this, "个性语音", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_set_header_bg:
                Toast.makeText(SetActivity.this, "天气背景", Toast.LENGTH_SHORT).show();
                break;
            case R.id.set_list_city_manager:
                Toast.makeText(SetActivity.this, "跳转城市管理", Toast.LENGTH_SHORT).show();
                break;
            case R.id.set_list_notify:
                Toast.makeText(SetActivity.this, "跳转通知提醒", Toast.LENGTH_SHORT).show();
                break;
            case R.id.background_update_weather_switch:
                if (autoUpdateSwitch.isSelected()) {
                    autoUpdateSwitch.setBackgroundResource(R.mipmap.ic_switch_close);
                    autoUpdateSwitch.setSelected(false);
                    editor.putBoolean("isSelected", false);

                    //停止后台更新天气服务
                    Intent intent = new Intent(SetActivity.this, AutoUpdateService.class);
                    stopService(intent);
                } else {
                    autoUpdateSwitch.setBackgroundResource(R.mipmap.ic_switch_open);
                    autoUpdateSwitch.setSelected(true);
                    editor.putBoolean("isSelected", true);

                    //启动后台更新天气服务
                    Intent intent = new Intent(SetActivity.this, AutoUpdateService.class);
                    startService(intent);
                }
                editor.apply();
                break;

            default:
                break;
        }
    }
}
