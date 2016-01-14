package clwang.chunyu.me.wcl_droid_plugin_demo.controller;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import clwang.chunyu.me.wcl_droid_plugin_demo.R;
import clwang.chunyu.me.wcl_droid_plugin_demo.modules.ApkItem;

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
    private Context mContext; // 上下文
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
        mContext = activity.getApplicationContext();
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
            mBDo.setText("安装");
        } else if (mType == ApkOperator.TYPE_START) {
            mBUndo.setText("卸载");
            mBDo.setText("启动");
        }

        mBUndo.setOnClickListener(this::onClickEvent);
        mBDo.setOnClickListener(this::onClickEvent);
    }

    // 点击事件
    private void onClickEvent(View view) {
        if (mType == ApkOperator.TYPE_STORE) {
            if (view.equals(mBUndo)) {
                mApkOperator.deleteApk(mApkItem);
            } else if (view.equals(mBDo)) {
                // 安装Apk较慢需要使用异步线程
                new InstallApkTask().execute();
            }
        } else if (mType == ApkOperator.TYPE_START) {
            if (view.equals(mBUndo)) {
                mApkOperator.uninstallApk(mApkItem);
            } else if (view.equals(mBDo)) {
                mApkOperator.openApk(mApkItem);
            }
        }
    }

    // 安装Apk的线程, Rx无法使用.
    private class InstallApkTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPostExecute(String v) {
            Toast.makeText(mContext, v, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(Void... params) {
            return mApkOperator.installApk(mApkItem);
        }
    }
}
