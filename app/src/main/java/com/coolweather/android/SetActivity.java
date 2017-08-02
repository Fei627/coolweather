package com.coolweather.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.android.service.AutoUpdateService;

import org.w3c.dom.Text;

import java.util.Set;

public class SetActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SetActivity";

    private Button backBtn;
    private Button voiceBtn;
    private Button bgBtn;

    private RelativeLayout cityManagerLayout;
    private TextView cityManagerText;
    private RelativeLayout notifyLayout;
    private Button autoUpdateSwitch;

    static public void actionStart(Context context, String countyName) {
        Intent intent = new Intent(context, SetActivity.class);
        intent.putExtra("county_name", countyName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();
    }

    /*点击事件*/
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
            case R.id.set_list_city_manager: {
                //城市管理
                Intent intent = new Intent(this, CityListActivity.class);
                startActivityForResult(intent, 100);
                break;
            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    String return_county = data.getStringExtra("select_county");
                    cityManagerText.setText(return_county);
                }
                break;
            default:
                break;
        }
    }

    private void initUI() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            //设置状态栏文字和图标颜色为深色
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        setContentView(R.layout.activity_set);
        //初始化控件
        backBtn = (Button) findViewById(R.id.set_nav_back);
        voiceBtn = (Button) findViewById(R.id.btn_set_header_voice);
        bgBtn = (Button) findViewById(R.id.btn_set_header_bg);
        cityManagerLayout = (RelativeLayout) findViewById(R.id.set_list_city_manager);
        cityManagerText = (TextView) findViewById(R.id.tv_set_list_city_manager);
        notifyLayout = (RelativeLayout) findViewById(R.id.set_list_notify);
        autoUpdateSwitch = (Button) findViewById(R.id.background_update_weather_switch);

        backBtn.setOnClickListener(this);
        voiceBtn.setOnClickListener(this);
        bgBtn.setOnClickListener(this);
        cityManagerLayout.setOnClickListener(this);
        notifyLayout.setOnClickListener(this);
        autoUpdateSwitch.setOnClickListener(this);
    }

    private void initData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isSelected = preferences.getBoolean("isSelected", Boolean.parseBoolean(null));

        if (isSelected) {
            autoUpdateSwitch.setBackgroundResource(R.mipmap.ic_switch_open);
            autoUpdateSwitch.setSelected(true);
        } else {
            autoUpdateSwitch.setBackgroundResource(R.mipmap.ic_switch_close);
            autoUpdateSwitch.setSelected(false);
        }

        String countyName = getIntent().getStringExtra("county_name");
        cityManagerText.setText(countyName);
    }
}
