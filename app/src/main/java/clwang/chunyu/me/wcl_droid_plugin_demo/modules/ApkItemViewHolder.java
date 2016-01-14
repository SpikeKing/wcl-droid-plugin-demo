package clwang.chunyu.me.wcl_droid_plugin_demo.modules;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
     * @param type 类型
     * @param callback
     */
    public ApkItemViewHolder(Activity activity, View itemView
            , int type, ApkOperator.RemoveCallback callback) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mApkOperator = new ApkOperator(activity, callback);
        mType = type;
    }

    // 绑定ViewHolder
    public void bindTo(ApkItem apkItem) {
        mApkItem = apkItem;

        mIvIcon.setImageDrawable(apkItem.icon);
        mTvTitle.setText(apkItem.title);
        mTvVersion.setText(String.format("%s(%s)", apkItem.versionName, apkItem.versionCode));

        mBUndo.setText("删除");
        mBUndo.setOnClickListener(this::onClickEvent);

        mBDo.setText("安装");
        mBDo.setOnClickListener(this::onClickEvent);
    }

    private void onClickEvent(View view) {
        if (mType == ApkOperator.TYPE_STORE) {
            if (view.equals(mBUndo)) {
                mApkOperator.deleteApk(mApkItem);
            } else if (view.equals(mBDo)) {
                mApkOperator.installApk(mApkItem);
            }
        } else if (mType == ApkOperator.TYPE_START) {
            if (view.equals(mBUndo)) {
                mApkOperator.uninstallApk(mApkItem);
            } else if (view.equals(mBDo)) {
                mApkOperator.openApk(mApkItem);
            }
        }
    }
}
