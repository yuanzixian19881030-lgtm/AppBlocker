package com.example.appblocker;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.IBinder;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class AppBlockerService extends Service {

    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isRunning = true;
    
    // 定时检查任务：每隔 1 秒检查一次当前最上层的应用
    private Runnable checkTask = new Runnable() {
        @Override
        public void run() {
            if (!isRunning) return;
            
            String currentApp = getForegroundApp();
            if (currentApp != null) {
                // 如果检测到用户打开了系统设置（作为测试包名，你可以后续改成其他应用包名）
                if (currentApp.equals("com.android.settings")) {
                    Toast.makeText(getApplicationContext(), "已被应用锁拦截！", Toast.LENGTH_SHORT).show();
                    
                    // 触发拦截后，强制把用户弹回手机桌面
                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory(Intent.CATEGORY_HOME);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(homeIntent);
                }
            }
            
            // 循环执行
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        handler.post(checkTask);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    // 获取当前正在前台运行的应用包名
    private String getForegroundApp() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        // 查询最近 5 秒内的应用使用记录
        List<UsageStats> stats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, time - 1000 * 5, time);
        
        if (stats != null && !stats.isEmpty()) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
            for (UsageStats usageStats : stats) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (!mySortedMap.isEmpty()) {
                return mySortedMap.get(mySortedMap.lastKey()).getPackageName();
            }
        }
        return null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        handler.removeCallbacks(checkTask);
    }
}
