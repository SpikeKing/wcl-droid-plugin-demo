package clwang.chunyu.me.wcl_droid_plugin_demo.modules;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.morgoo.droidplugin.pm.PluginManager;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import clwang.chunyu.me.wcl_droid_plugin_demo.ApkItem;
import clwang.chunyu.me.wcl_droid_plugin_demo.R;

/**
 * Apk的列表, 参考: R.layout.apk_item
 * <p>
 * Created by wangchenlong on 16/1/13.
 */
public class ApkItemViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.apk_item_iv_icon) ImageView mIvIcon; // 图标
    @Bind(R.id.apk_item_tv_title) TextView mTvTitle; // 标题
    @Bind(R.id.apk_item_tv_version) TextView mTvVersion; // 版本号
    @Bind(R.id.apk_item_b_install) Button mBInstall; // 安装Apk
    @Bind(R.id.apk_item_b_uninstall) Button mBUninstall; // 卸载Apk

    private ApkItem mApkItem;
    private Activity mActivity;
    private ApkOperator.RemoveCallback mCallback;
    private ApkOperator mApkOperator;

    public ApkItemViewHolder(View itemView, Activity activity, ApkOperator.RemoveCallback callback) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        mActivity = activity;
        mCallback = callback;

        mApkOperator = new ApkOperator(activity, callback);
    }

    // 绑定ViewHolder
    public void bindTo(ApkItem apkItem) {
        mApkItem = apkItem;

        mIvIcon.setImageDrawable(apkItem.icon);
        mTvTitle.setText(apkItem.title);
        mTvVersion.setText(String.format("%s(%s)", apkItem.versionName, apkItem.versionCode));

        mBUninstall.setText("删除");
        mBUninstall.setOnClickListener(this::onClickEvent);

        mBInstall.setText("安装");
        mBInstall.setOnClickListener(this::onClickEvent);
    }

    private void onClickEvent(View view) {
        if (view.equals(mBUninstall)) {
            mApkOperator.deleteApk(mApkItem);
        } else if (view.equals(mBInstall)) {
            mApkOperator.installApk(mApkItem);
        }
    }

    // 删除Apk
    private void deleteApk(final ApkItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("警告");
        builder.setMessage("你确定要删除" + item.title + "么？");
        builder.setNegativeButton("删除", (dialog, which) -> {
            if (new File(item.apkfile).delete()) {
                mCallback.removeItem(item);
                Toast.makeText(mActivity, "删除成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mActivity, "删除失败", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNeutralButton("取消", null);
        builder.show();
    }


    // 安装Apk
    private void installApk(final ApkItem item) {
        if (!PluginManager.getInstance().isConnected()) {
            Toast.makeText(mActivity, "插件服务初始化失败", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mActivity, "插件服务初始化成功", Toast.LENGTH_SHORT).show();
        }

        try {
            PackageInfo info = PluginManager.getInstance().getPackageInfo(item.packageInfo.packageName, 0);
            if (info != null) {
                Toast.makeText(mActivity, "已经安装", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            int result = PluginManager.getInstance().installPackage(item.apkfile, 0);
            boolean isRequestPermission = (result == PluginManager.INSTALL_FAILED_NO_REQUESTEDPERMISSION);
            Toast.makeText(mActivity, isRequestPermission ? "需要权限" : "安装完成", Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // 卸载Apk
    private void uninstallApk(final ApkItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("警告，你确定要卸载么？");
        builder.setMessage("警告，你确定要删除" + item.title + "么？");
        builder.setNegativeButton("卸载", (dialog, which) -> {
            if (!PluginManager.getInstance().isConnected()) {
                Toast.makeText(mActivity, "服务未连接", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    PluginManager.getInstance().deletePackage(item.packageInfo.packageName, 0);
                    Toast.makeText(mActivity, "卸载完成", Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNeutralButton("取消", null);
        builder.show();
    }

    // 打开Apk
    private void openApk(final ApkItem item) {
        PackageManager pm = mActivity.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(item.packageInfo.packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intent);
    }
}
