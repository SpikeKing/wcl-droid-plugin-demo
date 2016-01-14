package clwang.chunyu.me.wcl_droid_plugin_demo.modules;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.RemoteException;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.morgoo.droidplugin.pm.PluginManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import clwang.chunyu.me.wcl_droid_plugin_demo.ApkItem;
import clwang.chunyu.me.wcl_droid_plugin_demo.R;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Apk的列表, 参考: R.layout.apk_item
 * <p>
 * Created by wangchenlong on 16/1/13.
 */
public class ApkItemViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.apk_item_iv_icon) ImageView mIvIcon; // 图标
    @Bind(R.id.apk_item_tv_title) TextView mTvTitle; // 标题
    @Bind(R.id.apk_item_tv_version) TextView mTvVersion; // 版本号
    @Bind(R.id.apk_item_b_do) Button mBDo; // 确定按钮
    @Bind(R.id.apk_item_b_undo) Button mBUndo; // 取消按钮

    private ApkItem mApkItem; // Apk项
    private ApkOperator mApkOperator; // Apk操作
    private int mType; // 类型

    /**
     * 初始化ViewHolder
     *
     * @param activity Dialog绑定Activity
     * @param itemView 项视图
     * @param type     类型, 加载或启动
     * @param callback 删除Item的调用
     */
    public ApkItemViewHolder(Activity activity, View itemView
            , int type, ApkOperator.RemoveCallback callback) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mApkOperator = new ApkOperator(activity, callback); // Apk操作
        mType = type; // 类型
    }

    // 绑定ViewHolder
    public void bindTo(ApkItem apkItem) {
        mApkItem = apkItem;

        mIvIcon.setImageDrawable(apkItem.icon);
        mTvTitle.setText(apkItem.title);
        mTvVersion.setText(String.format("%s(%s)", apkItem.versionName, apkItem.versionCode));

        // 修改文字
        if (mType == ApkOperator.TYPE_STORE) {
            mBUndo.setText("删除");
            if (isApkInstall(apkItem)) {
                mBDo.setText("已装");
            } else {
                mBDo.setText("安装");
            }
        } else if (mType == ApkOperator.TYPE_START) {
            mBUndo.setText("卸载");
            mBDo.setText("启动");
        }

        mBUndo.setOnClickListener(this::onClickEvent);
        mBDo.setOnClickListener(this::onClickEvent);
    }

    // Apk是否安装
    private boolean isApkInstall(ApkItem apkItem) {
        PackageInfo info = null;
        try {
            info = PluginManager.getInstance().getPackageInfo(apkItem.packageInfo.packageName, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return info != null;
    }

    // 点击事件
    private void onClickEvent(View view) {
        if (mType == ApkOperator.TYPE_STORE) {
            if (view.equals(mBUndo)) {
                mApkOperator.deleteApk(mApkItem);
            } else if (view.equals(mBDo)) {
                // 安装Apk较慢需要使用异步线程
                Observable.just(mApkOperator.installApk(mApkItem))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::checkInstallResult);
            }
        } else if (mType == ApkOperator.TYPE_START) {
            if (view.equals(mBUndo)) {
                mApkOperator.uninstallApk(mApkItem);
            } else if (view.equals(mBDo)) {
                mApkOperator.openApk(mApkItem);
            }
        }
    }

    // 检查安装状态
    private void checkInstallResult(int i) {
        if (i == -1) {
            mBDo.setText("安装");
        } else if (i == 0) {
            mBDo.setText("已装");
        }
    }
}
