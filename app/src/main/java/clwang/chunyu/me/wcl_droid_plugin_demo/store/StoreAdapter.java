package clwang.chunyu.me.wcl_droid_plugin_demo.store;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import clwang.chunyu.me.wcl_droid_plugin_demo.ApkItem;
import clwang.chunyu.me.wcl_droid_plugin_demo.R;

/**
 * 本地Apk的适配器
 * <p/>
 * Created by wangchenlong on 16/1/8.
 */
public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private ArrayList<ApkItem> mApkItems;

    public StoreAdapter() {
        mApkItems = new ArrayList<>();
    }

    public void setApkItems(ArrayList<ApkItem> apkItems) {
        mApkItems = apkItems;
    }

    public void addApkItems(ApkItem apkItem) {
        mApkItems.add(apkItem);
        notifyItemInserted(mApkItems.size() + 1);
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.apk_item, parent);
        return new StoreViewHolder(view);
    }

    @Override public void onBindViewHolder(StoreViewHolder holder, int position) {

    }

    @Override public int getItemCount() {
        return mApkItems.size();
    }

    public static class StoreViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.apk_item_iv_icon) ImageView mIvIcon; // 图标
        @Bind(R.id.apk_item_tv_title) TextView mTvTitle; // 标题
        @Bind(R.id.apk_item_tv_version) TextView mTvVersion; // 版本号
        @Bind(R.id.apk_item_b_install) Button mBInstall; // 安装Apk
        @Bind(R.id.apk_item_b_uninstall) Button mBUninstall; // 卸载Apk

        public StoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        // 绑定ViewHolder
        public void bindTo(ApkItem apkItem) {
            mIvIcon.setImageDrawable(apkItem.icon);
            mTvTitle.setText(apkItem.title);
            mTvVersion.setText(String.format("%s(%s)", apkItem.versionName, apkItem.versionCode));
        }
    }
}
