package com.example.appblocker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 创建一个简单的布局和按钮，用来引导用户去开启权限
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 100, 50, 50);

        // 按钮 1：引导开启“使用情况访问权限”
        Button btnUsage = new Button(this);
        btnUsage.setText("1. 点击开启：应用使用情况权限");
        btnUsage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "跳转失败，请手动去系统设置中开启", Toast.LENGTH_SHORT).show();
                }
            }
        });
        layout.addView(btnUsage);

        // 按钮 2：引导开启“悬浮窗权限”
        Button btnOverlay = new Button(this);
        btnOverlay.setText("2. 点击开启：悬浮窗阻断权限");
        btnOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "跳转失败，请手动去系统设置中开启", Toast.LENGTH_SHORT).show();
                }
            }
        });
        layout.addView(btnOverlay);

        setContentView(layout);
    }
}
