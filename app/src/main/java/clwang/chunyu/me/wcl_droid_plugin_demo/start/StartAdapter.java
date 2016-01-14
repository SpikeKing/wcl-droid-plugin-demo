package clwang.chunyu.me.wcl_droid_plugin_demo.start;

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
 * 启动的适配器
 * <p>
 * Created by wangchenlong on 16/1/13.
 */
public class StartAdapter extends RecyclerView.Adapter<ApkItemViewHolder> {

    private ArrayList<ApkItem> mApkItems;
    private Activity mActivity;

    public StartAdapter(Activity activity) {
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

    public ApkItem getApkItem(int index) {
        return mApkItems.get(index);
    }

    public int getCount() {
        return mApkItems.size();
    }

    @Override public ApkItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.apk_item, parent, false);
        return new ApkItemViewHolder(mActivity, view, ApkOperator.TYPE_START, this::removeApkItem);
    }

    @Override public void onBindViewHolder(ApkItemViewHolder holder, int position) {
        holder.bindTo(mApkItems.get(position));
    }

    @Override public int getItemCount() {
        return mApkItems.size();
    }
}
