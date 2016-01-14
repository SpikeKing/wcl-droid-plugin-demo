package clwang.chunyu.me.wcl_droid_plugin_demo.store;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import clwang.chunyu.me.wcl_droid_plugin_demo.ApkItem;
import clwang.chunyu.me.wcl_droid_plugin_demo.R;
import clwang.chunyu.me.wcl_droid_plugin_demo.modules.ApkItemViewHolder;
import clwang.chunyu.me.wcl_droid_plugin_demo.modules.ApkOperator;

/**
 * 本地Apk的适配器
 * <p>
 * Created by wangchenlong on 16/1/8.
 */
public class StoreAdapter extends RecyclerView.Adapter<ApkItemViewHolder> {

    private ArrayList<ApkItem> mApkItems;
    private Activity mActivity;

    public StoreAdapter(Activity activity) {
        mActivity = activity;
        mApkItems = new ArrayList<>();
    }

    public void setApkItems(ArrayList<ApkItem> apkItems) {
        mApkItems = apkItems;
        notifyDataSetChanged();
    }

    public void addApkItem(ApkItem apkItem) {
        mApkItems.add(apkItem);
        notifyItemInserted(mApkItems.size() + 1);
    }

    public void removeApkItem(ApkItem apkItem) {
        mApkItems.remove(apkItem);
        notifyDataSetChanged();
    }

    @Override
    public ApkItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.apk_item, parent, false);
        return new ApkItemViewHolder(mActivity, view, ApkOperator.TYPE_STORE, this::removeApkItem);
    }

    @Override public void onBindViewHolder(ApkItemViewHolder holder, int position) {
        holder.bindTo(mApkItems.get(position));
    }

    @Override public int getItemCount() {
        return mApkItems.size();
    }
}
