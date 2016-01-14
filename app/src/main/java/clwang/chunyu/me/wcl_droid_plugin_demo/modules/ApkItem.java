package clwang.chunyu.me.wcl_droid_plugin_demo.modules;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * Apk项的条目
 *
 * @author wangchenlong
 */
public class ApkItem {

    public Drawable icon; // 图标
    public CharSequence title; // 标题
    public String versionName; // 版本名称
    public int versionCode; // 版本号
    public String apkFile; // Apk路径
    public PackageInfo packageInfo; // 包信息

    public ApkItem(PackageManager pm, PackageInfo pi, String path) {

        // 必须设置, 否则title无法获取
        pi.applicationInfo.sourceDir = path;
        pi.applicationInfo.publicSourceDir = path;

        try {
            icon = pm.getApplicationIcon(pi.applicationInfo);
        } catch (Exception e) {
            icon = pm.getDefaultActivityIcon();
        }
        try {
            title = pm.getApplicationLabel(pi.applicationInfo);
        } catch (Exception e) {
            title = pi.packageName;
        }
        versionName = pi.versionName;
        versionCode = pi.versionCode;
        apkFile = path;
        packageInfo = pi;
    }
}